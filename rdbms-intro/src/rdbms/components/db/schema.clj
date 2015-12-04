(ns rdbms.components.db.schema
  (:require [rdbms.components.db.schematools :refer [id-column fk-column]]))


(def schema
  [:customer          [(id-column)
                       [:name 'string]]
   :person            [(id-column)
                       [:name 'string]]
   :project           [(id-column)
                       [:name 'string]
                       (fk-column :person :manager_id false)
                       (fk-column :customer false)]
   :task              [(id-column)
                       [:description 'longstring]
                       [:effort 'integer]
                       (fk-column :project false)
                       (fk-column :person :assignee_id false)]
   :person_project    [(fk-column :project false)
                       (fk-column :person false)]])
