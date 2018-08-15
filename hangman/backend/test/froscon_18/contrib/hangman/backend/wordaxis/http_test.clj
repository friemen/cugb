(ns froscon-18.contrib.hangman.backend.wordaxis.http-test
  "Http test"
  (:require
   [clojure.test :refer :all]
   [stubadub.core :as stub]
   [org.httpkit.client :as http]

   [froscon-18.contrib.hangman.backend.wordaxis.http :as sut]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fixtures

(def ^:private fixtures
  {["http://foo.com"]
   (atom {:body "html-1"})

   ["http://bar.com"]
   (atom {:body "html-2"})})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test__fetch!
  (stub/with-stub http/get
    :return-fn fixtures

    (is (= "html-1"
           (sut/fetch! "http://foo.com")))

    (is (= "html-2"
           (sut/fetch! "http://bar.com")))))
