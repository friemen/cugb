(ns rdbms.playground
  (:require [rdbms.components.db.schema :refer [schema]]
            [rdbms.components.db.schematools :as st]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as hsql]
            [honeysql.helpers :as hsh]
            [yesql.core :refer [defqueries]]
            [aggregate.core :as agg]))


(comment
  ;; preparation
  ;; make sure you have started the system via (user/system-go!)

  (def pool (-> user/system :dbpool))

  ;; reset schema in one go
  (st/reset-schema! pool schema st/h2-dialect)
,,,)


(comment
  ;; plain clojure.java.jdbc

  ;; insert a record by a plain execute
  (jdbc/with-db-connection [c pool]
    (jdbc/execute! c ["INSERT INTO PERSON (ID,NAME) VALUES (1,'Hans')"]))

  ;; BUT don't do this at home, it makes you vulnerable to SQL injection!


  ;; jdbc/insert!
  (jdbc/with-db-connection [c pool]
    (jdbc/insert! c :person {:id 2 :name "Max"}))


  ;; jdbc/delete!
  (jdbc/with-db-connection [c pool]
    (jdbc/delete! c :person ["id = ?" 1]))


  ;; query table
  (jdbc/with-db-connection [c pool]
    (jdbc/query c ["SELECT * FROM PERSON"]))

,,,)


(comment
  ;; transactions!

  (jdbc/with-db-transaction [tx pool]
    (jdbc/query tx ["SELECT * FROM PERSON"]))

  ;; Be aware that query returns a lazy sequence!
  ;; use doall to make sure that you realized the whole sequence
  ;; before transaction is committed


  (jdbc/with-db-transaction [tx pool]
    (jdbc/insert! tx :person {:id 3 :name "Moritz"})
    (jdbc/insert! tx :person {:id 4 :name "Klaus"}))

  (jdbc/with-db-transaction [tx pool]
    (jdbc/delete! tx :person ["id = ?" 3])
    (jdbc/delete! tx :person ["id = ?" 4]))

  ;; In a real world application the tx is obtained
  ;; when a request enters the system.

  ;; To make the tx available to many functions
  ;; you can either pass tx as parameter down into functions,
  ;; or attach the tx to the Ring request map,
  ;; or hold it in a dynamic var.

  ;; BUT: Having many functions deal with the DB
  ;;      undermines the main idea of functional
  ;;      programming!!!


  ;; Remember that requests are just like events,
  ;; and FP prefers a [state event] -> state'
  ;; style of organizing logic in a system (without side-effects).
  ;; Thus a system can be seen as one BIG reduction function!
  ;; State is then the fusion of DB and Session state.

  ;; IDEALLY: Relevant state from DB is queried
  ;;      at the beginning of request processing.
  ;;      And all results of the processing are
  ;;      finally merged into DB.

,,,)


(comment
  ;; Queries as data == composable queries !
  ;; Meet HoneySQL

  (def q-person {:select [:id :name]
                 :from [:person]})

  (hsql/format q-person)

  (jdbc/with-db-connection [c pool]
    (jdbc/query c (-> q-person
                      (hsh/merge-where [:= :id 2])
                      (hsql/format))))

  (jdbc/with-db-connection [c pool]
    (jdbc/query c (-> q-person
                      (hsh/select :%count.*)
                      (hsql/format))))

,,,)


(comment
  ;; But I already have my SQL statements made-up!
  ;; Meet Yesql

  (defqueries "queries.sql")

  (jdbc/with-db-connection [c pool]
    (q-person-by-id {:id 2} {:connection c}))

,,,)


(comment
  ;; I have complex relationships and need a full push-pull roundtrip
  ;; Meet aggregate


  (def er
    (agg/make-er-config
     (agg/entity :customer
                 (agg/->n :projects :project {:fk-kw :customer_id}))
     (agg/entity :person
                 (agg/->n :tasks :task {:fk-kw :assignee_id
                                        :owned? false})
                 (agg/->n :projects_as_manager :project {:fk-kw :manager_id
                                                         :owned? false})
                 (agg/->mn :projects_as_member :project))
     (agg/entity :project
                 (agg/->1 :customer :customer {:owned? false})
                 (agg/->mn :members :person {:query-fn (agg/make-query-<many>-fn
                                                        :person
                                                        :person_project
                                                        :project_id
                                                        :person_id)
                                             :update-links-fn (agg/make-update-links-fn
                                                               :person_project
                                                               :project_id
                                                               :person_id)})
                 (agg/->1 :manager :person {:owned? false})
                 (agg/->n :tasks :task {:fk-kw :project_id}))
     (agg/entity :task
                 (agg/->1 :project :project {:owned? false})
                 (agg/->1 :assignee :person {:owned? false}))))


  (def p1 {::agg/entity :person
           :name "Carlo"
           :tasks [{:description "Lesen"}
                   {:description "Lernen"}]})


  (jdbc/with-db-connection [c pool]
    (agg/save! er c p1))


  (jdbc/with-db-connection [c pool]
    (agg/load (-> er (agg/only [:person :tasks])) c :person 1))

  ;; The interesting question here is:
  ;; can we fuse HoneySQL and aggregate for a
  ;; GraphQL like behaviour?

,,,)
