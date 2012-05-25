(ns hilbert)

(defn encode [n]
	(if (= n 0)
		[[0],[0]]
		(let [coords (encode (- n 1))
			  x-s (first coords)
			  y-s (second coords)
			  x-first (map #(*(- % 0.5) 0.5) y-s)
			  x-second (map #(*(- % 0.5) 0.5) x-s)
			  x-third (map #(*(+ % 0.5) 0.5) x-s)
			  x-fourth (map #(*(- 0.5 %) 0.5) y-s)
			  y-first (map #(*(- % 0.5) 0.5) x-s)
			  y-second (map #(*(+ % 0.5) 0.5) y-s)
			  y-third (map #(*(+ % 0.5) 0.5) y-s)
			  y-fourth (map #(*(- -0.5 %) 0.5) x-s)]
			[(flatten (lazy-cat [x-first x-second x-third x-fourth])) (flatten (lazy-cat [y-first y-second y-third y-fourth]))])))

(println (encode 9))