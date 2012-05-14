(ns barchart
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]))

(def css "
body { background-color:white;}
line { stroke:grey;}
")

; Also see horizontal-barchart for a better way to index the datapoints (using map-indexed)
(let [x-s (range 800)
      y-s (take 800 (repeatedly #(rand-int 300)))
      data (apply assoc {} (interleave x-s y-s))
      s (scale/linear :domain [0 (apply max y-s)]
                      :range [300 0])]
  [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	[:line {:x1 0 :x2 800 :y1 350 :y2 350}]
	[:g.chart
		(unify data (fn [[x y]]
			[:line {:x1 x :x2 x :y1 350 :y2 (s y)}]))]])
