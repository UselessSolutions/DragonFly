package org.useless;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import turniplabs.halplibe.util.ModelEntrypoint;

public class DragonFlyClient implements ModelEntrypoint {
	//public static final @NotNull Animation animation = DragonFly.loadEntityAnimations("dragonfly", "walk");


	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {
		//DragonFly.loadEntityModel("geometry.dragonfly.test", 0);
		//ModelHelper.setEntityModel(TestMob.class, () -> new MobRendererTest());
	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher tileEntityRenderDispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher blockColorDispatcher) {

	}

	@Override
	public void initBlockModels(BlockModelDispatcher blockModelDispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher itemModelDispatcher) {
	}
}
