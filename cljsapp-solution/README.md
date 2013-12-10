# ClojureScript

The `slides`-folder contains the presentation.

## Workflow

This project uses [lein-cljsbuild](https://github.com/emezeske/lein-cljsbuild) and [dommy](https://github.com/Prismatic/dommy).

To compile the ClojureScript-code to JavaScript:

    lein cljsbuild once

Use

    lein cljsbuild auto

to rebuild the source whenever it changes.

Now open or reload `resources/public/index.html` in your browser to test the application.

The output of `cljsbuild` can be removed with:

    lein cljsbuild clean

For debugging purposes you can use standard Clojure `print`-functions and view the output in your browsers developer console.

## Excercices

1. Add the message in the textfield to the message list, when the add button is clicked.
2. Render the message list using dommys `deftemplate`.
3. Implement the former without rerendering the whole message list everytime.
4. Integrate this app with your Clojure webapp via Ajax. You could use the Google Closure Library or a ClojureScript-library like [cljs-ajax](https://github.com/yogthos/cljs-ajax).

## License

Copyright © 2013 A. Bendel, F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
