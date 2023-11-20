package useless.dragonfly.debug.testentity;

import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.model.ModelBase;

public class DragonRenderer extends LivingRenderer<EntityDragon> {
	public DragonRenderer(ModelBase modelbase, float shadowSize) {
		super(modelbase, shadowSize);
	}
}
