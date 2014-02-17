(ns practicums.sqrt)

;; Given a number N, find the square root of N
;;
;; The Babylonian method of square root approximation starts with
;; an initial guess G_0 of a solution.
;; Given a guess G_n a better solution is always G_n+1 := (N / G_n + G_n) / 2
;; Repeat this until the error is small enough.


(defn next-guess
  [n g]
  (/ (+ (/ n g) g) 2))

(defn good-enough?
  [max-error n g]
  (<= (Math/abs (double (- n (* g g))))
      max-error))

(defn sqrt
  [n]
  (->> (iterate (partial next-guess n) 1)
       (drop-while (complement (partial good-enough? 0.01 n)))
       first
       float))

