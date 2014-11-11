(ns helloom.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [schema.macros :as sm])
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [cljs.core.async :refer [put! chan <!]]
            [schema.core :as s :include-macros true]))

(enable-console-print!)


;; ---------------------------------------------------------------------------
;; Generic component with a dispatching controller
;; Takes an actions map and a render function

(defcomponent view-with-controller
  [state owner {:keys [actions render-fn] :as options}]
  (init-state [_]
              {:ch (chan)})
  (will-mount [_]
              (let [ch (om/get-state owner :ch)]
                (go-loop []
                  (let [evt       (<! ch)
                        action-fn (get actions (:action evt))]
                    (if action-fn
                      (om/transact! state #(action-fn % evt))
                      (prn (str "WARNING" (:action evt) " is unknown"))))
                  (recur))))
  (render-state [_ {:keys [ch]}]
   (render-fn state ch)))



;; ---------------------------------------------------------------------------
;; App specific code

(def Counter {:clicks js/Number})


(def app-state (atom {:text "Hello Om World!"
                      :counter {:clicks 0}}))


(sm/defn ^:always-validate render-click-counter
  [state :- Counter ch]
  (dom/div
    (dom/input {:type "button"
                :value "Hit me!"
                :on-click #(put! ch {:action :click})})
    "Hits " (:clicks state)))


(sm/defn ^:always-validate inc-clicks
  [state :- Counter evt]
  (update-in state [:clicks] inc))


(defcomponent header
  "A header component."
  [state owner options]
  (render [_]
          (dom/h1 (:text state))))


(defcomponent page
  [state owner options]
  (render [_]
          (dom/div
           (om/build header state)
           (om/build view-with-controller (:counter state)
                     {:opts {:actions {:click inc-clicks}
                             :render-fn render-click-counter}}))))


(om/root
  page
  app-state
  {:target (. js/document (getElementById "app"))})
