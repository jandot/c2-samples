(def two-pi 6.28318530717959)

(defn scale-linear [value from-min from-max to-min to-max]
	(let [range-from (- from-max from-min)
			range-to (- to-max to-min)]
		(+ to-min (* value (/ range-to range-from)))))

(defn linear-to-polar [pos l]
	"Convert linear to polar coordinate, for scale of length l. Returns theta."
	(scale-linear pos 0 l 0 two-pi))

(defn polar-to-cartesian [r theta]
	"Convert polar coordinate to cartesian x and y. Returns array of length 2 (i.e. x and y)"
	(map int [(* r (Math/cos theta)) (* r (Math/sin theta))]))

(defn x [r theta]
	(first (polar-to-cartesian r theta)))

(defn y [r theta]
	(second (polar-to-cartesian r theta)))

(defn degree-to-rad [d]
	(scale-linear d 0 360 0 two-pi))

(defn rad-to-degree [r]
	(scale-linear r 0 two-pi 0 360))
