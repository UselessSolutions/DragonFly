package useless.dragonfly.mixins.mixin;

import net.minecraft.client.entity.fx.EntityDiggingFX;
import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.registries.TextureRegistry;

@Mixin(value = EntityDiggingFX.class, remap = false)
public class EntityDiggingFXMixin extends EntityFX {
	@Shadow
	private Block block;

	public EntityDiggingFXMixin(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(world, x, y, z, motionX, motionY, motionZ);
	}
	@Redirect(method = "<init>(Lnet/minecraft/core/world/World;DDDDDDLnet/minecraft/core/block/Block;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;getBlockTextureFromSideAndMetadata(Lnet/minecraft/core/util/helper/Side;I)I"))
	private int particleFromModel(Block instance, Side side, int meta){
		BlockModel model = BlockModelDispatcher.getInstance().getDispatch(instance);
		int tex;
		if (model instanceof BlockModelDragonFly){
			tex = TextureRegistry.getIndexOrDefault(((BlockModelDragonFly) model).getModelsFromState(block, (int) x, (int) y, (int) z, true)[0].model.getTexture("particle"), block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, meta));
		} else {
			tex = block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, meta);
		}
		return tex;
	}
}
