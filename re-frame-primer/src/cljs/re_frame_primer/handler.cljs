(ns re-frame-primer.handler
  (:require
   [re-frame.core :refer [reg-event-db subscribe reg-event-fx reg-fx]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Basic Handler

(reg-event-db :db/initialize
  (fn [db [_ seed]]
    seed))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Route Handler

(reg-event-db :app.route.current/set
  (fn [db [_ route]]
    (assoc-in db [:app :route :current] route)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Slide

(reg-event-db :app.slide/change
  (fn [db [_ v]]
    (let [!current-slide
          (subscribe [:app.slide/current])]

      (->> (+ @!current-slide v)
           (assoc-in db [:app :slide :current])))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Test-State

(reg-event-db :test-state/inc
  (fn [db [event-name params]]
    (update db :test-state inc)))
