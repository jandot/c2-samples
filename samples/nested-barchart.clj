(ns nested-barchart
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]))

(def css "
body { background-color:white;}
.total { fill:grey; opacity: 0.2; stroke:none;}
.male { fill:blue; stroke:none;}
.female { fill:pink; stroke:none;}
")

(let [data [{:m 5 :f 13} {:m 8 :f 4} {:m 7 :f 11} {:m 11 :f 9}]
      s (scale/linear :domain [15 0] :range [300 0])
	  indexed-data (map-indexed vector data)]
  [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	[:line {:x1 0 :x2 800 :y1 350 :y2 350}]
	[:g.chart
		[:g.total
			(unify indexed-data (fn [[idx data-point]]
				[:rect {:x (* idx 25) :y 0 :width 20 :height (+ (s (:m data-point)) (s (:f data-point)))}]))]
		[:g.male
			(unify indexed-data (fn [[idx data-point]]
				[:rect {:x (* idx 25) :y 0 :width 10 :height (s (:m data-point))}]))]
		[:g.female
			(unify indexed-data (fn [[idx data-point]]
				[:rect {:x (+ (* idx 25) 10) :y 0 :width 10 :height (s (:f data-point))}]))]
		]])