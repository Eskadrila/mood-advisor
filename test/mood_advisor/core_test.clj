(ns mood-advisor.core-test
  (:require
    [midje.sweet :refer :all]
    [mood-advisor.core :refer :all]))

(fact "pozitivan tekst daje pozitivno raspoloženje"
      (-> "Srećan sam i osećam se super"
          extract-keywords
          mood-score
          detect-mood)
      => :positive)

(fact "negativan tekst daje veoma negativno raspoloženje"
      (-> "Ovo je katastrofa i potpuni haos"
          extract-keywords
          mood-score
          detect-mood)
      => :very-negative)