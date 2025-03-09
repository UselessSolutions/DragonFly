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
import org.useless.DragonFly;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.data.block.mojang.state.AppliedData;
import org.useless.dragonfly.data.block.mojang.state.BlockstateData;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;
import org.useless.dragonfly.data.block.mojang.state.ModelPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

	public BlockModelDFJava<T> setStateData(String id) {
		this.stateData = DragonFly.loadStateData(id);
		return this;
	}

	public BlockModelDFJava<T> setStateInterpreter(MetaStateInterpreter stateInterpreter) {
		this.stateInterpreter = stateInterpreter;
		return this;
	}

	public StaticBlockModel[] getModelsFromState(WorldSource worldSource, Block<?> block, int x, int y, int z) {
		if (stateData == null || stateInterpreter == null){
			return new StaticBlockModel[]{baseModel};
		}

		int meta = worldSource.getBlockMetadata(x,y,z);
		Random random = DragonFly.getRandomFromPos(x, y, z);

		HashMap<String, String> blockStateList = stateInterpreter.getStateMap(worldSource, x, y, z, block, meta);

		//TODO:
		/*if (stateData.variants != null){ // If model uses variant system
			return getModelVariant(blockStateList, random);
		}*/

		if (stateData.multipart != null){
			return getModelFromMultipart(blockStateList, random);
		}

		return new StaticBlockModel[]{baseModel};
	}

	public StaticBlockModel[] getModelFromMultipart(HashMap<String, String> blockState, Random random){
		List<StaticBlockModel> modelsToRender = new ArrayList<>();
		for (ModelPart modelPart : stateData.multipart){
			if (modelPart.when == null || modelPart.when.match(blockState)){
				AppliedData data = modelPart.getRandomModel(random);
				modelsToRender.add(DragonFly.loadDataModel(data.model).asModel());
			}
		}
		return modelsToRender.toArray(new StaticBlockModel[0]);
	}

	//    @Override
    public boolean renderNoCulling(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
		StaticBlockModel[] models = getModelsFromState(worldSource, block, x, y, z);
		boolean rendered = true;
		for (StaticBlockModel model : models) {
			rendered = model.renderAttached(this,
				tessellator,
				worldSource, x,
				y,
				z,
				0, 0, 0, false, null);;
			if(!rendered) {
				return false;
			}
		}
		return rendered;
    }

//    @Override
    public boolean render(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
		StaticBlockModel[] models = getModelsFromState(worldSource, block, x, y, z);
		boolean rendered = true;
		for (StaticBlockModel model : models) {
			rendered = model.renderAttached(this,
				tessellator,
				worldSource, x,
				y,
				z,
				0, 0, 0, true, null);;
			if(!rendered) {
				return false;
			}
		}
		return rendered;
    }

//    @Override
    public boolean renderWithOverrideTexture(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z, final IconCoordinate textureIndex) {
		StaticBlockModel[] models = getModelsFromState(worldSource, block, x, y, z);
		boolean rendered = true;
		for (StaticBlockModel model : models) {
			rendered = model.renderAttached(this,
				tessellator,
				worldSource, x,
				y,
				z,
				0, 0, 0, true, textureIndex);;
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
	public final boolean render(Tessellator tessellator, int x, int y, int z) {
		return render(tessellator, renderBlocks.blockAccess, x, y, z);
	}

	public final boolean renderNoCulling(Tessellator tessellator, int x, int y, int z) {
		return renderNoCulling(tessellator, renderBlocks.blockAccess, x, y, z);
	}

	public final boolean renderWithOverrideTexture(Tessellator tessellator, int x, int y, int z, IconCoordinate textureIndex) {
		return renderWithOverrideTexture(tessellator, renderBlocks.blockAccess, x, y, z, textureIndex);
	}

	@Override
	public final void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
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
	public IconCoordinate getBlockTexture(WorldSource worldSource, int x, int y, int z, Side side) {
		return getOverlayTexture(worldSource.getBlockMetadata(x, y, z));
	}

	@Override
	public IconCoordinate getBlockOverbrightTexture(WorldSource worldSource, int i, int j, int k, int l) {
		return null;
	}

	@Override
	public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(Side side, int i) {
		return null;
	}

	@Override
	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int meta) {
		return getOverlayTexture(meta);
	}

	@Override
    public @Nullable IconCoordinate getParticleTexture(@NotNull final Side side, final int meta) {
        return this.baseModel.getParticle(side);
    }

	@Override
	public boolean shouldSideBeRendered(WorldSource worldSource, AABB aABB, int i, int j, int k, int l, int m) {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(WorldSource worldSource, AABB aABB, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public boolean shouldSideBeColored(WorldSource worldSource, int i, int j, int k, int l, int m) {
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
