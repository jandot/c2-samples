(ns hilbert)

(defn create-structure [n]
	"Creates a Hilbert structure of order n."
	(if (= n 0)
		[{:pos 0 :x 0 :y 0}]
		(let [coords (create-structure (- n 1))
			  x-s (map #(:x %) coords)
			  y-s (map #(:y %) coords)
			  positions (range 0 (apply * (repeat n 4)))
			  x-first (map #(*(- % 0.5) 0.5) y-s)
			  x-second (map #(*(- % 0.5) 0.5) x-s)
			  x-third (map #(*(+ % 0.5) 0.5) x-s)
			  x-fourth (map #(*(- 0.5 %) 0.5) y-s)
			  y-first (map #(*(- % 0.5) 0.5) x-s)
			  y-second (map #(*(+ % 0.5) 0.5) y-s)
			  y-third (map #(*(+ % 0.5) 0.5) y-s)
			  y-fourth (map #(*(- -0.5 %) 0.5) x-s)]
			(map #(zipmap (cycle [:pos :x :y]) %)
				(partition 3 (interleave
					positions
					(flatten (lazy-cat [x-first x-second x-third x-fourth]))
					(flatten (lazy-cat [y-first y-second y-third y-fourth]))))))))

(defn encode [pos, n]
	"Returns x and y coordinate for a given position and order (0-encoded)."
	(let [element (first (filter #(= pos (:pos %)) (create-structure n)))]
		[(:x element) (:y element)]))

(defn decode [x, y, n]
	"Returns the position along the curve for a given x and y coordinate and order (0-encoded)."
	(:pos (first (filter #(= x (:x %)) (filter #(= y (:y %)) (create-structure n))))))

; (println (create-structure 0))
; (println (create-structure 1))
; (println (create-structure 2))
; (println (encode 2 1))
; (println (encode 2 2))
; (println (encode 5 1))
; (println (encode 0 1))
; (println (encode -1 1))
; (println (decode -0.25 0.25 1))
; (println (decode -0.125 0.375 2))
; (println (encode 250000 9))