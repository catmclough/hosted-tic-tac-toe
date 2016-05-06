(ns hosted-tic-tac-toe.gameboard-html-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.gameboard-html :refer :all]))

(def gameboard [0 "X" 2 "O" 4 5 6 7 8])

(deftest board-displays-contents-of-filled-spots
  (is (true? (.contains (get-page gameboard) (get-filled-space gameboard 1)))))

(deftest unfilled-spots-contain-button
  (is (true? (.contains (get-page gameboard) (get-button (apply str gameboard) 0 "X")))))
