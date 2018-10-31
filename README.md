# node

If you want to run this project locally:
 - Clone this project
 - Compile with `mvn clean install`
 - Download the [Tomcat server](https://tomcat.apache.org/download-90.cgi)
 - Copy the file `app.war` (it's inside the target directory) to ~/path/to/apache/webapps
 - Start the Tomcat server running the startup.sh (inside the apache bin directory)
 - You can also change the `<packaging>war</packaging>` to `<packaging>jar</packaging>` in the pom and run the fat jar inside target.

# URLs

 - POST /app/node: Creates a new node
 - GET /app/node: return all nodes with their children
 - GET /app/node/parentId: returns all children from the specified node
 - PUT /app/node: updates some node
