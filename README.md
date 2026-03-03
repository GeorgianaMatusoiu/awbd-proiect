## Introducere
Această bază de date modelează activitatea unei farmacii, gestionând informații despre farmaciști, clienți, rețete, medicamente, furnizori și categorii de medicamente.
Un client poate avea mai multe rețete, iar fiecare rețetă este procesată de un farmacist și poate conține mai multe medicamente. Relația dintre rețete și medicamente este implementată prin entitatea intermediară DetaliiReteta, care păstrează informații suplimentare precum cantitatea și prețul.
Medicamentele sunt furnizate de furnizori și organizate în categorii, iar fiecare client poate avea un profil și un card de fidelitate. Modelul reflectă structura reală a unei farmacii și asigură o organizare coerentă a datelor.

## Diagrama ER

![Diagrama ER](docs/diagrama-er.jpeg)
