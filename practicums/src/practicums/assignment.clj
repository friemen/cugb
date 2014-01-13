(ns practicums.assignment)

(def groups [{:id 0 :title "<unassigned>" :max 0}
             {:id 1 :title "Math" :max 2}
             {:id 2 :title "English" :max 2}
             {:id 3 :title "History" :max 2}])

(def persons [{:name "Donald" :prefs [1 3] :group 0}
              {:name "Daisy" :prefs [1 2] :group 0}
              {:name "Mickey" :prefs [1 2] :group 0}
              {:name "Goofy" :prefs [3 1] :group 0}])




; This invocation here 
#_(assign groups persons)

; should return:
; ({:group 3, :name "Donald", :prefs [1 3]}
;  {:group 1, :name "Daisy", :prefs [1 2]}
;  {:group 1, :name "Mickey", :prefs [1 2]}
;  {:group 3, :name "Goofy", :prefs [3 1]})
; or something similar

