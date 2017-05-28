(ns re-frame-primer.views.app
  (:require
   [re-frame.core :as rf]

   [re-frame-primer.views.slides :as slides]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sub Views

(defn app-state
  []
  (let [!app-state
        (rf/subscribe [:db])]

    (fn []
      [:pre
       (with-out-str (cljs.pprint/pprint @!app-state))])))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; View

(defn view
  []
  [:div
   [app-state]
   [slides/view]])
