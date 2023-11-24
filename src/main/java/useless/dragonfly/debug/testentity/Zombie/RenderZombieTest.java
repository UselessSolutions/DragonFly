package useless.dragonfly.debug.testentity.Zombie;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import useless.dragonfly.model.entity.BenchEntityModel;

public class RenderZombieTest extends LivingRenderer<EntityZombieTest> {
	protected BenchEntityModel modelBench;

	public RenderZombieTest(BenchEntityModel modelbase, float shadowSize) {
		super(modelbase, shadowSize);
		this.modelBench = modelbase;
	}

	@Override
	protected void renderEquippedItems(EntityZombieTest entity, float f) {
		ItemStack itemstack = entity.getHeldItem();
		if (itemstack != null) {
			if (!this.modelBench.getIndexBones().isEmpty() && this.modelBench.getIndexBones().containsKey("RightArm")) {
				GL11.glPushMatrix();
				this.modelBench.postRender(this.modelBench.getIndexBones().get("RightArm"), 0.0625f);
				;
				GL11.glTranslatef(-0.0625f, 0.4375f, 0.0625f);
				if (itemstack.itemID < Block.blocksList.length && ((BlockModel) BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID])).shouldItemRender3d()) {
					float f1 = 0.5f;
					GL11.glTranslatef(0.0f, 0.1875f, -0.3125f);
					GL11.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(f1 *= 0.75f, -f1, f1);
				} else if (itemstack.itemID == Item.toolBow.id) {
					float f2 = 0.625f;
					GL11.glTranslatef(0.0f, 0.125f, 0.3125f);
					GL11.glRotatef(-20.0f, 0.0f, 1.0f, 0.0f);
					GL11.glScalef(f2, -f2, f2);
					GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
				} else if (Item.itemsList[itemstack.itemID].isFull3D()) {
					float f2 = 0.625f;
					GL11.glTranslatef(0.0f, 0.1875f, 0.0f);
					GL11.glScalef(f2, -f2, f2);
					GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
				} else {
					float f3 = 0.375f;
					GL11.glTranslatef(0.25f, 0.1875f, -0.1875f);
					GL11.glScalef(f3, f3, f3);
					GL11.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glRotatef(20.0f, 0.0f, 0.0f, 1.0f);
				}
				this.renderDispatcher.itemRenderer.renderItem(entity, itemstack);
				GL11.glPopMatrix();
			}
		}
	}
}
