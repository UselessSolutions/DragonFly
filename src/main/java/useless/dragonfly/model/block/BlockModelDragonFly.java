package useless.dragonfly.model.block;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import useless.dragonfly.mixininterfaces.ExtraRendering;

import java.lang.reflect.Field;

public class BlockModelDragonFly extends BlockModelRenderBlocks {
	public BlockBenchModel baseModel;
	public boolean render3d;
	public float renderScale;
	public BlockModelDragonFly(BlockBenchModel model) {
		this(model,true, 0.25f);
	}

	public BlockModelDragonFly(BlockBenchModel model, boolean render3d, float renderScale) {
		super(0);
		this.baseModel = model;
		this.render3d = render3d;
		this.renderScale = renderScale;
	}

	@Override
	public boolean render(Block block, int x, int y, int z) {
		return ((ExtraRendering)getRenderBlock()).renderModelNormal(baseModel, block, x, y, z);
	}

	@Override
	public boolean renderNoCulling(Block block, int x, int y, int z) {
		return ((ExtraRendering)getRenderBlock()).renderModelNoCulling(baseModel, block, x, y, z);
	}

	@Override
	public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
		return ((ExtraRendering)getRenderBlock()).renderModelBlockUsingTexture(baseModel, block, x, y, z, textureIndex);
	}

	@Override
	public boolean shouldItemRender3d() {
		return render3d;
	}

	@Override
	public float getItemRenderScale() {
		return renderScale;
	}
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
}
