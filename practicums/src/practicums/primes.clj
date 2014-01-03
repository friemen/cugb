(ns practicums.primes
  (:require [clojure.set :refer [difference]]))

;; Find all prime numbers up to N.

;; The idea using the Sieve of Eratosthenes is to
;; initially list all numbers from 2 to N.
;; Start with the first P which is guaranteed to be prime,
;; then remove all multiples of P.
;; Continue with the next until P > sqrt(N)


(defn primes
  [n]
)
