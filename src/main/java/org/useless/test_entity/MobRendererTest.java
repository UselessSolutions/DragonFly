package org.useless.test_entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.DragonFly;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.dragonfly.models.entity.mojang.StaticEntityModelMojang;
import org.useless.dragonfly.renderer.MobRenderer;

public class MobRendererTest extends MobRenderer<TestMob> {
	public MobRendererTest() {
		super(0.5F);
	}


	@Override
	protected @Nullable StaticEntityModel getAndSetupModelForLayer(@NotNull TestMob entity, float brightness, float partialTick, int layer) {
		StaticEntityModel staticEntityModel = DragonFly.loadEntityModel("geometry.dragonfly.test", 0);
		if (staticEntityModel instanceof StaticEntityModelMojang) {
			staticEntityModel.resetBones();


			//animateWalk((StaticEntityModelMojang)staticEntityModel, DragonFlyClient.animation, getLimbSwing(entity, partialTick), getLimbYaw(entity, partialTick), 2.0F, 2.5F);
		}
		return staticEntityModel;
	}
}
