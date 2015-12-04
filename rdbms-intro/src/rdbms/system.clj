(ns rdbms.system
  (:require [com.stuartsierra.component :as c]
            [rdbms.components.db :as db]
            [rdbms.components.dbpool :as dbpool]))


(def default-config
  {:db
   {:uri "jdbc:h2:tcp://localhost/~/test"
    :username "sa"
    :password ""}})


(defn new-system
  [config]
  (c/system-map
   :db    (c/using
           (db/new-database)
           [])
   :dbpool  (c/using
           (dbpool/new-connection-pool (:db config))
           [])))
