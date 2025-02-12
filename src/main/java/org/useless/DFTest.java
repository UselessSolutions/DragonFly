package org.useless;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.animal.MobCow;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import org.useless.dragonfly.models.block.BlockModelDFObj;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.Objects;

public class DFTest implements GameStartEntrypoint, ModelEntrypoint {
	public static final String MOD_ID = "dragonfly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static String version;
	static {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
	}

	public static Block<?> block = Blocks.register("test", "df:block/test", 3000, (b) -> new BlockLogicTransparent(b, Material.stone));
	public static Block<?> benz = Blocks.register("benz", "df:block/benz", 3001, (b) -> new BlockLogicTransparent(b, Material.stone));

	@Override
	public void beforeGameStart() {

	}


	@Override
	public void afterGameStart() {
		LOGGER.info("DragonFly initialized.");

	}

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		dispatcher.addDispatch(new BlockModelDFJava<>(block, loadDataModel("block/dragon_egg")));
		dispatcher.addDispatch(new BlockModelDFObj<>(benz, Minecraft.getMinecraft().texturePackList.getResourceAsStream("/cat.obj")).setAllTextures(BlockModelStandard.BLOCK_TEXTURES, "minecraft:block/cat"));
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {
		ModelHelper.setEntityModel(MobCow.class, () -> new MobRendererQuadruped<>(0.7f));
	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}

	private static @NotNull BlockModelMojangData loadDataModel(@NotNull final String id) {
		final Minecraft mc = Minecraft.getMinecraft();
		return Objects.requireNonNull(BlockModelMojangData.Cache.loadModelData(mc.texturePackList, id), "Cannot find model for id '" + id + "'!");
	}
}

