(ns de.doctronic.prgdemo.magman.database
  "Database access functions")

;; Only a fake implementation

(def app-state
  (atom {:issues {1 {:id         1
                     :journal-id "Zeitschrift 1"
                     :year       "2017"
                     :number     "01"}}}))


(defn- new-id
  []
  (let [max-id
        (some->> (:issues @app-state)
                 (keys)
                 (seq)
                 (reduce max))]
    (inc (or max-id 0))))


(defn- ensure-id
  [entity]
  (update entity :id #(if (number? %) % (new-id))))



;; Public API

(defn update-issue
  [issue]
  (let [{:keys [id] :as issue}
        (ensure-id issue)]

    (swap! app-state
           assoc-in [:issues id] issue)
    issue))


(defn delete-issue
  [issue-id]
  (swap! app-state
         update :issues dissoc issue-id)
  nil)
