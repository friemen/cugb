(ns transducers-intro.naive.step-8
  "Transducible context for RxJava"

  (:require
   [rx.lang.clojure.interop :as rx]
   [transducers-intro.rx :as r]
   [transducers-intro.naive.step-7 :refer :all]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Convenience

(defn subscribe!
  [observable]
  (println :==================)
  (.subscribe observable (rx/action* println)))

(def xform
  (comp (ttake 12)
        (tfilter even?)
        (tmap vector)
        (tpartition 2)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Playground

(comment

  (-> (r/new-interval)
      (.take 12)
      (.filter (rx/fn* even?))
      (.map (rx/fn* vector))
      (subscribe!))


  (-> (r/new-interval)
      (r/transduce xform)
      (subscribe!))


  (-> (r/new-interval)
      (.map (rx/fn* inc))
      (r/transduce xform)
      (.take 2)
      (subscribe!))

  ,,,)
