# Introduction to relational database access


Content of the rdbms namespaces:
* h2 - Start and stop H2 DB server.
* ds - Configuration of the datasource.
* schema - Create / drop DB schema.
* entities - Korma entity model for schema.


## Getting started 

### Setting up an in-process database

You can start and stop an in-process H2 DB in the REPL by using two functions
from the h2 namespace:
```clojure
(require 'h2)
;= nil
(h2/start-db)
; Starting DB, web console is available on localhost:8082
;= nil
```

To shut the DB down use `(h2/stop-db)`.

### Connection, Connection Pool and db-spec

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

For working with a DB in a backend application the datasource is
usually defined as JNDI object in the application container
(Tomcat, Jetty, JBoss, ...) configuration.
For this purpose db-spec supports a JNDI :name.
This datasource will usually be a pooling datasource, so there
is no need for additional configuration of a pool inside of
your application.


If you want to you can also create a pooling datasource on your own
by instantiation of a C3P0 ComboPooledDataSource instance.
In this case the db-spec map will contain the :datasource.

This is demonstrated in the ds namespace.


### Setting up a schema

The DB schema can be expressed by pure Clojure data:

```clojure
(def schema {:project     [(id-column)
                           [:name "varchar(30)"]]
             :release     [(id-column)
                           (fk-column :project true)
                           [:name "varchar(30)"]]
             :member      [(id-column)
                           [:name "varchar(30)"]
                           (fk-column :project true)]
             :task        [(id-column)
                           [:summary "varchar(100)"]
                           (fk-column :release true)
                           (fk-column :member :owner_id false)]
             :watchers    [(fk-column :member :member_id true)
                           (fk-column :task :task_id true)]})
```						   

In the example above lengthy recurring declarations for
primary or foreign key columns were replaced by calls to functions
`id-column` and `fk-column`
(see the schema namespace for the complete picture).

Two additional functions will actually perform the SQL DDL statements:

```clojure
(defn create!
  [db-spec]
  (doseq [t (map (fn [[k v]] (cons k v)) schema)]
      (jdbc/execute! db-spec [(apply ddl/create-table t)]))
  (jdbc/execute! db-spec ["create sequence pkseq"]))


(defn drop!
  [db-spec]
  (doseq [t (keys schema)]
    (jdbc/execute! db-spec [(ddl/drop-table t)]))
  (jdbc/execute! db-spec ["drop sequence pkseq"]))
```

With a schema definition and those functions you can create or drop 
the complete schema very easily:

```clojure
(require 'schema 'ds)
;= nil
(schema/create! ds/db-spec)
;= (0)
(schema/drop! ds/db-spec)
;= (0)
```


### Working with the DB

Now we have a means to connect to the DB and to create a schema.
To add data or query existing data we can directly use `query`, 
`insert!`, `update!` and `delete!` from clojure.java.jdbc.


The data structure to pass to insert! or update! is usually a map.

```clojure
(require '[clojure.java.jdbc :as jdbc])
;= nil
(jdbc/insert! ds/db-spec :project {:name "FooBar"})
;= ({:scope_identity() 1})
```

The data structure returned by query is a seq of maps:

```clojure
(require '[clojure.java.jdbc.sql :as sql])
;= nil
(jdbc/query ds/db-spec (sql/select * :project (sql/where {:name "FooBar"})))
;= ({:name "FooBar", :id 1})
```

Update and delete work like expected:

```clojure
(jdbc/update! ds/db-spec :project {:name "Baz"} (sql/where {:id 1}))
;= (1)
(jdbc/query ds/db-spec (sql/select * :project))
;= ({:name "Baz", :id 1})
(jdbc/delete! ds/db-spec :project (sql/where {:id 1}))
;= (1)
```


### Using Transactions

To wrap function execution inside a DB transaction use 
clojure.java.jdbc/db-transaction* function. The passed function must
accept the db connection as single argument.

```clojure
(jdbc/db-transaction* ds/db-spec #(jdbc/insert! % :project {:name "FooBar"}))
;= ({:scope_identity() 2})
```

It makes sense to use robert.hooke to augment those functions 
that are the entry points to a system with cross-cutting solutions 
like TX or error handling.


## Preserving purity

In general we prefer pure functions that must not access db state (neither read nor write).
But in addition there must be functions that are the glue between pure functions and the stateful world.
This gives an application a different structure than in an OO / imperative world.


## Korma

[Korma](http://sqlkorma.com/) is a DSL to define table metadata and prepare queries and
statements to make working with SQL easier.

To give it a try enter in the REPL:

```clojure
(use 'entities 'korma.core)
;= nil
(select project)
;= [{:NAME "Baz", :ID 1} {:NAME "FooBar", :ID 2}]
```

A very helpful [introduction-by-examples](http://sqlkorma.com/docs) is available.


## Excercises
1. Create simple schema to hold messages for webapp.
2. Write functions that add a message to DB and query all messages, you can use Korma or clojure.java.jdbc.
3. Integrate this into your webapp.
 

## Discussion of typical JPA features

* Unit of work / identity map
* DDL generator
* SQL/DML generator
* Database dialect abstraction
* Support for different locking strategies
* Support for cursors
* Mapping
  * Type conversion
  * Read/write column values from/to bean properties
  * Association mapping: how are 1:1, 1:n, m:n associations mapped to relational tables with foreign keys?
  * Inheritance mapping

For Clojure a subset of these features would be nice:
 * How are type conversions (i.e. to/from java.sql.Date) handled?
 * How can loading / saving of nested data structures be supported?
 * What is the right approach to implement optimistic concurrency control?

## License

Copyright © 2013 F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
