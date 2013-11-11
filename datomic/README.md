
# Introduction to the Datomic database system

## Rationale

Datomic is a functional database. Instead of having a global mutable variable, i.e. a traditional database system, that is shared by multiple processes, it separates the identity of the database, e.g. the "customer database hosted on this machine", from the value of the database. Each value is immutable which facilitates reasoning, reproducibility, caching, combining multiple databases etc...

To have immutable database values it is required that you can only add data to the database, thus Datomic is accretive. This allows to perform queries over the history of the data, e.g., how often did the price for a product change and when, how many transactions happened across the last week, retrieve deltas necessary to transition from an old state to the current state, ...

Datomic supports Datalog as query language which follows another design philosophy of Clojure: use data. Datalog querys are data which can be combined with more data into new data. The same holds for Datomic schemas which are also comprised of data.

Datomic has ACID properties including transactions. Every write goes through the transactor that serializes all changes to the system to create a database-wide ordering.

The Datomic Console provides a web-interface for exploring/querying/traversing the persisted data.

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
The in-memory version does not require a running transactor. The free transactor uses a built-in H2 database for storage.

Cognitect recently released a free [Datomic Pro Starter Edition](https://my.datomic.com/starter) that supports all storage providers without any fee.

### Setting up a schema

Datomic DB schemas are expressed by pure Clojure data:

```clojure
[{:db/ident :project/name,
  :db/cardinality :db.cardinality/one,
  :db/valueType :db.type/string,
  :db/id #db/id[:db.part/db],
  :db.install/_attribute :db.part/db,}
 {:db/ident :project/release,
  :db/cardinality :db.cardinality/many,
  :db/valueType :db.type/ref,
  :db/id #db/id[:db.part/db],
  :db/isComponent true,
  :db.install/_attribute :db.part/db}
  ...]
```
This schema can be stored in a plain EDN file.

You can also define a schema in code:

```clojure
(def schema [(-> (attribute :project/name)
                 (docstring "The name of the project")
                 type-string
                 cardinality-one)
             (-> (attribute :project/release) type-ref cardinality-many component)
             (-> (attribute :project/member) type-ref cardinality-many component)
             (-> (attribute :release/name) type-string cardinality-one)
             (-> (attribute :release/task) type-ref cardinality-many component)
             (-> (attribute :release/member) type-ref cardinality-many)
             (-> (attribute :task/summary) type-string cardinality-one)
             (-> (attribute :member/name) type-string cardinality-one)
             (-> (attribute :member/watched-task) type-ref cardinality-many component)])
```						   

In the example above the somewhat verbose schema declarations were replaced by calls to functions
that work on maps (see the schema namespace for the complete picture). Additional helpers could
reduce the repitition further, but this way it is easily extendable because all the
functions just add values to a map, thus handle the schema definition as data.

The following helper functions simplify the creation and deletion of tables.

```clojure
(defn create!
  [uri schema]
  (let [created? (d/create-database uri)
        conn (d/connect uri)]
    (when created? 
      @(d/transact conn schema))
    conn))
    
(defn drop!
  [uri]
  (d/delete-database uri))
```

With a schema definition and those functions you can create or drop 
the complete schema very easily:

```clojure
(require 'schema)
;= nil
(def conn (schema/create! "datomic:mem://projects" schema/schema))
;= #'user/conn
(schema/drop! "datomic:mem://projects")
;= true
```

### Working with the DB

Now we have a means to connect to the DB and to create a schema.
To add data or query existing data we can use `transact` and `q`
from `datomic.api`.

The data structure to pass to a `transact` call is a list

```clojure
(require '[datomic.api :refer [db q] :as d])
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

## Excercises
1. Create simple schema to hold messages for webapp.
2. Write functions that add a message to the DB and query all messages, you can use datalog querys (`datomic.api/q`) or direct index access (`datomic.api/datoms`).
3. Integrate this into your webapp.

## License

Copyright © 2013 G. Hentschel, F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
