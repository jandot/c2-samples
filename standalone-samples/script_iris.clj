(use '[leiningen.exec :only (deps)])
(deps '[[com.keminglabs/c2 "0.1.1"] [hiccup "1.0.0"] [incanter "1.3.0"]])

(use '[c2.core :only (unify)])
(use 'hiccup.core)
(use '[incanter core stats charts datasets])
(require '[c2.scale :as scale])

(def css "
body { background-color:white;}
.coordinate-axes { stroke: black; }
.setosa { stroke: rgb(228,26,28) }
.versicolor { stroke: rgb(55,126,184) }
.virginica { stroke: rgb(77,175,74)}
text {font-family: 'Georgia'; font-weight:lighter; font-size:14px}
.setosa-datapoint:hover { stroke: black; stroke-width: 3}
.versicolor-datapoint:hover { stroke: black; stroke-width: 3}
.virginica-datapoint:hover { stroke: black; stroke-width: 3}
")

(def s-sl (scale/linear :domain [4.3 7.9] :range [0 400]))
(def s-sw (scale/linear :domain [2 4.4] :range [0 400]))
(def s-pl (scale/linear :domain [1 6.9] :range [0 400]))
(def s-pw (scale/linear :domain [0.1 2.5] :range [0 400]))

(def data (:rows (get-dataset :iris)))
(def setosa (filter #(= "setosa" (:Species %)) data))
(def versicolor (filter #(= "versicolor" (:Species %)) data))
(def virginica (filter #(= "virginica" (:Species %)) data))

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
	[:g.setosa
		(unify setosa (fn [data-point]
			[:g.setosa-datapoint
				[:line {:x1 20 :y1 (s-sl (:Sepal.Length data-point)) :x2 220 :y2 (s-sw (:Sepal.Width data-point))}]
				[:line {:x1 220 :y1 (s-sw (:Sepal.Width data-point)) :x2 420 :y2 (s-pl (:Petal.Length data-point))}]
				[:line {:x1 420 :y1 (s-pl (:Petal.Length data-point)) :x2 620 :y2 (s-pw (:Petal.Width data-point))}]]))]
	[:g.versicolor
		(unify versicolor (fn [data-point]
			[:g.versicolor-datapoint
				[:line {:x1 20 :y1 (s-sl (:Sepal.Length data-point)) :x2 220 :y2 (s-sw (:Sepal.Width data-point))}]
				[:line {:x1 220 :y1 (s-sw (:Sepal.Width data-point)) :x2 420 :y2 (s-pl (:Petal.Length data-point))}]
				[:line {:x1 420 :y1 (s-pl (:Petal.Length data-point)) :x2 620 :y2 (s-pw (:Petal.Width data-point))}]]))]
	[:g.virginica
		(unify virginica (fn [data-point]
			[:g.virginica-datapoint
				[:line {:x1 20 :y1 (s-sl (:Sepal.Length data-point)) :x2 220 :y2 (s-sw (:Sepal.Width data-point))}]
				[:line {:x1 220 :y1 (s-sw (:Sepal.Width data-point)) :x2 420 :y2 (s-pl (:Petal.Length data-point))}]
				[:line {:x1 420 :y1 (s-pl (:Petal.Length data-point)) :x2 620 :y2 (s-pw (:Petal.Width data-point))}]]))]])

(spit "iris_data.html" (html svg))
