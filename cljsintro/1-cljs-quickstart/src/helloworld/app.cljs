(ns helloworld.app)

(enable-console-print!)

(println "Hello ClojureScript World")

(set! (.. js/document (getElementById "app") -innerHTML)
      "<h1>Hello World</h1>")
