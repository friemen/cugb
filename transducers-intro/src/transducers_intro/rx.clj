(ns transducers-intro.rx
  (:refer-clojure
   :exclude [transduce])

  (:require
   [rx.lang.clojure.interop :as rx])

  (:import
   rx.Observable
   java.util.concurrent.TimeUnit))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Rx Convenience

(defn new-interval
  ([]
   (new-interval 65))

  ([ms]
   (Observable/interval ms TimeUnit/MILLISECONDS)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transducible Context

(defn transduce
  [observable-in xform]
  (Observable/create
   (rx/action [^rx.Subscriber subscriber]
              (let [!continue
                    (volatile! true)

                    rf
                    (xform (fn
                             ([acc]
                              acc)

                             ([acc x]
                              (.onNext acc x)
                              acc)))]

                (-> observable-in
                    (.takeWhile (rx/fn [_]
                                  @!continue))

                    (.subscribe (rx/action [x]
                                           (when (reduced? (rf subscriber x))
                                             (vreset! !continue false)
                                             (rf subscriber)
                                             (.onCompleted subscriber)))))))))
