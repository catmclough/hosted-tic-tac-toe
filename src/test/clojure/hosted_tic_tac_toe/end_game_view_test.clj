(ns hosted-tic-tac-toe.end-game-view-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-view :as view]
           [hosted-tic-tac-toe.copy-en-us :as copy]))

(def user-marker "X")

(def ai-marker "O")

(deftest tie-game-end-screen-info
  (is (true? (.contains (view/get-game-over-page "nil") copy/tie-game-text))))

(deftest end-screen-has-winner-info
  (is (true? (.contains (view/get-game-over-page ai-marker) (copy/winner-text ai-marker)))))

(deftest user-cannot-win-page-shows-illegal-action-was-taken
  (is (true? (.contains view/get-user-cannot-win-page copy/illegal-action-message))))

(deftest end-screen-has-button-to-start-new-game
  (is (true? (.contains (view/get-game-over-page ai-marker) view/new-game-button))))

(deftest new-game-button-sends-get-to-root-directory
  (is (true? (.contains view/new-game-button "action=\"/\" method=\"/get\""))))
