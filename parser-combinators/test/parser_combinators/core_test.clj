(ns parser-combinators.core-test
  (:require [clojure.test :refer :all]
            [parser-combinators.core :as pc]))

(deftest test__pred->parser
  (let [p (pc/pred->parser #{:p :h1 :h2})]
    (are [input result] (= result (first (p input)))
      []
      ::pc/invalid

      [:foo]
      ::pc/invalid

      [:p]
      :p

      [:h1]
      :h1)))


(deftest test__alt
  (let [p (pc/alt (pc/pred->parser #{:p})
                  (pc/pred->parser #{:h1}))]
    (are [input result] (= result (first (p input)))
      []
      ::pc/invalid

      [:foo]
      ::pc/invalid

      [:p]
      :p

      [:h1]
      :h1)))


(deftest test__many*
  (let [p (pc/many* (pc/pred->parser #{:p}))]
    (are [input result] (= result (first (p input)))
      []
      []

      [:foo]
      []

      [:p]
      [:p]

      [:p :p :h1]
      [:p :p])))


(deftest test__many+
  (let [p (pc/many+ (pc/pred->parser #{:p}))]
    (are [input result] (= result (first (p input)))
      []
      ::pc/invalid

      [:foo]
      ::pc/invalid

      [:p]
      [:p]

      [:p :p :h1]
      [:p :p])))


(deftest test__optional
  (let [p (pc/optional (pc/pred->parser #{:p}))]
    (are [input result] (= result (first (p input)))
      []
      nil

      [:foo]
      nil

      [:p]
      :p)))


(deftest test__sequence
  (let [p (pc/sequence (pc/pred->parser #{:h1})
                       (pc/pred->parser #{:p})
                       (pc/pred->parser #{:p}))]
   (are [input result] (= result (first (p input)))
      []
      ::pc/invalid

      [:foo]
      ::pc/invalid

      [:h1 :p]
      ::pc/invalid

      [:h1 :p :p]
      [:h1 :p :p])))


(defn into-first
  [[first items]]
  (into [first] items))


(deftest test__transform
  (let [p (pc/transform
           into-first
           (pc/sequence (pc/pred->parser #{:h1})
                        (pc/many* (pc/pred->parser #{:p}))))]
    (are [input result] (= result (first (p input)))
      []
      ::pc/invalid

      [:foo]
      ::pc/invalid

      [:h1]
      [:h1]

      [:h1 :p :p]
      [:h1 :p :p])))



(def paragraph-parser
  (pc/pred->parser #{:p}))

(def section-parser
  (pc/transform
           into-first
           (pc/sequence (pc/pred->parser #{:h1})
                        (pc/many* paragraph-parser))))

(def tr-parser
  (pc/transform into-first
                (pc/sequence
                 (pc/pred->parser #{:tr})
                 (pc/many+ (pc/pred->parser #{:td})))))

(def table-parser
  (pc/transform into-first
                (pc/sequence
                 (pc/pred->parser #{:table})
                 (pc/many+ (pc/descend tr-parser)))))


(def content-parser
  (pc/many+ (pc/alt section-parser paragraph-parser (pc/descend table-parser))))


(deftest test__descend
  (let [p content-parser]
    (are [input result] (= result (first (p input)))
      []
      ::pc/invalid

      [:p :p]
      [:p :p]

      [:h1]
      [[:h1]]

      [:h1 :p :p]
      [[:h1 :p :p]]

      [:p :p :h1 :p [:table [:tr :td :td] [:tr :td]]]
      [:p :p [:h1 :p] [:table [:tr :td :td] [:tr :td]]])))
