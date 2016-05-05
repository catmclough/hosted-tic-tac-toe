(ns hosted-tic-tac-toe.end-game-html-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-html :as html]))

(deftest end-screen-has-button-to-start-new-game
  (is (true? (.contains (html/get-page) html/new-game-button))))

(deftest new-game-button-sends-get-to-root-directory
  (is (true? (.contains html/new-game-button "action=\"/\" method=\"/get\""))))
