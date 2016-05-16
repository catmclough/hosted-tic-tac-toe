(ns hosted-tic-tac-toe.end-game-view-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-view :as view]
           [hosted-tic-tac-toe.copy-en-us :as copy]))

(deftest tie-game-end-screen-info
  (is (true? (.contains (view/get-page "nil") copy/tie-game-text))))

(deftest end-screen-has-winner-info
  (is (true? (.contains (view/get-page "X") (copy/winner-text "X")))))

(deftest end-screen-has-button-to-start-new-game
  (is (true? (.contains (view/get-page "X") view/new-game-button))))

(deftest new-game-button-sends-get-to-root-directory
  (is (true? (.contains view/new-game-button "action=\"/\" method=\"/get\""))))
