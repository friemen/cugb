(ns pdftext.step1
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
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



(defn- parser
  "Returns a PDFTextStripper instance that writes text positions to an
  atom."
  [d tps-atom]
  (let [pages (->> d .getPages (into []))]
    (proxy [PDFTextStripper] []
      (processTextPosition [textpos]
        (let [page-no (dec (.getCurrentPageNo this))
              page    (nth pages page-no)
              tp      (text-position page-no page textpos)]
          (swap! tps-atom conj tp))))))


(defn text-positions
  "Returns all text positions within the PDFDocument d."
  [d]
  (let [tps-atom (atom [])]
    (doto (parser d tps-atom)
      (.setSortByPosition true)
      (.writeText d (java.io.PrintWriter. System/out)))
    @tps-atom))



#_ (with-open [d (-> "simple1.pdf" io/resource io/file PDDocument/load)]
     (->> d
          text-positions
          (map :text)
          (apply str)))

