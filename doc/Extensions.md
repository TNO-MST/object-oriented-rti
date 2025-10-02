# RTI interface extensions
The interface extensions are described in detail below. The extensions mimic the corresponding RTIambassador or FederateAmbassador methods, and for further information the reader is referred to the HLA documentation on those methods.

## OORTIambassador

### Connect to RTI

Two additional methods to connect to the RTI are defined. Both take an `OOFederateAmbassador` as federate reference.

```
public void connect(
    OOFederateAmbassador federateReference,
    CallbackModel callbackModel,
    String localSettingsDesignator)

public void connect(
    OOFederateAmbassador federateReference,
    CallbackModel callbackModel)
```

### Join federation

One join method is added to provide the current FDD (FOM Document Data). That is, a method to provide the set of FOM modules used in the federation, but not provided by the joining federate. Typically this is the set of FOM modules (including the MIM) provided in the create federation method.

The set of current FOM modules is combined with the additional FOM modules so that the OORTI can marshall all objects and interactions that are potentially exchanged by the joining federate.

````
public FederateHandle joinFederationExecution(
    String federateName,
    String federateType,
    String federationExecutionName,
    URL[] additionalFomModules,
    URL[] currentFddModules)
````

If only the standard MIM is used in the create federation service call, and the additional FOM modules in the join federation service call make up the current FDD, then any of the other `joinFederationExecution` service calls can be used as is. In this case the current FDD can be derived from the FOM modules provided in the join federation service call.

Note that the OORTI has a construction property to configure the OORTI to obtain the current FDD from the RTI. This only works if the (HLA Evolved) RTI provides an HLA Evolved format FDD. More recent RTIs may only provide an HLA-4 format FDD.

### Publish or subscribe object class attributes

Several methods are added to declare interest in HLA-FOM Object Class attributes. The Java Class in these methods represents an HLA Object Class.

```
public Set<OOattribute> publishObjectClass(
    Class clazz)

public Set<OOattribute> publishObjectClass(
    Class clazz, Set<? extends Object> cookies)

public Set<OOattribute> subscribeObjectClass(
    Class clazz)

public Set<OOattribute> subscribeObjectClass(
    Class clazz,
    Set<? extends Object> cookies)

public void unpublishObjectClass(
    Class clazz)

public void unsubscribeObjectClass(
    Class clazz)
```

If only a Java Class argument is provided then all Java Class properties are published or subscribed to, including inherited Java Class properties. The publish and subscribe methods return a set of `OOattribute` values to identify each property that is published or subscribed to.

Optionally a set of (caller defined) `cookies` can be provided to identify what properties need to be published or need to be subscribed to. The OORTI uses the `toString` method of the cookie object to determine the property name.

The returned set of `OOattribute` values is used in subsequent OORTI methods, such as the `reflectAttributeValues` callback to indicate the properties that are reflected / updated. A previously provided cookie can be retrieved from the `OOattribute`.

### Publish or subscribe interactions class

Several methods are added to declare interest in HLA-FOM Interaction Classes. The Java Class in these methods represents an HLA Interaction Class.

Similar to the methods related to Object Class publication and subscription, an optional set of cookies can be provided for the Interaction Class parameters.

```
public Set<OOparameter> publishInteractionClass(
    Class clazz)

public Set<OOparameter> publishInteractionClass(
    Class clazz,
    Set<? extends Object> cookies)

public Set<OOparameter> subscribeInteractionClass(
    Class clazz)

public Set<OOparameter> subscribeInteractionClass(
    Class clazz,
    Set<? extends Object> cookies)

public void unpublishInteractionClass(
    Class clazz)

public void unsubscribeInteractionClass(
    Class clazz)
```

### Register object instance

One method is added to register a Java object as an HLA Object Class instance. The related Java Class must have been published as an HLA Object Class.

```
public void registerObjectInstance(
    Object theObject)
```

### Update attribute values

Several methods are added to update Java object property values in the federation execution. The related Java Class must have been published as an HLA Object Class.

Optionally a set of `OOattribute` objects can be provided to indicate which properties are to be updated as attribute value. If no set is provided then all properties are to be updated.

```
public void updateAttributeValues(
    Object theObject,
    byte[] userSuppliedTag)

public void updateAttributeValues(
    Object theObject,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag)

public MessageRetractionReturn updateAttributeValues(
    Object theObject,
    byte[] userSuppliedTag,
    LogicalTime theTime)

public MessageRetractionReturn updateAttributeValues(
    Object theObject,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag,
    LogicalTime theTime)
```

Two additional methods are added to update Java object property values in the federation execution, given another Java object (but of the same class) than originally registered. The HLA object intance to be updated is identified by the provided object name.

````
public void updateAttributeValues(
    String theObjectName,
    Object theObject,
    byte[] userSuppliedTag)

public MessageRetractionReturn updateAttributeValues(
    String theObjectName,
    Object theObject,
    byte[] userSuppliedTag,
    LogicalTime theTime)
````

### Delete object instance

Several methods are added to delete a Java object from the federation. The related Java Class must have been published as an HLA Object Class.

```
public void deleteObjectInstance(
    Object theObject,
    byte[] userSuppliedTag)
	
public void deleteObjectInstance(
    String theObjectName,
    byte[] userSuppliedTag)

public MessageRetractionReturn deleteObjectInstance(
    Object theObject,
    byte[] userSuppliedTag,
    LogicalTime theTime)
	
public MessageRetractionReturn deleteObjectInstance(
    String theObjectName,
    byte[] userSuppliedTag,
    LogicalTime theTime)
```

### Request attribute value update

Several methods are added to request the update of Java object properties in the federation execution. The related Java Class must have been subscribed to as an HLA Object Class.

Optionally a set of `OOattribute` objects can be provided to indicate which properties are to be updated. If no set is provided then all properties are to be updated.

```
public void requestAttributeValueUpdate(
    Class clazz,
    byte[] userSuppliedTag)
	
public void requestAttributeValueUpdate(
    Class clazz,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag)

public void requestAttributeValueUpdate(
    Object theObject,
    byte[] userSuppliedTag)

public void requestAttributeValueUpdate(
    Object theObject,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag)
```

### Send interaction

Several methods are added to send Java objects in the federation execution as an HLA interaction. The related Java Class must have been published as an HLA Interaction Class.

Optionally a set `OOparameter` objects can be provided to indicate which properties are to be sent as interaction parameter. If no set is provided then all properties are to be sent.

```
public void sendInteraction(
    Object theInteraction,
    byte[] userSuppliedTag)

public void sendInteraction(
    Object theInteraction,
    Set<OOparameter> theParameters,
    byte[] userSuppliedTag);

public MessageRetractionReturn sendInteraction(
    Object theInteraction,
    byte[] userSuppliedTag,
    LogicalTime theTime)

public MessageRetractionReturn sendInteraction(
    Object theInteraction,
    Set<OOparameter> theParameters,
    byte[] userSuppliedTag,
    LogicalTime theTime)
```

### Support methods

The following support methods are added.

```
public String getObjectName(Object theObject)

public Object getObject(String theObjectName)
```

These methods provide a translation between a Java object (as an HLA object instance) and its name.

## OOFederateAmbassador

### Discover object instance

Two callback methods are added to notify the federate on the discovery of an HLA Object Class instance. The related Java Class must have been subscribed to as an HLA Object Class.

```
public void discoverObjectInstance(
    Object theObject,
    String theObjectName,
    FederateHandle producingFederate)

public void discoverObjectInstance(
    Object theObject,
    String theObjectName)
```

### Reflect attribute values

Several callback methods are added to notify the federate on an update of Java Class property values in the federation execution. The related Java Class must have been subscribed to as an HLA Object Class.

```
public void reflectAttributeValues(
    Object theObject,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    TransportationTypeHandle theTransport,
    LogicalTime theTime,
    OrderType receivedOrdering,
    MessageRetractionHandle retractionHandle,
    SupplementalReflectInfo reflectInfo)

public void reflectAttributeValues(
    Object theObject,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    TransportationTypeHandle theTransport,
    LogicalTime theTime,
    OrderType receivedOrdering,
    SupplementalReflectInfo reflectInfo)

public void reflectAttributeValues(
    Object theObject,
    Set<OOattribute> theAttributes,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    TransportationTypeHandle theTransport,
    SupplementalReflectInfo reflectInfo)
```

### Remove object instance

Several callback methods are added to notify the federate on the removal of an HLA Object Class instance. The related Java Class must have been subscribed to as an HLA Object Class.

```
public void removeObjectInstance(
    Object theObject,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    LogicalTime theTime,
    OrderType receivedOrdering,
    MessageRetractionHandle retractionHandle,
    SupplementalRemoveInfo removeInfo)

public void removeObjectInstance(
    Object theObject,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    LogicalTime theTime,
    OrderType receivedOrdering,
    SupplementalRemoveInfo removeInfo)

public void removeObjectInstance(
    Object theObject,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    SupplementalRemoveInfo removeInfo)
```

### Receive interaction

Several callback methods are added to notify the federate on the receipt of Java objects that represent HLA interactions. The related Java Class must have been subscribed to as an HLA Interaction Class.

```
public void receiveInteraction(
    Object theInteraction,
    Set<OOparameter> theParameters,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    TransportationTypeHandle theTransport,
    LogicalTime theTime,
    OrderType receivedOrdering,
    MessageRetractionHandle retractionHandle,
    SupplementalReceiveInfo receiveInfo)

public void receiveInteraction(
    Object theInteraction,
    Set<OOparameter> theParameters,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    TransportationTypeHandle theTransport,
    LogicalTime theTime,
    OrderType receivedOrdering,
    SupplementalReceiveInfo receiveInfo)

public void receiveInteraction(
    Object theInteraction,
    Set<OOparameter> theParameters,
    byte[] userSuppliedTag,
    OrderType sentOrdering,
    TransportationTypeHandle theTransport,
    SupplementalReceiveInfo receiveInfo)
```

## OOattribute and OOparameter

An HLA attribute is identified by an object the implements the `OOattribute` interface. Via the interface the name of the attribute and the previously provided cookie (if any) can be requested. The cookie is a caller provided object at object class publication/subscription. A cookie can be as simple as an integer value, or a more complex object with several methods. The `OOattribute` interface is:

```
public interface OOattribute {

    public String getName();

    public Object getCookie();
}
```

Similarly an HLA parameter is identified by an object the implements the `OOparameter` interface:

```
public interface OOparameter {

    public String getName();

    public Object getCookie();
}
```
