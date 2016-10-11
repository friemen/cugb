(ns parser-combinators.core
  "An educational implementation of parser combinators."
  (:refer-clojure :exclude [sequence]))

(defn pred->parser
  [pred]
  (fn [[x & remaining :as input]]
    (if (pred x)
      [x remaining]
      [::invalid input])))

(defn alt
  [& parsers]
  (fn [input]
    (loop [[parser & parsers] parsers]
      (if parser
        (let [[results remaining] (parser input)]
          (if (= results ::invalid)
            (recur parsers)
            [results remaining]))
        [::invalid input]))))

(defn many*
  [parser]
  (fn [input]
    (loop [results [], remaining input]
      (if (seq remaining)
        (let [[result remaining] (parser remaining)]
          (if (= result ::invalid)
            [results remaining]
            (recur (conj results result) remaining)))
        [results remaining]))))

(defn many+
  [parser]
  (let [parser (many* parser)]
    (fn [input]
      (let [[results remaining] (parser input)]
        (if (empty? results)
          [::invalid input]
          [results remaining])))))

(defn optional
  [parser]
  (fn [input]
    (let [[results remaining] (parser input)]
      (if (= results ::invalid)
        [nil input]
        [results remaining]))))

(defn sequence
  [& parsers]
  (fn [input]
    (loop [[parser & parsers] parsers
           remaining          input
           results            []]
      (if parser
        (let [[result' remaining'] (parser remaining)]
          (if (= result' ::invalid)
            [::invalid input]
            (recur parsers remaining' (conj results result'))))
        [results remaining]))))

(defn transform
  [f parser]
  (fn [input]
    (let [[results remaining] (parser input)]
      (if (= results ::invalid)
        [::invalid remaining]
        [(f results) remaining]))))

(defn descend
  [parser]
  (fn [input]
    (let [[input' & remaining] input
          [result remaining'] (parser input')]
      (if (seq remaining')
        [::invalid input]
        [result remaining]))))
