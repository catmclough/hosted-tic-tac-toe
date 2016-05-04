(ns hosted-tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.core :refer :all]
            [hosted-tic-tac-toe.ttt-routes :as ttt-routes]))

(import '(http_messages Request$RequestBuilder)
        '(javaserver Server))

(defn stub-output [f]
  (with-out-str
    (f)))

(use-fixtures :once stub-output)

(def test-server (atom {:port nil}))

(defn update-server-port [new-port]
  (swap! test-server
         (fn [server-state]
           (merge server-state {:port new-port}))))

(defn mock-server [f]
  (with-redefs [create-server (fn [port router] (update-server-port port))]
    (with-redefs [run-server (fn [server] nil)]
      (f)
      (reset! test-server {:port nil}))))

(use-fixtures :each mock-server)

(deftest creates-server-with-given-port
  (-main "-P" "9090")
  (is (= (:port @test-server) 9090)))

(deftest creates-server-with-default-port
  (-main)
  (is (= (:port @test-server) 5000)))

