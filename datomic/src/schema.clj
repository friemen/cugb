(ns schema
  "Schema declaration and database initialization (drop + create)."
  (:require [datomic.api :refer [db q] :as d]))

(defn attribute [ident]
  {:db.install/_attribute :db.part/db
   :db/id (d/tempid :db.part/db)
   :db/ident ident})

(defn docstring [a docstring]
  (assoc a :db/doc docstring))

(defn type-string
  [a]
  (assoc a :db/valueType :db.type/string))

(defn type-ref
  [a]
  (assoc a :db/valueType :db.type/ref))

(defn component
  [a]
  (assoc a :db/isComponent true))

(defn cardinality-one [a]
  (assoc a :db/cardinality :db.cardinality/one))

(defn cardinality-many [a]
  (assoc a :db/cardinality :db.cardinality/many))

(def schema [(-> (attribute :project/name)
                 (docstring "The name of the project")
                 type-string
                 cardinality-one)
             (-> (attribute :project/release) type-ref cardinality-many component)
             (-> (attribute :project/member) type-ref cardinality-many component)
             (-> (attribute :release/name) type-string cardinality-one)
             (-> (attribute :release/task) type-ref cardinality-many component)
             (-> (attribute :release/member) type-ref cardinality-many)
             (-> (attribute :task/summary) type-string cardinality-one)
             (-> (attribute :member/name) type-string cardinality-one)
             (-> (attribute :member/watched-task) type-ref cardinality-many component)])

(def tempid
  (partial d/tempid :db.part/user))

(defn create!
  [uri schema]
  (let [created? (d/create-database uri)
        conn (d/connect uri)]
    (when created? 
      @(d/transact conn schema))
    conn))

(defn drop!
  [uri]
  (d/delete-database uri))
