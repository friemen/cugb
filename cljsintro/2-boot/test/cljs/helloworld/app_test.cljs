(ns helloworld.app-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]))

(deftest first-test
  (is (= 1 1)))
