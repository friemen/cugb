(ns de.doctronic.prgdemo.magman.actions
  "Processing functions for user actions issued via POST request."
  (:require
   [de.doctronic.prgdemo.magman.database :as db]
   [de.doctronic.prgdemo.magman.util :as u]))



(defmulti process!
  (fn [context action-params]
    (:action-id action-params)))



(defmethod process! "save-issue"
  [context action-params]
  (let [{:keys [id journal-id year number]}
        action-params

        issue
        (db/update-issue {:id         (u/str->int id)
                          :journal-id journal-id
                          :year       year
                          :number     number})]
    {:view-id "issue-details"
     :view-params {:id (:id issue)}}))
