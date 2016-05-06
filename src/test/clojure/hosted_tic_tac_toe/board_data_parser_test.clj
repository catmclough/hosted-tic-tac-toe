(ns hosted-tic-tac-toe.board-data-parser-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.board-data-parser :refer :all]))

(def raw-request-data (str "board=X12345678&choice=1&marker=O"))

(def board ["X" 1 2 3 4 5 6 7 8])

(def choice 1)

(def marker "O")

(deftest parses-board
  (is (= (get-board raw-request-data) board)))

(deftest parses-spot-choice
  (is (= (get-spot-choice raw-request-data) choice)))

(deftest parses-player-marker
  (is (= (get-player-marker raw-request-data) marker)))
