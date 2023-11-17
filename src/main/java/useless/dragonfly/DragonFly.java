package useless.dragonfly;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockGlass;
import net.minecraft.core.block.BlockMesh;
import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import useless.dragonfly.model.BlockBenchModel;


public class DragonFly implements ModInitializer {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new Gson();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static final Block testBlock = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(BlockBenchModel.decodeModel(MOD_ID, "TestBlock.json")))
		.setTextures(3,1)
		.build(new BlockMesh("testblock", 1000));
	public static final Block testBlock2 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(BlockBenchModel.decodeModel(MOD_ID, "TestBlock2.json")))
		.setTextures(13,13)
		.build(new BlockMesh("testblock2", 1001));
	public static final Block testBlock3 = new BlockBuilder(MOD_ID)
		.setBlockModel(new BlockModelDragonFly(BlockBenchModel.decodeModel(MOD_ID, "TestBlock3.json")))
		.setTextures(4,0)
		.build(new BlockMesh("testblock3", 1002));

    @Override
    public void onInitialize() {
        LOGGER.info("DragonFly initialized.");
    }
	public static String getModelLocation(String modID, String modelSource){
		return "/assets/" + modID + "/model/" + modelSource;
	}
}
