(ns bars
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]))

(def css "
body { background-color:white;}
rect { fill:rgb(0,0,255);}
circle { fill:rgb(0,255,0); opacity:0.4;}
")

(let [width 500 bar-height 20
      data {"A" {:x 300 :y 50 :w 120 :h 15 :r 32},
            "B" {:x 120 :y 80 :w 39 :h 53 :r 19}}
     ]
  [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
    (unify data (fn [[label val]]
 		[:rect {:x (:x val) :y (:y val) :height (:h val) :width (:w val)}]))
	(unify data (fn [[label val]]
		[:circle {:cx (:x val) :cy (:y val) :r (:r val)}]))])
