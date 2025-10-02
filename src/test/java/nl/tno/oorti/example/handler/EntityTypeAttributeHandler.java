package nl.tno.oorti.example.handler;

import nl.tno.oorti.test.netn4.objects.PhysicalEntity;

/**
 *
 * @author bergtwvd
 */
class EntityTypeAttributeHandler implements AttributeHandler {

	@Override
	public void handle(Object theObject) {
		PhysicalEntity pe = (PhysicalEntity) theObject;
		System.out.println("Received entity type value " + pe.getEntityType().getDomain() + " for object instance " + pe.toString());
	}

	@Override
	public String toString() {
		return "EntityType";
	}

}
