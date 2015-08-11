(ns component-demo.app.students
  "Student access."
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]))


(defn find-all
  [conn]
  (let [query (sql/select * :students)]
    (jdbc/query conn query)))


(defn find-by-id
  [conn id]
  (let [query (sql/select * :students (sql/where {:id id}))]
    (jdbc/query conn query)))
