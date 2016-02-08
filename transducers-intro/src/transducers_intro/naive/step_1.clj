(ns transducers-intro.naive.step-1
  "Step: Common abstraction
      - Express sequence functions in terms of reduce"

  (:require [clojure.test :refer [deftest is]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sequence Functions

(defn map*
  [f xs]
  (reduce (fn [acc x]
            (conj acc (f x)))
          [] xs))

(defn filter*
  [f xs]
  (reduce (fn [acc x]
            (if (f x) (conj acc x) acc))
          [] xs))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test-step-1
  (is (= [[0] [1] [2]]
         (map* vector (range 3))))

  (is (= [0 2]
         (filter* even? (range 3))))

  ,,,)
