package useless.dragonfly.mixins.mixin;

import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.dragonfly.DragonFly;

@Mixin(value = ItemEntityRenderer.class, remap = false)
public class ItemEntityRendererMixin {
	@Inject(method = "doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V", at = @At("HEAD"))
	private void setRenderState(EntityItem entity, double d, double d1, double d2, float f, float f1, CallbackInfo ci){
		DragonFly.renderState = "ground";
	}
	@Inject(method = "doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V", at = @At("TAIL"))
	private void unsetRenderState(EntityItem entity, double d, double d1, double d2, float f, float f1, CallbackInfo ci){
		DragonFly.renderState = "gui";
	}
}
