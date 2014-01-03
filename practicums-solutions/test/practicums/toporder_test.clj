(ns practicums.toporder-test
  (:require [practicums.toporder :refer :all]
            [clojure.test :refer :all]))

(deftest topord-test
  (are [r g] (= r (topord (mapbased-graph g)))
       '() {}
       '(:a) {:a []}
       '(:a :b) {:a [:b]}
       '(:a :b :c) {:a [:b] :b [:c]}
       '(:a :c :d :b) {:a [:b :c] :c [:d]})
  (is (thrown? IllegalArgumentException
               (topord (mapbased-graph {:a [:b] :b [:a]})))))
