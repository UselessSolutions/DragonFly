package useless.dragonfly.mixins.mixin;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.model.block.BlockModelRenderer;

@Mixin(value = RenderBlocks.class, remap = false)
public abstract class RenderBlocksMixin {

	@Inject(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IFF)V", at = @At("HEAD"), cancellable = true)
	public void redirectRenderer(Block block, int metadata, float brightness, float alpha, CallbackInfo ci){
		if (BlockModelDispatcher.getInstance().getDispatch(block) instanceof BlockModelDragonFly){
			BlockModelDragonFly blockModelDragonFly = (BlockModelDragonFly) BlockModelDispatcher.getInstance().getDispatch(block);
			BlockModelRenderer.renderModelInventory(blockModelDragonFly, block, metadata, brightness);
			ci.cancel();
		}
	}
}
