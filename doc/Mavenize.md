# Create Maven RTI artifacts for the Pitch and MaK RTI
The Pitch and MaK RTI artifacts are vendor-supplied proprietary components. They should not be redistributed through public Maven repositories unless explicit permission has been obtained from the vendor. If these artifacts are stored in an internal Maven repository, this must remain consistent with the applicable software license agreements and limited to authorized users.

This section describes how to make the proprietary RTI Java libraries from Pitch and MaK available through a Maven repository, so they can be used as normal Maven dependencies in Java projects.

## Pitch RTI
This section shows how to extract the required JARs from a local pRTI 6 installation, publish them to a Maven repository, and include the correct combination of artifacts in a Java project depending on the HLA standard being used.

Reference: https://onearc.com

### Install Pitch pRTI 6 on Windows.
Install `pRTI 6` on Windows. The default installation folder is `C:\Program Files\Pitch pRTI 6`.

### Collect files
Collect the relevant JAR files from the installation directory, including:
- core RTI libraries such as prticore, booster1516, prti1516e, and prti1516-hla4
- optional FedPro-related libraries such as fedpro-client-evolved, fedpro-client-hla4, fedpro-session, and protobuf

The JAR files to deploy are the following from the installation folder (reflecting the status as of version 6.1.3):

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

### Deploy files as artifacts
Deploy each JAR manually to a Maven repository using mvn deploy:deploy-file

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

### Configure repository credentials
Configure repository credentials in ~/.m2/settings.xml as follows, so Maven can get the RTI artifacts.

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

### Use RTI artifacts
Reference the RTI artifacts in the project POM as normal Maven dependencies. For example:

````
<dependency>
  <groupId>se.pitch</groupId>
  <artifactId>prticore</artifactId>
  <version>6.1.3</version>
  <scope>runtime</scope>
</dependency>
````

Use the correct dependency set depending on the HLA version:
- for IEEE 1516-2010 / HLA Evolved: prti1516e, booster1516, prticore
- for IEEE 1516-2025 / HLA 4: prti1516-hla4, booster1516, prticore

# MaK RTI
This section explains how to package the MaK RTI Java wrapper as a Maven artifact and use it in a Java project, while noting that the required native MaK RTI libraries still need to be installed separately on the host system.

Reference: https://www.mak.com

### Install MaK RTI on Windows.
Install the MaK RTI on Windows. The default installation folder is `C:\MAK\makRti<version>`.

### Collect files
The JAR files to deploy are the following from the installation folder (reflecting the status as of version 4.6c):

| path-to-file  | group-id | artifact-id | comments   |
| --------      | -------- | ----------- | ---------- |
| lib/hla.jar   | com.mak  | hla         | Java wrapper for compiled C++ libraries |

### Deploy files as artifacts
See the previous section.

### Configure repository credentials
See the previous section.

### Use RTI artifacts
The MaK RTI artifact can be included in your project's POM as the following dependency:

````
<dependency>
  <groupId>com.mak</groupId>
  <artifactId>hla</artifactId>
  <version>4.6c</version>
  <scope>runtime</scope>
</dependency>
````

And most important, the MaK RTI artifact is not sufficient by itself, because the actual MaK RTI implementation is in native C++ libraries that must also be installed on the host machine. The MaK RTI artifact is only a Java wrapper for these libraries. For further information on how to do this, refer to the MaK RTI user documentation.
