### Create the parent project
mvn archetype:generate -DgroupId=com.roy4j -DartifactId=mutual-auth
Change parent pom packaging: <packaging>pom</packaging>

### Create the server project
cd mutual-auth
mvn archetype:generate -DgroupId=com.roy4j -DartifactId=server

### Create the client project
cd mutual-auth
mvn archetype:generate -DgroupId=com.roy4j -DartifactId=client
