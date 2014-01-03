(ns practicums.primes-test
  (:require [practicums.primes :refer :all]
            [clojure.test :refer :all]))

(deftest primes-test
  (are [r n] (= r (primes n))
       [2] 2
       [2 3 5 7] 10)
  (are [c n] (= c (count (primes n)))
       25 100
       9592 100000))
