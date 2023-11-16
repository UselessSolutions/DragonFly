package useless.dragonfly.mixin;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BlockModelRenderBlocks.class, remap = false)
public interface BlockModelRenderBlocksAccessor {
	@Accessor
	RenderBlocks getRenderBlocks();
}
