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


; Simpler solution found online
((fn [x] (= (reverse x)
            (seq x))) "racecar")

; https://4clojure.oxal.org/#/problem/51
(=
  [1 2 3 [4 5] [1 2 3 4 5]]
  (let [[a b e & c :as d] '(1 2 3 4 5)] [a b c e d]))

; https://4clojure.oxal.org/#/problem/99
(= ((fn [x y] (map #(Character/digit % 10) (seq (str (* x y))))) 999 99) [9 8 9 0 1])
(= ((fn [x y] (map (comp read-string str) (seq (str (* x y))))) 999 99) [9 8 9 0 1])

; https://4clojure.oxal.org/#/problem/50
((fn [x] (vals (group-by type x))) [1 :a 2 :b 3 :c])

; https://4clojure.oxal.org/#/problem/114
; Super interesting problem on lazy-sequences!
((fn global-take-while
   ([n pred s]
    (lazy-seq
      (when (> n 0)
        (if (pred (first s))
          (when (> n 1)
            (cons (first s) (global-take-while (dec n) pred (rest s))))
          (cons (first s) (global-take-while n pred (rest s))))))
    )
   )
 4 #(= 2 (mod % 3)) [2 3 5 7 11 13 17 19 23])

; https://4clojure.oxal.org/#/problem/158
; (reduce + 10 [1 2 3])
; (+ 10 1)
; (reduce + 11 [2 3])
; (reduce + 13 [3])
; (reduce + 16 [])
; (reduce #(% %2) f [1 2 3 4])
; (reduce #(% %2) (f [b] ... (* 1 b c d)  [2 3 4])
; (reduce #(% %2) (f [c] ... (* 1 2 c d)  [3 4])
; (reduce #(% %2) (f [d] (* 1 2 3 d)  [4])
; (reduce #(% %2) (* 1 2 3 4)  [])
; decurry reduces a curry of functions to a single function
; The single function takes variable number of args
(((fn [f]
    (fn [& a]
      (reduce #(% %2) f a)))
  (fn [a]
    (fn [b]
      (fn [c]
        (fn [d]
          (* a b c d))))))
 1 2 3 4)

; https://4clojure.oxal.org/#/problem/177
; Couldn't solve myself. Had to look up solutions.
; Understood that string when passed to reduce is broken into characters.
#(= ()
    (reduce
      (fn [a x]
        (if a (case x
                (\[ \{ \() (conj a x)
                (\] \} \))
                (if (= ({\] \[ \} \{ \) \(} x) (first a))
                  (rest a) nil)
                a)))
      () %))

; Kitty's solution
(defn balanced? [s]
  (let [pairs {\( \) \{ \} \[ \]} ;; Map is chosen here because it's versatile.
        openers (set (keys pairs)) ;; Using sets here,
        closers (set (vals pairs)) ;; And here, so we can use contains? later.
        ;; Create a set by combining the openers and closers.
        search-set (into openers closers)
        ;; We can now simply filter the string against the set,
        ;; To return just the pair-occurrences that we care about.
        occurrences (filter #(contains? search-set %) s)]
    (empty? ;; If the height-stack is empty, stuff should be balanced.
      (reduce (fn [height-stack occurrence]
                (cond
                  ;; (contains?) reads better than get or shorthand
                  (contains? openers occurrence)
                  (conj height-stack occurrence)
                  ;; Checks if the closing occurrence closes the top of the height-collecting stack.
                  ;; If so, pop the top.
                  ;; (peek) is more efficient than (last) on a vector.
                  (and (contains? closers occurrence)
                       (= (get pairs (peek height-stack)) occurrence))
                  (pop height-stack)
                  ;; This is sort of a null-case, for dangling closers.
                  ;; It's efficient to exit-early here, hence wrap in (reduced)
                  :else (reduced (conj height-stack occurrence))))
              []
              occurrences))))
; I wanted to solve this by regex pattern matching,
; Realized that iterating over is much more efficient.

; https://4clojure.oxal.org/#/problem/76
; Writing trampoline is easy
; Check if return value is funciton using function?
; Do recursion accordingly.
(= [1 3 5 7 9 11]
   (letfn
     [(foo [x y] #(bar (conj x y) y))
      (bar [x y] (if (> (last x) 10)
                   x
                   #(foo x (+ 2 y))))]
     (trampoline foo [] 1)))

; https://4clojure.oxal.org/#/problem/106
; Double, Halve or Add 2
; Unsolved

; https://4clojure.oxal.org/#/problem/73
; This solution isn't extensible :( 😭
((defn tic-tac-toe
   ([board] (tic-tac-toe 0 board))
   ([c board]
    ; QQQ: Idiomatic placeholder in Clojure is '_'?
    ; Iterate over all rows to find a winner.
    (let [winner (reduce (fn [_ r]
                           (when (and (= 1 (count (distinct r)))
                                      (not (= :e (first r))))
                             (reduced (first r))))
                         []
                         board)]
      (if winner
        winner
        (case c
          ; QQQ: Don't understand how (apply map vector board) works.
          ; Source: https://stackoverflow.com/a/8315139/4552292.

          ; Transpose the matrix over diagonal so columns become rows.
          0 (tic-tac-toe 1 (apply map vector board))
          ; Provide 2 diagonals as rows.
          1 (tic-tac-toe 2 [(map-indexed (fn [i e] (get e i)) board)
                            (map-indexed (fn [i e] (get e (- 2 i))) board)])
          2 nil)))))
 [[:x :e :e]
  [:o :x :e]
  [:o :e :x]])