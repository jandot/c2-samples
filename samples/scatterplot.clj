(ns scatter
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]))

(def css "
body { background-color:white; }
circle { fill:grey; opacity:0.8;}
circle:hover { fill:red; }
")

(let [data [{:x 10.0 :y 9.14 :r 27}
			{:x  8.0 :y 8.14 :r 22}
			{:x 13.0 :y 8.74 :r 33}
 			{:x  9.0 :y 8.77 :r 19}
 			{:x 11.0 :y 9.26 :r 20}]
      scale-x (scale/linear :domain [0 (apply max (map :x data))] :range [50 500])
      scale-y (scale/linear :domain [0 (apply max (map :y data))] :range [500 50])]
  [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	[:g.chart
		(unify data (fn [data-point]
			[:circle {:cx (scale-x (:x data-point)) :cy (scale-y (:y data-point)) :r (:r data-point)}]))]])
