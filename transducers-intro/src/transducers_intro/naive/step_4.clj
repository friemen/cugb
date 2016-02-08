(ns transducers-intro.naive.step-4
  "Step: Data abstraction and Composability
     - Be agnostic to *concrete* data sources and sinks
       - Don't be tied to sequences!

     - Composability
       | Ring Handler    | Req -> Resp                    |
       | Reduction       | a -> b -> a                    |
       |-----------------+--------------------------------|
       | Ring Middleware | (Req -> Resp) -> (Req -> Resp) |
       | Transducer      | (a -> b -> a) -> (a -> b -> a) |"

  (:require [clojure.test :refer [deftest is]]))


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


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test-step-4
  (is (= [[0] [1] [2]]
         (seq* (tmap vector)
               (range 3))))

  (is (= [0 2]
         (seq* (tfilter even?)
               (range 3))))

  (is (= [[0] [2]]
         (seq* (comp (tfilter even?)
                     (tmap vector))
               (range 3))))
  ,,,)
