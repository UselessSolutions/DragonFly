package useless.dragonfly;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import useless.dragonfly.mixininterfaces.ExtraRendering;
import useless.dragonfly.model.BlockBenchModel;

import java.lang.reflect.Field;

public class BlockModelDragonFly extends BlockModelRenderBlocks {
	BlockBenchModel baseModel;
	public static RenderBlocks getRenderBlock(){
		try {
			Field f = BlockModelRenderBlocks.class.getDeclaredField("renderBlocks");
			f.setAccessible(true);
			RenderBlocks rb = (RenderBlocks) f.get(null);
			f.setAccessible(false);
			return rb;
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	public BlockModelDragonFly(BlockBenchModel model) {
		super(0);
		this.baseModel = model;
	}

	@Override
	public boolean render(Block block, int x, int y, int z) {
		return ((ExtraRendering)getRenderBlock()).renderModelNormal(baseModel, block, x, y, z);
	}

	@Override
	public boolean renderNoCulling(Block block, int x, int y, int z) {
		return getRenderBlock().renderBlockAllFaces(block, this.renderType, x, y, z);
	}

	@Override
	public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
		return getRenderBlock().renderBlockUsingTexture(block, this.renderType, x, y, z, textureIndex);
	}

	@Override
	public boolean shouldItemRender3d() {
		if (this.renderType == 0 || this.renderType == 20 || this.renderType == 30) {
			return true;
		}
		if (this.renderType == 13) {
			return true;
		}
		if (this.renderType == 10) {
			return true;
		}
		if (this.renderType == 11) {
			return true;
		}
		if (this.renderType == 18) {
			return true;
		}
		if (this.renderType == 21) {
			return true;
		}
		if (this.renderType == 22) {
			return true;
		}
		if (this.renderType == 23) {
			return true;
		}
		if (this.renderType == 27) {
			return true;
		}
		if (this.renderType == 31) {
			return true;
		}
		return this.renderType == 16;
	}

	@Override
	public float getItemRenderScale() {
		if (this.renderType == 1 || this.renderType == 19 || this.renderType == 12 || this.renderType == 2) {
			return 0.5f;
		}
		return 0.25f;
	}
}
