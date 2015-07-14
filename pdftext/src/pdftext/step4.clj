(ns pdftext.step4
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
          (when-not (>!! ch tp)
            ;; failed to put into channel?
            ;; then signal termination
            (throw (InterruptedException.))))))))



;; Closable Seq (just a thin wrapper - mostly delegates to the underlying coll)
(deftype CloseableSeq [coll close-fn]
  clojure.lang.Seqable
  (seq [_]
    coll)

  clojure.lang.ISeq
  (first [this]
    (or (first coll) (.close this)))

  (next [this]
    (if-let [coll* (next coll)]
      (CloseableSeq. coll* close-fn)
      (.close this)))

  (more [this]
    (or (next this) '()))

  (cons [this o]
    (CloseableSeq. (cons o coll) close-fn))

  java.io.Closeable
  (close [_]
    (close-fn)))


(defn- ch->lazy-seq
  "A lazy sequence reading from channel ch."
  [ch]
  (lazy-seq (if-let [tp (<!! ch)]
              (cons tp (lazy-ch ch)))))


(defn- enqueue-text-positions!
  [d ch]
  (try (doto (parser d ch)
         (.setSortByPosition true)
         (.writeText d (java.io.PrintWriter. System/out)))
       (catch InterruptedException _
         nil)))


(defn text-positions
  "Returns all text-positions within the PDFDocument doc as lazy sequence
  (which also does implement java.io.Closeable)."
  [d]
  (let [textpos-ch (chan)
        close-fn   #(do (close! textpos-ch)
                        (.close d))]
    (future
      (enqueue-text-positions! d textpos-ch)
      (close-fn)
      (println "Terminating parser"))
    
    (CloseableSeq. (ch->lazy-seq textpos-ch) close-fn)))


;;; API changes a tiny bit

#_ (with-open [tps (-> "large.pdf" io/resource io/file PDDocument/load text-positions)]
                 (time (->> tps
                            (map :text)
                            (take 5000)
                            (apply str)))

                 (time (->> tps
                            (map :text)
                            (take 5000)
                            (apply str))))

