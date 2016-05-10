(ns hosted-tic-tac-toe.end-game-responder-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-responder :as responder]
           [hosted-tic-tac-toe.cookie-manager :as cookie-manager]
           [hosted-tic-tac-toe.end-game-html :as end-game-view]))

(import '(http_messages Request$RequestBuilder HTTPStatus ResponseHeader Request)
        '(java.net URLEncoder))

(def required-cookie (str "Cookie: " cookie-manager/session-cookie))

(def end-game-responder (responder/new-end-game-responder))

(def valid-end-game-request
  (.build (Request$RequestBuilder. (str "GET /game-over" (URLEncoder/encode "?winner=X" "UTF-8") (Request/newLine) required-cookie))))

(def unallowed-request (.build (Request$RequestBuilder. (str "POST /game-over" (Request/newLine) required-cookie))))

(def invalid-request (.build (Request$RequestBuilder. (str "GET /game-over?winner=" (Request/newLine) required-cookie))))

(def request-without-cookie (.build (Request$RequestBuilder. "GET /game-over?/winner=X")))

(deftest valid-end-game-response-status
  (is (= (.getStatusLine (.getResponse end-game-responder valid-end-game-request)) (.getStatusLine HTTPStatus/OK))))

(deftest invalid-method-response-status
  (is (= (.getStatusLine (.getResponse end-game-responder unallowed-request)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(deftest request-without-cookie-gets-method-not-allowed-status
  (is (= (.getStatusLine (.getResponse end-game-responder request-without-cookie)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(deftest catches-errors-and-responds
  (is (= (.getStatusLine (.getResponse end-game-responder invalid-request)) (.getStatusLine HTTPStatus/NOT_FOUND))))

(deftest end-game-response-has-content-type-header
  (is (= (.getLine (get (.getHeaders (.getResponse end-game-responder valid-end-game-request)) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) end-game-view/content-type ";"))))

(deftest end-game-response-has-end-game-view
  (is (= (.getBody (.getResponse end-game-responder valid-end-game-request)) (end-game-view/get-page "X"))))

(deftest request-without-cookie-does-not-display-a-winner
  (is (false? (.contains (.getBody (.getResponse end-game-responder request-without-cookie)) (end-game-view/get-page "X")))))

