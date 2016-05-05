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

(def mock-server (atom {:port nil}))

(def mock-router (atom {:port nil}))

(defn update-atom [atom-object new-data]
  (swap! atom-object
         (fn [atom-state]
           (merge atom-state new-data))))

(defn create-mock-server [f]
  (with-redefs [get-server (fn [chosen-port router] (update-atom mock-server {:port chosen-port}))]
    (with-redefs [run-server (fn [server] nil)]
      (f)
      (reset! mock-server {:port nil}))))

(use-fixtures :each create-mock-server)

(deftest creates-server-with-given-port
  (-main "-P" "9090")
  (is (= (:port @mock-server) 9090)))

(deftest creates-server-with-default-port
  (-main)
  (is (= (:port @mock-server) 5000)))

(deftest creates-router-with-ttt-routes
  (with-redefs [get-router (fn [app-routes] (update-atom mock-router {:routes app-routes}))]
    (-main)
    (is (= (keys (:routes @mock-router)) (keys (ttt-routes/get-routes))))))
