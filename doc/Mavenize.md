# Create Maven artifacts for LRC JARs
This section shows how to create Maven artifacts from the Local RTI Component (LRC) JAR files.

## Pitch RTI 6

### Deploy files
Install `pRTI 6` on Windows. The default installation folder is `C:\Program Files\Pitch pRTI 6`.

The JAR files to deploy to the Maven repository are the following from the installation folder (reflecting the status as of version 6.1.3):

| path-to-file                            | group-id | artifact-id | comments   |
| --------                                | -------- | ---------- | ---------- |
| lib/booster1516.jar                     | se.pitch | booster1516 | |
| lib/prti1516e.jar                       | se.pitch | prti1516e |IEEE 1516-2010 JAVA LRC|
| lib/prti1516-hla4.jar                   | se.pitch | prti1516-hla4 |IEEE 1516-2025 JAVA LRC|
| lib/prticore.jar                        | se.pitch | prticore ||
| fedpro/lib/fedpro-client-evolved.jar    | se.pitch | fedpro-client-evolved |IEEE 1516-2010 JAVA FedPro LRC|
| fedpro/lib/fedpro-client-hla4.jar       | se.pitch | fedpro-client-hla4 |IEEE 1516-2025 JAVA FedPro LRC|
| fedpro/lib/fedpro-session.jar           | se.pitch | fedpro-session ||
| fedpro/lib/protobuf.jar                 | se.pitch | fedpro-protobuf ||


Use the following Maven commmand to deploy each JAR file to the Maven repository:

````
mvn deploy:deploy-file -DgroupId=<group-id> \
  -DartifactId=<artifact-id> \
  -Dversion=<version> \
  -Dpackaging=<type-of-packaging> \
  -Dfile=<path-to-file> \
  -DrepositoryId=<id-to-map-on-server-section-of-settings.xml> \
  -Durl=<url-of-the-repository-to-deploy>
````

Note that this can be automated by creating a script or by creating your own Maven POM file to do the deployment for you.

For example:

````
mvn deploy:deploy-file \
  -Dfile=lib/prticore.jar \
  -DgroupId=se.pitch \
  -DartifactId=prticore \
  -Dversion=6.1.3 \
  -Dpackaging=jar \
  -DrepositoryId=my-repo \
  -Durl=https://repo.example.com/repository/maven-releases/
````

Note that the `repositoryId` must match the credentials that are configured in your `~/.m2/settings.xml`:

````
<settings>
  <servers>
    <server>
      <id>my-repo</id>
      <username>user</username>
      <password>password</password>
    </server>
  </servers>
</settings>
````

### Use Maven artifacts
The Pitch JAR files can be included in your project's POM, such as the following dependency.

````
<dependency>
  <groupId>se.pitch</groupId>
  <artifactId>prticore</artifactId>
  <version>6.1.3</version>
  <scope>runtime</scope>
</dependency>
````

For `IEEE 1516-2010 JAVA LRC` (HLA Evolved) you need the following artifacts as dependencies in your POM: `prti1516e`, `booster1516`, and `prticore`.
And for `IEEE 1516-2025 JAVA LRC` (HLA 4) you need: `prti1516-hla4`, `booster1516`, and `prticore`.

# MaK RTI

### Deploy files
Install the MaK RTI on Windows. The default installation folder is `C:\MAK\makRti<version>`.

The JAR files to deploy are the following from the installation folder (reflecting the status as of version 4.6c):

| path-to-file  | group-id | artifact-id | comments   |
| --------      | -------- | ----------- | ---------- |
| lib/hla.jar   | com.mak  | hla         | Java wrapper for compiled C++ libraries |

For the Maven deploy command see the previous section.

### Use Maven artifacts

The MaK JAR file can be included in your project's POM as the following dependency:

````
<dependency>
  <groupId>com.mak</groupId>
  <artifactId>hla</artifactId>
  <version>4.6c</version>
  <scope>runtime</scope>
</dependency>
````

Since the MaK RTI (LRC) is implemented in C++ and is linked as a set of (Windows or Linux) library files with your application, you need to have these libraries installed on your host machine. The HLA JAR is only a Java wrapper for these library files. For further information on how to do this, refer to the MaK RTI user documentation.
