(ns rectangles-hover
  (:use [c2.core :only [unify]]))

(def css "
body { background-color:white;}
rect { -webkit-transition: all 1s ease-in-out; fill:rgb(255,0,0);}
rect:hover { -webkit-transform: translate(3em,0); fill:rgb(0,255,0);}
circle { opacity: 1; -webkit-transition: opacity 1s linear; fill:rgb(0,0,255);}
circle:hover { opacity: 0; }
")

[:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	(unify {"A" 50 "B" 120} (fn [[label val]]
		[:rect {:x val :y val :height 50 :width 60}]))
	(unify {"C" 180 "D" 240} (fn [[label val]]
		[:circle {:cx val :cy val :r 15}]))]
