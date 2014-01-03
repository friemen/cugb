(ns practicums.partition-test
  (:require [practicums.partition :refer :all]
            [clojure.test :refer :all]))

(deftest partition-by-eq-test
  (are [r xs] (= r (partition-by-eq < xs))
       '() []
       '((1)) [1]
       '((1 2)) [1 2] 
       '((1 2 3) (2 3 4)) [1 2 3 2 3 4]))
