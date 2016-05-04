(ns hosted-tic-tac-toe.ttt-routes-test
  (:require [clojure.test :refer :all]
    [hosted-tic-tac-toe.ttt-routes :as routes]
    [hosted-tic-tac-toe.gameboard-responder :as gameboard-responder]))

(import '(responders RedirectResponder)
        '(http_messages Request$RequestBuilder ResponseHeader))

(deftest get-routes-returns-a-java-hashmap
  (is (= java.util.HashMap (type (routes/get-routes)))))

(deftest root-points-to-redirect-responder
  (is (= RedirectResponder (type (get (routes/get-routes) "/")))))

(deftest root-redirects-to-gameboard
  (is (= (str (.getKeyword ResponseHeader/REDIRECT) "/gameboard")
         (.getLine (get (.getHeaders (.getResponse (get (routes/get-routes) "/") (.build (Request$RequestBuilder. "GET /")))) 0)))))

(deftest gameboard-points-to-gameboard-responder
  (is (= (type (gameboard-responder/new-gameboard-responder)) (type (get (routes/get-routes) "/gameboard")))))
