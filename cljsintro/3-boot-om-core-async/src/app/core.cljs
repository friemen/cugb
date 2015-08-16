(ns app.core
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]))

(defn widget [data owner]
  (om/component
   (html [:h2 (:text data)])))


(defonce app-state (atom {:text "Hello OM World!!!"}))

(defn refresh []
  (om/root widget app-state
           {:target (. js/document (getElementById "app"))}))

(refresh)
