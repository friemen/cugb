(ns ds
  "Establish connection to DB, provide TX setup"
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))


(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname "tcp://localhost/~/test"
              :user "sa"
              :password ""})

;; To close a connection use (.close conn)


;; how to setup a connection pool

(defn pooled-ds
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec)) 
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec)))] 
    {:datasource cpds}))


(def connection-pool (delay (pooled-ds db-spec)))

(defn ds-spec
  "Returns a db-spec map with the C3P0 connection pooling datasource."
  []
  @connection-pool)
