(ns bars
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]))

(let [width 500 bar-height 20
      data {"A" 1, "B" 2, "C" 4, "D" 3}
      s (scale/linear :domain [0 (apply max (vals data))]
                      :range [0 width])]
  [:div#bars
    (unify data (fn [[label val]]
      [:div {:style (str "height: " bar-height ";"
                         "width: " (s val) ";"
                         "background-color: gray")}
        [:span {:style "color: white;"} label]]))])


; <html>
; <head>
; <script src="/livereload.js?port=8987"></script>
; 	<style>
; 		body { background-color: #222222; color: white;}
; 		h1 { text-align: center; font: 5em sans-serif; margin: 1em;}
; 	</style>
; </head>
; <body>
; 	<div id="bars">
; 		<div style="height: 20;width: 125;background-color: gray">
;			<span style="color: white;">A</span></div>
; 		<div style="height: 20;width: 250;background-color: gray">
;			<span style="color: white;">B</span></div>
; 		<div style="height: 20;width: 500;background-color: gray">
;			<span style="color: white;">C</span></div>
; 		<div style="height: 20;width: 375;background-color: gray">
;			<span style="color: white;">D</span></div>
; 	</div>
; </body>
; </html>
			
			
			
			
			
			