(ns hosted-tic-tac-toe.gameboard-responder
  (:require [tictactoe.board :as board]))

(import '(responders Responder)
        '(http_messages HTTPStatus HTMLContent Request
                        Response$ResponseBuilder Header$HeaderBuilder
                        Header ResponseHeader))

(def page-name "Game Board")

(def supported-methods ["GET" "PUT"])

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-status-line [request]
  (if (request-is-supported request)
    (.getStatusLine HTTPStatus/OK)
    (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))

(defn- get-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
    (into-array Header [content-type])))

(defn- get-board-data [request]
  (let [board (.getData request)]
      board))

(defn parse-board-data [board-string]
  (let [symbolic-board (map read-string (clojure.string/split (clojure.string/replace board-string #"\[|\]" "") #" "))]
    (vec (map #(if (not (number? %)) (name %) %) symbolic-board))))

(defn- update-board [request-data]
  (let [lines (clojure.string/split request-data #"\r\n")]
    (let [board-string (first lines)]
      (let [current-board (parse-board-data board-string)]
        (let [choice (read-string (second lines))]
          (board/fill-space choice (last lines) current-board))))))

(defn- get-body [request]
  (if (request-is-supported request)
    (cond
      (= (.getMethod request) "GET")
        (str (HTMLContent/openHTMLAndBody page-name) (board/make-board) (HTMLContent/closeBodyAndHTML))
      (= (.getMethod request) "PUT")
        (str (HTMLContent/openHTMLAndBody page-name) (update-board (.getData request)) (HTMLContent/closeBodyAndHTML))
      :else "")
    ""))

(defn get-board-response [request]
  (let [status-line (get-status-line request)]
    (let [headers (get-headers)]
      (let [body (get-body request)]
        (.build (.body (.headers (.statusLine (Response$ResponseBuilder.) status-line) headers) body))))))

(defn new-gameboard-responder []
  (reify
    Responder
      (getResponse [this request] (get-board-response request))))
