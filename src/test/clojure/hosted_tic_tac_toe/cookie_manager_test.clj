(ns hosted-tic-tac-toe.cookie-manager-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.cookie-manager :refer :all]))

(deftest session-cookie-header-sets-cookie
  (is (true? (.startsWith (.getLine (get-set-cookie-header)) "Set-Cookie: "))))

(deftest session-cookie-header-has-session-id
  (is (not (nil? (re-find #"sessionId=\d+" (.getLine (get-set-cookie-header)))))))

(deftest remove-cookies-header-deletes-session-id
  (is (true? (.startsWith (.getLine remove-cookies-header) "Set-Cookie: sessionId=deleted;"))))

(deftest remove-cookies-header-sets-expiration-date-in-the-past
  (is (true? (.contains (.getLine remove-cookies-header) "expires=Thu, 01 Jan 1970 00:00:00 GMT"))))
