(ns core-logic-primer.sudoku
  (:refer-clojure :exclude [== !=])
  (:use [clojure.core.logic])
  (:require [clojure.core.logic.fd :as fd]))


(def single-solution-puzzle
  ;; Quelle: http://www.welt.de/spiele/online-spiele/article1336655/Sudoku.html
  [0 0 0 2 0 0 0 0 0
   0 0 6 0 1 5 0 0 4
   0 0 8 4 0 0 0 0 7
   0 6 2 0 0 0 4 0 0
   0 0 0 9 0 6 0 0 0
   0 0 7 0 0 0 6 3 0
   7 0 0 0 0 3 5 0 0
   1 0 0 6 2 0 3 0 0
   0 0 0 0 0 8 0 0 0])

(def multiple-solutions-puzzle
  ;; Quelle: http://sandwalk.blogspot.de/2007/06/i-knew-it-there-can-be-more-than-one.html
  [9 0 6 0 7 0 4 0 3
   0 0 0 4 0 0 2 0 0
   0 7 0 0 2 3 0 1 0
   5 0 0 0 0 0 1 0 0
   0 4 0 2 0 8 0 6 0
   0 0 3 0 0 0 0 0 5
   0 3 0 7 0 0 0 5 0
   0 0 7 0 0 5 0 0 0
   4 0 5 0 1 0 7 0 8])

(defn- get-square
  [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]
    (get-in rows [x y])))

(defn- utilise-hint
  [var hint]
  (when-not (zero? hint)
    (== var hint)))

(defn- init
  [vars hints]
  (->> (map utilise-hint vars hints)
       (remove nil?)
       (and*)))

(defn solve-sudoku
  [hints]
  (let [digits (fd/domain 1 2 3 4 5 6 7 8 9)
        board (repeatedly 81 lvar)
        rows (->> board (partition 9) (map vec) (into []))
        columns (apply map vector rows)
        squares (for [x (range 0 9 3)
                      y (range 0 9 3)]
                  (get-square rows x y))]
    (run* [q]
      (== q board)
      (everyg #(fd/in % digits) board)
      (init board hints)
      (everyg fd/distinct rows)
      (everyg fd/distinct columns)
      (everyg fd/distinct squares))))
