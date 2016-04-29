(ns hosted-tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.core :refer :all]
            [tictactoe.board :as board]))

(import '(http_messages Request$RequestBuilder)
        '(javaserver Server))

(deftest sets-chosen-port
  (testing "Sets up server with specified port arg"
    (is (= 9090 (getPortChoice (into-array String '("-P" "9090")))))))

(deftest sets-default-port
  (testing "Sets up server with default port (5000)"
    (is (= 5000 (getPortChoice (into-array String '()))))))

