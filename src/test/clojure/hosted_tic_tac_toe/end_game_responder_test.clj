(ns hosted-tic-tac-toe.end-game-responder-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-responder :as responder]
           [hosted-tic-tac-toe.end-game-html :as html]))

(import '(http_messages Request$RequestBuilder HTTPStatus ResponseHeader HTMLContent))

(def end-game-responder (responder/new-end-game-responder))

(def end-screen-request (.build (Request$RequestBuilder. "GET /game-over")))

(def unallowed-request (.build (Request$RequestBuilder. "POST /game-over")))

(deftest valid-end-game-response-status
  (is (= (.getStatusLine (.getResponse end-game-responder end-screen-request)) (.getStatusLine HTTPStatus/OK))))

(deftest invalid-end-game-response-status
    (is (= (.getStatusLine (.getResponse end-game-responder unallowed-request)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(deftest end-game-response-has-html-content-header
  (is (= (.getLine (get (.getHeaders (.getResponse end-game-responder end-screen-request)) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest end-game-response-has-end-game-html
  (is (= (.getBody (.getResponse end-game-responder end-screen-request)) (html/get-page))))

(deftest unallowed-end-game-response-has-empty-body
  (is (empty? (.getBody (.getResponse end-game-responder unallowed-request)))))
