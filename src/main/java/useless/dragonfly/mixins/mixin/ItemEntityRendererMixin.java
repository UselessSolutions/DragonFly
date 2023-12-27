package useless.dragonfly.mixins.mixin;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
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
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.utilities.NamespaceId;

@Mixin(value = ItemEntityRenderer.class, remap = false)
public class ItemEntityRendererMixin {
	@Shadow
	public boolean field_27004_a;

	@Shadow
	@Final
	private RenderBlocks renderBlocks;

	@Inject(method = "doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V", at = @At("HEAD"))
	private void setRenderState(EntityItem entity, double d, double d1, double d2, float f, float f1, CallbackInfo ci){
		DragonFly.renderState = "ground";
	}
	@Inject(method = "doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V", at = @At("TAIL"))
	private void unsetRenderState(EntityItem entity, double d, double d1, double d2, float f, float f1, CallbackInfo ci){
		DragonFly.renderState = "gui";
	}
	@Inject(method = "drawItemIntoGui(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;IIIIIFF)V", at = @At("HEAD"), cancellable = true)
	private void setRenderStateGUI(FontRenderer fontrenderer, RenderEngine renderengine, int itemId, int meta, int k, int l, int i1, float brightness, float alpha, CallbackInfo ci){
		DragonFly.renderState = "gui";
		if (ModelHelper.getItemModel(Item.itemsList[itemId]) != null && ModelHelper.getItemModel(Item.itemsList[itemId]).parent != null && ModelHelper.getItemModel(Item.itemsList[itemId]).parent.contains(":block/")){
			GL11.glBlendFunc(770, 771);
            renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
			NamespaceId namespaceId = NamespaceId.idFromString(ModelHelper.getItemModel(Item.itemsList[itemId]).parent);
			BlockModel model = ModelHelper.getOrCreateBlockModel(namespaceId.getNamespace(), namespaceId.getId());
			GL11.glPushMatrix();
			GL11.glTranslatef(l - 2, i1 + 3, -3.0f);
			GL11.glScalef(10.0f, 10.0f, 10.0f);
			GL11.glTranslatef(1.0f, 0.5f, 1.0f);
			GL11.glScalef(1.0f, 1.0f, -1.0f);
			GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
			int l1 = Item.itemsList[itemId].getColorFromDamage(meta);
			float f2 = (float)(l1 >> 16 & 0xFF) / 255.0f;
			float f4 = (float)(l1 >> 8 & 0xFF) / 255.0f;
			float f5 = (float)(l1 & 0xFF) / 255.0f;
			if (this.field_27004_a) {
				GL11.glColor4f(f2 * brightness, f4 * brightness, f5 * brightness, alpha);
			} else {
				GL11.glColor4f(brightness, brightness, brightness, alpha);
			}
			GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
			this.renderBlocks.useInventoryTint = this.field_27004_a;
			BlockModelRenderer.renderModelInventory(model,Block.blocksList[1], meta, brightness);
			this.renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
			ci.cancel();
		}
	}

}
