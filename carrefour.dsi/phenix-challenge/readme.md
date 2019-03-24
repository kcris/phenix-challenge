# Phenix Challenge


## Introduction



## TODOs

- OPTIMIZE - intermediary files?
- TESTS - full test data
- DOC, CLEANUP

## Running the application

```bash
cd carrefour.dsi

#re-build application via maven if needed
mvn clean package
cp phenix-challenge/target/phenix-challenge-0.0.1-SNAPSHOT.jar phenix-challenge/lib/

#run the application
java -Xmx512M -cp "phenix-challenge/lib/*" fr.carrefour.phenix.challenge.Application

#inputs taken from folder: phenix-challenge/data/input
#outputs sent to folder: phenix-challenge/data/output/<date>
```
