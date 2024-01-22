package useless.dragonfly.model.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import useless.dragonfly.DragonFly;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.mixins.mixin.accessor.RenderBlocksAccessor;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.model.blockstates.data.BlockstateData;
import useless.dragonfly.model.blockstates.data.ModelPart;
import useless.dragonfly.model.blockstates.data.VariantData;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;
import useless.dragonfly.utilities.NamespaceId;
import useless.dragonfly.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockModelDragonFly extends BlockModelRenderBlocks {
	public BlockModel baseModel;
	public boolean render3d;
	public float renderScale;
	public BlockstateData blockstateData;
	public MetaStateInterpreter metaStateInterpreter;
	public BlockModelDragonFly(BlockModel model) {
		this(model, null, null,true, 0.25f);
	}

	public BlockModelDragonFly(BlockModel model, BlockstateData blockstateData, MetaStateInterpreter metaStateInterpreter, boolean render3d) {
		this(model, blockstateData, metaStateInterpreter, render3d, 0.25f);
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
		BlockModel[] models = getModelsFromState(block, x, y, z, false);
		boolean didRender = false;
        for (BlockModel model : models) {
            didRender |= BlockModelRenderer.renderModelNormal(model, block, x, y, z);
        }
		return didRender;
	}

	@Override
	public boolean renderNoCulling(Block block, int x, int y, int z) {
		BlockModel[] models = getModelsFromState(block, x, y, z, false);
		boolean didRender = false;
		for (BlockModel model : models) {
			didRender |= BlockModelRenderer.renderModelNoCulling(model, block, x, y, z);
		}
		return didRender;
	}

	@Override
	public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
		BlockModel[] models = getModelsFromState(block, x, y, z, false);
		boolean didRender = false;
		for (BlockModel model : models) {
			didRender |= BlockModelRenderer.renderModelBlockUsingTexture(model, block, x, y, z, textureIndex);
		}
		return didRender;
	}

	@Override
	public boolean shouldItemRender3d() {
		return render3d;
	}

	@Override
	public float getItemRenderScale() {
		return renderScale;
	}
	public BlockModel[] getModelsFromState(Block block, int x, int y, int z, boolean sourceFromWorld){
		if (blockstateData == null || metaStateInterpreter == null){
			return new BlockModel[]{baseModel};
		}
		RenderBlocksAccessor blocksAccessor = (RenderBlocksAccessor) BlockModelRenderer.getRenderBlocks();
		WorldSource blockSource;
		if (sourceFromWorld){
			blockSource = Minecraft.getMinecraft(Minecraft.class).theWorld; //world
		} else {
			blockSource = blocksAccessor.getBlockAccess(); // chunk cache
		}
		int meta = blockSource.getBlockMetadata(x,y,z);
		Random random = Utilities.getRandomFromPos(x, y, z);
		HashMap<String, String> blockStateList = metaStateInterpreter.getStateMap(blockSource, x, y, z, block, meta);
		if (blockstateData.variants != null){ // If model uses variant system
			return getModelVariant(blockStateList, random);
		}
		if (blockstateData.multipart != null){
			return getMultipartModel(blockStateList, random);
		}
		return new BlockModel[]{baseModel};
	}
	public BlockModel[] getModelVariant(HashMap<String, String> blockState, Random random){
		VariantData variantData = null;
		for (String stateString: blockstateData.variants.keySet()) {
			String[] conditions = stateString.split(",");
			HashMap<String, String> conditionMap = new HashMap<>();
			for (String condition : conditions){
				conditionMap.put(condition.split("=")[0], condition.split("=")[1]);
			}
			if (matchConditionsAND(blockState, conditionMap)){
				variantData = blockstateData.variants.get(stateString).getRandomModel(random);
				break;
			}
		}
		if (variantData == null) return new BlockModel[]{baseModel};


		return new BlockModel[]{getModelFromKey(variantData.model, variantData.x, variantData.y)};
	}
	public BlockModel[] getMultipartModel(HashMap<String, String> blockState, Random random){
		List<BlockModel> modelsToRender = new ArrayList<>();
		for (ModelPart modelPart : blockstateData.multipart){
			if (modelPart.when == null || modelPart.when.match(blockState)){
				VariantData data = modelPart.getRandomModel(random);
				modelsToRender.add(getModelFromKey(data.model, data.x, data.y));
			}
		}
		return modelsToRender.toArray(new BlockModel[0]);
	}
	public boolean matchConditionsAND(HashMap<String, String> blockState, HashMap<String, String> conditions){
		if (conditions == null){
			DragonFly.LOGGER.warn("conditions for model '" + baseModel.namespaceId + "' have returned null!");
			return false;
		}
		boolean stateMet = true;
		for (Map.Entry<String, String > entry: conditions.entrySet()) {
			String stateValue = blockState.get(entry.getKey());
			if (stateValue == null){
				DragonFly.LOGGER.warn("Could not find corresponding value for '" + entry.getKey() + "' in model '" + baseModel.namespaceId + "'!");
				stateMet = false;
				continue;
			}
			stateMet &= stateValue.equals(entry.getValue());
		}
		return stateMet;
	}
	public BlockModel getModelFromKey(String key, int x, int y){
		String[] modelID = key.split(":");
		String namespace;
		String model;
		if (modelID.length < 2){
			namespace = NamespaceId.coreNamespaceId;
			model = modelID[0];
		} else {
			namespace = modelID[0];
			model = modelID[1];
		}
		return ModelHelper.getOrCreateBlockModel(namespace, model, x, -y);
	}
}
