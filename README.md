# Soluzioni Avanzate per la Progettazione Sicura e lo Sviluppo Efficiente di Microservizi

## Indice

- [Abstract](#abstract)
- [Obiettivi del Progetto](#obiettivi-del-progetto)
- [Contesto e Background](#contesto-e-background)
- [Metodologia](#metodologia)
- [Struttura del Progetto](#struttura-del-progetto)
- [Tecnologie Utilizzate](#tecnologie-utilizzate)
- [Requisiti](#requisiti)
- [Installazione e Configurazione](#installazione-e-configurazione)
- [Documentazione delle API](#documentazione-delle-api)
- [Risultati](#risultati)
- [Limitazioni e Sviluppi Futuri](#limitazioni-e-sviluppi-futuri)
## Abstract
Questo progetto fa parte della tesi intitolata "Soluzioni Avanzate per la Progettazione Sicura e lo Sviluppo Efficiente di Microservizi", in cui si analizzano tecniche di scalabilità e sicurezza per un’architettura a microservizi applicata a un sistema di eCommerce. Lo scopo è dimostrare come l’adozione di pratiche avanzate possa migliorare la resilienza, la sicurezza e l’efficienza di sistemi distribuiti.

## Obiettivi del Progetto
- Dimostrare i vantaggi dei microservizi in termini di scalabilità e modularità.
- Implementare un sistema di autenticazione centralizzato con Keycloak.
- Garantire la comunicazione sicura e asincrona tra i servizi tramite Apache Kafka.

## Contesto e Background
I microservizi rappresentano un’evoluzione rispetto ai sistemi monolitici, offrendo modularità e indipendenza tra i componenti. In questo progetto, si è scelto di utilizzare Spring Cloud per facilitare la comunicazione e la gestione dei microservizi.

## Metodologia
Il progetto è stato sviluppato seguendo un approccio modulare. Dopo un’analisi delle esigenze e delle tecnologie disponibili, i vari microservizi sono stati progettati e integrati tramite Docker e Docker Compose per garantire la portabilità.

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
Accedi alla documentazione Swagger per ogni microservizio: `http://<host>:<port>/<nome microservizio>/swagger-ui/index.html`.

## Risultati
Il progetto ha dimostrato che l’utilizzo di un’architettura a microservizi migliora la modularità e la scalabilità del sistema di eCommerce. L'integrazione di Keycloak ha permesso un'autenticazione centralizzata e sicura, mentre l'uso di Kafka ha facilitato la comunicazione asincrona tra i servizi.

## Limitazioni e Sviluppi Futuri
Il progetto è stato testato su un ambiente di sviluppo limitato. Per applicazioni in produzione, ulteriori ottimizzazioni di sicurezza e performance sono raccomandate. Sviluppi futuri potrebbero includere l'adozione di tecniche di monitoraggio e il miglioramento della resilienza tramite circuit breakers.

