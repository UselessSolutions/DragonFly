package useless.dragonfly;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockMesh;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.block.BlockModel;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.BlockModelDragonFly;


public class DragonFly implements ModInitializer {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new Gson();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static final Block testBlock = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateModel(MOD_ID, "TestBlock.json")))
		.setTextures(3,1)
		.build(new BlockModel("testblock", 1000, Material.dirt, ModelHelper.getOrCreateModel(MOD_ID, "TestBlock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateModel(MOD_ID, "TestBlock2.json")))
		.setTextures(13,13)
		.build(new BlockModel("testblock2", 1001, Material.dirt, ModelHelper.getOrCreateModel(MOD_ID, "TestBlock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateModel(MOD_ID, "TestBlock3.json")))
		.setTextures(4,0)
		.build(new BlockModel("testblock3", 1002, Material.dirt, ModelHelper.getOrCreateModel(MOD_ID, "TestBlock3.json")));

    @Override
    public void onInitialize() {
        LOGGER.info("DragonFly initialized.");

		//TEST Entity
		//EntityHelper.createEntity(EntityZombieTest.class, new RenderZombieTest(BenchEntityModel.decodeModel(MOD_ID, "zombie_test.json", ZombieModelTest.class), 0.5F), 10000, "ZombieTest");
    }

}
