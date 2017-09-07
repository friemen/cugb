(ns de.doctronic.prgdemo.magman.views.index
  "Render Heftübersicht page."
  (:require
   [de.doctronic.prgdemo.magman.views :as views]
   [de.doctronic.prgdemo.magman.database :as db]
   [de.doctronic.prgdemo.magman.util :as u]))


(defmethod views/render-content "index"
  [context view-params]
  (let [issues
        (->> (:issues @db/app-state)
             (vals)
             (sort-by (juxt :year :number))
             (reverse))]
    [:div
     [:table
      [:thead
       [:th "Heft-Id"]
       [:th "Zeitschrift"]
       [:th "Jahr"]
       [:th "Number"]]
      [:tbody
       (for [{:keys [id journal-id year number]} issues]
         [:tr
          [:td [:a {:href (u/to-view "issue-details" {:id id})} id]]
          [:td journal-id]
          [:td year]
          [:td number]])]]
     [:a {:href (u/to-view "issue-details" {:id "new"})} "Neues Heft anlegen"]]))
