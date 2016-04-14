(ns hosted-tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.core :refer :all]
            [tictactoe.board :as board]))

(import [javaserver Form])

(deftest imports-tictactoe
  (testing "Successfully calls upon functions in the tictactoe namespace."
    (is (=  (vec(range board/board-size)) (board/make-board)))))

(deftest imports-java-server
  (testing "Successfully calls upon functions in the Java Server."
    (let [form (new Form "some data")]
      (is (= (.getData form) "some data")))))
