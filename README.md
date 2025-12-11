# Mood Advisor (Clojure)

Jednostavan Clojure projekat koji analizira tekst koji korisnik unese i daje kratku preporuku na osnovu raspoloženja.

---

## Pokretanje projekta

Pokretanje u REPL-u:

(require '[mood-advisor.core :as ma])

(ma/advice "Danas me boli glava i umoran sam.")