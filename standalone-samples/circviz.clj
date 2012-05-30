(use '[leiningen.exec :only (deps)])
(deps '[[com.keminglabs/c2 "0.1.1"] [hiccup "1.0.0"]])
(use '[c2.core :only (unify)])
(use 'hiccup.core)

(load-file "polar.clj")

(def RADIUS 150)

(def css "
body { background-color: white; }
circle { stroke: grey; fill: none; }
text { font-family: Georgia; font-size: 10px; }
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

(def bp-cumul-stop (reductions + (map :length chromosome-lengths)))

(def bp-cumul-start (butlast (cons 0 (map #(+ 1 %) bp-cumul-stop))))

(defn bp-cumul-to-degree-cumul [p]
	(float (scale-linear p 0 genome-length 0 360)))

(def chromosome-lengths-degree (map #(bp-cumul-to-degree-cumul %) (map :length chromosome-lengths)))

(def degree-cumul-start (map #(bp-cumul-to-degree-cumul %) bp-cumul-start))

(def degree-cumul-stop (map #(bp-cumul-to-degree-cumul %) bp-cumul-stop))

(def chromosomes
	(map #(apply assoc {}
			(interleave [:name :bp-length :bp-cumul-start :bp-cumul-stop :degree-length :degree-cumul-start :degree-cumul-stop] %))
			(partition 7 (interleave (map :name chromosome-lengths)
									(map :length chromosome-lengths)
									bp-cumul-start
									bp-cumul-stop
									chromosome-lengths-degree
									degree-cumul-start
									degree-cumul-stop))))

(def data
	[{:type "gain" :start 45 :stop 80}
	{:type "gain" :start 120 :stop 150}
	{:type "loss" :start 60 :stop 80}
	{:type "loss" :start 90 :stop 130}
	{:type "loss" :start 220 :stop 250}
	{:type "loss" :start 255 :stop 260}
	{:type "gain" :start 257 :stop 258}])

; (def data
; 	[{:type "gain" :chr "chr1" :start 450000 :stop 800000}
; 	 {:type "gain" :chr "chr3" :start 120000 :stop 150000}
; 	 {:type "loss" :chr "chr3" :start 130000 :stop 160000}
; 	 {:type "loss" :chr "chr7" :start 90000 :stop 130000}
; 	 {:type "loss" :chr "chr12" :start 220000 :stop 250000}
; 	 {:type "loss" :chr "chr16" :start 255000 :stop 260000}
; 	 {:type "gain" :chr "chr16" :start 257000 :stop 258000}])



(def svg
	[:svg
		[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
		[:g.plot {:transform "translate(300,300)"}
			[:circle {:cx 0 :cy 0 :r RADIUS}]
			[:g.chromosome-ticks
				(unify (map :degree-cumul-start chromosomes) (fn [datapoint]
					(let [x1 (x RADIUS (degree-to-rad datapoint))
						y1 (y RADIUS (degree-to-rad datapoint))
						x2 (x (+ 10 RADIUS) (degree-to-rad datapoint))
						y2 (y (+ 10 RADIUS) (degree-to-rad datapoint))]
						[:line {:x1 x1 :y1 y1 :x2 x2 :y2 y2}])))
				(unify chromosomes (fn [datapoint]
					(let [x (x 170 (degree-to-rad (:degree-cumul-start datapoint)))
						y (y 170 (degree-to-rad (:degree-cumul-start datapoint)))]
						[:text {:x x :y y} (:name datapoint)])))]
			[:g.gains
				(unify (filter #(= "gain" (:type %)) data) (fn [datapoint]
					[:path {:d (arc (:start datapoint) (:stop datapoint) (- RADIUS 5)) :stroke "green"}]))]
			[:g.losses
				(unify (filter #(= "loss" (:type %)) data) (fn [datapoint]
					[:path {:d (arc (:start datapoint) (:stop datapoint) (- RADIUS 10)) :stroke "red"}]))]]])

(spit "circviz.html" (html svg))