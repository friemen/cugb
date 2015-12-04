(ns helloworld.app
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [sablono.core :as s :refer-macros [html]]
            [cljs.core.async :as async :refer [put! chan <! >!]]))

(enable-console-print!)

;; Steps
;; - Explain separation of rendering state from event processing
;; - Introduce React
;; - Show om components doing the same rendering
;; - Introduce core.async
;; - Add channel
;; - Add event-handlers (on-change, on-click)
;; - Add go-loop in controller function
;; - Add dispatcher and action functions



(defn todo-comp
  [{:keys [id desc] :as cursor} owner options]
  (om/component
   (html [:div {:class "mui-row"}
          [:div {:class "mui-col-md-1"}
           id]
          [:div {:class "mui-col-md-4"}
           [:input {:id (str "todo-desc-" id)
                    :type "text"
                    :class "mui-form-control"
                    :value desc}]]
          [:div {:class "mui-col-md-1"}
           [:button {:id (str "delete-todo-" id)
                     :class "mui-btn mui-btn-raised"}
            "Delete"]]])))


(defn todo-app
  [{:keys [todos] :as cursor} owner options]
  (om/component
   (html [:div
          [:h1 "Hello ClojureScript World"]
          (om/build-all todo-comp todos {:key :id})
          [:button {:id "add-todo"
                    :class "mui-btn mui-btn-raised"}
           "Add Item"]])))


(def app-state (atom {:todos [{:id 1 :desc "Foo"}
                              {:id 2 :desc "Bar"}]}))



(defn init
  []
  (om/root todo-app app-state {:target (.getElementById js/document "app")
                               :opts {}}))









(comment


  (def events-ch (chan))

  (defn new-todo
    [todos todo]
    (let [new-id (->> todos
                      (map :id)
                      (reduce max 0)
                      (inc))]
      (conj todos (assoc todo :id new-id))))


  (defn delete-todo
    [state id]
    (update state :todos
            (fn [todos]
              (->> todos
                   (remove #(= (:id %) id))
                   (vec)))))


  (defn update-todo
    [todo key new-value]
    (assoc todo key new-value))


  (defn dispatch-event
    [state event]
    (case (:action event)
      "delete-todo" (delete-todo state (:id event))
      "update-todo" (update-todo state (:key event) (:value event))
      "add-todo"    (new-todo state {:desc "New"})
      state))


  (defn controller
    [ch]
    (go-loop []
      (when-let [{:keys [cursor] :as event} (<! events-ch)]
        (om/transact! (or cursor (om/root-cursor app-state))
                      #(dispatch-event % event))
        (recur))
      (.log js/console "Terminating controller")))


  (defn todo-comp
    [{:keys [id desc] :as cursor} owner options]
    (om/component
     (html [:div {:class "mui-row"}
            [:div {:class "mui-col-md-1"}
             id]
            [:div {:class "mui-col-md-4"}
             [:input {:id (str "todo-desc-" id)
                      :type "text"
                      :class "mui-form-control"
                      :value desc
                      :on-change (fn [evt]
                                   (put! events-ch {:cursor cursor
                                                    :action "update-todo"
                                                    :id id
                                                    :key :desc
                                                    :value (.. evt -target -value)}))}]]
            [:div {:class "mui-col-md-1"}
             [:button {:id (str "delete-todo-" id)
                       :class "mui-btn mui-btn-raised"
                       :on-click (fn [_]
                                   (put! events-ch {:action "delete-todo"
                                                    :id id}))}
              "Delete"]]])))


  (defn todo-app
    [{:keys [todos] :as cursor} owner options]
    (om/component
     (html [:div
            [:h1 "Hello ClojureScript World"]
            (om/build-all todo-comp todos {:key :id})
            [:button {:id "add-todo"
                      :class "mui-btn mui-btn-raised"
                      :on-click (fn [_]
                                  (put! events-ch {:cursor todos
                                                   :action "add-todo"}))}
             "Add Item"]])))


  (defn init
    []
    (controller events-ch)
    (om/root todo-app app-state {:target (.getElementById js/document "app")
                                 :opts {}})))
