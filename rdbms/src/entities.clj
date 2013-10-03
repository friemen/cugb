(ns entities
  "Define Korma metadata for schema"
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [korma.config :refer [set-delimiters]]
            [ds :refer [db-spec]]))



(defdb db db-spec)
(set-delimiters "")

(declare project release member watcher task)


(defentity project
  (table :project)
  (has-many release)
  (has-many member))


(defentity release
  (table :release)
  (belongs-to project)
  (has-many task))


(defentity member
  (table :member)
  (belongs-to project))


(defentity watcher
  (table :member)
  (many-to-many task :watchers))


(defentity task
  (table :task)
  (belongs-to release)
  (belongs-to member {:fk :owner_id})
  (many-to-many watcher :watchers))

