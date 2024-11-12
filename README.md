# Soluzioni Avanzate per lo Sviluppo e la Sicurezza di Microservizi

Progetto di esempio di un sistema eCommerce realizzato con architettura a microservizi, sviluppato per dimostrare tecniche avanzate di sviluppo e sicurezza.

## Indice

- [Introduzione](#introduzione)
- [Struttura del Progetto](#struttura-del-progetto)
- [Tecnologie Utilizzate](#tecnologie-utilizzate)
- [Requisiti](#requisiti)
- [Installazione e Configurazione](#installazione-e-configurazione)
- [Documentazione delle API](#documentazione-delle-api)

## Introduzione

Questo progetto implementa un esempio di sistema eCommerce utilizzando un'architettura a microservizi. Ogni componente è sviluppato per funzionare in modo autonomo e scalabile, facilitando la gestione, il mantenimento e l’espansione del sistema.

## Struttura del Progetto

Il sistema è composto dai seguenti microservizi:

- **Gestione Clienti**: gestisce autenticazione e autorizzazioni degli utenti.
- **Catalogo Prodotti**: gestisce i dettagli dei prodotti.
- **Gestione Carrello**: gestisce le operazioni sul carrello.
- **Processamento Ordini**: coordina il processo di gestione degli ordini.
- **Inventario**: monitora le disponibilità dei prodotti.
- **Gateway**: punto d’accesso centralizzato per instradare le richieste.
- **Server di scoperta (Eureka)**: registra e monitora i microservizi.
- **Keycloak**: gestisce l’autenticazione e autorizzazione centralizzate.

## Tecnologie Utilizzate

Le principali tecnologie utilizzate includono:

- **Spring Framework** per l'implementazione del backend.
- **Spring Cloud Netflix Eureka** per il service discovery.
- **Spring Cloud Gateway** per la gestione del gateway.
- **Spring Cloud OpenFeign** per la comunicazione sincrona.
- **Apache Kafka** per la comunicazione asincrona basata su eventi.
- **Docker** per la containerizzazione dei microservizi.
- **Swagger/OpenAPI** per la documentazione delle API.
- **MySQL** e **Spring Data JPA** per la gestione dei dati.
- **Keycloak** per la gestione della sicurezza.

## Requisiti

Assicurati di avere installato:

- [Docker](https://www.docker.com/)
- [JDK 11+](https://www.oracle.com/java/technologies/javase-downloads.html)

## Installazione e Configurazione

1. **Clona il repository**
   ```bash
   git clone https://github.com/booleanadv97/spring-microservices-example.git
   cd spring-microservices-example

2. **Configura i Servizi**
Modifica il file di configurazione **docker-compose.yml** per le connessioni al database e ad altri servizi, come Eureka e Kafka.

3. **Avvia i Container Docker**
Utilizza Docker Compose per avviare i container dei vari servizi:
   ```bash
   docker-compose up -d

## Documentazione delle API
Le API di ciascun microservizio sono documentate tramite Swagger:
Accedi alla documentazione Swagger per ogni microservizio all’indirizzo:

   ```bash
   http://<host>:<port>/<nome microservizio>/swagger-ui/index.html
