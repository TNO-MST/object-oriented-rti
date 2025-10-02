# OORTI factory
The `OORTIfactory` is used to create an `OORTIambassador`. To create an `OORTIambassador` with default properties, use:

```
OORTIambassador rtiamb = new OORTIfactory().getRtiAmbassador();
```

 The `OORTIfactory` can be constructed with an `OOproperties` object, containing the following properties that are applied to each `OORTIambassador` created by the factory:
 
| Property | Description | Default |
| - | - | - |
| `encodingType` | Use this encoding type to encode/decode Java Bean properties. | RPR |
| `accessorType`  | Use this accessor type to access Java Bean properties. | LAMBDA |
| `useInPlaceCopy` | If true, use in-place copy for constructed datatypes and array datatypes (when size is the same). If false, create a Java Class instance for all data received. In-place copy provides better performance, but the receiving federate must be aware that data can be overwritten by callback methods. | false |
| `useRtiForCurrentFdd` | If true, request the current FDD from the RTIambassador. If false, use the the provided FOM modules in the join federation method to determine the current FDD. | false |

In addition, the `OORTIfactory` supports the following arguments in the creation of each individual `OORTIambassador`:

| Argument | Description | Default |
| - | - | - |
| `rtiamb` | The RTI ambassador to be used. | The RTI ambassador created by the first available RTI factory on the Java class path. |
| `objectFactory` | Use this factory to creats a Java object representing an HLA object class instance or an HLA interaction. | Default object factory for creating Java Beans: `DefaultOOobjectFactory`. |

# Object factory
The `OORTI` makes use of an `ObjectFactory` to:
- create Java objects of a requested Java type (that matches with an HLA Object/Interaction Class).
- request the Java type of a Java object previously created by the factory.

The `ObjectFactory` is a federate provided factory and the types of objects created is entirely under the control of the federate. For instance, a federate may sub-class a Java Bean class, and instead can create Java objects of that sub-class.

A default `ObjectFactory` is available.
