package useless.dragonfly.debug;

import turniplabs.halplibe.helper.EntityHelper;
import useless.dragonfly.debug.testentity.Dragon.DragonModel;
import useless.dragonfly.debug.testentity.Dragon.DragonRenderer;
import useless.dragonfly.debug.testentity.Dragon.EntityDragon;
import useless.dragonfly.debug.testentity.HTest.EntityHTest;
import useless.dragonfly.debug.testentity.HTest.HModelTest;
import useless.dragonfly.debug.testentity.HTest.RenderHTest;
import useless.dragonfly.debug.testentity.Warden.EntityWarden;
import useless.dragonfly.debug.testentity.Warden.WardenModel;
import useless.dragonfly.debug.testentity.Warden.WardenRenderer;
import useless.dragonfly.helper.ModelHelper;

import static useless.dragonfly.DragonFly.MOD_ID;

public class DebugEntities {
	public static void init(){
		EntityHelper.createEntity(EntityHTest.class, new RenderHTest(ModelHelper.getOrCreateEntityModel(MOD_ID, "hierachyTest.json", HModelTest.class), 0.5f), 1000, "ht");
		EntityHelper.createEntity(EntityDragon.class, new DragonRenderer(ModelHelper.getOrCreateEntityModel(MOD_ID, "mod_dragon.json", DragonModel.class), 0.5f), 1001, "dragon");
		EntityHelper.createEntity(EntityWarden.class, new WardenRenderer(ModelHelper.getOrCreateEntityModel(MOD_ID, "warden.json", WardenModel.class), 0.5f), 1002, "warden");
	}
}
