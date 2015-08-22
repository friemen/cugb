(ns libdemos.coreasync-demo
  (:require [clojure.core.async :as async :refer [<! >!]]))


(def <ch1 (async/chan))

(defn <print-process
  [<ch]
  (async/go-loop []
    (let [msg (<! <ch)]
      (println msg)
      (if (not= :quit msg)
        (recur)
        (do (println "Done!")
            :finished)))))


(comment
  (<print-process <ch1)

  (async/put! <ch1 :quit)
,,,)



(defn make-stock-quote-provider
  [provider min-sleep max-sleep]
  (fn []
    (Thread/sleep (+ min-sleep (rand-int (- max-sleep min-sleep))))
    [provider (rand-int 100)]))


(def slow-provider (make-stock-quote-provider "slow" 300 600))
(def fast-provider (make-stock-quote-provider "fast" 200 500))


(comment
  (async/alt!! (async/go (slow-provider)) ([v] v)
               (async/go (fast-provider)) ([v] v)
               (async/timeout 400) :timeout)

,,,)
