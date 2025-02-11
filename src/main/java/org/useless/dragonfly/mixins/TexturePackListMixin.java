package org.useless.dragonfly.mixins;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.texturepack.TexturePackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;

@Mixin(value = TexturePackList.class, remap = false)
public class TexturePackListMixin {
	@Inject(method = "refresh", at = @At("TAIL"))
	public void refreshInject(CallbackInfo ci) {
//		BlockModelDispatcher.getInstance().reload();
//		ItemModelDispatcher.getInstance().reload();
		EntityGeometryMojangData.Cache.reload(TexturePackList.class.cast(this));
//		EntityRenderDispatcher.instance.reload();
//		TileEntityRenderDispatcher.instance.reload();
	}
}
