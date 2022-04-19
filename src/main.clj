; https://4clojure.oxal.org/#/problem/1
(= true true)

; solved problems 2-18 in the web browser.

; https://4clojure.oxal.org/#/problem/25
(= (filter (fn [x] (= 1 (mod x 2))) #{1 2 3 4 5}) '(1 3 5))

; https://4clojure.oxal.org/#/problem/26
((fn fibonacci
   ([n]
    (case n
      0 ()
      1 '(1)
      2 '(1 1)
      (fibonacci (- n 2) '(1 1))))
   ([n s]
    (if (< n 1)
      s
      (fibonacci (- n 1) (concat s [(reduce + (take-last 2 s))]))))
   )
 8)
; QQQ: How to do early return? Less indentation is clean code. But SO answers say early return is a bad idea.
; QQQ: append/conjoin to end of list. conj is super confusing.
