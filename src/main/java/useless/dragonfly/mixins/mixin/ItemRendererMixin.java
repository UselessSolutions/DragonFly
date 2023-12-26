package useless.dragonfly.mixins.mixin;

import net.minecraft.client.render.ItemRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.dragonfly.DragonFly;

@Mixin(value = ItemRenderer.class, remap = false)
public class ItemRendererMixin {
	@Inject(method = "renderItem(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/item/ItemStack;Z)V", at = @At("HEAD"))
	private void setRenderState(Entity entity, ItemStack itemstack, boolean handheldTransform, CallbackInfo ci){
		if (!DragonFly.renderState.equals("head")){
			if (handheldTransform){
				DragonFly.renderState = "firstperson_righthand";
			} else {
				DragonFly.renderState = "ground";
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
