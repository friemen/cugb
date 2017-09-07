(ns specter.core
  (:require [com.rpl.specter :as sp]
            [clojure.string :as str]))


;; What is Specter?
;; It's update-in on steroids!

(comment

  ;; General structure on how the API works

  ;; (sp/OPERATION navigator-spec value-or-function data-structure)


  ;; OPERATIONs (most important)
  ;;  select
  ;;  setval
  ;;  transform

  ;; for the list of navigators see
  ;; https://github.com/nathanmarz/specter/wiki/List-of-Navigators

  ,,,)

(defn bar1?
  [{:keys [bar]}]
  (= bar "1"))

(def data
  {:foo [{:bar "1"
          :baz "EDIT ME"}
         {:bar "2"}
         {:bar "1"
          :baz "EDIT ME, TOO!"}]})

(get-in data [:foo 0 :bar])

(sp/select [:foo sp/ALL] data)

(sp/setval [:foo sp/ALL bar1? :baz] "OK" data)



(def m {:foo "FOO" :bar "BAR"})

(defn map-vals
  [f m]
  (->> m
       (map (fn [[k v]]
              [k (f v)]))
       (into {})))


(map-vals str/lower-case m)


(map-vals (fn [foos]
            (->> foos
                 (map (fn [{:keys [bar baz] :as foo}]
                         (if (= bar "1")
                           (assoc foo :baz "OK")
                           foo)))
                 (into (empty foos))))
          data)

(def mylist '(1 2 3 4 5))

(sp/transform [sp/ALL] inc mylist)






















(comment

  ;; try out select
  (sp/select :foo {:foo "FOO"})

  (sp/select [(sp/srange 1 2) sp/FIRST] [{:foo "FOO1"} {:foo "FOO2"}])

  ;; task:
  ;; replace :baz values where :bar equals "1"

  ;; try to find things
  (sp/select [:foo sp/ALL bar1?] data)

  ;; apply transformation
  (sp/transform [:foo sp/ALL bar1?] #(assoc % :baz "OK")  data)

  ;; does the same, but is more concise
  (sp/setval [:foo sp/ALL bar1? :baz] "OK"  data)


  ;; same result, but using update-in
  (update-in data [:foo]
          (fn [foos]
            (->> foos
                 (mapv #(cond-> %
                          (bar1? %)
                          (assoc :baz "OK"))))))


  ,,,)



(comment

  ;; task: prepend an item
  (sp/transform [:foo sp/BEGINNING] #(conj % {:bar "0"}) data)

  ;; task: append an item
  (sp/transform [:foo sp/END] #(conj % {:bar "3"}) data)

  ;; what happens if :foo is empty?
  (sp/transform [:foo sp/END] #(conj % {:bar "3"}) {:foo []})

  ;; what happens if :foo doesn't exist?
  (sp/transform [:foo sp/END] #(conj % {:bar "0"}) {})


  ;; setval's behaviour with END/BEGINNING is a bit surprising, it assumes value to be a sequence
  (sp/setval [:foo sp/END] {:bar "3"} {})

  (sp/setval [:foo sp/END] {:bar "3"} data)

  (def transformer (partial sp/setval* [:foo sp/END] {:bar "3"}))
  (transformer data)

  ;; better:
  (sp/setval [:foo sp/END] [{:bar "3"}] data)
  (sp/setval [:foo sp/BEGINNING] [{:bar "-1"} {:bar "0"}] data)

  ,,,)



(comment
  ;; define your own navigator snippets
  (defn nav-nth
    [n]
    [(sp/srange n (inc n)) sp/FIRST])

  (sp/select-one (concat (nav-nth 0) [:foo]) [{:foo "FOO1"} {:foo "FOO2"}])


  ,,,)
