(ns transducers-intro.naive.step-6
  "Step: Completion
     - Example: tpartition"

  (:require [clojure.test :refer [deftest is]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Process / Transducible Context

(defn seq*
  [xform xs]
  (let [rf (xform conj)]
    (rf (reduce rf [] xs))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transducers

(defn tmap
  [f]
  (fn [rf]
    (fn
      ;; Completion
      ([acc]
       (rf acc))

      ;; Reduction
      ([acc x]
       (rf acc (f x))))))

(defn tfilter
  [f]
  (fn [rf]
    (fn
      ;; Completion
      ([acc]
       (rf acc))

      ;; Reduction
      ([acc x]
       (if (f x) (rf acc x) acc)))))

(defn ttake
  [n]
  (fn [rf]
    (let [!i (volatile! n)]
      (fn
        ;; Completion
        ([acc]
         (rf acc))

        ;; Reduction
        ([acc x]
         (case (vswap! !i dec)
           0 (ensure-reduced (rf acc x))
           -1 (ensure-reduced acc)
           (rf acc x)))))))

(defn tpartition
  [n]
  (fn [rf]
    (let [!buffer (volatile! [])]
      (fn
        ;; Completion
        ([acc]
         (let [xs @!buffer]
           (if (empty? xs)
             (rf acc)
             (do (vreset! !buffer [])
                 (rf acc xs)))))

        ;; Reduction
        ([acc x]
         (let [xs (conj @!buffer x)]
           (if (= n (count xs))
             (do (vreset! !buffer [])
                 (rf acc xs))

             (do (vreset! !buffer xs)
                 acc))))))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test-step-6
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

  (is (= [[0 1] [2 3]]
         (seq* (tpartition 2)
               (range 4))))

  (is (= [[0 1] [2 3] [4]]
         (seq* (tpartition 2)
               (range 5))))

  (is (= [[0 1] [2 3] [4]]
         (seq* (comp (ttake 5)
                     (tpartition 2))
               (range))))

  (is (= [[0 1] [2 3] [4 5] [6 7] [8 9]]
         (seq* (comp (tpartition 2)
                     (ttake 5))
               (range))))
  ,,,)
