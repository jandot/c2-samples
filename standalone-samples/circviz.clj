(use '[leiningen.exec :only (deps)])
(deps '[[com.keminglabs/c2 "0.1.1"] [hiccup "1.0.0"]])
(use '[c2.core :only (unify)])
(use 'hiccup.core)

(load-file "polar.clj")

(def css "
body { background-color: white; }
circle { stroke: grey; fill: none; }
path { fill: none; stroke-width: 3}
")

(defn arc [start-degree stop-degree radius]
	(let [large-arc-flag (if (> 180 (- stop-degree start-degree)) 0 1)]
		(str "M " (* radius (Math/cos (degree-to-rad start-degree))) " " (* radius (Math/sin (degree-to-rad start-degree)))
			"A " radius " " radius " 0 " large-arc-flag " 1 " (* radius (Math/cos (degree-to-rad stop-degree))) " " (* radius (Math/sin (degree-to-rad stop-degree))))))

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
			[:g.gains
				(unify (filter #(= "gain" (:type %)) data) (fn [datapoint]
					[:path {:d (arc (:start datapoint) (:stop datapoint) 145) :stroke "green"}]))]
			[:g.gains
				(unify (filter #(= "loss" (:type %)) data) (fn [datapoint]
					[:path {:d (arc (:start datapoint) (:stop datapoint) 140) :stroke "red"}]))]]])

(spit "circviz.html" (html svg))