build:
	mvn -f pom_client.xml compile assembly:single
	mvn -f pom_server.xml compile assembly:single
clean:
	mvn clean