(ns froscon-18.contrib.hangman.backend.wordaxis.parser-test
  "Contrib: Parser test"
  (:require
   [clojure.test :refer :all]

   [froscon-18.contrib.hangman.backend.wordaxis.parser :as sut]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fixtures

(def ^:private fixture-html
  (slurp "test/fixtures/wordaxis.html"))


(def fixture-result
  [:div
   {:id "result"}
   [:div
    {:id "div_adslot_1"}
    "\n\n"
    [:ins
     {:class "adsbygoogle adslot_1",
      :style "display:block;",
      :data-ad-client "ca-pub-7639106915413519",
      :data-ad-slot "1448802414",
      :data-ad-format "auto"}]
    "\n"
    [:script
     {}
     "\n(adsbygoogle = window.adsbygoogle || []).push({});\n"]
    "\n"]
   "\n"
   [:a {:href "/word/hating"} "hating"]
   [:br {}]
   [:a {:href "/word/hatpeg"} "hatpeg"]
   [:br {}]
   [:a {:href "/word/heting"} [:b {} "heting"]]
   [:br {}]
   [:a {:href "/word/hotdog"} "hotdog"]
   [:br {}]])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest test__element-by-id
  (testing "valid id"
    (is (= fixture-result
           (sut/element-by-id "result" fixture-html))))

  (testing "invalid id"
    (is (= nil
           (sut/element-by-id "???" fixture-html)))))
