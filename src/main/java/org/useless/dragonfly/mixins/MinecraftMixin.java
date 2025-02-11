package org.useless.dragonfly.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texturepack.TexturePackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {

	@Shadow
	public TexturePackList texturePackList;

	@Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundRepository;initialize(Lnet/minecraft/client/Minecraft;)V"))
	public void initStuffMixin(CallbackInfo ci) {
		EntityGeometryMojangData.Cache.reload(this.texturePackList);
	}
}
