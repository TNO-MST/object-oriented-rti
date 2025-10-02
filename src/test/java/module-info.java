open module nl.tno.oorti {
	// in-module junit tests
	// the following requires are copied from the source package
	requires transitive hla.rti1516e;
	requires nl.tno.omt;
	requires java.logging;
	requires jakarta.json.bind;

	// these are needed for junit tests
	requires transitive org.junit.jupiter.api;
}
