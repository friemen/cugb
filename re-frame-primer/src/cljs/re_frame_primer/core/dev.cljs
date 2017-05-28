(ns re-frame-primer.core.dev
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch-sync]]

   ;; NOTE: Make sure handler and subscriptions are required so they will be loaded
   [re-frame-primer.handler]
   [re-frame-primer.subscriptions]

   [re-frame-primer.views.app :as app]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Mount

(defn- mount!
  []
  (reagent/render
    [app/view]
    (gdom/getElement "app")))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Figwheel

(defn on-jsload
  []
  (js/console.clear)
  (mount!))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Init

(def ^:private seed
  {:test-state
   0

   :app
   {:slide
    {:current 1}}})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Main

(defn ^:export main
  []
  (dispatch-sync [:db/initialize seed])
  (mount!))
