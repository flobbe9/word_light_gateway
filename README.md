# word_light_gateway
Gateway handling all routing for the Word light api. Uses Java 17 and Spring Cloud.

# Run 
### Locally
```docker-compose -f docker-compose.local.yml up``` <br>
Call this inside project root folder of repository inside dev or stage branch. <br>

### Dockerhub
```docker-compose up``` <br>
Call this with .env file in same folder as docker-compose.yml. <br>

### The whole thing
```docker-compose -f docker-compose.all.yml up``` <br>
This file can be found at https://github.com/flobbe9/word_light_document_builder <br>
Call this with .env file in same folder as docker-compose.all.yml file. <br>
Will start the whole microservice including frontend etc.. No further configuration needed.

# More documentation
Run document_builder api (https://github.com/flobbe9/word_light_document_builder), then visit http://localhost:4001