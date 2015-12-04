# rdbms-intro

* Some terms
* Managing the schema
* Plain [clojure.java.jdbc](http://clojure.github.io/java.jdbc/)
* [HoneySQL](https://github.com/jkk/honeysql)
* [Yesql](https://github.com/krisajenkins/yesql)
* [aggregate](https://github.com/friemen/aggregate)

For sample code see [playground](src/rdbms/playground.clj)


## Introduction to Connection, Connection Pool and db-spec

The *connection* is the central starting point for
a client to work with a database server.
The function clojure.java.jdbc/get-connection offers
numerous ways to specify how a connection will be obtained.
Here's the documentation copied from the docs of
clojure.java.jdbc/get-connection.

```
db-spec is a map containing values for one of the following parameter sets:

Existing Connection:
:connection (required) an existing open connection that can be used
but cannot be closed (only the parent connection can be closed)

Factory:
:factory (required) a function of one argument, a map of params
(others) (optional) passed to the factory function in a map

DriverManager:
:subprotocol (required) a String, the jdbc subprotocol
:subname (required) a String, the jdbc subname
:classname (optional) a String, the jdbc driver class name
(others) (optional) passed to the driver as properties.

DataSource:
:datasource (required) a javax.sql.DataSource
:username (optional) a String
:password (optional) a String, required if :username is supplied

JNDI:
:name (required) a String or javax.naming.Name
:environment (optional) a java.util.Map

Raw:
:connection-uri (required) a String
Passed directly to DriverManager/getConnection

URI:
Parsed JDBC connection string - see below
String:
subprotocol://user:password@host:post/subname
An optional prefix of jdbc: is allowed.
```

For working with a DB in a backend application the datasource can
also be defined as JNDI object in the application container
(Tomcat, Jetty, JBoss, ...) configuration.
For this purpose db-spec supports a JNDI :name.
This datasource will usually be a pooling datasource, so there
is no need for additional configuration of a pool inside of
your application.


If you want to, you can also create a pooling datasource on your own
by instantiation of a C3P0 ComboPooledDataSource instance.
In this case the db-spec map will contain the :datasource.

For managing your connection pool you should use
[component](https://github.com/stuartsierra/component).

## Managing the schema

For developing an initial version of the schema you can write it down
in Clojure data and transform it to statements as in
[schematools](src/rdbms/components/db/schematools.clj) namespace.

A schema in Clojure data might then look like this:

```clojure
(def schema
  [:customer          [(id-column)
                       [:name 'string]]
   :person            [(id-column)
                       [:name 'string]]
   :project           [(id-column)
                       [:name 'string]
                       (fk-column :person :manager_id false)
                       (fk-column :customer false)]
   :task              [(id-column)
                       [:description 'longstring]
                       [:effort 'integer]
                       (fk-column :project false)
                       (fk-column :person :assignee_id false)]
   :person_project    [(fk-column :project false)
                       (fk-column :person false)]])
```

In the long run you will need to use some migration tooling to manage
the evolution of you schema, in other words you will usually store
snippets of SQL statements that, applied in the right order, transform
the schema step-by-step.
