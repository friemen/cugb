
# Hangman Frontend

## Requirements

* JDK 1.7+
* Leiningen 2.5.3
* sass

## Development

Start a Clojure REPL

```
$ lein repl
```

Launch a ClojureScript REPL (along with cljs+sass autoloading)

```
user> (user/fig-init)
```

Open `resources/public/index.html` in a webbrowser

## Dist

Open up `dist/index.html` in a webbrowser while having the backend run on `localhost:1337`.

The html source includes a switch to control whether the backend should actually talk to
https://www.wordaxis.com or rather generate mock responses.

```
  <!-- Switch to enable or disable wordaxis mock -->
  <script>window.mock=false;</script>

```
