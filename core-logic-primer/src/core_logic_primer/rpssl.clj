(ns core-logic-primer.rpssl
  (:refer-clojure :exclude [== !=])
  (:use [clojure.core.logic])
  (:use [clojure.core.logic.pldb]))

;; Rock-Paper-Scissors-Spock-Lizard
;; http://www.samkass.com/theories/RPSSL.html

;;
;; Either: Rules as a set of clauses.
;;
(defn beatso
  [player1 player2]
  (conde
   [(== player1 :scissors) (== player2 :paper)]
   [(== player1 :paper) (== player2 :rock)]
   [(== player1 :rock) (== player2 :lizard)]
   [(== player1 :lizard) (== player2 :spock)]
   [(== player1 :spock) (== player2 :scissors)]
   [(== player1 :scissors) (== player2 :lizard)]
   [(== player1 :lizard) (== player2 :paper)]
   [(== player1 :paper) (== player2 :spock)]
   [(== player1 :spock) (== player2 :rock)]
   [(== player1 :rock) (== player2 :scissors)]))

;;
;; Or: Using relations and facts
;;

;; Relations
(db-rel game gesture)
(db-rel beats gesture1 gesture2)

;; Facts
(def rpssl
  (db
   ;; Gestures
   [game :rock]
   [game :paper]
   [game :scissors]
   [game :spock]
   [game :lizard]
   ;; Rules
   [beats :scissors :paper]
   [beats :paper :rock]
   [beats :rock :lizard]
   [beats :lizard :spock]
   [beats :spock :scissors]
   [beats :scissors :lizard]
   [beats :lizard :paper]
   [beats :paper :spock]
   [beats :spock :rock]
   [beats :rock :scissors]))

;; Source: http://de.slideshare.net/normanrichards/corelogic-introduction (Slide 34)
(defn win-chaino
  [chain]
  (fresh [new-gesture result-chain]
    (game new-gesture)
    (conso new-gesture result-chain chain)
    (conde
     [(emptyo result-chain)]
     [(fresh [counter-gesture]
        (beats new-gesture counter-gesture)
        (firsto result-chain counter-gesture)
        (win-chaino result-chain))])))
