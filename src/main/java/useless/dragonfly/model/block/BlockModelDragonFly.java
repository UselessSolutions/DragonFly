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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		InternalModel[] models = getModelsFromState(block, x, y, z, false);
		boolean didRender = false;
        for (InternalModel model : models) {
            didRender |= BlockModelRenderer.renderModelNormal(model.model, block, x, y, z, model.rotationX, -model.rotationY);
        }
		return didRender;
	}

	@Override
	public boolean renderNoCulling(Block block, int x, int y, int z) {
		InternalModel[] models = getModelsFromState(block, x, y, z, false);
		boolean didRender = false;
		for (InternalModel model : models) {
			didRender |= BlockModelRenderer.renderModelNoCulling(model.model, block, x, y, z, model.rotationX, -model.rotationY);
		}
		return didRender;
	}

	@Override
	public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
		InternalModel[] models = getModelsFromState(block, x, y, z, false);
		boolean didRender = false;
		for (InternalModel model : models) {
			didRender |= BlockModelRenderer.renderModelBlockUsingTexture(model.model, block, x, y, z, textureIndex, model.rotationX, -model.rotationY);
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
	public InternalModel[] getModelsFromState(Block block, int x, int y, int z, boolean sourceFromWorld){
		if (blockstateData == null || metaStateInterpreter == null){
			return new InternalModel[]{new InternalModel(baseModel, 0, 0)};
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
		if (blockstateData.variants != null){ // If model uses variant system
			return getModelVariant(blockStateList);
		}
		if (blockstateData.multipart != null){
			return getMultipartModel(blockStateList);
		}
		return new InternalModel[]{new InternalModel(baseModel, 0, 0)};
	}
	public InternalModel[] getModelVariant(HashMap<String, String> blockState){
		VariantData variantData = null;

		for (String stateString: blockstateData.variants.keySet()) {
			String[] conditions = stateString.split(",");
			HashMap<String, String> conditionMap = new HashMap<>();
			for (String condition : conditions){
				conditionMap.put(condition.split("=")[0], condition.split("=")[1]);
			}
			if (matchConditionsAND(blockState, conditionMap)){
				variantData = blockstateData.variants.get(stateString);
				break;
			}
		}
		if (variantData == null) return new InternalModel[]{new InternalModel(baseModel, 0, 0)};


		return new InternalModel[]{new InternalModel(getModelFromKey(variantData.model), variantData.x, variantData.y)};
	}
	public InternalModel[] getMultipartModel(HashMap<String, String> blockState){
		List<InternalModel> modelsToRender = new ArrayList<>();
		for (ModelPart modelPart : blockstateData.multipart){
			if (modelPart.when.get("AND") != null){
				ArrayList<Map<String, String>> and = (ArrayList<Map<String, String>>) modelPart.when.get("AND");
				if (matchConditionsAND(blockState, arrToMap(and))){
					modelsToRender.add(new InternalModel(getModelFromKey(modelPart.apply.model), modelPart.apply.x, modelPart.apply.y));
				}
				continue;
			}
			if (modelPart.when.get("OR") != null){
				ArrayList<Map<String, String>> or = ((ArrayList<Map<String, String>>) modelPart.when.get("OR"));
				if (matchConditionsOR(blockState, arrToMap(or))){
					modelsToRender.add(new InternalModel(getModelFromKey(modelPart.apply.model), modelPart.apply.x, modelPart.apply.y));
				}
				continue;
			}
			HashMap<String,String> conditions = new HashMap<>();
			for (Map.Entry<String, Object> entry : modelPart.when.entrySet()){
				conditions.put(entry.getKey(), (String) entry.getValue());
			}
			if (matchConditionsAND(blockState, conditions)){
				modelsToRender.add(new InternalModel(getModelFromKey(modelPart.apply.model), modelPart.apply.x, modelPart.apply.y));
			}
		}
		return modelsToRender.toArray(new InternalModel[0]);
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
	public boolean matchConditionsOR(HashMap<String, String> blockState, HashMap<String, String> conditions){
		if (conditions == null){
			DragonFly.LOGGER.warn("conditions for model '" + baseModel.namespaceId + "' have returned null!");
			return false;
		}
		for (Map.Entry<String, String > entry: conditions.entrySet()) {
			String stateValue = blockState.get(entry.getKey());
			if (stateValue == null){
				DragonFly.LOGGER.warn("Could not find corresponding value for '" + entry.getKey() + "' in model '" + baseModel.namespaceId + "'!");
				continue;
			}
			if(stateValue.equals(entry.getValue())){
				return true;
			}
		}
		return false;
	}
	private HashMap<String, String> arrToMap(ArrayList<Map<String, String>> arr){
		HashMap<String, String> map = new HashMap<>();
        for (Map<String, String> entry : arr){
			map.putAll(entry);
		}
        return new HashMap<>(map);
	}
	public BlockModel getModelFromKey(String key){
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
		return ModelHelper.getOrCreateBlockModel(namespace, model);
	}
}
