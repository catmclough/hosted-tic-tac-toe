(ns hosted-tic-tac-toe.end-game-responder-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-responder :as responder]
           [hosted-tic-tac-toe.end-game-html :as html]))

(import '(http_messages Request$RequestBuilder HTTPStatus ResponseHeader HTMLContent Request)
        '(java.net URLEncoder))

(def end-game-responder (responder/new-end-game-responder))

(def x-winner-end-game-request (.build (Request$RequestBuilder. (str "GET /game-over?" (URLEncoder/encode "winner=X")))))

(def unallowed-request (.build (Request$RequestBuilder. "POST /game-over")))

(def invalid-request (.build (Request$RequestBuilder. "GET /game-over?winner=")))

(deftest valid-end-game-response-status
  (is (= (.getStatusLine (.getResponse end-game-responder x-winner-end-game-request)) (.getStatusLine HTTPStatus/OK))))

(deftest invalid-method-response-status
    (is (= (.getStatusLine (.getResponse end-game-responder unallowed-request)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(deftest catches-errors-and-responds
    (is (= (.getStatusLine (.getResponse end-game-responder invalid-request)) (.getStatusLine HTTPStatus/NOT_FOUND))))

(deftest end-game-response-has-html-content-header
  (is (= (.getLine (get (.getHeaders (.getResponse end-game-responder x-winner-end-game-request)) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest end-game-response-has-end-game-html
  (is (= (.getBody (.getResponse end-game-responder x-winner-end-game-request)) (html/get-page "X"))))

(deftest unallowed-end-game-response-has-empty-body
  (is (empty? (.getBody (.getResponse end-game-responder unallowed-request)))))
