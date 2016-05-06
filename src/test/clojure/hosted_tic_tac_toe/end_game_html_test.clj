(ns hosted-tic-tac-toe.end-game-html-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-html :as html]))

(deftest tie-game-end-screen-info
  (is (true? (.contains (html/get-page "nil") html/tie-game-text))))

(deftest end-screen-has-winner-info
  (is (true? (.contains (html/get-page "X") (html/winner-text "X")))))

(deftest end-screen-has-button-to-start-new-game
  (is (true? (.contains (html/get-page "X") html/new-game-button))))

(deftest new-game-button-sends-get-to-root-directory
  (is (true? (.contains html/new-game-button "action=\"/\" method=\"/get\""))))
