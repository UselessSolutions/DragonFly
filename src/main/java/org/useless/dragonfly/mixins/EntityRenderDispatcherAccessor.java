package org.useless.dragonfly.mixins;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = EntityRenderDispatcher.class, remap = false)
public interface EntityRenderDispatcherAccessor {
	@Accessor
	Map<Class<?>, EntityRenderer<?>> getRenderers();
}
