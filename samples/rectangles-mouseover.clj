(ns rectangles-hover
  (:use [c2.core :only [unify]]))

(def css "
body { background-color:white;}
rect { fill:rgb(0,0,255);}
rect:hover { fill:rgb(0,255,0);}
")

(let [data {"A" 50 "B" 120}]
  [:svg
		[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
    	(unify data (fn [[label val]]
	  		[:rect {:x val :y val :height 50 :width 60}]))])
