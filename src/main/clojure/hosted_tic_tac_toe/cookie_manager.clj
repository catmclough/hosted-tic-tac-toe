(ns hosted-tic-tac-toe.cookie-manager)

(import '(http_messages Header$HeaderBuilder))

(def session-cookie "sessionId=5623")

(def set-cookie-header (.build (Header$HeaderBuilder. (str "Set-Cookie: " session-cookie))))
