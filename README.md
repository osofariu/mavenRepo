# Maven Concepts

-   There are three **build lifecycles**:
    -   default
    -   clean
    -   site
-   lifecycles are made up of **phases**
    -   each phase represents a **stage**
    -   each phase is responsible for a specific step in the build lifecycle
    -   you declare plugin goals, bound to build phases:
        -   a plugin goal represents a specific task, which contributes to building and managing
            the project.
        -   a goal may be bound to zero or more phases
        -   for example: dependency:copy-dependencies is a goal (of a plugin)
        -   if a goal is bound to one or more build phases, that goal will be called in all those phases.

## Useful references

-   [Maven Basics](http://alexander.holbreich.org/maven-concepts/)
-   [Maven Getting Started guide](http://maven.apache.org/guides/getting-started/index.html)
-   [Lifecycle Reference](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)
-   [Plugin Bindings for default lifecycle](http://maven.apache.org/ref/3.5.4/maven-core/default-bindings.html)
-   



# Pull a WAR from Maven:
mvn org.apache.maven.plugins:maven-dependency-plugin:2.4:get -Dartifact=com.aep.cxi:test-one:1.3-SNAPSHOT:war  -DremoteRepositories=http://localhost:8081/repository/aep-maven-snapshots/ -Ddest=lr-demo.war

- it gets the WAR from a specific artifact (-Dartifact) from nexus repo (remoteRepository), and puts it on a file called lr-demo.war
- settings.xml doesn't need credentials by default, since we are just reading an artifact.

# Settings
This setup assumes the following:
- a nexus server with id **local-nexus** for whom credentials are added to settings.xml (in ~/.m2), so we can push to it
- a tomcat server with id **local-tomcat** for whom credentials are added to settings.xml, so we can deploy to Tomcat using the tomcat7 plugin (defined in the pom.xml)

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ```html
  <localRepository/>
  <interactiveMode/>
  <usePluginRegistry/>
  <offline/>
  <pluginGroups/>
  <servers>
    <server>
      <id>local-nexus</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
    <server>
      <id>local-tomcat</id>
      <username>deployment</username>
      <password>deployment</password>
    </server>
  </servers>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>
```