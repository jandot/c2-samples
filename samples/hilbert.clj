(ns hilbert)

(defn create-structure [n]
	(if (= n 0)
		[[0,0]]
		(let [coords (create-structure (- n 1))
			  x-s (map #(first %) coords)
			  y-s (map #(second %) coords)
			  ; positions (range 0 (apply * (repeat n 4)))
			  x-first (map #(*(- % 0.5) 0.5) y-s)
			  x-second (map #(*(- % 0.5) 0.5) x-s)
			  x-third (map #(*(+ % 0.5) 0.5) x-s)
			  x-fourth (map #(*(- 0.5 %) 0.5) y-s)
			  y-first (map #(*(- % 0.5) 0.5) x-s)
			  y-second (map #(*(+ % 0.5) 0.5) y-s)
			  y-third (map #(*(+ % 0.5) 0.5) y-s)
			  y-fourth (map #(*(- -0.5 %) 0.5) x-s)]
			
			(partition 2 (interleave
				(flatten (lazy-cat [x-first x-second x-third x-fourth])) (flatten (lazy-cat [y-first y-second y-third y-fourth])))))))

(defn encode [pos, n] "Returns x and y coordinate for a given position and order. 0-encoded."
	(let [data (create-structure n)]
		(if (or (> pos (count data)) (> 0 pos))
			nil
			(nth data pos))))

(defn decode [x, y, n] "Returns the position along the curve for a given x and y coordinate and order. 0-encoded"
	(filter #(= x (first %)) (filter #(= y (second %)) (create-structure n))))

(println (create-structure 0))
(println (create-structure 1))
(println (create-structure 2))
(println (encode 2 1))
(println (encode 2 2))
(println (encode 5 1))
(println (encode 0 1))
(println (encode -1 1))
(println (decode -0.25 0.25 1))
(println (decode -0.125 0.375 2))