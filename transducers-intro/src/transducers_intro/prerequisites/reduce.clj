(ns transducers-intro.prerequisites.reduce)

;; reduce: (a -> b -> a) -> a -> [b] -> a


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example 1
;; Summation

(reduce +
        [1 2 3])

(reduce (fn [acc x] (+ acc x))
        [1 2 3])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example 2
;; Maximum

(reduce max
        [2 3 1])

(reduce (fn [acc x] (max acc x))
        [2 3 1])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example 3
;; Reverse collection

(reduce (fn [acc x] (cons x acc))
        '()
        [:foo :bar :baz :qux])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example 4
;; Early Termination

(reduced 42)
(clojure.lang.Reduced. 42)
(unreduced (reduced 42))
(deref (reduced 42))
(ensure-reduced 42)
(ensure-reduced (reduced 42))


(reduce (fn [acc x]
          (if (= :stop x)
            (reduced acc)
            (conj acc x)))
        []
        [:foo :bar :stop :baz :qux])
