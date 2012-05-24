(use '[leiningen.exec :only (deps)])
(deps '[[com.keminglabs/c2 "0.1.1"] [hiccup "1.0.0"] [incanter "1.3.0"]])

(use '[c2.core :only (unify)])
(use 'hiccup.core)
(use '[incanter core stats charts datasets])
(require '[c2.scale :as scale])

(def s-sl (scale/linear :domain [4.3 7.9] :range [0 400]))
(def s-sw (scale/linear :domain [2 4.4] :range [0 400]))
(def s-pl (scale/linear :domain [1 6.9] :range [0 400]))
(def s-pw (scale/linear :domain [0.1 2.5] :range [0 400]))

(def data (:rows (get-dataset :iris)))
(def setosa {:data (filter #(= "setosa" (:Species %)) data), :colour "rgb(228,26,28)"})
(def versicolor {:data (filter #(= "versicolor" (:Species %)) data), :colour "rgb(55,126,184)"})
(def virginica {:data (filter #(= "virginica" (:Species %)) data), :colour "rgb(77,175,74)"})

(def css (str "
body { background-color:white;}
.coordinate-axes line {stroke:black}
.setosa-datapoint:hover { stroke: black; stroke-width: 3}
.versicolor-datapoint:hover { stroke: black; stroke-width: 3}
.virginica-datapoint:hover { stroke: black; stroke-width: 3}
text {font-family: 'Georgia'; font-weight:lighter; font-size:12px}"
"rect.setosa { stroke:none; fill: " (:colour setosa) "}"
"rect.versicolor { stroke:none; fill: " (:colour versicolor) "}"
"rect.virginica { stroke:none; fill: " (:colour virginica) "}"
".setosa { stroke: " (:colour setosa) "}"
".versicolor { stroke: " (:colour versicolor) "}"
".virginica { stroke: " (:colour virginica) "}"
))

(def svg [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	[:g.coordinate-axes
		[:line {:x1 20 :y1 0 :x2 20 :y2 400}]
		[:line {:x1 220 :y1 0 :x2 220 :y2 400}]
		[:line {:x1 420 :y1 0 :x2 420 :y2 400}]
		[:line {:x1 620 :y1 0 :x2 620 :y2 400}]
		[:text {:x 20 :y 420} "sepal-length"]
		[:text {:x 220 :y 420} "sepal-width"]
		[:text {:x 420 :y 420} "petal-length"]
		[:text {:x 620 :y 420} "petal-width"]]
	[:g.legend
		[:rect.setosa {:x 650 :y 20 :width 20 :height 20}]
		[:rect.versicolor {:x 650 :y 50 :width 20 :height 20}]
		[:rect.virginica {:x 650 :y 80 :width 20 :height 20}]
		[:text {:x 680 :y 40} "setosa"]
		[:text {:x 680 :y 70} "versicolor"]
		[:text {:x 680 :y 100} "virginica"]]
	[:g.setosa
		(unify (:data setosa) (fn [data-point]
			[:g.setosa-datapoint
				[:line {:x1 20 :y1 (s-sl (:Sepal.Length data-point)) :x2 220 :y2 (s-sw (:Sepal.Width data-point))}]
				[:line {:x1 220 :y1 (s-sw (:Sepal.Width data-point)) :x2 420 :y2 (s-pl (:Petal.Length data-point))}]
				[:line {:x1 420 :y1 (s-pl (:Petal.Length data-point)) :x2 620 :y2 (s-pw (:Petal.Width data-point))}]]))]
	[:g.versicolor
		(unify (:data versicolor) (fn [data-point]
			[:g.versicolor-datapoint
				[:line {:x1 20 :y1 (s-sl (:Sepal.Length data-point)) :x2 220 :y2 (s-sw (:Sepal.Width data-point))}]
				[:line {:x1 220 :y1 (s-sw (:Sepal.Width data-point)) :x2 420 :y2 (s-pl (:Petal.Length data-point))}]
				[:line {:x1 420 :y1 (s-pl (:Petal.Length data-point)) :x2 620 :y2 (s-pw (:Petal.Width data-point))}]]))]
	[:g.virginica
		(unify (:data virginica) (fn [data-point]
			[:g.virginica-datapoint
				[:line {:x1 20 :y1 (s-sl (:Sepal.Length data-point)) :x2 220 :y2 (s-sw (:Sepal.Width data-point))}]
				[:line {:x1 220 :y1 (s-sw (:Sepal.Width data-point)) :x2 420 :y2 (s-pl (:Petal.Length data-point))}]
				[:line {:x1 420 :y1 (s-pl (:Petal.Length data-point)) :x2 620 :y2 (s-pw (:Petal.Width data-point))}]]))]])

(spit "iris_data.html" (html svg))
