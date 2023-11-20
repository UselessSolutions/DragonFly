package useless.dragonfly.debug;

import turniplabs.halplibe.helper.EntityHelper;
import useless.dragonfly.debug.testentity.EntityHTest;
import useless.dragonfly.debug.testentity.HModelTest;
import useless.dragonfly.debug.testentity.RenderHTest;
import useless.dragonfly.helper.ModelHelper;

import static useless.dragonfly.DragonFly.MOD_ID;

public class DebugEntities {
	public static void init(){
		//EntityHelper.createEntity(EntityHTest.class, new RenderHTest(ModelHelper.getOrCreateEntityModel(MOD_ID, "hierachyTest.json", HModelTest.class), 0.5f), 1000, "ht");
	}
}
