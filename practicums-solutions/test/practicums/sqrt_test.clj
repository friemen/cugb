(ns practicums.sqrt-test
  (:require [practicums.sqrt :refer :all]
            [clojure.test :refer :all]))

(deftest sqrt-test
  (are [r n] (= r (int (sqrt n)))
       1 1
       3 9
       10 100))
