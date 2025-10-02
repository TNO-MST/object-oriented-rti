package nl.tno.oorti.example.handler;

import nl.tno.oorti.test.netn4.objects.PhysicalEntity;

/**
 *
 * @author bergtwvd
 */
class MarkingAttributeHandler implements AttributeHandler {

	@Override
	public void handle(Object theObject) {
		PhysicalEntity pe = (PhysicalEntity) theObject;
		System.out.println("Received marking value " + new String(pe.getMarking().getMarkingData()) + " for object instance " + pe.toString());
	}

	@Override
	public String toString() {
		return "Marking";
	}

}
