(ns hosted-tic-tac-toe.gameboard-html-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.gameboard-html :refer :all]))

(def gameboard [0 "X" 2 "O" 4 5 6 7 8])

(deftest board-contains-marker-if-spot-is-taken
  (is (true? (and (.contains (formatted-board gameboard) "<td>X</td>")
                  (.contains (formatted-board gameboard) "<td>O</td>")))))

(deftest unfilled-spots-contain-form-with-post-method
  (is (true? (.contains (formatted-board gameboard) "<form action=\"/gameboard\" method=\"post\">"))))
