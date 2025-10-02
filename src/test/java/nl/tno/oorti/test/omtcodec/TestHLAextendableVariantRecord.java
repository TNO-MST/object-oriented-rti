package nl.tno.oorti.test.omtcodec;

import java.io.IOException;
import nl.tno.oorti.test.netn4.datatypes.EntityControlActionEnum;
import nl.tno.oorti.test.netn4.datatypes.LocationStruct;
import nl.tno.oorti.test.netn4.datatypes.MoveToLocationTaskStruct;
import nl.tno.oorti.test.netn4.datatypes.MoveTypeEnum32;
import nl.tno.oorti.test.netn4.datatypes.TaskDefinitionVariantRecord;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.helpers.OmtFunctions;
import nl.tno.oorti.ooencoder.OOencoder;
import nl.tno.oorti.ooencoder.OOencoderFactory;
import nl.tno.oorti.ooencoder.OOencoderFactoryFactory;
import nl.tno.oorti.ooencoder.exceptions.OOcodecException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author bergtwvd
 */
public class TestHLAextendableVariantRecord {

	static ObjectModelType module;
	static ObjectModelType mim;
	static OOencoderFactory factory;

	@BeforeAll
	public static void setUpClass() throws IOException {
		module = OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/NETN-Merged-FULL.xml"));
		mim = OmtFunctions.readOmt(TestOMTcodecFactoryClassStructure.class.getResource("/foms/NETN-MIM.xml"));
        factory = OOencoderFactoryFactory.getOOencoderFactory(new ObjectModelType[]{module, mim});
	}

	@AfterAll
	public static void tearDownClass() {
	}

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	TaskDefinitionVariantRecord createTaskDefinitionVariantRecord() {
		TaskDefinitionVariantRecord rec = new TaskDefinitionVariantRecord();
		rec.setTaskType(EntityControlActionEnum.MoveToLocation);

		MoveToLocationTaskStruct task = new MoveToLocationTaskStruct();
		task.setLocation(new LocationStruct());
		task.getLocation().setX(11);
		task.getLocation().setY(12);
		task.getLocation().setZ(13);
		task.setMoveType(MoveTypeEnum32.OnlyRoads);
		task.setPath(new LocationStruct[0]);
		task.setSpeed(37);
		rec.setMoveToLocation(task);

		return rec;
	}

	@Test
	public void testTaskDefinitionVariantRecord() throws OOcodecException {
		OOencoder encoder = factory.createOOencoder(TaskDefinitionVariantRecord.class);

		TaskDefinitionVariantRecord in = createTaskDefinitionVariantRecord();

		byte[] bytes = encoder.encode(in);
		// encoded length must be:
		// 4  - discriminant
		// 0  - no padding needed
		// 4  - encoding length
		// 0  - no padding needed to get to 8 byte multiple
		// ===== variant
		// 24 - location X Y Z
		// 4  - size of path array
		// 4  - move type
		// 4  - speed
		// ====== +
		// 44 bytes
		Assertions.assertEquals(44, bytes.length);

		TaskDefinitionVariantRecord out = (TaskDefinitionVariantRecord) encoder.decode(bytes);

		Assertions.assertEquals(in.getTaskType().getValue(), out.getTaskType().getValue());
		Assertions.assertEquals(in.getMoveToLocation().getLocation().getX(), out.getMoveToLocation().getLocation().getX(), 0);
		Assertions.assertEquals(in.getMoveToLocation().getLocation().getY(), out.getMoveToLocation().getLocation().getY(), 0);
		Assertions.assertEquals(in.getMoveToLocation().getLocation().getZ(), out.getMoveToLocation().getLocation().getZ(), 0);

		Assertions.assertEquals(in.getTaskType().getValue(), out.getTaskType().getValue());
		Assertions.assertEquals(in.getMoveToLocation().getMoveType().getValue(), out.getMoveToLocation().getMoveType().getValue());
		Assertions.assertEquals(in.getMoveToLocation().getPath().length, out.getMoveToLocation().getPath().length);
		Assertions.assertEquals(in.getMoveToLocation().getSpeed(), out.getMoveToLocation().getSpeed(), 0);
	}

}
