package useless.dragonfly.mixins.mixin;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.dragonfly.DragonFly;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelRenderer;

@Mixin(value = ItemEntityRenderer.class, remap = false)
public class ItemEntityRendererMixin {
	@Shadow
	public boolean field_27004_a;

	@Shadow
	@Final
	private RenderBlocks renderBlocks;

	@Inject(method = "doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V", at = @At("HEAD"), cancellable = true)
	private void setRenderState(EntityItem entity, double d, double d1, double d2, float f, float f1, CallbackInfo ci){
		DragonFly.renderState = "ground";
		ItemStack itemstack = entity.item;
		if (itemstack == null) {
			return;
		}
		if (ModelHelper.itemModelMap.containsKey(entity.item.getItem())){
			GL11.glPushMatrix();
			float f2 = MathHelper.sin(((float)entity.age + f1) / 10.0f + entity.field_804_d) * 0.1f + 0.1f;
			GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
			GL11.glEnable(32826);
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			float f3 = (((float)entity.age + f1) / 20.0f + entity.field_804_d) * 57.29578f;
			int renderCount = 1;
			if (entity.item.stackSize > 1) {
				renderCount = 2;
			}
			if (entity.item.stackSize > 5) {
				renderCount = 3;
			}
			if (entity.item.stackSize > 20) {
				renderCount = 4;
			}
			GL11.glPushMatrix();
			GL11.glScaled(1.0, 1.0, 1.0);
			GL11.glRotated(f3, 0.0, 1.0, 0.0);
			GL11.glTranslated(-0.5, 0.0, -0.05 * (double)(renderCount - 1));
			for (int j = 0; j < renderCount; ++j) {
				GL11.glPushMatrix();
				GL11.glTranslated(0.0, 0.0, 0.1 * (double)j);
				EntityRenderDispatcher.instance.itemRenderer.renderItem(entity, entity.item, false);
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();
			ci.cancel();
			GL11.glDisable(32826);
			GL11.glPopMatrix();
			DragonFly.renderState = "gui";
		}
	}
	@Inject(method = "doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V", at = @At("TAIL"))
	private void unsetRenderState(EntityItem entity, double d, double d1, double d2, float f, float f1, CallbackInfo ci){
		DragonFly.renderState = "gui";
	}

	@Inject(method = "drawItemIntoGui(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;IIIIIFF)V", at = @At("HEAD"), cancellable = true)
	private void guiRedirect(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, float brightness, float alpha, CallbackInfo ci){
		if (i > Block.blocksList.length){
			Item item = Item.itemsList[i];
			if (!ModelHelper.itemModelMap.containsKey(item)) return;
			DragonFly.renderState = "gui";
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glEnable(2884);

			GL11.glPushMatrix();
			GL11.glTranslatef(l - 2, i1 + 3, -3.0f);
			GL11.glScalef(10.0f, 10.0f, 10.0f);
			GL11.glTranslatef(1.0f, 0.5f, 1.0f);
			GL11.glScalef(1.0f, 1.0f, -1.0f);
			GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);

			int l1 = Item.itemsList[i].getColorFromDamage(j);
			float f2 = (float)(l1 >> 16 & 0xFF) / 255.0f;
			float f4 = (float)(l1 >> 8 & 0xFF) / 255.0f;
			float f5 = (float)(l1 & 0xFF) / 255.0f;
			if (this.field_27004_a) {
				GL11.glColor4f(f2 * brightness, f4 * brightness, f5 * brightness, alpha);
			} else {
				GL11.glColor4f(brightness, brightness, brightness, alpha);
			}

			GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);

			renderengine.bindTexture(renderengine.getTexture("/terrain.png"));

			this.renderBlocks.useInventoryTint = this.field_27004_a;
			BlockModelRenderer.renderModelInventory(ModelHelper.itemModelMap.get(item), i, j, brightness);
			this.renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
			GL11.glEnable(2884);
			GL11.glDisable(3042);
			ci.cancel();
		}
	}
}
