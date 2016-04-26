(ns hosted-tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.core :refer :all]
            [tictactoe.board :as board]))

(import [javaserver App Form RequestParser])

(deftest imports-tictactoe
  (testing "Successfully calls upon functions in the tictactoe namespace"
    (is (= (vec(range board/board-size)) (board/make-board)))))

;(deftest sets-default-port
  ;(testing "Running hosted-tic-tac-toe.core sets up server with port args"
    ;(do
      ;(with-out-str (-main))
        ;(is (= (App/port) 5000)))))

;(deftest sets-chosen-port
  ;(testing "Running hosted-tic-tac-toe.core sets up server with port args"
    ;(do
      ;(with-out-str (-main "-P" "8888"))
        ;(is (= (App/port) 8888)))))

(deftest redirects-user-to-game-board
  (testing "GET request to root directory redirects browser to /game-board"
    (do
      (with-out-str (-main)
        (let [request (RequestParser/createRequest  "GET /")]
          (is (= "/" (.getURI request))))))))
