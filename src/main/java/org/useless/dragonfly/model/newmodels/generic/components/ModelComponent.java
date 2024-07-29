package org.useless.dragonfly.model.newmodels.generic.components;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.model.newmodels.generic.ModelEntry;

public abstract class ModelComponent {
    public abstract void drawComponent(ModelEntry modelEntry, Tessellator tessellator, int meta, double x, double y, double z, float r, float g, float b, float brightness, float alpha);

    /**
     * @return True if something rendered, false otherwise
     */
    public abstract boolean drawComponentInWorld(BlockModel<?> model, ModelEntry modelEntry, Tessellator tessellator, WorldSource worldSource, Block block, int meta, int x, int y, int z, float r, float g, float b);
    public abstract boolean doOverbright();
}
