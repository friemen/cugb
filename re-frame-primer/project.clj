(defproject re-frame-primer "0.1.0"
  :description
  "Day8/re-frame Primer for Clojure User Group Bonn."

  :url
  "https://github.com/friemen/cugb/"

  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.521"]

   ;; Common
   [re-frame "0.9.2"]
   [reagent "0.6.1"]

   ;; Web related
   [hiccup "1.0.5"]]

  :source-paths
  ["src/cljs"]

  :plugins
  [[lein-cljsbuild "1.1.3"]
   [lein-figwheel "0.5.8"]]

  :clean-targets
  ^{:protect false}
  ["target" "resources/public/js"]

  :profiles
  {:dev
   {:cljsbuild
    {:builds
     [{:id
     "dev"

     :source-paths
     ["src/cljs"]

     :figwheel
     {:on-jsload
      "re_frame_primer.core.dev/on-jsload"}

     :compiler
     {:main
      "re_frame_primer.core.dev"

      :asset-path
      "js/compiled/out"

      :output-to
      "resources/public/js/compiled/re_frame_primer.js"

      :output-dir
      "resources/public/js/compiled/out"

      :source-map-timestamp
      true}}]}}})
