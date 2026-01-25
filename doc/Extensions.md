# RTI interface extensions
The interface extensions are summarised below. The extensions mimic the corresponding RTIambassador or FederateAmbassador methods, and for further information the reader is referred to the HLA documentation on those methods.

## OORTIambassador

### Connect to RTI

Two additional methods to connect to the RTI are defined. Both take an `OOFederateAmbassador` as federate reference.

### Join federation

A number of `joinFederationExecutionWithCurrentFDD` methods are added to provide the current FDD (FOM Document Data). That is, a method to provide the set of FOM modules used in the federation, but not provided by the joining federate. Typically this is the set of FOM modules (including the MIM) provided in the create federation method.

The set of current FOM modules is combined with the additional FOM modules so that the OORTI can marshall all objects and interactions that are potentially exchanged by the joining federate. If only the standard MIM is used in the create federation service call, and the additional FOM modules in the join federation service call make up the current FDD, then any of the `joinFederationExecution` methods can be used. In this case the current FDD can be derived from the FOM modules provided in the join federation method.

Note that the OORTI has a construction property to configure the OORTI to obtain the current FDD from the RTI. This only works if the (HLA Evolved) RTI provides an HLA Evolved format FDD. More recent RTIs may only provide an HLA 4 format FDD.

### Publish or subscribe object class attributes

Several methods are added to declare interest in HLA FOM Object Class attributes. The Java Class in these methods represents an HLA Object Class.

If only a Java Class argument is provided then all Java Class properties are published or subscribed to, including inherited Java Class properties.

### Publish or subscribe interactions class

Several methods are added to declare interest in HLA FOM Interaction Classes. The Java Class in these methods represents an HLA Interaction Class.

### Register object instance

One method is added to register a Java object as an HLA Object Class instance. The associated Java Class must have been published as an HLA Object Class.

### Update attribute values

Several methods are added to update Java object property values in the federation execution. The associated Java Class must have been published as an HLA Object Class.
Optionally a set of `OOattribute` objects can be provided to indicate which properties are to be updated as attribute value. If no set is provided then all published properties are to be updated.

Also, two additional methods are added to update Java object property values in the federation execution, given another Java object (but of the same class) than originally registered. The HLA object intance to be updated is identified by the provided object name.

### Delete object instance

Several methods are added to delete a Java object from the federation. The associated Java Class must have been published as an HLA Object Class.

### Request attribute value update

Several methods are added to request the update of Java object properties in the federation execution. The associated Java Class must have been subscribed to as an HLA Object Class.

Optionally a set of `OOattribute` objects can be provided to indicate which properties are to be requested. If no set is provided then all properties are to be `requested.

### Send interaction

Several methods are added to send Java objects in the federation execution as an HLA interaction. The associated Java Class must have been published as an HLA Interaction Class.

Optionally a set `OOparameter` objects can be provided to indicate which properties are to be sent as interaction parameter. If no set is provided then all published properties are to be sent.

### Support methods

Several support methods are added.

## OOFederateAmbassador

### Discover object instance

Callback methods are added to notify the federate on the discovery of an HLA Object Class instance. The related Java Class must have been subscribed to as an HLA Object Class.

### Reflect attribute values

Callback methods are added to notify the federate on an update of Java Class property values in the federation execution. The related Java Class must have been subscribed to as an HLA Object Class.

### Remove object instance

Callback methods are added to notify the federate on the removal of an HLA Object Class instance. The related Java Class must have been subscribed to as an HLA Object Class.

### Receive interaction

Callback methods are added to notify the federate on the receipt of a Java object that represents an HLA interaction. The related Java Class must have been subscribed to as an HLA Interaction Class.

## OOattribute and OOparameter

An HLA attribute is identified by an object the implements the `OOattribute` interface.

Similarly an HLA parameter is identified by an object the implements the `OOparameter` interface.

Via the interface the name of the attribute and a previously provided cookie (if any) can be set or retrieved. The cookie is a caller provided object that can be associated with an attribute or parameter.
