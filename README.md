# node

If you want to run this project locally:
 - Clone this project
 - Compile with `mvn clean install`
 - Download the [Tomcat server](https://tomcat.apache.org/download-90.cgi)
 - Create a mysql database called `node` with user and password `root`.
   - Why am I exposing the user and password? Because this database will be created locally and all data will be also stored locally. If it were a remote dabase with sensitive data I'd surely keep the credentials in some env var.
 - `java -jar tar/app.jar`

# URLs

 - POST /node: Creates a new node
 - GET /node: return all nodes with their children
 - GET /node/parentId: returns all children from the specified node
 - PUT /node: updates some node

# Want to use it at Google cloud?
 - [It's right here](https://github.com/cunhazera/docker-compose-tree)
