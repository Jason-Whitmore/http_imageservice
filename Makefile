build_client:
	mvn -f pom.xml clean compile assembly:single
clean:
	mvn clean
run_server:
	mvn -f pom_server.xml clean compile spring-boot:run
test:
	mvn -f pom.xml test
	mvn -f pom_server.xml test