# Java Bean construction rules
For the constructing of the Java Beans there are certain rules. In order for the **OORTI** to perform data-marshalling, a Java class must follow these construction rules:

- The Java class hierarchy must follow the class-hierarchy in the HLA-FOM, using Java class extensions for FOM sub-classes.
- The Java class name shall match with the HLA-FOM class name.
- The Java class property name shall match with the HLA-FOM object class attribute name or HLA-FOM interaction class parameter name.
- The Java class property datatype shall match with the HLA-FOM datatype name and structure. Java datatypes are used for HLA-FOM simple datatypes and for some of the non-simple HLA-FOM datatypes (HLABoolean, HLAunicodeString, and HLAASCIIstring).
- The Java class shall define public getters and setters for access to Java class properties.

The Java Beans can be created by hand, or can be created automatically by a `Bean Generator` (see Bean Generator project).
