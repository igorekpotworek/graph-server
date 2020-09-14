# graph-server

TCP server implementation with simple string protocol.
Server accepts string commands which allows adding and removing nodes and edges to graph. 
Additionally, it allows also finding the shortest path in graph and finding nodes closer than given radius from specified node.  
Those two features are implemented using Dijkstra algorithm.

## Build the project
```
gradlew clean build
```
## Run container
```
java -jar graph-server-1.0-SNAPSHOT.jar
```
