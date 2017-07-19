(ns re-frame-primer.views.slides
  (:require
   [re-frame.core :refer [subscribe dispatch]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Slides

(defn- slide
  ([title]
   (slide title nil))
  ([title & contents]
   [:div.slide.panel.panel-default
    {:style {:margin-top "10px"}}
    (into [:div.panel-body
           [:h3 title]] contents)]))

(defmulti ^:private render-slide
  (fn [n]
    n))

(defmethod ^:private render-slide :default
  [n]
  (slide
   (str "Für " n " ist keine Folie hinterlegt.")))

(defmethod ^:private render-slide 1
  [_]
  (slide
   "Re-Frame Primer"
   [:ul
    [:li
     "6-domino cascade"]
    [:li
     "Form 1"]
    [:li
     "Form 2"]
    [:li
     "Form 3"]
    [:li
     "re-frame.core/subscribe"]
    [:li
     "re-frame.core/dispatch"]
    [:li
     "Anfängerfehler"]]))

(defmethod ^:private render-slide 2
  [_]
  (slide
   "6-domino cascade"
   [:h4 "Re-Frame"]
   [:ul
    [:li "1. Event Dispatch => dispatch"]
    [:li "2. Event Handling => reg-event-fx (reg-event-db)"]
    [:li "3. Effect Handling => reg-fx"]
    [:li "4. Query => reg-sub"]
    [:li "5. View"]]
   [:h4 "Reagent/React"]
   [:ul
    [:li "6. DOM => Reagent/React magic"]]))

(defmethod ^:private render-slide 3
  [_]
  (slide
   "Form 1 Komponente"
   [:pre
    (str
     "(defn form-1-component\n"
     "  [x]\n"
     "  [:div x])")]
   [:ul
    [:li "Render Funktion die bei einer Änderung der `props` aufgerufen wird"]
    [:li "Erzeugt eine Clojurescript Datenstruktur in Hiccup Form"]]))

(defmethod ^:private render-slide 4
  [_]
  (slide
   "Form 2 Komponente"
   [:pre
    (str
     "(defn form-2-component\n"
     "  [name]\n"
     "  (let [greeting\n"
     "        \"Hello\"\n"
     "\n"
     "        !some-value\n"
     "        (re-frame.core/subscribe [:some-sub-path])]\n"
     "\n"
     "     (fn [name]\n"
     "       [:div (str greeting name @!some-value)])))")]
   [:ul
    [:li "Eine Funktion die eine Render Funktion zurückgibt"]
    [:li "Die erzeugte Render-Funktion erzeugt eine Clojurescript Datenstruktur in Hiccup Form"]
    [:li "Lokaler State wird nur beim initialen Aufruf (Komponentenerzeugung) erzeugt/ausgeführt"]]))

(defmethod ^:private render-slide 5
  [_]
  (slide
   "Form 3 Komponente"
   [:pre
    (str
     "(defn form-3-component\n"
     "  [name]\n"
     "  (reagent/create-class\n"
     "    {:component-did-mount\n"
     "     #(js/console.log \"did-mount\")\n\n"
     "     :component-will-mount\n"
     "     #(js/console.log \"will-mount\")\n\n"
     "     :display-name\n"
     "     \"form-3-component\"\n\n"
     "     :reagent-render\n"
     "     (fn [name]\n"
     "       [:div (str @local-state name)])}))")]
   [:ul
    [:li "Erzeugt eine Komponente bei der wir die React Lifecycle Methoden überschreiben können"]
    [:li "Die Funktion die unter `reagent-render` gebunden ist wird zum Rendern der Komponente genutzt"]
    [:li "Lokaler State kann hier auch genutzt werden und ist in allen Lifecycle Funktionen zugreifbar"]
    [:li "React Lifecycle: " [:a {:href "https://facebook.github.io/react/docs/react-component.html"} "https://facebook.github.io/react/docs/react-component.html"]]]))

(defmethod ^:private render-slide 6
  [_]
  (let [!state
        (subscribe [:db])

        !state-transformed
        (subscribe [:test-state/transformed])]

    (fn [_]
      (slide
       "`re-frame.core/dispatch` und `re-frame.core/subscribe`"
       [:h5 "Aktueller App State"]
       [:pre (with-out-str (cljs.pprint/pprint @!state))]
       [:h5 "Registrierung eines Handlers"]
       [:pre
        (str "(re-frame.core/reg-event-db :test-state/inc\n"
             "  (fn [db [event-name params]]\n"
             "    (update db :test-state inc)))")]
       [:h5 "Nutzung eines Handlers"]
       [:pre
        (str "[:button\n"
             "  {:on-click #(dispatch [:test-state/inc])} ;; Anfängerfehler! \n"
             " \"Increment\"]\n")]
       [:button
        {:on-click #(dispatch [:test-state/inc])}
        "Increment"]
       [:h5 "Registrierung der Subscription"]
       [:pre
        (str
         "(re-frame.core/reg-sub :test-state/get\n"
         "  (fn [db [name params]]\n"
         "    (:test-state db)))")]
       [:h5 "Nutzung der Subscription"]
       [:pre "@(re-frame.core/subscribe [:test-state]) => "
        [:span
         {:style {:font-size "18px"
                  :font-weight "bold"}}
         (:test-state @!state)]]
       [:h5 "Live Beispiel (Transformation)"
        [:pre
         {:style {:font-size "18px"
                  :font-weight "bold"}}
         @!state-transformed]]))))


(defmethod ^:private render-slide 7
  [_]
  (slide
   "Anfängerfehler"
   [:ul
    [:li "Form 2 / Form 3: Nutzung von Parametern"
     [:pre
      (str "(defn some-view\n"
           "  [x]\n"
           "  (let [value-1 (subscribe [:some.path x])]\n"
           "    (fn [x]\n"
           "      [:div @value-1])))\n")]]
    [:li "Handler oder Subscriptions sind nicht bekannt"]
    [:p "Lösung: Die Namespaces müssen irgendwo eingebunden sein, da sie sonst nicht geladen werden und somit die Handler/subscriptions nicht geladen sind."]
    [:li "Übergabe von Werten die aus einer Subscription kommen an Views"]
    [:p "Lösung: Je nachdem welche Daten an eine Sub-View übergeben werden kann es zu extremen Performance einbußen kommen."]
    [:p "Beispiel: Eine View die eine Tabelle rendern soll. Sie bekommt alle \"items\" übergeben. Bei einer Änderung *irgendeines* der \"items\" wird die gesamte Tabelle neu gerendert."]
    [:li "Zu \"grobe\" Subscriptions"]
    [:li "Render von Komponenten mit Hilfe einer `multimethod`"
     [:ul
      [:li "Erläuterung: "
       [:a {:href "http://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame"} "http://stackoverflow.com/questions/33299746/why-are-multi-methods-not-working-as-functions-for-reagent-re-frame"]]]]
    [:li "Nutzung von `subscribe` innerhalb einer Render-Funktion"]
    [:li "Funktionen inline definieren"
     [:ul
      [:li "Lösung: z.B. in einem `let` binden oder als eigene Funktion auslagern damit diese nicht bei jedem rerendern neu erzeugt werden muss. Das gleiche gilt auch für dynamische Attribute."]]]]))

(defmethod ^:private render-slide 8
  [_]
  (slide
   "Nützliche Ressourcen"
   [:ul
    [:li "Re-Frame: " [:a {:href "https://github.com/Day8/re-frame"} "https://github.com/Day8/re-frame"]]
    [:li "Re-Frame Wiki: " [:a {:href "https://github.com/Day8/re-frame/wiki"} "https://github.com/Day8/re-frame/wiki"]]
    [:li "Re-Frisk [Debugging App-State/Events]: " [:a {:href "https://github.com/flexsurfer/re-frisk"} "https://github.com/flexsurfer/re-frisk"]]
    [:li "!React: " [:a {:href "https://facebook.github.io/react/"} "https://facebook.github.io/react/"]]]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sub Views

(defn- next-slide-btn
  []
  (let [on-click
        #(dispatch [:app.slide/change 1])]

    (fn []
      [:button.btn.btn-default
       {:style {:margin-right "20px"}
        :on-click
        on-click}
       "Nächste Folie"])))

(defn- previous-slide-btn
  []
  (let [!back-button-disabled?
        (subscribe [:app.slide.back-button/disabled?])

        on-click
        #(dispatch [:app.slide/change -1])]

    (fn []
      [:button.btn.btn-default
       {:disabled @!back-button-disabled?
        :on-click
        on-click}
       "Vorherige Folie"])))

(defn- form-2-local-state-test
  [v]
  (let [!some-value
        (subscribe [:app.slide/get v])]

    (fn [v]
      [:div "Aktuelle Folie: " @!some-value])))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; View

(defn view
  []
  (let [!current-slide
        (subscribe [:app.slide/current])]

    (fn []
      [:div.container
       {:style {:margin-top "20px"}}
       [:div.slide-nav
        [next-slide-btn]
        [previous-slide-btn]]

       ;; WATCHOUT: Das setzen des `:key`'s für dazu, dass die
       ;; Komponente und deren Sub-Komponenten bei jedem rerender neu aufgebaut wird.
       ^{:key @!current-slide}
       [render-slide (or @!current-slide 1)]])))
