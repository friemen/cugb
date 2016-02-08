(ns transducers-intro.naive.step-7
  "Step: Restore the regular collection arity
     - Accept a collection as the second argument"

  (:require [clojure.test :refer [deftest testing is]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Process / Transducible Context

(defn seq*
  [xform xs]
  (let [rf (xform conj)]
    (rf (reduce rf [] xs))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transducers

(defn tmap
  ;; Transducer
  ([f]
   (fn [rf]
     (fn
       ;; Completion
       ([acc]
        (rf acc))

       ;; Reduction
       ([acc x]
        (rf acc (f x))))))

  ;; Regular
  ([f xs]
   (seq* (tmap f) xs)))


(defn tfilter
  ;; Transducer
  ([f]
   (fn [rf]
     (fn
       ;; Completion
       ([acc]
        (rf acc))

       ;; Reduction
       ([acc x]
        (if (f x) (rf acc x) acc)))))

  ;; Regular
  ([f xs]
   (seq* (tfilter f) xs)))


(defn ttake
  ;; Transducer
  ([n]
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

  ;; Regular
  ([n xs]
   (seq* (ttake n) xs)))


(defn tpartition
  ;; Transducer
  ([n]
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

  ;; Regular
  ([n xs]
   (seq* (tpartition n) xs)))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test-step-7
  (testing "Transducer Arity"
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
                 (range)))))

  (testing "Regular Arity"
    (is (= [[0] [1] [2]]
           (tmap vector
                 (range 3))))

    (is (= [0 2]
           (tfilter even?
                    (range 3))))

    (is (= [0 1 2]
           (ttake 3 (range))))

    (is (= [[0 1] [2 3]]
           (tpartition 2 (range 4))))

    (is (= [[0 1] [2 3] [4]]
           (tpartition 2 (range 5)))))
  ,,,)
