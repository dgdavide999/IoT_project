<h1 style="text-align: center;">TrafficTraker</h1>

 <h4> 1. Obiettivo </h4>
TrafficTraker è un'app sviluppata per Android che permette di sapere in tempo reale le condizioni del traffico. Il sistema funziona grazie ad un’ intelligenza artificiale che, attraverso delle telecamere, osserva il numero di veicoli che passano per un determinato percorso e la loro velocità.

<h4> 2. Target </h4>
il nostro sistema è pensato per essere utilizzato dai vari e singoli comuni (essendo loro gli unici che possono posizionare e usufruire di videocamere su suolo pubblico): ogni comune gestirà autonomamente il numero e la posizione delle videocamere, mentre il dispositivo si occuperà di indirizzare le richieste al server corretto in base alla posizione.
<h4> 3. Funzionalità </h4>
   <h5>Pagina Iniziale <h5>
All'avvio l’app farà periodicamente una richiesta delle coordinate GPS, (richiederà eventuali permessi se non sono mai stati concessi e chiederà di attivare il GPS se esso è disattivo).
ricevuta la risposta la schermata iniziale sarà simile a questa:<br>
## inserisci immagine
 i dati visualizzati saranno: 
<ul style="list-style-type:circle">
  <li>Latitudine</li>
  <li>Longitudine</li>
  <li>Altitudine</li>
  <li>Accuratezza della posizione</li>
  <li>Velocità</li>
  <li>Indirizzo</li>
</ul>

Accuratezza e Velocità non sono supportate da tutti i dispositivi.

È inoltre possibile aumentare la precisione a discapito di un dispendio di energia maggiore (usando il sensore GPS)
o mantenere un dispendio di energia più basso non utilizzando effettivamente il GPS ma facendo richieste alle torri cellulare.
È anche possibile fermare l’aggiornamento della posizione dato che essa è irrilevante per sapere lo stato del traffico nelle varie strade.
  <h5>Mappa<h5>
    <br>##inserisci immagine<br>
Una volta cliccato il pulsante “show map” verrà mostrata una mappa in prossimità della posizione corrente, con la posizione effettiva segnata da un marker azzurro.
Da qui sarà possibile trascinare e/o applicare uno zoom alla mappa e vedere le telecamere sparse per le zone segnate con marker rossi,se uno di questi marker vengono toccati, apparirà una schermata che mostrerà tutte le informazioni della telecamera.
    
Una volta cliccato il pulsante “show map” verrà mostrata una mappa in prossimità della posizione corrente, con la posizione effettiva segnata da un marker azzurro.
Da qui sarà possibile trascinare e/o applicare uno zoom alla mappa e vedere le telecamere sparse per le zone segnate con marker rossi,se uno di questi marker vengono toccati, apparirà una schermata che mostrerà tutte le informazioni della telecamera.
 <h5>Informazioni sul traffico </h5>
    
Le prime informazioni che notiamo in questa schermata sono, il nome della strada/via in cui si trova la videocamera e il suo stato, quest'ultimo può essere di due tipi: “attivo” o “disattivo”, lo stato disattivo può voler dire che la telecamera è disattiva per aggiornamenti, riparazione, calibrazione o per inagibilità della strada; se la telecamera è disattiva non riceveremo altre informazioni.

Se la telecamera è attiva riceveremo lo stato del traffico, esso può essere di tre tipi:
“scorrevole”, “traffico” o “traffico intenso” 
<br>##inserisci immagine<br><br>
    Server side : https://github.com/v-Alenz/ServerIoTpp.git

