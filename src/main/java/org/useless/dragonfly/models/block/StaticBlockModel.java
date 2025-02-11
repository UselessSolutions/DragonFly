package org.useless.dragonfly.models.block;

import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.DisplayPos;

public interface StaticBlockModel {

    /**
     * @return True if anything rendered, false otherwise
     */
    boolean renderStandalone(@NotNull BlockModel<? extends BlockLogic> sourceModel, @NotNull Tessellator tessellator, double x, double y, double z, int metadata, @NotNull BlockColor color, float brightness, float alpha);

    /**
     * @return True if anything rendered, false otherwise
     */
    boolean renderAttached(@NotNull BlockModel<? extends BlockLogic> sourceModel, @NotNull Tessellator tessellator, @NotNull WorldSource worldSource, int x, int y, int z, double xOff, double yOff, double zOff, boolean cullFaces, @Nullable IconCoordinate overrideTexture);

    /**
     * @return Block texture to overlay when inside a block
     */
    @Nullable IconCoordinate getOverlay();

    /**
     * @return Block texture to use for particles when being broken from a specific side
     */
    @Nullable IconCoordinate getParticle(@NotNull Side side);

    /**
     * @return Color index for particle to use from side, default -1 for no color
     */
    int particleColorIndex(@NotNull Side side);

    /**
     * @return Return the render pass to render the model on
     */
    int renderLayer();

    /**
     * @return Return the display position to use for the given id
     */
    @NotNull DisplayPos getItemDisplayPos(@NotNull String id);
}
