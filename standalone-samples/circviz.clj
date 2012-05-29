(use '[leiningen.exec :only (deps)])
(deps '[[com.keminglabs/c2 "0.1.1"] [hiccup "1.0.0"]])
(use '[c2.core :only (unify)])
(use 'hiccup.core)

(load-file "polar.clj")

(def css "
body { background-color: white; }
circle { stroke: grey; fill: none; }
text { stroke: black; font-family: Georgia; font-size: 10px; }
line { stroke: grey; }
path { fill: none; stroke-width: 3}
")

(defn arc [start-degree stop-degree radius]
	(let [large-arc-flag (if (> 180 (- stop-degree start-degree)) 0 1)]
		(str "M " (* radius (Math/cos (degree-to-rad start-degree))) " " (* radius (Math/sin (degree-to-rad start-degree)))
			"A " radius " " radius " 0 " large-arc-flag " 1 " (* radius (Math/cos (degree-to-rad stop-degree))) " " (* radius (Math/sin (degree-to-rad stop-degree))))))

(def chromosome-lengths
	[{:name "chr1" :length 249250621}
	{:name "chr2" :length 243199373}
	{:name "chr3" :length 198022430}
	{:name "chr4" :length 191154276}
	{:name "chr5" :length 180915260}
	{:name "chr6" :length 171115067}
	{:name "chr7" :length 159138663}
	{:name "chr8" :length 146364022}
	{:name "chr9" :length 141213431}
	{:name "chr10" :length 135534747}
	{:name "chr11" :length 135006516}
	{:name "chr12" :length 133851895}
	{:name "chr13" :length 115169878}
	{:name "chr14" :length 107349540}
	{:name "chr15" :length 102531392}
	{:name "chr16" :length 90354753}
	{:name "chr17" :length 81195210}
	{:name "chr18" :length 78077248}
	{:name "chr19" :length 59128983}
	{:name "chr20" :length 63025520}
	{:name "chr21" :length 48129895}
	{:name "chr22" :length 51304566}
	{:name "chrX" :length 155270560}])

(def genome-length (reduce + (map :length chromosome-lengths)))

(def chromosome-lengths-degree (map #(scale-linear % 0 genome-length 0 360) (map :length chromosome-lengths)))

(def chromosome-thetas
	(reductions + chromosome-lengths-degree))

(def chromosome-thetas-with-names
	(partition 2 (interleave (map :name chromosome-lengths) (map float chromosome-thetas))))

(def data
	[{:type "gain" :start 45 :stop 80}
	 {:type "gain" :start 120 :stop 150}
	 {:type "loss" :start 60 :stop 80}
	 {:type "loss" :start 90 :stop 130}
	 {:type "loss" :start 220 :stop 250}
	 {:type "loss" :start 255 :stop 260}
	 {:type "gain" :start 257 :stop 258}])

(def svg
	[:svg
		[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
		[:g.plot {:transform "translate(300,300)"}
			[:circle {:cx 0 :cy 0 :r 150}]
			[:g.chromosome-ticks
				(unify chromosome-thetas (fn [datapoint]
					(let [x1 (x 150 (degree-to-rad datapoint))
						y1 (y 150 (degree-to-rad datapoint))
						x2 (x 160 (degree-to-rad datapoint))
						y2 (y 160 (degree-to-rad datapoint))]
						[:line {:x1 x1 :y1 y1 :x2 x2 :y2 y2}])))
				(unify chromosome-thetas-with-names (fn [datapoint]
					(let [x (x 170 (degree-to-rad (second datapoint)))
						y (y 170 (degree-to-rad (second datapoint)))]
						[:text {:x x :y y} (first datapoint)])))]
			[:g.gains
				(unify (filter #(= "gain" (:type %)) data) (fn [datapoint]
					[:path {:d (arc (:start datapoint) (:stop datapoint) 145) :stroke "green"}]))]
			[:g.gains
				(unify (filter #(= "loss" (:type %)) data) (fn [datapoint]
					[:path {:d (arc (:start datapoint) (:stop datapoint) 140) :stroke "red"}]))]]])

(spit "circviz.html" (html svg))