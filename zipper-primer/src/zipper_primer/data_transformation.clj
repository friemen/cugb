(ns zipper-primer.core
  (:require [clojure.java.io :as io]
            [clojure.zip :as zip])
  (:gen-class))

;; -- Data model: ------------------------------------------------------

(def data
  [{:customer 1
    :project "Clojure User Group"
    :team "Clojure"}
   {:customer 1
    :project "Java Web-Services"
    :team "Java"}
   {:customer 2
    :project "Clojure Web-Services"
    :team "Clojure"}
   {:customer 2
    :project "Java User Group"
    :team "Java"}])

;;-- Part 2: Data transformation functions -----------------------------

(defn entity-group-zipper
  [key coll]
  (letfn [(navigable-map? [node]
            (and (map? node)
                 (not (contains? node key))
                 (every? (some-fn map? vector?) (vals node))))
          (make-map-branch [node children]
            (if (map? node)
              (zipmap (keys node) children)
              {key children}))]
    (zip/zipper navigable-map?
                vals
                make-map-branch
                coll)))

(defn group-leaf-by
  [key loc]
  (let [node (zip/node loc)]
    (zip/make-node loc
                   node
                   (group-by key node))))

(defn group-entities-by
  [key loc]
  (->> loc
       (iterate #(zip/next
                  (if (zip/branch? %)
                    %
                    (zip/replace % (group-leaf-by key %)))))
       (filter zip/end?)
       (first)))

(defn group-all-by
  [key coll]
  (->> coll
       (entity-group-zipper key)
       (group-entities-by key)
       (zip/root)))

;; -- Playground -------------------------------------------------------

#_(->> data
       (group-all-by :customer)
       (group-all-by :team))
