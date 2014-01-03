(ns practicums.toporder)

;; See http://en.wikipedia.org/wiki/Topological_sorting

(defprotocol Graph
  (nodes [g])
  (neighbors [g n]))


(defn mapbased-graph
  "Returns a Graph impl that assumes a map as graph representation.
   A node is a pair [key neighbors], where neighbors is a seq of keys
   pointing to other nodes. A graph is a map consisting of those pairs."
  [node-map]
  (reify Graph
    (nodes [g] (seq node-map))
    (neighbors [g k] (node-map k))))

(def g (mapbased-graph {:a []
                        :b [:a]
                        :c []
                        :d [:c :b]
                        :e [:a :b :c :d]}))


(defn topord [g]
  (let [g* (->> g nodes
                  (into {::parent (->> g nodes (map first) vec)})
                  mapbased-graph)
        visit (fn visit [visited k]
                (if-not (visited k)
                  (conj (->> (neighbors g* k)
                             (mapcat (partial visit (conj visited k)))
                             vec)
                        k)
                  (throw (IllegalArgumentException. "Graph contains a cycle"))))]
    (->> (visit #{} ::parent)
         drop-last
         distinct
         reverse)))

(topord g)
