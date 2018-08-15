(ns froscon-18.hangman.backend.wordaxis.helper-test
  "Helper test"
  (:require
   [clojure.test :refer :all]

   [froscon-18.hangman.backend.wordaxis.helper :as sut]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test__compute
  (testing "blank pattern returns empty vector"
    (is (= []
           (sut/compute "")
           (sut/compute "    \n\t"))))

  (testing "wildcard only returns empty vector"
    (is (= []
           (sut/compute "?")
           (sut/compute "???"))))

  (testing "literal pattern (no wildcards)"
    (is (= ["foo"]
           (sut/compute "foo"))))


  (testing "returns nil for a valid pattern"
    (is (= nil
           (sut/compute "progr???in?")))))
