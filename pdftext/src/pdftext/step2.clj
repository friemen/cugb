(ns pdftext.step2
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.core.async :refer [chan >!! <!! close! alts!!]])
  (:import [org.apache.pdfbox.pdmodel PDDocument]
           [org.apache.pdfbox.text PDFTextStripper TextPosition]))


(defn- text-position
  "Expects a TextPosition instance tp and returns a map with entries:
    :page    Integer representing 0-based page number
    :left    x position where word starts on page
    :right   x position where word ends on page
    :top     y position where word starts on page
    :bottom  y position where word ends on page
    :text    Unicode text"
  [page-no page tp]
  (let [box (.getBBox page)
        l   (- (.getXDirAdj tp) (.getLowerLeftX box))
        t   (+ (.getYDirAdj tp) (.getLowerLeftY box))
        w   (.getWidthDirAdj tp)
        h   (.getHeightDir tp)
        fs  (.getFontSizeInPt tp)]
    {:page     page-no
     :text     (.getUnicode tp)
     :left     l
     :top      t
     :right    (+ l w)
     :bottom   (+ t h)
     :height   h
     :width    w
     :fontsize fs}))


;;; general idea:
;;; use a different thread to push the text positions to a channel
;;; and implement a lazy sequence reading from this channel


(defn- parser
  "Returns a PDFTextStripper instance that writes text positions to a
  channel."
  [d ch]
  (let [pages (->> d .getPages (into []))]
    (proxy [PDFTextStripper] []
      (processTextPosition [textpos]
        (let [page-no (dec (.getCurrentPageNo this))
              page    (nth pages page-no)
              tp      (text-position page-no page textpos)]
          (>!! ch tp))))))


(defn- ch->lazy-seq
  "A lazy sequence reading from channel ch."
  [ch]
  (lazy-seq (if-let [tp (<!! ch)]
              (cons tp (lazy-ch ch)))))


(defn text-positions
  "Returns all text positions within the PDFDocument d as lazy
  sequence."
  [d]
  (let [textpos-ch (chan 100)]
    (future (doto (parser d textpos-ch)
              (.setSortByPosition true)
              (.writeText d (java.io.PrintWriter. System/out)))
            (close! textpos-ch))
    (ch->lazy-seq textpos-ch)))

;;; BIG caveat:
;;; if document is not entirely consumed the thread may be blocked forever!!!


#_ (with-open [d (-> "simple1.pdf" io/resource io/file PDDocument/load)]
     (->> d
          text-positions
          (map :text)
          (apply str)))

