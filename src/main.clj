; https://4clojure.oxal.org/#/problem/1
(= true true)

; solved problems 2-18 in the web browser.

; https://4clojure.oxal.org/#/problem/25
(= (filter (fn [x] (= 1 (mod x 2))) #{1 2 3 4 5}) '(1 3 5))

; https://4clojure.oxal.org/#/problem/26
((fn fibonacci
   ([n]
    (case n
      0 '()
      1 [1]
      2 [1 1]
      (fibonacci (- n 2) [1 1])))
   ([n s]
    (if (< n 1)
      s
      (fibonacci (- n 1) (conj s (reduce + (take-last 2 s))))))
   )
 4)

; https://4clojure.oxal.org/#/problem/27
; My solution.
((fn palindrome?
   [x]
    (case (count x)
       0 true
       1 true
       (if (= (first x) (last x))
          (palindrome? (drop-last (rest x)))
          false))
    ) '(1 2 1))

;QQQ: Given a sequence [1 2 3 4], how do I get a subset: [2 3]?
; Simpler solution found online
((fn [x] (= (reverse x)
            (seq x))) "racecar")

; QQQ: Explain https://4clojure.oxal.org/#/problem/51
(=
   [1 2 [3 4 5] [1 2 3 4 5]]
   (let [[a b & c :as d] '( 1 2 3 4 5 )] [a b c d]))

;https://4clojure.oxal.org/#/problem/99
(= ((fn [x y] (map #(Character/digit % 10) (seq (str (* x y))))) 999 99) [9 8 9 0 1])
(= ((fn [x y] (map (comp read-string str) (seq (str (* x y))))) 999 99) [9 8 9 0 1])

; https://4clojure.oxal.org/#/problem/50
((fn [x] (vals (group-by type x))) [1 :a 2 :b 3 :c])