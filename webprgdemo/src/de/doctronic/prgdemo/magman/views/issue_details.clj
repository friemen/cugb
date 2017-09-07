(ns de.doctronic.prgdemo.magman.views.issue-details
  "Render Heft-Details page."
  (:require
   [hiccup.form :as form]
   [de.doctronic.prgdemo.magman.views :as views]
   [de.doctronic.prgdemo.magman.util :as u]
   [de.doctronic.prgdemo.magman.database :as db]))


(defmethod views/render-content "issue-details"
  [context view-params]
  (let [id
        (-> view-params :id u/str->int)

        {:keys [journal-id year number]}
        (get-in @db/app-state [:issues id])]

    [:div
     (form/form-to
      [:post "/actions/save-issue"]

      (form/hidden-field :id id)

      [:div
       (form/label :journal-id "Zeitschrift")
       (form/drop-down :journal-id [nil "Zeitschrift 1" "Zeitschrift 2"] journal-id)]

      [:div
       (form/label :year "Jahr")
       (form/text-field :year year)]

      [:div
       (form/label :number "Nummer")
       (form/text-field :number number)]

      [:div
       (form/submit-button "Speichern")
       [:a {:href "/views/index"} "Zur Heftübersicht"]])]))
