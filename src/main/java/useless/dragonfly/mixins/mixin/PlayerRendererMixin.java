package useless.dragonfly.mixins.mixin;

import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.dragonfly.DragonFly;

@Mixin(value = PlayerRenderer.class, remap = false)
public class PlayerRendererMixin {
	@Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/EntityPlayer;F)V", at = @At("HEAD"))
	private void setRenderState(EntityPlayer entity, float f, CallbackInfo ci){
		DragonFly.renderState = "head";
	}
	@Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/EntityPlayer;F)V", at = @At("TAIL"))
	private void unsetRenderState(EntityPlayer entity, float f, CallbackInfo ci){
		DragonFly.renderState = "gui";
	}
}
