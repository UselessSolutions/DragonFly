package useless.dragonfly.model.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.mixins.mixin.accessor.RenderBlocksAccessor;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockstateData;
import useless.dragonfly.model.blockstates.data.VariantData;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;
import useless.dragonfly.utilities.NamespaceId;

import java.util.HashMap;

public class BlockModelDragonFly extends BlockModelRenderBlocks {
	public BlockModel baseModel;
	public boolean render3d;
	public float renderScale;
	public BlockstateData blockstateData;
	public MetaStateInterpreter metaStateInterpreter;
	private int rotationX = 0;
	private int rotationY = 0;
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
		rotationX = 0;
		rotationY = 0;
		BlockModel model = getModelFromState(block, x, y, z, false);
		return BlockModelRenderer.renderModelNormal(model, block, x, y, z, rotationX, rotationY);
	}

	@Override
	public boolean renderNoCulling(Block block, int x, int y, int z) {
		rotationX = 0;
		rotationY = 0;
		BlockModel model = getModelFromState(block, x, y, z, false);
		return BlockModelRenderer.renderModelNoCulling(model, block, x, y, z, rotationX, rotationY);
	}

	@Override
	public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
		rotationX = 0;
		rotationY = 0;
		BlockModel model = getModelFromState(block, x, y, z, false);
		return BlockModelRenderer.renderModelBlockUsingTexture(model, block, x, y, z, textureIndex, rotationX, rotationY);
	}

	@Override
	public boolean shouldItemRender3d() {
		return render3d;
	}

	@Override
	public float getItemRenderScale() {
		return renderScale;
	}
	public BlockModel getModelFromState(Block block, int x, int y, int z, boolean sourceFromWorld){
		if (blockstateData == null || metaStateInterpreter == null){
			return baseModel;
		}
		RenderBlocksAccessor blocksAccessor = (RenderBlocksAccessor) BlockModelRenderer.getRenderBlocks();
		WorldSource blockSource;
		if (sourceFromWorld){
			blockSource = Minecraft.getMinecraft(Minecraft.class).theWorld; //world
		} else {
			blockSource = blocksAccessor.getBlockAccess(); // chunk cache
		}
		int meta = blockSource.getBlockMetadata(x,y,z);
		HashMap<String, String> blockStateList = metaStateInterpreter.getStateMap(blockSource, x, y, z, block, meta);
		VariantData variantData = null;

		for (String stateString: blockstateData.variants.keySet()) {
			String[] conditions = stateString.split(",");
			boolean stateMet = true;
			for (String condition : conditions) {
				String s1 = condition.split("=")[0];
				String s2 = condition.split("=")[1];
				stateMet &= blockStateList.get(s1).equals(s2);
			}
			if (stateMet){
				variantData = blockstateData.variants.get(stateString);
				break;
			}
		}
		if (variantData == null) return baseModel;

		String[] modelID = variantData.model.split(":");
		String namespace;
		String model;
		if (modelID.length < 2){
			namespace = NamespaceId.coreNamespaceId;
			model = modelID[0];
		} else {
			namespace = modelID[0];
			model = modelID[1];
		}
		rotationX = variantData.x;
		rotationY = variantData.y;
		return ModelHelper.getOrCreateBlockModel(namespace, model);
	}
}
