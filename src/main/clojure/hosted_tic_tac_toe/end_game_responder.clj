(ns hosted-tic-tac-toe.end-game-responder
  (require [hosted-tic-tac-toe.end-game-html :as end-game-html]))

(import '(responders Responder)
        '(http_messages Response$ResponseBuilder Header$HeaderBuilder
                        ResponseHeader HTMLContent Header HTTPStatus))

(def supported-methods ["GET"])

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-response-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
    (into-array Header [content-type])))

(defn- get-response-body [request]
    (end-game-html/get-page))

(defn- get-game-over-response [request]
  (if (not (request-is-supported request))
    (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))
    (.build (.body (.headers (.statusLine (Response$ResponseBuilder.)
        (.getStatusLine HTTPStatus/OK)) (get-response-headers)) (get-response-body request)))))

(defn new-end-game-responder []
  (reify
    Responder
      (getResponse [this request] (get-game-over-response request))))

