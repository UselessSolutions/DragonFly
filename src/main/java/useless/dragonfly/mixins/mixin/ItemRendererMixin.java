package useless.dragonfly.mixins.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
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

@Mixin(value = ItemRenderer.class, remap = false)
public class ItemRendererMixin {
	@Shadow
	private RenderBlocks renderBlocksInstance;

	@Shadow
	private Minecraft mc;

	@Inject(method = "renderItem(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
	private void setRenderState(Entity entity, ItemStack itemstack, boolean handheldTransform, CallbackInfo ci){
		if (!DragonFly.renderState.equals("head") && !DragonFly.renderState.equals("thirdperson_righthand")){
			if (handheldTransform){
				DragonFly.renderState = "firstperson_righthand";
			} else {
				DragonFly.renderState = "ground";
			}
		}
		if (ModelHelper.getItemModel(itemstack.getItem()) != null && ModelHelper.getItemModel(itemstack.getItem()).parent != null && ModelHelper.getItemModel(itemstack.getItem()).parent.contains(":block/")){
			GL11.glPushMatrix();
			BlockModelRenderBlocks.setRenderBlocks(this.renderBlocksInstance);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
			float brightness = entity.getBrightness(1.0f);
			if (this.mc.fullbright) {
				brightness = 1.0f;
			}
			NamespaceId namespaceId = NamespaceId.idFromString(ModelHelper.getItemModel(itemstack.getItem()).parent);
			BlockModel model = ModelHelper.getOrCreateBlockModel(namespaceId.getNamespace(), namespaceId.getId());
			BlockModelRenderer.renderModelInventory(model,Block.blocksList[1], itemstack.getMetadata(), brightness);
			GL11.glDisable(3042);
			unsetRenderState(entity, itemstack, handheldTransform, ci);
			GL11.glPopMatrix();
			ci.cancel();
		}

	}
	@Inject(method = "renderItem(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/item/ItemStack;Z)V", at = @At("TAIL"))
	private void unsetRenderState(Entity entity, ItemStack itemstack, boolean handheldTransform, CallbackInfo ci){
		if (!DragonFly.renderState.equals("head")){
			DragonFly.renderState = "gui";
		}
	}
}
