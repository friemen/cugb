(ns helloreagent.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [put! chan <!]]))

(enable-console-print!)

;; ---------------------------------------------------------------------------
;; Generic component with a dispatching controller

(defn view-with-controller
  [initial-state-fn actions render-fn]
  (r/create-class
   {:get-initial-state
    (fn [_]
      {:ch (chan)
       :state (r/atom (initial-state-fn))})
    :component-will-mount
    (fn [this]
      (let [{:keys [state ch]} (r/state this)]
        (go-loop []
          (let [evt (<! ch)
                action-fn (get actions (:action evt))]
            (if action-fn
              (swap! state action-fn evt)
              (prn (str "WARNING: " (:action evt) " is unknown"))))
          (recur))))
    :render
    (fn [this]
      (let [{:keys [state ch]} (r/state this)]
        (render-fn @state ch)))}))


;; ---------------------------------------------------------------------------
;; App specific code

(defn render-click-counter
  [state ch]
  [:div
   [:input {:type "button"
            :value "Hit me!"
            :on-click #(put! ch {:action :click})}]
   [:span "Hits " (:clicks state)]])


(defn inc-clicks
  [state evt]
  (update-in state [:clicks] inc))


(defn header
  []
  (let [state (r/atom {:text "Hello Reagent World!"})]
    (fn []
      [:h1 (:text @state)])))


(defn page
  []
  [:div
   [header]
   [view-with-controller (constantly {:clicks 0})
    {:click inc-clicks}
    render-click-counter]])


(r/render-component
 [page]
 (. js/document (getElementById "app")))
