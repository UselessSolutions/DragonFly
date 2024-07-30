package org.useless.dragonfly.model.block;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.DragonFly;
import org.useless.dragonfly.model.blockstates.data.BlockstateData;
import org.useless.dragonfly.model.blockstates.data.ModelPart;
import org.useless.dragonfly.model.blockstates.data.VariantData;
import org.useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;
import org.useless.dragonfly.model.newmodels.BlockModelStandardBTA;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;
import org.useless.dragonfly.model.newmodels.generic.StaticModelRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BlockModelDragonFly extends BlockModelStandardBTA<Block> {
	public StaticModel baseModel;
	public boolean render3d;
	public float renderScale;
	public BlockstateData blockstateData;
	public MetaStateInterpreter metaStateInterpreter;

	public BlockModelDragonFly(Block block, StaticModel model, BlockstateData blockstateData, MetaStateInterpreter metaStateInterpreter, boolean render3d, float renderScale) {
		super(block, model);
		this.baseModel = model;
		this.render3d = render3d;
		this.renderScale = renderScale;
		this.blockstateData  = blockstateData;
		this.metaStateInterpreter = metaStateInterpreter;
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		return super.render(tessellator, x, y, z);
	}

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		if (true) {
			super.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
			return;
		}
//        float xOffset;
//		float yOffset;
//		float zOffset;
//		float xScale;
//		float yScale;
//		float zScale;
//		float xRot;
//		float yRot;
//		float zRot;
//		PositionData displayData = baseModel.getDisplayPosition(DragonFly.renderState);
//		switch (DragonFly.renderState) {
//			case "ground":
//				xScale = (float) displayData.scale[2] * 4;
//				yScale = (float) displayData.scale[1] * 4;
//				zScale = (float) displayData.scale[0] * 4;
//
//				xOffset = 0.5f * xScale;
//				yOffset = 0.5f * yScale;
//				zOffset = 0.5f * zScale;
//
//				xOffset -= (float) displayData.translation[2] / 16f;
//				yOffset -= (float) displayData.translation[1] / 16f;
//				zOffset -= (float) displayData.translation[0] / 16f;
//
//				xRot = (float) displayData.rotation[0];
//				yRot = (float) displayData.rotation[1];
//				zRot = (float) displayData.rotation[2];
//				break;
//			case "head":
//				GL11.glFrontFace(GL11.GL_CW);
//				xScale = (float) displayData.scale[0];
//				yScale = (float) displayData.scale[1];
//				zScale = (float) displayData.scale[2];
//
//				xOffset = 0.5f * xScale;
//				yOffset = 0.5f * yScale;
//				zOffset = 0.5f * zScale;
//
//				xOffset -= (float) displayData.translation[0] / 16f;
//				yOffset -= (float) displayData.translation[1] / 16f;
//				zOffset -= (float) displayData.translation[2] / 16f;
//
//				xRot = (float) displayData.rotation[0];
//				yRot = (float) displayData.rotation[1] + 180;
//				zRot = (float) displayData.rotation[2];
//				break;
//			case "firstperson_righthand":
//				xScale = (float) displayData.scale[2] * 2.5f;
//				yScale = (float) displayData.scale[1] * 2.5f;
//				zScale = (float) displayData.scale[0] * 2.5f;
//
//				xOffset = 0.5f * xScale;
//				yOffset = 0.5f * yScale;
//				zOffset = 0.5f * zScale;
//
//				xOffset -= (float) displayData.translation[2] / 8f;
//				yOffset -= (float) displayData.translation[1] / 8f;
//				zOffset -= (float) displayData.translation[0] / 8f;
//
//				xRot = (float) displayData.rotation[0];
//				yRot = (float) displayData.rotation[1] + 45;
//				zRot = (float) displayData.rotation[2];
//				break;
//			case "thirdperson_righthand":
//				GL11.glFrontFace(GL11.GL_CW);
//				float scale = 8f/3;
//				xScale = (float) displayData.scale[2] * scale;
//				yScale = (float) displayData.scale[1] * scale;
//				zScale = (float) displayData.scale[0] * scale;
//
//				xOffset = 0.5f * xScale;
//				yOffset = 0.5f * yScale;
//				zOffset = 0.5f * zScale;
//
//				xOffset -= (float) displayData.translation[2] / 16f;
//				yOffset -= (float) displayData.translation[1] / 16f;
//				zOffset -= (float) displayData.translation[0] / 16f;
//
//				xRot = (float) -displayData.rotation[2] + 180;
//				yRot = (float) displayData.rotation[1] + 45;
//				zRot = (float) -displayData.rotation[0] - 100;
//				break;
//			case "gui":
//			default:
//				xScale = (float) displayData.scale[2] * 1.6f;
//				yScale = (float) displayData.scale[1] * 1.6f;
//				zScale = (float) displayData.scale[0] * 1.6f;
//
//				xOffset = 0.5f * xScale;
//				yOffset = 0.5f * yScale;
//				zOffset = 0.5f * zScale;
//
//				xOffset -= (float) displayData.translation[2] / 16f;
//				yOffset -= (float) displayData.translation[1] / 16f;
//				zOffset -= (float) displayData.translation[0] / 16f;
//
//				xRot = (float) displayData.rotation[0] - 30;
//				yRot = (float) displayData.rotation[1] + 45;
//				zRot = (float) displayData.rotation[2];
//		}
//
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//
//		GL11.glRotatef(yRot, 0, 1, 0);
//		GL11.glRotatef(xRot, 1, 0, 0);
//		GL11.glRotatef(zRot, 0, 0, 1);
//		GL11.glTranslatef(-xOffset, -yOffset, -zOffset);
//		GL11.glScalef(xScale, yScale, zScale);
//		if (baseModel.blockCubes != null){
//			tessellator.startDrawingQuads();
//			GL11.glColor4f(brightness, brightness, brightness, 1);
//			for (BlockCube cube: baseModel.blockCubes) {
//				for (BlockFace face: cube.getFaces().values()) {
//					tessellator.setNormal(face.getSide().getOffsetX(), face.getSide().getOffsetY(), face.getSide().getOffsetZ());
//					if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null){
//						tessellator.setLightmapCoord(lightmapCoordinate);
//					}
//					float r = 1;
//					float g = 1;
//					float b = 1;
//					if (face.useTint()){
//						int color = BlockColorDispatcher.getInstance().getDispatch(block).getFallbackColor(metadata);
//						r = (float)(color >> 16 & 0xFF) / 255.0f;
//						g = (float)(color >> 8 & 0xFF) / 255.0f;
//						b = (float)(color & 0xFF) / 255.0f;
//					}
//					BlockModelRenderer.renderModelFaceWithColor(tessellator, face, 0, 0, 0, r * brightness, g * brightness, b * brightness);
//				}
//			}
//			tessellator.draw();
//		}
//		GL11.glDisable(GL11.GL_CULL_FACE); // Deleting this causes render issues on vanilla transparent blocks
//		GL11.glTranslatef(xOffset, yOffset, zOffset);
	}

	@Override
	public void setWorldModel(ModelContainer modelContainer, WorldSource worldSource, int x, int y, int z, int meta) {
		if (blockstateData == null || metaStateInterpreter == null) {
			super.setWorldModel(modelContainer, worldSource, x, y, z, meta);
			return;
		}


		useVariant: {
			Random posRand = getRandomFromPos(x, y, z);
			HashMap<String, String> blockStateList = metaStateInterpreter.getStateMap(worldSource, x, y, z, block, meta);
			if (blockstateData.variants != null){ // If model uses variant system
				VariantData variantData = null;
				for (String stateString: blockstateData.variants.keySet()) {
					String[] conditions = stateString.split(",");
					HashMap<String, String> conditionMap = new HashMap<>();
					for (String condition : conditions){
						conditionMap.put(condition.split("=")[0], condition.split("=")[1]);
					}
					if (matchConditionsAND(blockStateList, conditionMap)){
						variantData = blockstateData.variants.get(stateString).getRandomModel(posRand);
						break;
					}
				}
				if (variantData == null) break useVariant;

				modelContainer.addModel("variant", StaticModelRegistry.getInstance().retrieveModel(variantData.model), variantData.x, variantData.y, 0);
			}
			if (blockstateData.multipart != null){
				int num = 0;
				for (ModelPart modelPart : blockstateData.multipart){
					if (modelPart.when == null || modelPart.when.match(blockStateList)){
						VariantData data = modelPart.getRandomModel(posRand);
						modelContainer.addModel("part" + num++, StaticModelRegistry.getInstance().retrieveModel(data.model), data.x, data.y, 0);
					}
				}
			}
			return;
		}

		modelContainer.addModel(StaticModelRegistry.getInstance().retrieveModel("builtin:block/missing", null));
	}

	@Override
	public void setInventoryModel(ModelContainer modelContainer, int meta) {
		super.setInventoryModel(modelContainer, meta);
	}

	@Override
	public boolean shouldItemRender3d() {
		return render3d;
	}

	@Override
	public float getItemRenderScale() {
		return renderScale;
	}
	@Override
	public IconCoordinate getParticleTexture(Side side, int meta) {
		if (baseModel.getTexture("particle", true) != null){
			return baseModel.getTexture("particle", false);
		}
		return super.getParticleTexture(side, meta);
	}


	public boolean matchConditionsAND(HashMap<String, String> blockState, HashMap<String, String> conditions){
		if (conditions == null){
			DragonFly.LOGGER.warn("conditions for model '{}' have returned null!", baseModel.toString());
			return false;
		}
		boolean stateMet = true;
		for (Map.Entry<String, String > entry: conditions.entrySet()) {
			String stateValue = blockState.get(entry.getKey());
			if (stateValue == null){
				DragonFly.LOGGER.warn("Could not find corresponding value for '{}' in model '{}'!", entry.getKey(), baseModel.toString());
				stateMet = false;
				continue;
			}
			stateMet &= stateValue.equals(entry.getValue());
		}
		return stateMet;
	}
}
