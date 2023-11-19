package useless.dragonfly;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import useless.dragonfly.block.BlockModel;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.model.block.data.RotationData;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.registries.TextureRegistry;
import useless.dragonfly.testentity.*;

import java.lang.reflect.Field;


public class DragonFly implements ModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new Gson();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static final float COMPARE_CONST = 0.001f;
	public static boolean equalFloats(float a, float b){
		return Math.abs(Float.compare(a, b)) < COMPARE_CONST;
	}
	public static String writeFields(Class clazz){
		Field[] fields = clazz.getFields();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < fields.length; i++)  {
			Field field = fields[i];
			builder.append(field.getName()).append(": ").append(field.getClass().cast(field));
			if (i < fields.length-1){
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	public static String tabBlock(String string, int tabAmount){
		StringBuilder builder = new StringBuilder();
		for (String line: string.split("\n")) {
			builder.append(tabString(tabAmount)).append(line).append("\n");
		}
		return builder.toString();
	}
	public static String tabString(int tabAmount){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < tabAmount; i++) {
			builder.append("\t");
		}
		return builder.toString();
	}
	public static final Block testBlock = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock.json")))
		.setTextures(3,1)
		.build(new BlockModel("testblock", 1000, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock.json")));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock2.json")))
		.setTextures(13,13)
		.build(new BlockModel("testblock2", 1001, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock2.json")));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock3.json")))
		.setTextures(4,0)
		.build(new BlockModel("testblock3", 1002, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "TestBlock3.json")));
	public static final Block testBlock5 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "beacon.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock5", 1004, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "beacon.json")));
	public static final Block testBlock6 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "directionPyramid.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock6", 1005, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "directionPyramid.json")));
	public static final Block testBlock7 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "stool.json")))
		.setTextures(1,3)
		.build(new BlockModel("testblock7", 1006, Material.dirt, ModelHelper.getOrCreateBlockModel(MOD_ID, "stool.json")));
    @Override
    public void onInitialize() {
		EntityHelper.createEntity(EntityHTest.class, new RenderHTest(ModelHelper.getOrCreateEntityModel(MOD_ID, "hierachyTest.json", HModelTest.class), 0.5f), 1000, "ht");
		StringBuilder builder = new StringBuilder();
		for (String string: ModelHelper.modelDataFiles.keySet()) {
			builder.append(string);
			builder.append(tabBlock(ModelHelper.modelDataFiles.get(string).toString(), 1));
		}
		LOGGER.info(builder.toString());
        LOGGER.info("DragonFly initialized.");
    }

	@Override
	public void onPreLaunch() {
		TextureRegistry.init();
	}
}
