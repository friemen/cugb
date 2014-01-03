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
  (let [max (Math/sqrt n)]
    (loop [candidates (into (sorted-set) (range 3 (inc n) 2)), primes [2]]
      (if-let [p (first candidates)]
        (if (> p max)
          (into primes candidates)
          (recur (difference candidates (set (range p (inc n) p)))
                 (conj primes p)))
        primes))))
