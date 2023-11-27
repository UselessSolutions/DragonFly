package useless.dragonfly.model.block;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import useless.dragonfly.mixins.mixin.accessor.RenderBlocksAccessor;
import useless.dragonfly.mixins.mixininterfaces.ExtraRendering;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockstateData;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.lang.reflect.Field;

public class BlockModelDragonFly extends BlockModelRenderBlocks {
	public BlockModel baseModel;
	public boolean render3d;
	public float renderScale;
	public BlockstateData blockstateData;
	public MetaStateInterpreter metaStateInterpreter;
	public BlockModelDragonFly(BlockModel model) {
		this(model, null, null,true, 0.25f);
	}

	public BlockModelDragonFly(BlockModel model, BlockstateData blockstateData, MetaStateInterpreter metaStateInterpreter, boolean render3d, float renderScale) {
		super(0);
		this.baseModel = model;
		this.render3d = render3d;
		this.renderScale = renderScale;
		this.blockstateData  = blockstateData;
		this.metaStateInterpreter = metaStateInterpreter;
	}

	@Override
	public boolean render(Block block, int x, int y, int z) {
		BlockModel model = getModelFromState(block, x, y, z);
		return ((ExtraRendering)getRenderBlock()).renderModelNormal(model, block, x, y, z);
	}

	@Override
	public boolean renderNoCulling(Block block, int x, int y, int z) {
		BlockModel model = getModelFromState(block, x, y, z);
		return ((ExtraRendering)getRenderBlock()).renderModelNoCulling(model, block, x, y, z);
	}

	@Override
	public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
		BlockModel model = getModelFromState(block, x, y, z);
		return ((ExtraRendering)getRenderBlock()).renderModelBlockUsingTexture(model, block, x, y, z, textureIndex);
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
	public BlockModel getModelFromState(Block block, int x, int y, int z){
		if (blockstateData == null || metaStateInterpreter == null){
			return baseModel;
		}
		RenderBlocksAccessor blocksAccessor = (RenderBlocksAccessor)getRenderBlock();
		int meta = blocksAccessor.getBlockAccess().getBlockMetadata(x,y,z);
		return baseModel;
	}
}
