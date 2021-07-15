# Esperimenti con GoCD 

Per la configurazione, vedi gli script di installazione. 

In breve, i task vengono eseguiti sugli agenti, in cui deve essere installato il software di sviluppo necessario (e.g., Jave e Gradle). 
Il PATH usato dall'agente deve essere però configurato appositamente. 

I plugin di interesse devono essere configurati nel server. 

## Concetti di GoCD 

Vedi https://docs.gocd.org/current/introduction/concepts_in_go.html 

Task: 
Un'azione da eseguire. 
Di solito è un singolo comando. 

Job: 
Una sequenza di task. 
I cambiamenti effettuati da un task nel filesystem sono visibili ai task successivi. 

Stage: 
Un gruppo di job, che vengono eseguiti ognuno indipendentemente dagli altri, in parallelo. 

Pipeline: 
Una sequenza di stage. 

Material: 
Il motivo per eseguire una pipeline. 
Di solito è codice sorgente. 

Dependency: 
L'esito di uno stage può essere usato come material per un'altra pipeline. 

Value Stream Map: 
Una deployment pipeline complessiva (può comprendere molte pipeline). 

Artifact: 
Ogni job può pubblicare uno o più artifact (file o folder). 
Per esempio, un job può pubblicare l'artifact hello/build/libs/* in hello-libs (potrebbe essere un solo file hello.jar). 
Un altro job può fare una fetch da hello-libs in hello-api-test/tmp. 
In questo job, trovo i file in hello-api-test/tmp/hello-libs. 


## Installazione 

### Installazione su un nodo 

Un solo nodo per il go-server e il go-agent. 
Funziona senza particolari problemi. 

### Installazione su due nodi  

Nodi separati per il go-server e il go-agent. 

Dopo un po' di fatica, finalmente funziona. 
- il server, dopo essere partito, deve condividere la chiave per l'autoregistrazione 
- l'agente deve essere configurato come segue: 
  - specificare la locazione del server 
  - specificare la chiave di autoregistrazione 


## Esperimenti 

### Semplice pipeline 

Una sola pipeline 
- Un solo stage 
  - Un solo job 
    - Un solo task (di tipo Script Executor)  

### Altra semplice pipeline 

Una sola pipeline 
- Un solo stage 
  - Un solo job 
    - Un task assemble (di tipo Script Executor)  
    - Un task test (di tipo Script Executor)  

Osservazioni: 
Il secondo task vede l'esito del primo (e.g., il file jar) e non ripete la compilazione. 

### Due job 

Una sola pipeline 
- Un solo stage 
  - Un job di assemble 
    - Un task assemble (di tipo Script Executor)  
  - Un job di test 
    - Un task test (di tipo Script Executor)  

Osservazioni: 
Non ha molto senso, perché i job di uno stage vengono eseguiti in parallelo. 
Infatti, il task di test ripete la compilazione. 

### Due stage 

Una sola pipeline 
- Un stage di assemble
  - Un solo job di assemble 
    - Un task assemble (di tipo Script Executor)  
- Un stage di test
  - Un job di test 
    - Un task test (di tipo Script Executor)  

Osservazioni: 
In questo caso ha poco senso, perché il task di test ripete la compilazione. 

### Una pipeline, due stage 

Una sola pipeline 
- Uno stage di assemble e test 
  - Un job di assemble 
    - Un task assemble (di tipo Script Executor)  
    - Il job pubblica l'artifact hello/build/libs/* in hello-libs 
  - Un job di test 
    - Un task test (di tipo Script Executor)  
- Un stage di API test
  - Un job di API test 
    - Un task di fetch - da hello-libs a hello-api-test/tmp 
    - Un task di API test (di tipo Script Executor)  

Questo è invece più sensato 

### Separazione di main e test unitari 

Con Spring Boot, il file jar per un progetto include i file compilati non come elementi di primo livello, 
ma all'interno della cartella BOOT-INF/classes. 

La conseguenza è che non posso eseguire test unitari avendo a disposizione solo il jar ma non il codice. 

Posso però provare ad avere test di integrazione separati. 

### Più pipeline 

Una pipeline di Java Build
- Dipende dal repo Git  
- Un stage di gradle build
  - Un job di build 
    - Un task assemble (di tipo Script Executor)  
    - Un task test (di tipo Script Executor) 
    - Il job pubblica l'artifact hello/build/libs/* in hello-libs 
Una pipeline di API Test 
- Dipende dal repo Git  
- Dipende dalla pipeline Java Build 
- Un stage di API test
  - Un job di API test 
    - Un task di fetch - da hello-libs a hello-api-test/tmp 
    - Un task di API test (di tipo Script Executor)  
	
### Uso di Docker 

Vedi https://stackoverflow.com/questions/60503946/run-a-docker-container-with-gocd-ci-cd 

Per eseguire docker nell'agent bisogna eseguire: 
- sudo usermod -a -G docker go (aggiunge l'utente go al gruppo docker, una volta per tutte) 
- oppure sudo chmod 666 /var/run/docker.sock (credo ad ogni esecuzione) 

Alle precedenti pipeline, ne aggiungiamo un'altra 

Una pipeline di Docker Build 
- Dipende dal repo Git  
- Dipende dalla pipeline Java Build 
- Dipende dalla pipeline di API Test 
- Un stage di Docker build
  - Un job di Docker build 
    - Un task di fetch - da hello-libs a hello-docker/tmp 
    - Un task di Docker build (di tipo Script Executor)  
    - Un task di API test (di tipo Script Executor) 
	  Questo task usa lo stesso test utilizzato in precedenza, 
	  ma con l'applicazione che viene eseguita in un container Docker

La possibilità di eseguire comandi Docker nell'agente suggerisce che sia possibile anche: 
- rilasciare immagini Docker in un repository esterno alla pipeline 
- eseguire container Docker in un host remoto rispetto all'agente 
- in modo analogo, rilasciare applicazioni Kubernetes complesse in un cluster remoto rispetto all'agente 
  (bisogna configurare l'agente per l'utilizzo di kubectl, ma non l'ho sperimentato) 

### Pipeline as a code 

Vedi https://docs.gocd.org/current/advanced_usage/pipelines_as_code.html 

Admin -> Config Repositories -> Add... 

Git Repository 
File pattern: pipelines/*.gocd.yaml 
Rules: Add: Allow All * 