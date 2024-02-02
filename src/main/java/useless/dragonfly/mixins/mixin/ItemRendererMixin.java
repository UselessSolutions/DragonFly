package useless.dragonfly.mixins.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.ItemRenderer;
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

@Mixin(value = ItemRenderer.class, remap = false)
public class ItemRendererMixin {
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
		if (ModelHelper.itemModelMap.containsKey(itemstack.getItem())){
			GL11.glBindTexture(3553, mc.renderEngine.getTexture("/terrain.png"));
			BlockModelRenderer.renderModelInventory(ModelHelper.itemModelMap.get(itemstack.getItem()), itemstack.getItem().id, itemstack.getMetadata(), entity.getBrightness(1f));
			ci.cancel();
			if (!DragonFly.renderState.equals("head")){
				DragonFly.renderState = "gui";
			}
		}
	}
	@Inject(method = "renderItem(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/item/ItemStack;Z)V", at = @At("TAIL"))
	private void unsetRenderState(Entity entity, ItemStack itemstack, boolean handheldTransform, CallbackInfo ci){
		if (!DragonFly.renderState.equals("head")){
			DragonFly.renderState = "gui";
		}
	}
}
