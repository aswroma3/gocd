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

