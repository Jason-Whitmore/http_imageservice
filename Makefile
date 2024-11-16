build:
	mvn clean compile jar:jar -DoutputDirectory=${project.build.directory}
	mvn clean compile jar:jar -DoutputDirectory=${project}
