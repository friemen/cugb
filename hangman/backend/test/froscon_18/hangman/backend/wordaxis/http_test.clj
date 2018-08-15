(ns froscon-18.hangman.backend.wordaxis.http-test
  "Http test"
  (:require
   [clojure.test :refer :all]
   [stubadub.core :as stub]

   [froscon-18.contrib.hangman.backend.wordaxis.http :as contrib]
   [froscon-18.hangman.backend.wordaxis.http :as sut]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Expose private function

(def pattern->url
  #'sut/pattern->url)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test__pattern->url
  (testing "it forms the correct url-encoded url"
    (is (= "http://www.wordaxis.com/pattern/TAM%3F%3F%3F%3FI%3F%3F"
           (pattern->url "TAM????I??")))

    (is (= "http://www.wordaxis.com/pattern/%3F%3FOG%3F%3F%3F%3FING"
           (pattern->url "??OG????ING"))))

  (testing "it automatically uppercases the pattern"
    (is (= (pattern->url "TAM????I??")
           (pattern->url "tam????i??")))))


(deftest test__fetch!
  (testing "`fetch!` build the correct url from a given pattern and returns the webpage's html"

    (stub/with-stub contrib/fetch!
      :return-fn {["http://www.wordaxis.com/pattern/TAM%3F%3F%3F%3FI%3F%3F"]
                  "html-1"

                  ["http://www.wordaxis.com/pattern/%3F%3FOG%3F%3F%3F%3FING"]
                  "html-2"}

      (is (= "html-1"
             (sut/fetch! "TAM????I??")))

      (is (= "html-2"
             (sut/fetch! "??OG????ING"))))))
