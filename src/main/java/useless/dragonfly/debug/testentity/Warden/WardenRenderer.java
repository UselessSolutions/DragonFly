package useless.dragonfly.debug.testentity.Warden;

import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.model.ModelBase;
import org.lwjgl.opengl.GL11;

public class WardenRenderer extends LivingRenderer<EntityWarden> {
	public WardenRenderer(ModelBase modelbase, float shadowSize) {
		super(modelbase, shadowSize);
	}
	public void doRenderPreview(EntityWarden entity, double x, double y, double z, float yaw, float partialTick) {
		GL11.glScalef(0.65f, 0.65f, 0.65f);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.doRender(entity, x, y, z, yaw, partialTick);
	}
}
