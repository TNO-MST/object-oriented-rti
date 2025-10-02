# Object-oriented RTI

The **Object Oriented RTI** (OORTI) extends the HLA Run Time Infrastructure (RTI) interface with methods to work with **Java Beans**. Java Beans are automatically encoded or decoded on behalf of the federate application. This approach simplifies data marshalling in the federate application. And in addition, since the HLA-RTI interface is extended with additional methods, the current RTI interface methods remain available as well.

## Build the OORTI library
To build the OORTI library use the following Maven command:

````
mvn clean install
````

To generate Java documentation:

````
mvn javadoc:javadoc
````

## Include the OORTI library in your application
The suggested method to include the OORTI library uses Maven. Add the following dependency to your Maven POM:

````
<dependency>
	<groupId>nl.tno</groupId>
	<artifactId>oorti</artifactId>
	<version>1.0.0</version>
	<type>jar</type>
</dependency>
````

Version 1.0.0 is used as example. Generally, the latest version should be used.

## Use the OORTI
The use of the OORTI library in an application requires an HLA RTI implementation from an RTI provider. Providers include [Pitch](https://pitchtechnologies.com), [VTMaK](https://www.mak.com), and  [Portico](https://github.com/openlvc/portico). The RTI libraries from a provider need to be included with your application in order to use the OORTI library.

## Example: Create an OORTI ambassador
The following code snippet provides an example on how to get an OORTIambassador object in your application.

````
public OORTIambassador getRtiAmbassador(String rtiName) throws RTIinternalError {

	// rtiName is either null for first on classpath,
	// or one off: ["pRTI 1516", "MAK RTI", "portico"]

	RTIambassador rtiamb = rtiName == null
		? RtiFactoryFactory.getRtiFactory().getRtiAmbassador()
		: RtiFactoryFactory.getRtiFactory(rtiName).getRtiAmbassador();

	return new OORTIfactory().getRtiAmbassador(rtiamb);
}
````

Once you have an OORTIambassador object you can connect to the RTI and join a federation execution, similar to using the regular RTIambassador.

A complete example is provided in the documentation section.

## Documentation
- [Overview](doc/Overview.md)
- [Bean construction rules](doc/Beans.md)
- [Example](doc/Example.md)
- [OORTI Factory](doc/OORTIfactory.md)
- [RTI interface extensions](doc/Extensions.md)
- [Caveats](doc/Caveats.md)
