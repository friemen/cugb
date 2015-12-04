(ns helloworld.app
  (:require [hiccups.runtime])
  (:require-macros [hiccups.core :refer [html]]))

(enable-console-print!)


(defn by-id
  [id]
  (.. js/document (getElementById id)))


(def app-state (atom {:todos [{:id 1 :desc "Foo"}
                              {:id 2 :desc "Bar"}]}))


(defn render [state]
  (html [:h1 "What has to be done?"]
        (for [{:keys [id desc]} (:todos state)]
          [:div {:class "mui-row"}
           [:div {:class "mui-col-md-1"}
            id]
           [:div {:class "mui-col-md-4"}
            [:input {:id (str "todo-desc-" id)
                     :type "text"
                     :class "mui-form-control"
                     :value desc}]]
           [:div {:class "mui-col-md-1"}
            [:button {:id (str "delete-todo-" id)
                      :class "mui-btn mui-btn-raised"} "Delete"]]])
        [:button {:id "add-todo"
                  :class "mui-btn mui-btn-raised"} "Add Item"]))

(defn on-click!
  [id f]
  (set! (.. (by-id id) -onclick) f))

(defn on-blur!
  [id f]
  (set! (.. (by-id id) -onblur)
        (fn [evt]
          (f (.. evt -target -value)))))

(defn new-todo
  [state todo]
  (let [new-id (->> state
                    :todos
                    (map :id)
                    (reduce max 0)
                    (inc))]
    (update state :todos conj (assoc todo :id new-id))))


(defn delete-todo
  [state id]
  (update state :todos
          (fn [todos]
            (->> todos
                 (remove #(= (:id %) id))
                 (vec)))))


(defn update-todo
  [state id new-value]
  (update state :todos
          (fn [todos]
            (->> todos
                 (map (fn [todo]
                        (if (= (:id todo) id)
                          (assoc todo :desc new-value)
                          todo)))))))

(defn attach-listeners!
  [state]
  (on-click! "add-todo" (fn [_]
                          (.log js/console "Add Todo")
                          (swap! app-state new-todo {:desc "New"})))
  (doseq [{:keys [id]} (:todos state)]
    (on-blur! (str "todo-desc-" id)
              (fn [v]
                (.log js/console "Update Description")
                (swap! app-state update-todo id v)))
    (on-click! (str "delete-todo-" id)
               (fn [_]
                 (.log js/console "Add Todo")
                 (swap! app-state delete-todo id)))))


(defn refresh
  [state]
  (let [app (.. js/document (getElementById "app"))]
    (set! (.. app -innerHTML) (render state))
    (attach-listeners! state)))


(defn init []
  (add-watch app-state :render
             (fn [k r o new-state]
               (refresh new-state)))
  (refresh @app-state))





;; Now we need to understand the language a bit better!!!

;; - Namespaces
;; - JS interop forms
;; - def, fn, defn, defn-
;; - Data (Strings, Numbers, Vectors, Maps, Sets)
;; - Atoms, Identity vs. State
;; - Sequences, map, filter, take, drop, reduce, into
;; - let and destructuring
;; - Threading Macros


;; Steps to evolve this example
;; - Introduce Hiccup-style data markup
;; - Introduce app-state atom and how to define global state
;; - Implement rendering of Todos
;; - Add watch on app-state
;; - Implement refresh and change init
;; - Demonstrate re-rendering when app-state changes
;; - Introduce on-click! and on-blur!
;; - Implement deletion of an item


;; helpers



;; functions on state
