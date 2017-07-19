(ns re-frame-primer.subscriptions
  (:require
   [re-frame.core :refer [reg-sub subscribe]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Basic Subscriptions

(reg-sub :db
  (fn [db _]
    db))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Slides

(reg-sub :app.slide.back-button/disabled?
  (fn [db _]
    (let [!current-slide
          @(subscribe [:app.slide/current])]

      (>= 1 !current-slide))))

(reg-sub :app.slide/current
  (fn [db _]
    (get-in db [:app :slide :current])))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Test-State

(reg-sub :test-state
  (fn [db [_ v]]
    (:test-state db)))

(reg-sub :test-state/transformed
  (fn [db _]
    (let [!test-state
          @(subscribe [:test-state])]

      (* !test-state !test-state))))
