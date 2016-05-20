(ns hosted-tic-tac-toe.ttt-router-test
  (:require [clojure.test :refer :all]
    [hosted-tic-tac-toe.ttt-router :as router]
    [hosted-tic-tac-toe.gameboard-responder :as gameboard-responder]
    [hosted-tic-tac-toe.end-game-responder :as end-game-responder]))

(import '(responders RedirectResponder ErrorResponder)
        '(http_messages Request$RequestBuilder ResponseHeader))

(def ttt-router (router/new-ttt-router))

(def unrecognized-request (.build (Request$RequestBuilder. "GET /inconceivable-route")))

(def redirect-request (.build (Request$RequestBuilder. "GET /")))

(def gameboard-request (.build (Request$RequestBuilder. "GET /gameboard")))

(def end-game-request (.build (Request$RequestBuilder. "GET /game-over?winner=X")))

(deftest get-routes-returns-a-java-hashmap-of-strings-and-responders
  (is (= String (type (first (keys (router/get-routes-and-responders)))))))

(deftest unrecognized-route-returns-error-responder
  (is (= ErrorResponder (type (.getResponder ttt-router unrecognized-request)))))

(deftest root-points-to-redirect-responder
  (is (= RedirectResponder (type (.getResponder ttt-router redirect-request)))))

(deftest root-redirects-to-gameboard
  (is (= (str (.getKeyword ResponseHeader/REDIRECT) "/gameboard")
         (.getLine (first (.getHeaders (.getResponse (.getResponder ttt-router redirect-request) redirect-request)))))))

(deftest gameboard-points-to-gameboard-responder
  (is (= (type (gameboard-responder/new-gameboard-responder)) (type (.getResponder ttt-router gameboard-request)))))

(deftest game-over-points-to-endgame-responder
  (is (= (type (end-game-responder/new-end-game-responder)) (type (.getResponder ttt-router end-game-request)))))
