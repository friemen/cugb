(ns transducers-intro.naive.step-5
  "Step: Stateful transduction
     - Example: ttake"

  (:require [clojure.test :refer [deftest testing is]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Process / Transducible Context

(defn seq*
  [xform xs]
  (reduce (xform conj) [] xs))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transducers

(defn tmap
  [f]
  (fn [rf]
    (fn [acc x]
      (rf acc (f x)))))

(defn tfilter
  [f]
  (fn [rf]
    (fn [acc x]
      (if (f x) (rf acc x) acc))))

(defn ttake
  [n]
  (fn [rf]
    (let [!i (volatile! n)]
      (fn [acc x]
        (case (vswap! !i dec)
           0 (ensure-reduced (rf acc x))
          -1 (ensure-reduced acc)
          (rf acc x))))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test-step-5
  (is (= [[0] [1] [2]]
         (seq* (tmap vector)
               (range 3))))

  (is (= [0 2]
         (seq* (tfilter even?)
               (range 3))))

  (is (= [0 1 2]
         (seq* (ttake 3)
               (range))))

  (is (= [[0] [1] [2]]
         (seq* (comp (tmap vector)
                     (ttake 3))
               (range))))

  (is (= [[0] [1] [2]]
         (seq* (comp (ttake 3)
                     (tmap vector))
               (range))))
  ,,,)
