package useless.dragonfly.debug;

import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.core.Global;
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
import useless.dragonfly.debug.testentity.Zombie.EntityZombieTest;
import useless.dragonfly.debug.testentity.Zombie.RenderZombieTest;
import useless.dragonfly.debug.testentity.Zombie.ZombieModelTest;
import useless.dragonfly.helper.AnimationHelper;
import useless.dragonfly.helper.ModelHelper;

import static useless.dragonfly.DragonFly.MOD_ID;

public class DebugEntities {
	public static void init(){
		EntityHelper.Core.createEntity(EntityHTest.class, 1000, "ht");
		EntityHelper.Core.createEntity(EntityZombieTest.class, 1000, "zt");
		AnimationHelper.getOrCreateEntityAnimation(MOD_ID, "zombie_test.animation");
		EntityHelper.Core.createEntity(EntityDragon.class, 1001, "dragon");
		EntityHelper.Core.createEntity(EntityWarden.class, 1002, "warden");
		MobInfoRegistry.register(EntityWarden.class, "df.warden.name", "df.warden.desc", 20, 0, null);
		if (!Global.isServer){
			EntityHelper.Client.assignEntityRenderer(EntityHTest.class, new RenderHTest(ModelHelper.getOrCreateEntityModel(MOD_ID, "hierachytest.json", HModelTest.class), 0.5f));
			EntityHelper.Client.assignEntityRenderer(EntityZombieTest.class, new RenderZombieTest(ModelHelper.getOrCreateEntityModel(MOD_ID, "zombie_test.json", ZombieModelTest.class), 0.5f));
			EntityHelper.Client.assignEntityRenderer(EntityDragon.class, new DragonRenderer(ModelHelper.getOrCreateEntityModel(MOD_ID, "mod_dragon.json", DragonModel.class), 0.5f));
			EntityHelper.Client.assignEntityRenderer(EntityWarden.class, new WardenRenderer(ModelHelper.getOrCreateEntityModel(MOD_ID, "warden.json", WardenModel.class), 0.5f));
		}
	}
}
