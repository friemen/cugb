(ns ui-sample
  (:import [javax.swing JFrame JPanel JTextField JLabel JButton]
           [net.miginfocom.swing MigLayout]))


(defn frame
  [content]
  (let [f (JFrame.)]
    (doto f
      (.setTitle "Hello Swing World")
      (.setContentPane content)
      (.setVisible true)
      (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE)
      (.pack))))

(defn ly
  ([vc]
     (ly "" vc))
  ([lyhint vc]
     {:vc vc
      :lyhint lyhint}))


(defn panel
  [lygeneral lycols lyrows lys]
  (let [p (JPanel.)]
    (doto p
      (.setLayout (MigLayout. lygeneral lycols lyrows)))
    (doseq [{lyhint :lyhint c :vc} lys]
      (.add p c lyhint))
    p))


(def p (panel "wrap 2, fillx" "[|100,grow]" ""
              [(ly (JLabel. "Name")) (ly "growx" (JTextField.))
               (ly "span, right" (panel "ins 0" "" ""
                                        [(ly (JButton. "OK")) (ly (JButton. "Cancel"))]))]))

(def f (frame p))

;; In order to attach arbitrary data to visual components
;; JComponents .putClientProperty / .getClientProperty can be used.
;; BUT you can't use .putClientProperty on a JFrame, argh.

;; Protocols to the rescue!

#_(defprotocol IPropertyHolder
  (put! [component key value])
  (get [component key]))

;; Please extend the protocol to type JComponent and JFrame so that
;; both uniformly support put! and get.
