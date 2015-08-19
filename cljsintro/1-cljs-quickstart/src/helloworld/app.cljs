(ns helloworld.app)

(enable-console-print!)


(set! (.. js/document (getElementById "app") -innerHTML)
      "<h1>Hello ClojureScript World</h1>")

(println "Logging to a console works as well")
