# Deduplicarea Datelor de Produse



Pentru a deduplica setul de date, mai intai am analizat ce informatii am in fisier si ce as putea folosi ca sa rezolv aceasta problema. Astfel, am inceput prin a grupa produsele initiale dupa **UNSPSC**.

Din fiecare grup cu acelasi unspsc am creat un singur produs care pune informatiile dupa niste reguli explicite:

* **Title**: mai intai grupam titlurile similare in proportie de ≥85% cu algoritmul Jaro–Winkler; din fiecare categorie rezultata alegem titlul cel mai complex, adica cel mai lung.
* **Name, Summary si Brand**: se aleg valorile care apar cel mai frecvent; daca exista mai multe valori cu aceeasi frecventa, sunt incluse toate variantele.
* **Root\_domain si Page\_url**: pastram toate valorile distincte.

**Motivatie**

Am decis sa unesc astfel informatiile pentru ca am considerat ca toate campurile alese sunt importante pentru descrierea unui produs si, fiind destul de diversificate, nu mi s-ar parea corect sa las doar una dintre ele. In plus, am observat ca multe dintre celelalte categorii nu sunt completate si nu sunt la fel de relevante ca aceste categorii principale.

**Formatul de intrare/iesire**

Am ales sa transform fisierul Parquet intr-unul JSON pentru ca am mai lucrat cu astfel de fisiere si am vrut sa ma asigur ca vin cu o rezolvare cat mai buna, folosind cunostintele pe care le am.

**Imbunatatiri posibile**

* Folosirea mai multor tehnici de fuzzy matching pe fiecare categorie afisata, pentru a avea informatii mai consistente si pentru a nu incarca produsul cu cantitati mari de date redundante.
* Adaugarea de teste unitare pentru logica de fuziune si validarea datelor.

