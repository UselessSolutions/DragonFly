package org.useless.dragonfly.models.block;

import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.data.block.mojang.state.AppliedData;
import org.useless.dragonfly.data.block.mojang.state.BlockstateData;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;
import org.useless.dragonfly.data.block.mojang.state.ModelPart;

import java.util.*;

public class BlockModelDFJava<T extends BlockLogic> extends BlockModel<T> {
    private final StaticBlockModel baseModel;
	private MetaStateInterpreter stateInterpreter;
	private BlockstateData stateData;

    public BlockModelDFJava(@NotNull final Block<T> block, @NotNull final StaticBlockModel baseModel) {
        super(block);
        this.baseModel = baseModel;
    }

    public BlockModelDFJava(@NotNull final Block<T> block, @NotNull final BlockModelData baseModel) {
        super(block);
        this.baseModel = baseModel.asModel();
    }

	public BlockModelDFJava<T> setStateData(final String id) {
		this.stateData = DragonFly.loadStateData(id);
		return this;
	}

	public BlockModelDFJava<T> setStateInterpreter(final MetaStateInterpreter stateInterpreter) {
		this.stateInterpreter = stateInterpreter;
		return this;
	}

	public StaticBlockModel[] getModelsFromState(final WorldSource worldSource, final Block<?> block, final int x, final int y, final int z) {
		if (this.stateData == null || this.stateInterpreter == null){
			return new StaticBlockModel[]{this.baseModel};
		}

		final int meta = worldSource.getBlockMetadata(x,y,z);
		final Random random = DragonFly.getRandomFromPos(x, y, z);

		final HashMap<String, String> blockStateList = this.stateInterpreter.getStateMap(worldSource, x, y, z, block, meta);

		//TODO:
		if (this.stateData.variants != null){ // If model uses variant system
			return getModelVariant(blockStateList, random);
		}

		if (this.stateData.multipart != null){
			return getModelFromMultipart(blockStateList, random);
		}

		return new StaticBlockModel[]{this.baseModel};
	}

	public boolean matchConditionsAND(final HashMap<String, String> blockState, final HashMap<String, String> conditions){
		if (conditions == null){
			DragonFly.LOGGER.warn("conditions for model '" + this.baseModel + "' have returned null!");
			return false;
		}
		boolean stateMet = true;
		for (final Map.Entry<String, String > entry: conditions.entrySet()) {
			final String stateValue = blockState.get(entry.getKey());
			if (stateValue == null){
				DragonFly.LOGGER.warn("Could not find corresponding value for '" + entry.getKey() + "' in model '" + this.baseModel + "'!");
				stateMet = false;
				continue;
			}
			stateMet &= stateValue.equals(entry.getValue());
		}
		return stateMet;
	}

	public StaticBlockModel[] getModelVariant(final HashMap<String, String> blockState, final Random random){
		AppliedData variantData = null;
		for (final String stateString: this.stateData.variants.keySet()) {
			final String[] conditions = stateString.split(",");
			final HashMap<String, String> conditionMap = new HashMap<>();
			for (final String condition : conditions){
				conditionMap.put(condition.split("=")[0], condition.split("=")[1]);
			}
			if (matchConditionsAND(blockState, conditionMap)){
				variantData = this.stateData.variants.get(stateString).getRandomModel(random);
				break;
			}
		}
		if (variantData == null) return new StaticBlockModel[]{this.baseModel};


		return new StaticBlockModel[]{DragonFly.loadBlockModel(variantData.model).asModel()};
	}

	public StaticBlockModel[] getModelFromMultipart(final HashMap<String, String> blockState, final Random random){
		final List<StaticBlockModel> modelsToRender = new ArrayList<>();
		for (final ModelPart modelPart : this.stateData.multipart){
			if (modelPart.when == null || modelPart.when.match(blockState)){
				final AppliedData data = modelPart.getRandomModel(random);
				modelsToRender.add(DragonFly.loadBlockModel(data.model).asModel());
			}
		}
		return modelsToRender.toArray(new StaticBlockModel[0]);
	}

	//    @Override
    public boolean renderNoCulling(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
		final StaticBlockModel[] models = getModelsFromState(worldSource, this.block, x, y, z);
		boolean rendered = true;
		for (final StaticBlockModel model : models) {
			rendered = model.renderAttached(this,
				tessellator,
				worldSource, x, y, z,
				0, 0, 0,
				0, 0, 0, false,
				false, null);
			if(!rendered) {
				return false;
			}
		}
		return rendered;
    }

//    @Override
    public boolean render(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
		final StaticBlockModel[] models = getModelsFromState(worldSource, this.block, x, y, z);
		boolean rendered = true;
		for (final StaticBlockModel model : models) {
			rendered = model.renderAttached(this,
				tessellator,
				worldSource, x, y, z,
				0, 0, 0,
				0, 0, 0, false,
				true, null);
			if(!rendered) {
				return false;
			}
		}
		return rendered;
    }

//    @Override
    public boolean renderWithOverrideTexture(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z, final IconCoordinate textureIndex) {
		final StaticBlockModel[] models = getModelsFromState(worldSource, this.block, x, y, z);
		boolean rendered = true;
		for (final StaticBlockModel model : models) {
			rendered = model.renderAttached(this,
				tessellator,
				worldSource, x, y, z,
				0, 0, 0,
				0, 0, 0, false,
				true, textureIndex);
			if(!rendered) {
				return false;
			}
		}
		return rendered;
    }

//    @Override
    public void renderStandalone(@NotNull final Tessellator tessellator, final int metadata, final float brightness, final float alpha, @Nullable final Integer lightmapCoordinate) {
		this.baseModel.renderStandalone(this,
            tessellator, 0, 0, 0,
            metadata,
            BlockColorDispatcher.getInstance().getDispatch(this.block),
            brightness, alpha);
    }

	@Override
	public final boolean render(final Tessellator tessellator, final int x, final int y, final int z) {
		return render(tessellator, renderBlocks.blockAccess, x, y, z);
	}

	public final boolean renderNoCulling(final Tessellator tessellator, final int x, final int y, final int z) {
		return renderNoCulling(tessellator, renderBlocks.blockAccess, x, y, z);
	}

	public final boolean renderWithOverrideTexture(final Tessellator tessellator, final int x, final int y, final int z, final IconCoordinate textureIndex) {
		return renderWithOverrideTexture(tessellator, renderBlocks.blockAccess, x, y, z, textureIndex);
	}

	@Override
	public final void renderBlockOnInventory(final Tessellator tessellator, final int metadata, final float brightness, final float alpha, @Nullable final Integer lightmapCoordinate) {
		renderStandalone(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}

	@Override
    public boolean shouldItemRender3d() {
        return true;
    }

	@Override
	public float getItemRenderScale() {
		return 0.25f;
	}

	//    @Override
    public @NotNull DisplayPos getItemDisplayPos(@NotNull final String id) {
        return this.baseModel.getItemDisplayPos(id);
    }

    @Override
    public int renderLayer() {
        return this.baseModel.renderLayer();
    }

	@Override
	public IconCoordinate getBlockTexture(final WorldSource worldSource, final int x, final int y, final int z, final Side side) {
		return getOverlayTexture(worldSource.getBlockMetadata(x, y, z));
	}

	@Override
	public IconCoordinate getBlockOverbrightTexture(final WorldSource worldSource, final int i, final int j, final int k, final int l) {
		return null;
	}

	@Override
	public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(final Side side, final int i) {
		return null;
	}

	@Override
	public IconCoordinate getBlockTextureFromSideAndMetadata(final Side side, final int meta) {
		return getOverlayTexture(meta);
	}

	@Override
    public @Nullable IconCoordinate getParticleTexture(@NotNull final Side side, final int meta) {
        return this.baseModel.getParticle(side);
    }

	@Override
	public boolean shouldSideBeRendered(final WorldSource worldSource, final AABB aABB, final int i, final int j, final int k, final int l, final int m) {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(final WorldSource worldSource, final AABB aABB, final int i, final int j, final int k, final int l) {
		return false;
	}

	@Override
	public boolean shouldSideBeColored(final WorldSource worldSource, final int i, final int j, final int k, final int l, final int m) {
		return false;
	}

	//    @Override
    public @Nullable IconCoordinate getOverlayTexture(final int meta) {
        return this.baseModel.getOverlay();
    }

//    @Override
    public int particleColorIndex(@NotNull final WorldSource worldSource, final int x, final int y, final int z, @NotNull final Side side, final int meta) {
        return this.baseModel.particleColorIndex(side);
    }
}
