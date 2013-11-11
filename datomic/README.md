
# Introduction to the Datomic database system

## Getting started

### Datomic Installation & Setup

[Download](https://my.datomic.com/downloads/free) the latest Datomic version & unzip.

Set JAVA_HOME & PATH environment variables

    export JAVA_HOME=/path/to/jdk
    export PATH=$JAVA_HOME/bin:$PATH

For Windows have a look at the [Getting Started](http://docs.datomic.com/getting-started.html) guide.

Start the transactor from the Datomic folder

    bin/transactor config/samples/free-transactor-template.properties

Datomic should print console output ending with this line:

    System started datomic:free://localhost:4334/<DB-NAME>

### Datomic Console Installation & Setup

The Datomic Console can be [downloaded](https://my.datomic.com/downloads/console) for free after [signing up](https://my.datomic.com/account/create) with Datomic.

Unzip the file and run the following command to install the Datomic Console:

    bin/install-console path-to-datomic-directory

Switch to your Datomic directory and run:

    bin/console -p 8090 local datomic:free://localhost:4334/

Open http://localhost:8090/browse in your browser to see the Console.

### Datomic documentation

API docs are available at http://docs.datomic.com/clojure/index.html

### Database Connections

Datomic does not handle the disk persistence itself but delegates it to a storage provider. It supports several different storage providers:

```
DynamoDB:
datomic:ddb://[aws-region]/[dynamodb-table]/[db-name]?aws_access_key_id=[XXX]&aws_secret_key=[YYY]

Riak:
datomic:riak://host[:port]/bucket/dbname[?interface=http|protobuf]
(interface defaults to protobuf)

Couchbase
datomic:couchbase://host/bucket/dbname[?password=xxx]

SQL:
datomic:sql://[db-name][?jdbc-url]

Infinispan:
datomic:inf://[cluster-member-host:port]/[db-name]

Dev Appliance: 
datomic:dev://[transactor-host:port]/[db-name]

Free transactor integrated storage:
datomic:free://[transactor-host:port]/[db-name]

In-process Memory:
datomic:mem://[db-name]
```
The in-memory version does not require a running transactor. The free transactor uses a builtin H2 database for storage.

Cognitect Inc recently released a free [Datomic Pro Starter Edition](https://my.datomic.com/starter) that supports all storage providers without any fee.

### Setting up a schema

Datomic DB schemas are expressed by pure Clojure data:

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

All functions properly handle the DB connection:
If db-spec contains a connection then this is reused. If it doesn't a
new connection is obtained at the beginning and returned before the 
function terminates.


### Using Transactions

To wrap function execution inside a DB transaction use 
clojure.java.jdbc/db-transaction* function. The passed function must
accept the db connection as single argument.

```clojure
(jdbc/db-transaction* ds/db-spec #(jdbc/insert! % :project {:name "FooBar"}))
;= ({:scope_identity() 2})
```

It makes sense to use [robert.hooke](https://github.com/technomancy/robert-hooke/) 
to augment those functions that are the entry points to a system with cross-cutting 
solutions like TX or error handling.


## Be careful with Laziness

To avoid realization of a lazy sequence at a point in time when a 
transaction isn't available any more use `(doall ...)` to materialize
all items of a collection before the TX is committed.


## Preserve purity

In general we prefer pure functions that must not access DB state (neither read nor write).
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
