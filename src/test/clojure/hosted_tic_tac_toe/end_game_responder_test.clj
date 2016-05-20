(ns hosted-tic-tac-toe.end-game-responder-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-responder :as responder]
           [hosted-tic-tac-toe.end-game-view :as end-game-view]))

(import '(http_messages Request$RequestBuilder HTTPStatus ResponseHeader Request)
        '(java.net URLEncoder))

(def ai-marker "O")

(def user-marker "X")

(def end-game-responder (responder/new-end-game-responder))

(defn end-game-request [winner-marker]
  (.build (Request$RequestBuilder. (str "GET /game-over" (URLEncoder/encode (str "?winner=" winner-marker) "UTF-8")))))

(def unallowed-request (.build (Request$RequestBuilder. (str "POST /game-over"))))

(def request-that-throws-error (.build (Request$RequestBuilder. "GET /game-over?winner=")))

(deftest valid-end-game-response-status
    (is (= (.getStatusLine (.getResponse end-game-responder (end-game-request ai-marker))) (.getStatusLine HTTPStatus/OK))))

(deftest invalid-method-response-status
  (is (= (.getStatusLine (.getResponse end-game-responder unallowed-request)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(deftest catches-errors-and-responds
  (is (= (.getStatusLine (.getResponse end-game-responder request-that-throws-error)) (.getStatusLine HTTPStatus/NOT_FOUND))))

(deftest end-game-response-has-content-type-header
    (is (= (.getLine (first (.getHeaders (.getResponse end-game-responder (end-game-request ai-marker)))))
           (str (.getKeyword ResponseHeader/CONTENT_TYPE) end-game-view/content-type ";"))))

(deftest valid-end-game-response-has-winner-info
    (is (= (.getBody (.getResponse end-game-responder (end-game-request ai-marker))) (end-game-view/get-game-over-page ai-marker))))

(deftest end-game-response-with-user-winner-is-not-allowed
  (is (true? (.contains (.getBody (.getResponse end-game-responder (end-game-request user-marker))) end-game-view/get-user-cannot-win-page))))

