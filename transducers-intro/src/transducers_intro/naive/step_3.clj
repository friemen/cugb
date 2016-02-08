(ns transducers-intro.naive.step-3
  "Step: Generic Process
      - Have a generic process / context hosting them"

  (:require [clojure.test :refer [deftest is]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Process / Context

(defn seq*
  [xform xs]
  (reduce xform [] xs))


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
;; Tests

(deftest test-step-3
  (is (= [[0] [1] [2]]
         (seq* (tmap vector)
               (range 3))))

  (is (= [0 2]
         (seq* (tfilter even?)
               (range 3))))
  ,,,)
