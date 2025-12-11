(ns mood-advisor.core
  (:require [clojure.string :as str]))

(def word-sentiment
  {"srec" 2, "odlicn" 2, "super" 2, "fantastic" 2, "sjajn" 2, "predivn" 2, "uzbudj" 2,
   "dobr" 1, "ok" 1, "okej" 1, "mirn" 1, "opusten" 1, "zadovol" 1, "smiren" 1,
   "tug" -1, "napet" -1, "umor" -1, "nervoz" -1, "glavobolj" -1, "bol" -1, "hladn" -1, "dosadn" -1,
   "stres" -2, "uzas" -2, "katastrof" -2, "ocajn" -2, "grozn" -2, "panik" -2, "uznemir" -2, "haos" -2})

(defn normalize-word [w]
  (let [base (-> w
                 str/lower-case
                 (str/replace #"č|ć" "c")
                 (str/replace #"š" "s")
                 (str/replace #"ž" "z")
                 (str/replace #"đ" "dj")
                 (str/replace #"lj" "l")
                 (str/replace #"nj" "n")
                 (str/replace #"(anjem|anje|ovima)$" "")
                 (str/replace #"(ima|ama|ovi|oga|ome|om|em)$" "")
                 (str/replace #"(ti|lo|na|no|ne|an|na|e|a|i|o|u)$" ""))]
    (cond
      (re-find #"glav" base) "glavobolj"
      :else base)))

(defn extract-keywords [text]
  (let [tokens (str/split (str/lower-case text) #"\W+")
        norm   (map normalize-word tokens)]
    (filter #(contains? word-sentiment %) norm)))

(defn mood-score [keywords]
  (let [raw (reduce (fn [acc w]
                      (+ acc (get word-sentiment w 0)))
                    0
                    keywords)]
    (cond
      (> raw 5) 5
      (< raw -5) -5
      :else raw)))

(defn detect-mood [score]
  (cond
    (>= score 3)  :very-positive
    (>= score 1)  :positive
    (<= score -3) :very-negative
    (<= score -1) :negative
    :else         :neutral))

(defn minimal-recommendation [mood score]
  (case mood
    :very-positive (str "Zvuči odlično! (skor: " score ")\nPreporuka: Iskoristi energiju na nešto kreativno.")
    :positive      (str "Deluje dobro. (skor: " score ")\nPreporuka: Nastavi sa dobrim navikama.")
    :neutral       (str "Neutralno raspoloženje. (skor: " score ")\nPredlog: Uradi nešto lagano i prijatno.")
    :negative      (str "Deluje kao težak dan. (skor: " score ")\nPredlog: Pravi pauzu, opusti se, udahni duboko.")
    :very-negative (str "Deluje baš teško. (skor: " score ")\nPredlog: Odmori, smanji pritisak i potraži podršku ako treba.")
    ))

(defn analyze-and-respond [text]
  (let [keywords (vec (extract-keywords text))
        score    (mood-score keywords)
        mood     (detect-mood score)
        msg      (minimal-recommendation mood score)]
    (println msg)))

(defn advice [text]
  (analyze-and-respond text))