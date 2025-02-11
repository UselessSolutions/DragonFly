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
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.data.block.BlockModelData;

public class BlockModelGeneric<T extends BlockLogic> extends BlockModel<T> {
    private final StaticBlockModel staticModel;

    public BlockModelGeneric(@NotNull final Block<T> block, @NotNull final StaticBlockModel staticModel) {
        super(block);
        this.staticModel = staticModel;
    }

    public BlockModelGeneric(@NotNull final Block<T> block, @NotNull final BlockModelData staticModel) {
        super(block);
        this.staticModel = staticModel.asModel();
    }

//    @Override
    public boolean renderNoCulling(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
        return this.staticModel.renderAttached(this,
            tessellator,
            worldSource, x,
            y,
            z,
            0, 0, 0, false, null);
    }

//    @Override
    public boolean render(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
        return this.staticModel.renderAttached(this,
            tessellator,
            worldSource, x,
            y,
            z,
            0, 0, 0, true, null);
    }

//    @Override
//    public boolean renderOverbright(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z) {
//        throw new UnsupportedOperationException();
//    }

//    @Override
    public boolean renderWithOverrideTexture(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z, final IconCoordinate textureIndex) {
        return this.staticModel.renderAttached(this,
            tessellator,
            worldSource, x,
            y,
            z,
            0, 0, 0, true, textureIndex);
    }

//    @Override
    public void renderStandalone(@NotNull final Tessellator tessellator, final int metadata, final float brightness, final float alpha, @Nullable final Integer lightmapCoordinate) {
        this.staticModel.renderStandalone(this,
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
        return this.staticModel.getItemDisplayPos(id);
    }

    @Override
    public int renderLayer() {
        return this.staticModel.renderLayer();
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
        return this.staticModel.getParticle(side);
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
        return this.staticModel.getOverlay();
    }

//    @Override
    public int particleColorIndex(@NotNull final WorldSource worldSource, final int x, final int y, final int z, @NotNull final Side side, final int meta) {
        return this.staticModel.particleColorIndex(side);
    }
}
