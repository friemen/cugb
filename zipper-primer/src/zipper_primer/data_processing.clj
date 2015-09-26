(ns zipper-primer.core
  (:require [clojure.java.io :as io]
            [clojure
             [xml :as xml]
             [zip :as zip]])
  (:import [java.io ByteArrayInputStream])
  (:gen-class))

;; -- Choose your model: -----------------------------------------------

#_(def data [[1 2] [3 4]])

#_(def data (partition 2 (range 1 5)))

#_(def data
  "<root>
    <branch>
      <node>1</node>
      <node>2</node>
    </branch>
    <branch>
      <node>3</node>
      <node>4</node>
    </branch>
  </root>")

;;-- Increase readability for novices. Don't be ashamed. ---------------

(defn parse-xml-string
  [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

;;-- Part 1: Data processing functions ---------------------------------

(defn leaf-nodes
  [zipper]
  (->> zipper
       (iterate zip/next)
       (take-while (complement zip/end?))
       (filter (complement zip/branch?))
       (map zip/node)))

(defn replace-leaf-nodes
  [zipper]
  (->> zipper
       (iterate (fn [loc]
                  (zip/next
                   (if (zip/branch? loc)
                     loc
                     (zip/replace loc "CUGB")))))
       (filter zip/end?)
       (first)
       (zip/root)))

;; -- Playground -------------------------------------------------------

#_(-> data
    (zip/vector-zip)
    (zip/next)
    (zip/next)
    (zip/node))

#_(-> data
    (zip/vector-zip)
    (leaf-nodes))

#_(-> data
    (zip/seq-zip)
    (leaf-nodes))

#_(xml/emit (replace-leaf-nodes
           (zip/xml-zip (parse-xml-string data))))
