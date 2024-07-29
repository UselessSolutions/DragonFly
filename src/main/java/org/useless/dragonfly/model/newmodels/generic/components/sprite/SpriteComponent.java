package org.useless.dragonfly.model.newmodels.generic.components.sprite;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.model.newmodels.generic.ModelEntry;
import org.useless.dragonfly.model.newmodels.generic.components.ModelComponent;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class SpriteComponent extends ModelComponent {
    public final SpriteEntry[] spriteEntries;
    private final boolean hasOverbright;
    public SpriteComponent(SpriteEntry ... entries){
        this.spriteEntries = entries;

        boolean _overbright = false;
        for (SpriteEntry e : entries){
            if (e.overbright){
                _overbright = true;
                break;
            }
        }
        hasOverbright = _overbright;
    }
    @Override
    public void drawComponent(ModelEntry modelEntry, Tessellator tessellator, int meta, double x, double y, double z, float r, float g, float b, float brightness, float alpha) {

    }

    @Override
    public boolean drawComponentInWorld(BlockModel<?> model, ModelEntry modelEntry, Tessellator tessellator, WorldSource worldSource, Block block, int meta, int x, int y, int z, float r, float g, float b) {
        throw new UnsupportedOperationException();
    }
    private void renderSprite3D(Tessellator tessellator, IconCoordinate tex, double x, double y, double z, float r, float g, float b, float brightness, float alpha){
        if (tex == null) return;

        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);

        tex.parentAtlas.bindTexture();
        int tileWidth = tex.width;

        float uMin = (float) tex.getIconUMin();
        float uMax = (float) tex.getIconUMax();
        float vMin = (float) tex.getIconVMin();
        float vMax = (float) tex.getIconVMax();
        float uDiff = uMin - uMax;
        float vDiff = vMin - vMax;
        final float width = 1.0F;

        final float foon = (0.5f / 512);
        final float goon = 0.0625f * (16.0f / tileWidth);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        final float thickness = 0.0625f;
        final float pixelWidth = 1f/tileWidth;

        // Back Face
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(x + 0.0D,  y + 0.0D, z + 0.0D, uMax, vMax);
        tessellator.addVertexWithUV(x + width, y + 0.0D, z + 0.0D, uMin, vMax);
        tessellator.addVertexWithUV(x + width, y + 1.0D, z + 0.0D, uMin, vMin);
        tessellator.addVertexWithUV(x + 0.0D,  y + 1.0D, z + 0.0D, uMax, vMin);
        tessellator.draw();

        // Front Face
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertexWithUV(x + 0.0D,  y + 1.0D, z - thickness, uMax, vMin);
        tessellator.addVertexWithUV(x + width, y + 1.0D, z - thickness, uMin, vMin);
        tessellator.addVertexWithUV(x + width, y + 0.0D, z - thickness, uMin, vMax);
        tessellator.addVertexWithUV(x + 0.0D,  y + 0.0D, z - thickness, uMax, vMax);
        tessellator.draw();

        // Right Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        for(int i = 0; i < tileWidth; i++)
        {
            double texProgress = i * pixelWidth;
            double u = (uMax + uDiff * texProgress) - foon;
            double _x = width * texProgress;
            tessellator.addVertexWithUV(_x + x, 0.0D + y,    -thickness + z, u, vMax);
            tessellator.addVertexWithUV(_x + x, 0.0D + y,    0.0D       + z, u, vMax);
            tessellator.addVertexWithUV(_x + x, 1.0D + y,    0.0D       + z, u, vMin);
            tessellator.addVertexWithUV(_x + x, 1.0D + y,    -thickness + z, u, vMin);
        }
        tessellator.draw();

        // Left Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        for(int i = 0; i < tileWidth; i++)
        {
            double texProgress = i * pixelWidth;
            double u = (uMax + uDiff * texProgress) - foon;
            double _x = width * texProgress + goon;
            tessellator.addVertexWithUV(_x + x, 1.0D + y,    -thickness + z, u, vMin);
            tessellator.addVertexWithUV(_x + x, 1.0D + y,    0.0D       + z, u, vMin);
            tessellator.addVertexWithUV(_x + x, 0.0D + y,    0.0D       + z, u, vMax);
            tessellator.addVertexWithUV(_x + x, 0.0D + y,    -thickness + z, u, vMax);
        }
        tessellator.draw();

        // Top Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        for(int i = 0; i < tileWidth; i++)
        {
            double texProgress = i * pixelWidth;
            double v = (vMax + vDiff * texProgress) - foon;
            double _y = width * texProgress + goon;
            tessellator.addVertexWithUV(0.0D  + x,  _y + y,  0.0D       + z, uMax, v);
            tessellator.addVertexWithUV(width + x,  _y + y,  0.0D       + z, uMin, v);
            tessellator.addVertexWithUV(width + x,  _y + y,  -thickness + z, uMin, v);
            tessellator.addVertexWithUV(0.0D  + x,  _y + y,  -thickness + z, uMax, v);
        }
        tessellator.draw();

        // Bottom Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        for(int i = 0; i < tileWidth; i++)
        {
            double texProgress = i * pixelWidth;
            double v = (vMax + vDiff * texProgress) - foon;
            double _y = width * texProgress;
            tessellator.addVertexWithUV(width + x,  _y + y, 0.0D,        uMin, v);
            tessellator.addVertexWithUV(0.0D  + x,  _y + y, 0.0D,        uMax, v);
            tessellator.addVertexWithUV(0.0D  + x,  _y + y, -thickness,  uMax, v);
            tessellator.addVertexWithUV(width + x,  _y + y, -thickness,  uMin, v);
        }
        tessellator.draw();

        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glDisable(GL_BLEND);
    }

    @Override
    public boolean doOverbright() {
        return hasOverbright;
    }

}
