
# Hangman Backend

## Requirements

* JDK 1.7+
* Leiningen 2.5.3

## Development

Start a Clojure REPL

```
$ lein repl

;; Switch to server namespace
user=> (ns froscon-18.hangman.backend.server)

;; Start the server
froscon-18.hangman.backend.server=> (start! {:port 1337} app/handler))

;; Stop the server
froscon-18.hangman.backend.server=> (stop!))
```
