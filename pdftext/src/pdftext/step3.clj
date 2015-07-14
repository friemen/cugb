(ns pdftext.step3
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


;;; implement the lazy seq on your own to introduce ref-counting


(deftype TextPositionSeq [next-fn       ; no-arg function that creates the next seq instance
                          seqcounter    ; atom that carries a counter for the number of seq objects
                          textpos-ch    ; channel to be used for termination when no seq objects survived
                          next-seq      ; atom containing cached next seq object
                          tp]           ; current text position (never nil)
  clojure.lang.ISeq
  (first [_]
    tp)
  (next [_]
    (swap! next-seq #(or % (next-fn))))
  (more [this]
    (or (next this) '()))
  (seq [this]
    (if tp this))
  clojure.lang.Counted
  (count [this]
    (loop [n 0, s this]
      (if (seq s)
        (recur (inc n) (rest this))
        n)))
  clojure.lang.Indexed
  (nth [this index]
    (nth this index (ArrayIndexOutOfBoundsException.)))
  (nth [this index not-found]
    (loop [i index, s this]
      (if (> i 0)
        (if-let [rst (next s)]
          (recur (dec i) rst)
          (if (instance? Exception not-found)
            (throw not-found)
            not-found))
        (if (seq s)
          (first s)
          (if (instance? Exception not-found)
            (throw not-found)
            not-found)))))
  java.lang.Object
  (finalize [_]
    (when (zero? (swap! seqcounter dec))
      (println "Closing channel")
      ;; no more instances of this lazy seq?
      ;; close channel (and read from it to unblock writing process)
      (close! textpos-ch)
      (alts!! [textpos-ch] :default 0))))


(defn text-positions
  "Returns all text-positions within the PDFDocument d as lazy sequence."  
  [d]
  (let [seqcounter  (atom 0)
        textpos-ch  (chan)
        next-fn     (fn next-fn []
                      (swap! seqcounter inc)
                      (if-let [tp (<!! textpos-ch)]
                        (TextPositionSeq. next-fn seqcounter textpos-ch (atom nil) tp)))]
    (future (println "Started parser")
            (try (doto (parser d textpos-ch)
                   (.setSortByPosition true)
                   (.writeText d (java.io.PrintWriter. System/out)))
                 (catch InterruptedException ex
                   nil))            
            (close! textpos-ch)
            (println "Terminated parser"))
    (next-fn)))


#_ (with-open [d (-> "simple1.pdf" io/resource io/file PDDocument/load)]
     (->> d
          text-positions
          (map :text)
          (apply str)))

