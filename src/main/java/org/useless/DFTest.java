package org.useless;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.animal.MobCow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.dragonfly.renderer.EntityRenderer;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;

public class DFTest implements GameStartEntrypoint, ModelEntrypoint {
	public static final String MOD_ID = "dragonfly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static String version;
	static {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
	}

	@Override
	public void beforeGameStart() {

	}


	@Override
	public void afterGameStart() {
		LOGGER.info("DragonFly initialized.");

	}

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {
		MobRendererQuadruped<MobCow> renderer = new MobRendererQuadruped<>(0.7f);
		renderer.init(MobCow.class, dispatcher);
		ModelHelper.setEntityModel(MobCow.class, () -> renderer);
	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}

