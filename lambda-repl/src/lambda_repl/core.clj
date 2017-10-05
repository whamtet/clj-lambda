(ns lambda-repl.core
  (:require
    [clj-http.lite.client :as client]))

(defn execute-form [s]
  (->
    (client/post "https://wrtbygcox0.execute-api.us-west-2.amazonaws.com/dev/test"
                 {:body s})
    :body
    read-string))

(defmacro e [& forms]
  `(execute-form (pr-str '(fn [~'input ~'context] ~@forms))))

(defn repl []
  (let [command (read-line)]
    (when (not= "exit" command)
      (prn (execute-form (format "(fn [input context] %s)" command)))
      (recur))))
