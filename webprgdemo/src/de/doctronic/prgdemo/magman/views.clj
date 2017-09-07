(ns de.doctronic.prgdemo.magman.views
  "Rendering of HTML pages via multimethod."
  (:require
   [hiccup.page :refer [html5]]))


(defmulti render-content
  (fn [context view-params]
    (:view-id view-params)))



(def view-titles
  {"index"         "Heftübersicht"
   "issue-details" "Heft-Details"})


;; Public API

(defn render
  [context {:keys [view-id] :as view-params}]
  (let [title (view-titles view-id)]
    (html5 [:head
            [:title (str "Zeitschriften Erfassung - " title)]]
           [:body
            [:h1 title]
            (render-content context view-params)])))
