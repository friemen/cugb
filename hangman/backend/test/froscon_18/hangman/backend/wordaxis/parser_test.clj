(ns froscon-18.hangman.backend.wordaxis.parser-test
  "Parser test"
  (:require
   [clojure.test :refer :all]

   [froscon-18.hangman.backend.wordaxis.parser :as sut]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fixtures

(def ^:private fixture-html
  (slurp "test/fixtures/wordaxis.html"))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test__parse
  (is (= ["hating" "hatpeg" "heting" "hotdog"]
         (sut/parse fixture-html))))
