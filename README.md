# node

If you want to run this project locally:
 - Clone this project
 - Compile with `mvn clean install`
 - Download the [Tomcat server](https://tomcat.apache.org/download-90.cgi)
 - Create a mysql database called `node` with user and password `root`.
   - Why am I exposing the user and password? Because this database will be created locally and all data will be also stored locally. If it were a remote dabase with sensitive data I'd surely keep the credentials in some env var.
 - Copy the file `app.war` (it's inside the target directory) to ~/path/to/apache/webapps
   - You need to be aware of this: if you run the jar the URLs listed below are not correct. Running the jar you must remove the /app from the URL.
 - Start the Tomcat server running the startup.sh (inside the apache bin directory)
 - You can also change the `<packaging>war</packaging>` to `<packaging>jar</packaging>` in the pom and run the fat jar inside target.

# URLs

 - POST /app/node: Creates a new node
 - GET /app/node: return all nodes with their children
 - GET /app/node/parentId: returns all children from the specified node
 - PUT /app/node: updates some node
