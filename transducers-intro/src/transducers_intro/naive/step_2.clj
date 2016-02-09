(ns transducers-intro.naive.step-2
  "Step: Factor out reduction functions
     - Distill sequence functions down to their essence"

  (:require [clojure.test :refer [deftest is]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Distilled Sequence Functions

(defn tmap
  [f]
  (fn [acc x]
    (conj acc (f x))))

(defn tfilter
  [f]
  (fn [acc x]
    (if (f x) (conj acc x) acc)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sequence Functions

(defn map*
  [f xs]
  (reduce (tmap f) [] xs))

(defn filter*
  [f xs]
  (reduce (tfilter f) [] xs))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test-step-2
  (is (= [[0] [1] [2]]
         (map* vector
               (range 3))))

  (is (= [0 2]
         (filter* even?
                  (range 3))))
  ,,,)
