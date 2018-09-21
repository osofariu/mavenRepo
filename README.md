# About this repo
This repo is meant as a hello/world refresher for starting a java project with maven, nexus, and tomcat. It focuses on how to use Maven to integrate with Nexus and Tomcat.  This is not a real project so it doesn't focus on best practices.

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

## Useful Maven references

-   [Maven Basics](http://alexander.holbreich.org/maven-concepts/)
-   [Maven Getting Started guide](http://maven.apache.org/guides/getting-started/index.html)
-   [Lifecycle Reference](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)
-   [Plugin Bindings for default lifecycle](http://maven.apache.org/ref/3.5.4/maven-core/default-bindings.html)
-   

# Getting Nexus docker set-up

- [Nexus docker](https://hub.docker.com/r/sonatype/nexus3/)

```bash
$ docker pull sonatype/nexus3
$ docker run -d -p 8081:8081 --name nexus sonatype/nexus3
```

- Default credentials are: admin / admin123, so we'll use those.
- For a real app you need to mount some volumes, for example: `-v nexus-data:/nexus-data` so that you persist the artifacts if the container gets destroyed.


## deploy your project to the Nexus repo:

```bash
$ mvn tomcat7:deploy
```
This should push a snapshot to Nexus

# How to pull a WAR from Maven:
Here's how you can download a specific artifact from Nexus (assuming it already exists) and save it to a local file. This is can be useful for deployment through a CI process.

Once you pushed your artifact to Nexus you'll have installed a new package to Nexus, with a version such as: `1.3-20180921.174029-4`.  Now you can pull the WAR file for that package by itself:

```bash
mvn org.apache.maven.plugins:maven-dependency-plugin:2.4:get -Dartifact=com.aep.cxi:test-one:1.3-SNAPSHOT:war  -DremoteRepositories=http://localhost:8081/repository/aep-maven-snapshots/ -Ddest=test-one-latest.war
```

- this plugin gets the WAR from a specific artifact (`-Dartifact`) from nexus repo (`-DremoteRepository`), and puts it on a file (`-Ddest`)
- `settings.xml` doesn't need credentials by default, since we are just reading an artifact.

# Getting tomcat docker set-up

- [Tomcat docker](https://hub.docker.com/_/tomcat/)

```bash
$ docker pull tomcat
$ docker run --name tomcat -it -d -p 8888:8080 tomcat:8.0

$ docker exec -it tomcat bash  # log-in to the container to create user(s); none created by default
# within the docker container, you'll be logged in as root:
$ apt update
$ apt install vim # no vim installed by default
$ vim conf/tomcat_users.xml
```

To be able to deploy, you need to add that user to certain roles, in `conf/tomcat-users.xml`.  You should also add another user, such as tomcat (below) so you can log on to the Web management console.

Add these lines to `tomcat_users.xml`:

```xml
<user username="deployment" password="deployment" roles="standard,manager-script"/>
<user username="tomcat" password="tomcat" roles="tomcat,manager-gui,admin-gui,admin-script"/>
```

```bash
# restart the container, so it picks up your changes:
$ docker restart tomcat
```

## Deploy the project to Tomcat:

```bash
$ mvn tomcat7:deploy
```

- I couldn't find a tomcat8 maven plugin, but people say that the tomcat7 maven plugin works fine against tomcat 8.

Now you can hit the app on tomcat at: http://localhost:8888/test-one/.  You should see the "Hello there" page.

# Settings
This setup assumes the following:
- a nexus server with id **local-nexus** for whom credentials are added to `settings.xml` (in ~/.m2/ directory), so we can push to it
- a tomcat server with id **local-tomcat** for whom credentials are added to `settings.xml`, so we can deploy to Tomcat using the tomcat7 plugin (defined in the pom.xml).  The default nexus credentials already match what comes with the docker image.  The tomcat credentials were added to tomcat_users.xml.


Here's a sample `settings.xml` file to support this project:


  ```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
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