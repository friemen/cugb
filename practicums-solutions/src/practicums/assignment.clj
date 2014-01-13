(ns practicums.assignment)


(def groups [{:id 0 :title "<unassigned>" :max 0}
             {:id 1 :title "Math" :max 2}
             {:id 2 :title "English" :max 2}
             {:id 3 :title "History" :max 2}])


(def persons [{:name "Donald" :prefs [1 3] :group 0}
              {:name "Daisy" :prefs [1 2] :group 0}
              {:name "Mickey" :prefs [1 2] :group 0}
              {:name "Goofy" :prefs [3 1] :group 0}])


(defn places-left?
  [group-with-persons]
  (< (count (:persons group-with-persons))
     (:max group-with-persons)))

(defn move-members
  [group-with-persons available-group-ids pref-index]
  (let [persons (:persons group-with-persons)
        n (- (count persons) (:max group-with-persons))]
    (->> persons
         (map #(vector (:name %) (available-group-ids (get (:prefs %) pref-index))))
         (filter second)
         (take n))))

(defn assign-step
  [groups [persons pref-index]]
  (let [persons-by-groups (group-by :group persons)
        groups-with-persons (->> groups
                                 (map #(assoc % :persons (persons-by-groups (:id %)))))
        available-group-ids (->> groups-with-persons
                                 (filter places-left?)
                                 (map :id)
                                 set)
        moved-persons (->> groups-with-persons
                           (mapcat #(move-members % available-group-ids pref-index))
                           (into {}))]
    (vector (->> persons
                 (map (fn [p]
                        (if-let [g (-> p :name moved-persons)]
                          (assoc p :group g)
                          p))))
            (inc pref-index))))

(defn assign
  [groups persons]
  (->> [persons 0]
       (iterate (partial assign-step groups))
       (take 3)
       last
       first))

; This invocation here 
#_(assign groups persons)

; should return:
; ({:group 3, :name "Donald", :prefs [1 3]}
;  {:group 1, :name "Daisy", :prefs [1 2]}
;  {:group 1, :name "Mickey", :prefs [1 2]}
;  {:group 3, :name "Goofy", :prefs [3 1]})
; or something similar

