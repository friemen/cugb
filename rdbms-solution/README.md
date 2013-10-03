# Introduction to relational database access

To run the [webapp](src/webapp.clj) start a REPL and enter:

```clojure
(require 'webapp)
;= nil
(webapp/-main)
#<server$run_server$stop_server__1255 org.httpkit.server$run_server$stop_server__1255@2161df1f>
```

The H2 database file is automatically created in your home directory (~/webapp.db)
and the schema is created when necessary.

The server is waiting for requests on http://localhost:8080


## License

Copyright © 2013 F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
