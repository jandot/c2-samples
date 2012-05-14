(ns bullet
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]))

(def css "
body { background-color:white; }
rect { fill: grey;}
")

(let [data [120 80 130 90 170]]
  [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	[:g.chart
		(unify (map-indexed vector data) (fn [[idx data-point]]
			[:rect {:x 20 :y (* idx 25) :width data-point :height 20}]))]])