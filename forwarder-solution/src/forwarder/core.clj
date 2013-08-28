(ns forwarder.core
  (:require [clojure.tools.logging :refer [debug]])
  (:import [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

;; timer functions

(defn create-timer
  "Creates a new timer with a task that can be started or stopped to run regularly."
  ([task]
     (create-timer task 1))
  ([task seconds]
     {:executor (atom nil)
      :seconds seconds
      :task task}))


(defn stopped?
  "True, if the timer does not run."
  [t]
  (-> t :executor deref nil?))


(defn start-timer
  "Schedules the task to run regularly with a fixed period between the starts."
  [t]
  (when (stopped? t)
    (debug "Starting timer" t)
    (reset! (:executor t) (ScheduledThreadPoolExecutor. 1))
    (.scheduleAtFixedRate @(:executor t) (:task t) 0 (:seconds t) TimeUnit/SECONDS)))


(defn stop-timer
  "Shuts the timer down."
  [t]
  (when (not (stopped? t))
    (.shutdown @(:executor t))
    (debug "Stopped timer" t)
    (reset! (:executor t) nil)))

;; helpers

(defn popn
  "Removes n items from the beginning of the queue."
  [n queue]
  (->> queue
       (iterate pop)
       (take (inc n))
       last))

;; forwarder functions

(defn create-forwarder
  "Creates a forwarder that upon (forward fwd msg) invokes the function f with
   a single argument on a different thread.
   If the previous invocation of f happened more than the specified number of
   seconds in the past, f is invoked immediately. Otherwise f is invoked
   the specified number of seconds after its previous invocation."
  [f seconds]
  (let [queue-atom (atom (clojure.lang.PersistentQueue/EMPTY))
        timer (create-timer nil seconds)
        task (fn []
               (let [msgs (seq @queue-atom)]
                 (if (empty? msgs)
                   (stop-timer timer)
                   (try (do
                          (f msgs)
                          (debug "Forward succeeded")
                          (swap! queue-atom (partial popn (count msgs))))
                      (catch Exception ex
                        (debug "Forward failed" ex))))))]
    {:queue queue-atom :timer (assoc timer :task task)}))


(defn forward
  "Forwards the msg to the function specified for forwarder fwd."
  [fwd msg]
  (swap! (:queue fwd) #(conj % msg))
  (debug "Added message" msg)
  (start-timer (:timer fwd)))


(defn waiting
  "Returns the messages currently waiting in the queue of forwarder fwd."
  [fwd]
  (into [] (-> fwd :queue deref)))


(defn stop-forwarder
  "Stops the scheduler of forwarder fwd.
   An already started forward task is not cancelled."
  [fwd]
  (-> fwd :timer stop-timer))
