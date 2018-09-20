# Pull a WAR from Maven:
mvn org.apache.maven.plugins:maven-dependency-plugin:2.4:get -Dartifact=com.aep.cxi:test-one:1.3-SNAPSHOT:war  -DremoteRepositories=http://localhost:8081/repository/aep-maven-snapshots/ -Ddest=lr-demo.war

