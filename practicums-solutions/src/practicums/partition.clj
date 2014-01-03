(ns practicums.partition)

;; Define a function partition-by-equality that takes
;; a binary predicate p and a sequence and returns
;; a sequence of partitions.
;; The elements of a sub-sequence X_1 to X_n belong to the
;; same partition if all (p X_1 X_2) to (p X_n-1 X_n) hold true.

;; Example of application:
;; (partition-by-eq < [1 2 3 2 3 4]) --> ((1 2 3) (2 3 4))

(defn partition-by-eq
  [p xs]
  (let [pairs (map vector xs (drop 1 xs))]
    (->> pairs
         (mapcat (fn [[a b]] (if (p a b) [b] [nil b])))
         (cons (first xs))
         (partition-by nil?)
         (remove (comp nil? first)))))
