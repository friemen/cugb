# Introduction to Datomic

To run the [webapp](src/webapp.clj) you need a [Datomic Installation](../datomic/README.md).

Start the transactor from the Datomic folder:

    bin/transactor config/samples/free-transactor-template.properties

and start the server:

    lein run

The Datomic database is stored in <Datomic directory>/data.

The server is waiting for requests on http://localhost:8080


## License

Copyright © 2013 G.Hentschel, F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.