
# Introduction to the Datomic database system

## Rationale

Datomic is a functional database. Instead of having a global mutable variable, i.e. a traditional database system, that is shared by multiple processes, it separates the identity of the database, e.g. the "customer database hosted on this machine", from the value of the database. Each value is immutable which facilitates reasoning, reproducibility, caching, combining multiple databases etc...

To have immutable database values it is required that you can only add data to the database, thus Datomic is accretive. This allows to perform queries over the history of the data, e.g., how often did the price for a product change and when, how many transactions happened across the last week, retrieve deltas necessary to transition from an old state to the current state, ...

Datomic supports Datalog as query language which follows another design philosophy of Clojure: use data. Datalog querys are data which can be combined with more data into new data. The same holds for Datomic schemas which are also comprised of data.

Datomic has ACID properties including transactions. Every write goes through the transactor that serializes all changes to the system to create a database-wide ordering.

The Datomic Console provides a web-interface for exploring/querying/traversing the persisted data.

## Architecture

[[http://www.datomic.com/uploads/3/5/9/7/3597326/6646785_orig.jpg]]

Datomic separates several concepts into invidual building blocks.
Writes happen in the transactor which creates Data Segments that are stored in
a Storage Server/Service like DynamoDB, Riak or PostgreSQL.
The client application uses a Peer to send transactions over to the transactor.
Reads happen in the Peer which accesses the Storage Server or an intermediate
memcached cluster to retrieve the data segments it needs to answer queries.
Thus reads can scale independently from writes. By connecting an additional
peer one can perform expensive analytics queries without risking an impact
on the production system. Another option for a client application to
access the database is via a REST server provided by Datomic.

## Getting started

### Datomic Installation & Setup

[Download](https://my.datomic.com/downloads/free) the latest Datomic version & unzip.

Set JAVA_HOME & PATH environment variables

    export JAVA_HOME=/path/to/jdk
    export PATH=$JAVA_HOME/bin:$PATH

For Windows have a look at the [Getting Started](http://docs.datomic.com/getting-started.html) guide.

*Note*: Datomic works for me without the JAVA_HOME env variable.

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

The data structure to pass to a `transact` call is a list of lists
and/or maps of which each is a statement in the transaction. A statement
represents either the addition or retraction of a fact about an
entity, an attribute and a value. A map contained in a transaction combines
multiple additions of facts about the same entity.

```clojure
(require '[datomic.api :refer [db q] :as d])
;= nil
@(d/transact conn [[:db/add entity-id1 attribute1 value1]
                   [:db/add entity-id1 attribute2 value2]
                   [:db/retract entity-id2 attribute value]])
;= #<promise$settable_future$reify__4426@6210d510: {:db-before ..., :db-after ..., :tx-data ..., :tempids ...}
;; or equivalently
@(d/transact conn [{:db/id entity-id1
                    attribute1 value1
                    attribute2 value2}
                   [:db/retract entity-id2 attribute value]])
;= #<promise$settable_future$reify__4426@6210d510: {:db-before ..., :db-after ..., :tx-data ..., :tempids ...}
```

Queries in Datomic use [Datalog](http://docs.datomic.com/query.html) which is a simple, declarative & logic-based query system.
A query consists of variables to return, a list of data sources and a set of clauses that describe the shape of the data to find:

```clojure
(require '[datomic.api :refer [db q] :as d])
;= nil
(q '[:find ?e :in $ :where [?e :project/name "KillerApp"]] (db conn))
;= #{[17592186045418]}
```

The example above shows that the main sequence abstraction in Datomic is a set. The result
set contains vectors of found variables. In this case we only asked for `?e` thus each vector
contains a single internal entity-id. You can use the entity-id to lazily fetch the data
associated with the entity and navigate its references.

```clojure
(d/touch (d/entity (db conn) 17592186045418))
;= {:db/id 17592186045418}
(d/touch *1)
;= {:project/name "KillerApp", :project/release #{{:release/name "Alpha labeled RC1", :release/task #{{:task/summary "Make features", :db/id 17592186045420}}, :db/id 17592186045419}}, :db/id 17592186045418}
```

The example shows the [Component Entities](http://blog.datomic.com/2013/06/component-entities.html) feature which allows to specify components of entities directly as nested maps inside a transaction, adding the feel of a document-database:

```clojure
(d/transact conn
            [{:project/name "KillerApp"
              :db/id (tempid)
              :project/release
              [{:release/name "Alpha labeled RC1"
                :release/task
                [{:task/summary "Make features"}]}]}])
;= #<promise$settable_future$reify__4426@6210d510: {:db-before ..., :db-after ..., :tx-data ..., :tempids ...}
```

The DB connection is explicit in Datomic, which requires you to pass
the connection to database function. This might seem tedious, but
circumvents problems that arise from ambient connections, i.e. global connections
bound to some var: such as the required knowledge about the hidden dependency on the connection &
the limitation to a single connection at any point in time.

## Excercises
1. Create simple schema to hold messages for webapp.
2. Write functions that add a message to the DB and query all messages, you can use datalog querys (`datomic.api/q`) or direct index access (`datomic.api/datoms`).
3. Integrate this into your webapp.
4. Visit www.learndatalogtoday.org and work through the exercises.

## License

Copyright © 2013 G. Hentschel, F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
