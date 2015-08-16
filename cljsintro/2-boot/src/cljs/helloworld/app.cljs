(ns helloworld.app
  (:require [hiccups.runtime])
  (:require-macros [hiccups.core :as hiccups :refer [html]]))


;; "business" functions

(defn add-user
  [state name]
  (update state :users conj {:name name}))


;; global state

(defonce app-state (atom {:users [{:name "Bob"}]}))


;; rendering code

(defn render-users
  [users]
  [:table
   (map (fn [user]
          [:tr [:td (:name user)]])
        users)])

(defn render-form
  [state]
  (html [:input {:id "user-name" :type "text"}]
        [:input {:id "add-user" :type "button" :value "Add user"}]
        (render-users (:users state))))

(defn render
  [state]
  (let [app (.. js/document (getElementById "app"))]
    (set! (.. app -innerHTML)
          (render-form state))
    (set! (.. js/document (getElementById "add-user") -onclick)
          (fn [event]
            (let [user-name (.. js/document (getElementById "user-name") -value)]
              (swap! app-state add-user user-name))))))



;; plumbing


(add-watch app-state
           :render
           (fn [key ref old-state new-state]
             (render new-state)))

(defn init []
  (render @app-state))
