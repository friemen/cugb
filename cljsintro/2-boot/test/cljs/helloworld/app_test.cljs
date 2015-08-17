(ns helloworld.app-test
  (:require [helloworld.app :as app]
            [cljs.test :refer-macros [deftest is testing run-tests]]))


;; for unit test demonstration add this to helloworld.app
#_ (defn adder
  [a b]
  (+ a b))


#_(deftest first-test
  (is (= 7 (app/adder 3 4))))
