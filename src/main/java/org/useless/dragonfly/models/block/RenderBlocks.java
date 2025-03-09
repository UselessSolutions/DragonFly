package org.useless.dragonfly.models.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;

public final class RenderBlocks
{
	public static boolean ENABLE_DIRECTIONAL_LIGHTING = true;
	public static final float[] SIDE_LIGHT_MULTIPLIER = {0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f};
	public static final float FULL_CUBE_THRESHOLD = 1.0f / 16.0f;

    public final Minecraft mc;
    public IconCoordinate overrideBlockTexture;
    public boolean flipTexture;
    public boolean renderAllFaces;
    public byte renderBitMask;
    public boolean useInventoryTint;
    public int uvRotateEast;
    public int uvRotateWest;
    public int uvRotateSouth;
    public int uvRotateNorth;
    public int uvRotateTop;
    public int uvRotateBottom;
    public boolean enableAO;
    public float colorRedTopLeft;
    public float colorRedBottomLeft;
    public float colorRedBottomRight;
    public float colorRedTopRight;
    public float colorGreenTopLeft;
    public float colorGreenBottomLeft;
    public float colorGreenBottomRight;
    public float colorGreenTopRight;
    public float colorBlueTopLeft;
    public float colorBlueBottomLeft;
    public float colorBlueBottomRight;
    public float colorBlueTopRight;
    public int lightmapCoordTopLeft;
    public int lightmapCoordBottomLeft;
    public int lightmapCoordBottomRight;
    public int lightmapCoordTopRight;
    public boolean overbright;
    public LightingCache cache = new LightingCache();

    public RenderBlocks()
    {
        overrideBlockTexture = null;
        flipTexture = false;
        renderAllFaces = false;
        useInventoryTint = true;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        renderBitMask = 0;
        this.mc = Minecraft.getMinecraft();
    }

    public void resetRenderBlocks(){
        renderAllFaces = false;
        enableAO = false;
//        overbright = false;
        useInventoryTint = true;
        flipTexture = false;
        overrideBlockTexture = null;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        renderBitMask = 0;
    }

    public void setRenderSide(Side side, boolean shouldRender){
        if (!shouldRender){
            renderBitMask |= (byte) (1 << side.getId());
        } else {
            renderBitMask &= (byte) ~(1 << side.getId());
        }
    }

    public float getBlockBrightness(WorldSource blockAccess, int x, int y, int z)
    {
        if (mc.fullbright) return 1.0f;
        int id = blockAccess.getBlockId(x, y, z);
        Block<?> block = blockAccess.getBlock(x, y, z);
        if (block != null)
        {

            return block.getBlockBrightness(blockAccess, x, y, z);
        }

        return blockAccess.getBrightness(x, y, z, Blocks.lightEmission[id]);
    }

    public boolean renderStandardBlock(Tessellator tessellator, WorldSource worldSource, BlockModelStandard<?> blockModel, AABB bounds, int x, int y, int z)
    {
        int color = BlockColorDispatcher.getInstance().getDispatch(blockModel.block).getWorldColor(worldSource, x, y, z);
        float r = (float)(color >> 16 & 0xff) / 255F;
        float g = (float)(color >> 8 & 0xff) / 255F;
        float b = (float)(color & 0xff) / 255F;
        return renderStandardBlock(tessellator, worldSource, blockModel, bounds, x, y, z, r, g, b);
    }

    /**
     * @author CyborgCabbage
     * Thanks! :D
     */
    public boolean renderStandardBlock(Tessellator tessellator, WorldSource worldSource, BlockModelStandard<?> blockModel, AABB bounds, int x, int y, int z, float r, float g, float b) {
        this.enableAO = true;
        int meta = worldSource.getBlockMetadata(x, y, z);
        cache.setupCache(blockModel.block, worldSource, x, y, z);
        boolean somethingRendered = false;
        for (Side side : Side.sides){
            somethingRendered |= renderSide(tessellator, worldSource, blockModel, bounds, x, y, z, r, g, b, side, meta);
        }
        this.enableAO = false;
        return somethingRendered;
    }
    public boolean renderSide(Tessellator tessellator, WorldSource worldSource, BlockModelStandard<?> blockModel, AABB bounds, int x, int y, int z, float r, float g, float b, Side side, int meta){
        boolean useColor;
        switch (side){
            case BOTTOM:
                useColor = blockModel.shouldSideBeColored(worldSource, x, y, z, side.getId(), meta);
                return renderSide(tessellator, worldSource, blockModel, bounds, x, y, z,
                        useColor ? r : 1, useColor ? g : 1, useColor ? b : 1,side, meta,
                        0, -1, 0, (float)bounds.minY,
                        0, 0, 1, (float)bounds.maxZ, (float)bounds.minZ,
                        -1, 0, 0, 1-(float)bounds.minX, 1-(float)bounds.maxX);
            case TOP:
                useColor = blockModel.shouldSideBeColored(worldSource, x, y, z, side.getId(), meta);
                return renderSide(tessellator, worldSource, blockModel, bounds, x, y, z,
                        useColor ? r : 1, useColor ? g : 1, useColor ? b : 1,side, meta,
                        0,1,0, 1-(float)bounds.maxY,
                        0,0,1, (float)bounds.maxZ, (float)bounds.minZ,
                        1,0,0, (float)bounds.maxX, (float)bounds.minX);
            case NORTH:
                useColor = blockModel.shouldSideBeColored(worldSource, x, y, z, side.getId(), meta);
                return renderSide(tessellator, worldSource, blockModel, bounds, x, y, z,
                        useColor ? r : 1, useColor ? g : 1, useColor ? b : 1,side, meta,
                        0,0,-1, (float)bounds.minZ,
                        -1,0,0, 1-(float)bounds.minX, 1-(float)bounds.maxX,
                        0,1,0, (float)bounds.maxY, (float)bounds.minY);
            case SOUTH:
                useColor = blockModel.shouldSideBeColored(worldSource, x, y, z, side.getId(), meta);
                return renderSide(tessellator, worldSource, blockModel, bounds, x, y, z,
                        useColor ? r : 1, useColor ? g : 1, useColor ? b : 1,side, meta,
                        0,0,1, 1-(float)bounds.maxZ,
                        0,1,0, (float)bounds.maxY, (float)bounds.minY,
                        -1,0,0, 1-(float)bounds.minX, 1-(float)bounds.maxX);
            case WEST:
                useColor = blockModel.shouldSideBeColored(worldSource, x, y, z, side.getId(), meta);
                return renderSide(tessellator, worldSource, blockModel, bounds, x, y, z,
                        useColor ? r : 1, useColor ? g : 1, useColor ? b : 1,side, meta,
                        -1,0,0, (float)bounds.minX,
                        0,0,1, (float)bounds.maxZ, (float)bounds.minZ,
                        0,1,0, (float)bounds.maxY, (float)bounds.minY);
            case EAST:
                useColor = blockModel.shouldSideBeColored(worldSource, x, y, z, side.getId(), meta);
                return renderSide(tessellator, worldSource, blockModel, bounds, x, y, z,
                        useColor ? r : 1, useColor ? g : 1, useColor ? b : 1,side, meta,
                        1,0,0, 1-(float)bounds.maxX,
                        0,0,1, (float)bounds.maxZ, (float)bounds.minZ,
                        0,-1,0, 1-(float)bounds.minY, 1-(float)bounds.maxY);
            default:
                throw new IllegalArgumentException("Side " + side + " not expected!");
        }
    }

    /**
     * @author CyborgCabbage
     * Thanks! :D
     */
    public boolean renderSide(Tessellator tessellator, WorldSource worldSource, BlockModelStandard<?> blockModel, AABB bounds, int x, int y, int z, float r, float g, float b, @NotNull Side side, int meta,
                              int dirX, int dirY, int dirZ, float depth,
                              int topX, int topY, int topZ, float topP, float botP,
                              int lefX, int lefY, int lefZ, float lefP, float rigP
    ){
        IconCoordinate tex;
        if (this.overbright) {
            tex = blockModel.getBlockOverbrightTexture(worldSource, x, y, z, side.getId());
        } else {
            tex = blockModel.getBlockTexture(worldSource, x, y, z, side);
        }
        if (tex == null || ((renderBitMask >> side.getId()) & 1) != 0) return false;

        boolean rendered = false;
        if (this.renderAllFaces || blockModel.shouldSideBeRendered(worldSource, bounds, x+dirX, y+dirY, z+dirZ, side.getId(), meta)) {

            setupLighting(blockModel.block, worldSource, x, y, z, r, g, b, side,
                dirX, dirY, dirZ, depth,
                    topX, topY, topZ, topP, botP,
                    lefX, lefY, lefZ, lefP, rigP);

            switch (side) {
                case BOTTOM:
                    this.renderBottomFace(tessellator, bounds, x, y, z, tex);
                    break;
                case TOP:
                    this.renderTopFace(tessellator, bounds, x, y, z, tex);
                    break;
                case NORTH:
                    this.renderNorthFace(tessellator, bounds, x, y, z, tex);
                    break;
                case SOUTH:
                    this.renderSouthFace(tessellator, bounds, x, y, z, tex);
                    break;
                case WEST:
                    this.renderWestFace(tessellator, bounds, x, y, z, tex);
                    break;
                case EAST:
                    this.renderEastFace(tessellator, bounds, x, y, z, tex);
                    break;
            }
            rendered = true;
        }
        return rendered;
    }
    public void setupLighting(Block<?> block, WorldSource worldSource, int x, int y, int z, float r, float g, float b, @NotNull Side side,
                              int dirX, int dirY, int dirZ, float depth,
                              int topX, int topY, int topZ, float topP, float botP,
                              int lefX, int lefY, int lefZ, float lefP, float rigP){
    	boolean ao = mc.isAmbientOcclusionEnabled() && block.emission == 0;
        boolean isFullCube = depth <= FULL_CUBE_THRESHOLD;

    	if(LightmapHelper.isLightmapEnabled()) {
    		if(overbright) {
    			int dirX2 = dirX;
    			int dirY2 = dirY;
    			int dirZ2 = dirZ;

    			if(!isFullCube) {
    				dirX2 = 0;
    				dirY2 = 0;
    				dirZ2 = 0;
    			}

    			int lmc = LightmapHelper.getOverbrightLightmapCoord(worldSource.getSavedLightValue(LightLayer.Sky, dirX2, dirY2, dirZ2));

        		lightmapCoordTopLeft = lightmapCoordBottomLeft = lightmapCoordBottomRight = lightmapCoordTopRight = lmc;
    		}else {
        		if(ao) {
        			int dirX2 = dirX;
        			int dirY2 = dirY;
        			int dirZ2 = dirZ;

        			if(!isFullCube) {
        				dirX2 = 0;
        				dirY2 = 0;
        				dirZ2 = 0;
        			}

        			boolean topT = cache.getOpacity(dirX2 + topX, dirY2 + topY, dirZ2 + topZ);
        			boolean botT = cache.getOpacity(dirX2 - topX, dirY2 - topY, dirZ2 - topZ);
        			boolean lefT = cache.getOpacity(dirX2 + lefX, dirY2 + lefY, dirZ2 + lefZ);
        			boolean rigT = cache.getOpacity(dirX2 - lefX, dirY2 - lefY, dirZ2 - lefZ);

        			boolean topLefT = cache.getOpacity(dirX2 + topX + lefX, dirY2 + topY + lefY, dirZ2 + topZ + lefZ);
        			boolean topRigT = cache.getOpacity(dirX2 + topX - lefX, dirY2 + topY - lefY, dirZ2 + topZ - lefZ);
        			boolean botLefT = cache.getOpacity(dirX2 - topX + lefX, dirY2 - topY + lefY, dirZ2 - topZ + lefZ);
        			boolean botRigT = cache.getOpacity(dirX2 - topX - lefX, dirY2 - topY - lefY, dirZ2 - topZ - lefZ);

        			int lmcCen = cache.getLightmapCoord(dirX2, dirY2, dirZ2);

        			int lmcTop = topT ? lmcCen : cache.getLightmapCoord(dirX2 + topX, dirY2 + topY, dirZ2 + topZ);
        			int lmcBot = botT ? lmcCen : cache.getLightmapCoord(dirX2 - topX, dirY2 - topY, dirZ2 - topZ);
        			int lmcLef = lefT ? lmcCen : cache.getLightmapCoord(dirX2 + lefX, dirY2 + lefY, dirZ2 + lefZ);
        			int lmcRig = rigT ? lmcCen : cache.getLightmapCoord(dirX2 - lefX, dirY2 - lefY, dirZ2 - lefZ);

        			int lmcTopLef = topT && lefT ? lmcLef : (topLefT ? lmcCen : cache.getLightmapCoord(dirX2 + topX + lefX, dirY2 + topY + lefY, dirZ2 + topZ + lefZ));
        			int lmcBotLef = botT && lefT ? lmcLef : (botLefT ? lmcCen : cache.getLightmapCoord(dirX2 - topX + lefX, dirY2 - topY + lefY, dirZ2 - topZ + lefZ));
        			int lmcTopRig = topT && rigT ? lmcRig : (topRigT ? lmcCen : cache.getLightmapCoord(dirX2 + topX - lefX, dirY2 + topY - lefY, dirZ2 + topZ - lefZ));
        			int lmcBotRig = botT && rigT ? lmcRig : (botRigT ? lmcCen : cache.getLightmapCoord(dirX2 - topX - lefX, dirY2 - topY - lefY, dirZ2 - topZ - lefZ));

        			lightmapCoordTopLeft     = LightmapHelper.avg(lmcCen, lmcLef, lmcTop, lmcTopLef);
        			lightmapCoordTopRight    = LightmapHelper.avg(lmcCen, lmcRig, lmcTop, lmcTopRig);
        			lightmapCoordBottomLeft  = LightmapHelper.avg(lmcCen, lmcLef, lmcBot, lmcBotLef);
        			lightmapCoordBottomRight = LightmapHelper.avg(lmcCen, lmcRig, lmcBot, lmcBotRig);
        		}else {
        			int lmc;
                	if(!isFullCube) {
                		lmc = block.getLightmapCoord(worldSource, x, y, z);
                	}else {
                		lmc = block.getLightmapCoord(worldSource, x + dirX, y + dirY, z + dirZ);
                	}
            		lightmapCoordTopLeft = lightmapCoordBottomLeft = lightmapCoordBottomRight = lightmapCoordTopRight = lmc;
        		}
    		}
    	}

        float lightTR;
        float lightBR;
        float lightBL;
        float lightTL;

        if (this.overbright) {
            lightTR = 1.0F;
            lightBR = 1.0F;
            lightBL = 1.0F;
            lightTL = 1.0F;
        } else {
            if (ao) {
                {
                    float dirB = cache.getBrightness(dirX, dirY, dirZ);
                    boolean lefT = cache.getOpacity(dirX + lefX, dirY + lefY, dirZ + lefZ);
                    boolean botT = cache.getOpacity(dirX - topX, dirY - topY, dirZ - topZ);
                    boolean topT = cache.getOpacity(dirX + topX, dirY + topY, dirZ + topZ);
                    boolean rigT = cache.getOpacity(dirX - lefX, dirY - lefY, dirZ - lefZ);
                    float lB = cache.getBrightness(dirX + lefX, dirY + lefY, dirZ + lefZ);
                    float bB = cache.getBrightness(dirX - topX, dirY - topY, dirZ - topZ);
                    float tB = cache.getBrightness(dirX + topX, dirY + topY, dirZ + topZ);
                    float rB = cache.getBrightness(dirX - lefX, dirY - lefY, dirZ - lefZ);
                    float blB = botT && lefT ? lB : cache.getBrightness(dirX + lefX - topX, dirY + lefY - topY, dirZ + lefZ - topZ);
                    float tlB = topT && lefT ? lB : cache.getBrightness(dirX + lefX + topX, dirY + lefY + topY, dirZ + lefZ + topZ);
                    float brB = botT && rigT ? rB : cache.getBrightness(dirX - lefX - topX, dirY - lefY - topY, dirZ - lefZ - topZ);
                    float trB = topT && rigT ? rB : cache.getBrightness(dirX - lefX + topX, dirY - lefY + topY, dirZ - lefZ + topZ);
                    lightTL = (tlB + lB + tB + dirB) / 4.0F;
                    lightTR = (tB + dirB + trB + rB) / 4.0F;
                    lightBR = (dirB + bB + rB + brB) / 4.0F;
                    lightBL = (lB + blB + dirB + bB) / 4.0F;
                }
                if(!isFullCube){
                    float dirB = cache.getBrightness(0, 0, 0);
                    boolean lefT = cache.getOpacity(lefX, lefY, lefZ);
                    boolean botT = cache.getOpacity(-topX, -topY, -topZ);
                    boolean topT = cache.getOpacity(topX, topY, topZ);
                    boolean rigT = cache.getOpacity(-lefX, -lefY, -lefZ);
                    float lB = cache.getBrightness(lefX, lefY, lefZ);
                    float bB = cache.getBrightness(-topX, -topY, -topZ);
                    float tB = cache.getBrightness(topX, topY, topZ);
                    float rB = cache.getBrightness(-lefX, -lefY, -lefZ);
                    float blB = botT && lefT ? lB : cache.getBrightness(lefX - topX, lefY - topY, lefZ - topZ);
                    float tlB = topT && lefT ? lB : cache.getBrightness(lefX + topX, lefY + topY, lefZ + topZ);
                    float brB = botT && rigT ? rB : cache.getBrightness(-lefX - topX, -lefY - topY, -lefZ - topZ);
                    float trB = topT && rigT ? rB : cache.getBrightness(-lefX + topX, -lefY + topY, -lefZ + topZ);
                    lightTL = (tlB + lB + tB + dirB) / 4.0F * depth + lightTL*(1-depth);
                    lightTR = (tB + dirB + trB + rB) / 4.0F * depth + lightTR*(1-depth);
                    lightBR = (dirB + bB + rB + brB) / 4.0F * depth + lightBR*(1-depth);
                    lightBL = (lB + blB + dirB + bB) / 4.0F * depth + lightBL*(1-depth);
                }
            } else {
                int dirX2 = dirX;
                int dirY2 = dirY;
                int dirZ2 = dirZ;

                if(!isFullCube) {
                    dirX2 = 0;
                    dirY2 = 0;
                    dirZ2 = 0;
                }

                float brightness = cache.getBrightness(dirX2, dirY2, dirZ2);
                lightTL = lightBL = lightBR = lightTR = brightness;
            }
        }

        if (overbright || !RenderBlocks.ENABLE_DIRECTIONAL_LIGHTING)
        {
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b;
        }
        else
        {
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r * SIDE_LIGHT_MULTIPLIER[side.getId()];
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g * SIDE_LIGHT_MULTIPLIER[side.getId()];
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b * SIDE_LIGHT_MULTIPLIER[side.getId()];
        }
        float tl = topP * lightTL + (1 - topP) * lightBL;
        float tr = topP * lightTR + (1 - topP) * lightBR;
        float bl = botP * lightTL + (1 - botP) * lightBL;
        float br = botP * lightTR + (1 - botP) * lightBR;
        float ltl = lefP * tl + (1 - lefP) * tr;
        float lbl = lefP * bl + (1 - lefP) * br;
        float lbr = rigP * bl + (1 - rigP) * br;
        float ltr = rigP * tl + (1 - rigP) * tr;
        this.colorRedTopLeft *= ltl;
        this.colorGreenTopLeft *= ltl;
        this.colorBlueTopLeft *= ltl;

        this.colorRedBottomLeft *= lbl;
        this.colorGreenBottomLeft *= lbl;
        this.colorBlueBottomLeft *= lbl;

        this.colorRedBottomRight *= lbr;
        this.colorGreenBottomRight *= lbr;
        this.colorBlueBottomRight *= lbr;

        this.colorRedTopRight *= ltr;
        this.colorGreenTopRight *= ltr;
        this.colorBlueTopRight *= ltr;
    }

    public void renderBottomFace(Tessellator tessellator, AABB bounds, double x, double y, double z, IconCoordinate tex)
    {
        if(overrideBlockTexture != null) tex = overrideBlockTexture;
        if (tex == null) return;
        double d3 = tex.getSubIconU(bounds.minX);
        double d4 = tex.getSubIconU(bounds.maxX);
        double d5 = tex.getSubIconV(bounds.minZ);
        double d6 = tex.getSubIconV(bounds.maxZ);
        if(bounds.minX < 0.0D || bounds.maxX > 1.0D)
        {
            d3 = tex.getIconUMin();
            d4 = tex.getIconUMax();
        }
        if(bounds.minZ < 0.0D || bounds.maxZ > 1.0D)
        {
            d5 = tex.getIconVMin();
            d6 = tex.getIconVMax();
        }
        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;
        if(uvRotateBottom == 2)
        {
            d3 = tex.getSubIconU(bounds.minZ);
            d5 = tex.getSubIconV(1 - bounds.maxX);
            d4 = tex.getSubIconU(bounds.maxZ);
            d6 = tex.getSubIconV(1 - bounds.minX);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else
        if(uvRotateBottom == 1)
        {
            d3 = tex.getSubIconU(1 - bounds.maxZ);
            d5 = tex.getSubIconV(bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.minZ);
            d6 = tex.getSubIconV(bounds.maxX);
            d7 = d4;
            d8 = d3;
            d3 = d7;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else
        if(uvRotateBottom == 3)
        {
            d3 = tex.getSubIconU(1 - bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.maxX);
            d5 = tex.getSubIconV(1 - bounds.minZ);
            d6 = tex.getSubIconV(1 - bounds.maxZ);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }
        double d11 = x + bounds.minX;
        double d12 = x + bounds.maxX;
        double d13 = y + bounds.minY;
        double d14 = z + bounds.minZ;
        double d15 = z + bounds.maxZ;
        if(enableAO)
        {
        	if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        } else
        {
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        }
    }

    public void renderTopFace(Tessellator tessellator, AABB bounds, double x, double y, double z, IconCoordinate tex)
    {
        if(overrideBlockTexture != null) tex = overrideBlockTexture;
        if (tex == null) return;
        double d3 = tex.getSubIconU(bounds.minX);
        double d4 = tex.getSubIconU(bounds.maxX);
        double d5 = tex.getSubIconV(bounds.minZ);
        double d6 = tex.getSubIconV(bounds.maxZ);
        if(bounds.minX < 0.0D || bounds.maxX > 1.0D)
        {
            d3 = tex.getIconUMin();
            d4 = tex.getIconUMax();
        }
        if(bounds.minZ < 0.0D || bounds.maxZ > 1.0D)
        {
            d5 = tex.getIconVMin();
            d6 = tex.getIconVMax();
        }
        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;
        if(uvRotateTop == 1)
        {
            d3 = tex.getSubIconU(bounds.minZ);
            d5 = tex.getSubIconV(1 - bounds.maxX);
            d4 = tex.getSubIconU(bounds.maxZ);
            d6 = tex.getSubIconV(1 - bounds.minX);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else
        if(uvRotateTop == 2)
        {
            d3 = tex.getSubIconU(1 - bounds.maxZ);
            d5 = tex.getSubIconV(bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.minZ);
            d6 = tex.getSubIconV(bounds.maxX);
            d7 = d4;
            d8 = d3;
            d3 = d7;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else
        if(uvRotateTop == 3)
        {
            d3 = tex.getSubIconU(1 - bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.maxX);
            d5 = tex.getSubIconV(1 - bounds.minZ);
            d6 = tex.getSubIconV(1 - bounds.maxZ);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }
        double d11 = x + bounds.minX;
        double d12 = x + bounds.maxX;
        double d13 = y + bounds.maxY;
        double d14 = z + bounds.minZ;
        double d15 = z + bounds.maxZ;
        if(enableAO)
        {
        	if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        } else
        {
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        }
    }

    public void renderNorthFace(Tessellator tessellator, AABB bounds, double x, double y, double z, IconCoordinate tex)
    {
        if(overrideBlockTexture != null) tex = overrideBlockTexture;
        if (tex == null) return;
        double d3 = tex.getSubIconU(bounds.minX);
        double d4 = tex.getSubIconU(bounds.maxX);
        double d5 = tex.getSubIconV(1 - bounds.maxY);
        double d6 = tex.getSubIconV(1 - bounds.minY);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(bounds.minX < 0.0D || bounds.maxX > 1.0D)
        {
            d3 = tex.getIconUMin();
            d4 = tex.getIconUMax();
        }
        if(bounds.minY < 0.0D || bounds.maxY > 1.0D)
        {
            d5 = tex.getIconVMin();
            d6 = tex.getIconVMax();
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateEast == 2)
        {
            d3 = tex.getSubIconU(bounds.minY);
            d5 = tex.getSubIconV(1 - bounds.minX);
            d4 = tex.getSubIconU(bounds.maxY);
            d6 = tex.getSubIconV(1 - bounds.maxX);
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateEast == 1)
        {
            d3 = tex.getSubIconU(1 - bounds.maxY);
            d5 = tex.getSubIconV(bounds.maxX);
            d4 = tex.getSubIconU(1 - bounds.minY);
            d6 = tex.getSubIconV(bounds.minX);
            d8 = d4;
            d9 = d3;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateEast == 3)
        {
            d3 = tex.getSubIconU(1 - bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.maxX);
            d5 = tex.getSubIconV(bounds.maxY);
            d6 = tex.getSubIconV(bounds.minY);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = x + bounds.minX;
        double d13 = x + bounds.maxX;
        double d14 = y + bounds.minY;
        double d15 = y + bounds.maxY;
        double d16 = z + bounds.minZ;
        if(enableAO)
        {
        	if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
        } else
        {
            tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
            tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
            tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
            tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
        }
    }

    public void renderSouthFace(Tessellator tessellator, AABB bounds, double x, double y, double z, IconCoordinate tex)
    {
        if(overrideBlockTexture != null) tex = overrideBlockTexture;
        if (tex == null) return;
        double d3 = tex.getSubIconU(bounds.minX);
        double d4 = tex.getSubIconU(bounds.maxX);
        double d5 = tex.getSubIconV(1 - bounds.maxY);
        double d6 = tex.getSubIconV(1 - bounds.minY);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(bounds.minX < 0.0D || bounds.maxX > 1.0D)
        {
            d3 = tex.getIconUMin();
            d4 = tex.getIconUMax();
        }
        if(bounds.minY < 0.0D || bounds.maxY > 1.0D)
        {
            d5 = tex.getIconVMin();
            d6 = tex.getIconVMax();
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateWest == 1)
        {
            d3 = tex.getSubIconU(bounds.minY);
            d6 = tex.getSubIconV(1 - bounds.minX);
            d4 = tex.getSubIconU(bounds.maxY);
            d5 = tex.getSubIconV(1 - bounds.maxX);
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateWest == 2)
        {
            d3 = tex.getSubIconU(1 - bounds.maxY);
            d5 = tex.getSubIconV(bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.minY);
            d6 = tex.getSubIconV(bounds.maxX);
            d8 = d4;
            d9 = d3;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateWest == 3)
        {
            d3 = tex.getSubIconU(1 - bounds.minX);
            d4 = tex.getSubIconU(1 - bounds.maxX);
            d5 = tex.getSubIconV(bounds.maxY);
            d6 = tex.getSubIconV(bounds.minY);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double x0 = x + bounds.minX;
        double x1 = x + bounds.maxX;
        double y0 = y + bounds.minY;
        double y1 = y + bounds.maxY;
        double z0 = z + bounds.maxZ;
        if(enableAO)
        {
        	if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(x0, y1, z0, d3, d5);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(x0, y0, z0, d9, d11);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(x1, y0, z0, d4, d6);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(x1, y1, z0, d8, d10);
        } else
        {
            tessellator.addVertexWithUV(x0, y1, z0, d3, d5);
            tessellator.addVertexWithUV(x0, y0, z0, d9, d11);
            tessellator.addVertexWithUV(x1, y0, z0, d4, d6);
            tessellator.addVertexWithUV(x1, y1, z0, d8, d10);
        }
    }

    public void renderWestFace(Tessellator tessellator, AABB bounds, double x, double y, double z, IconCoordinate tex)
    {
        if(overrideBlockTexture != null) tex = overrideBlockTexture;
        if (tex == null) return;
        double d3 = tex.getSubIconU(bounds.minZ);
        double d4 = tex.getSubIconU(bounds.maxZ);
        double d5 = tex.getSubIconV(1 - bounds.maxY);
        double d6 = tex.getSubIconV(1 - bounds.minY);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(bounds.minZ < 0.0D || bounds.maxZ > 1.0D)
        {
            d3 = tex.getIconUMin();
            d4 = tex.getIconUMax();
        }
        if(bounds.minY < 0.0D || bounds.maxY > 1.0D)
        {
            d5 = tex.getIconVMin();
            d6 = tex.getIconVMax();
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateNorth == 1)
        {
            d3 = tex.getSubIconU(bounds.minY);
            d5 = tex.getSubIconV(1 - bounds.maxZ);
            d4 = tex.getSubIconU(bounds.maxY);
            d6 = tex.getSubIconV(1 - bounds.minZ);
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateNorth == 2)
        {
            d3 = tex.getSubIconU(1 - bounds.maxY);
            d5 = tex.getSubIconV(bounds.minZ);
            d4 = tex.getSubIconU(1 - bounds.minY);
            d6 = tex.getSubIconV(bounds.maxZ);
            d8 = d4;
            d9 = d3;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateNorth == 3)
        {
            d3 = tex.getSubIconU(1 - bounds.minZ);
            d4 = tex.getSubIconU(1 - bounds.maxZ);
            d5 = tex.getSubIconV(bounds.maxY);
            d6 = tex.getSubIconV(bounds.minY);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = x + bounds.minX;
        double d13 = y + bounds.minY;
        double d14 = y + bounds.maxY;
        double d15 = z + bounds.minZ;
        double d16 = z + bounds.maxZ;
        if(enableAO)
        {
        	if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
        } else
        {
            tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
            tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
        }
    }

    public void renderEastFace(Tessellator tessellator, AABB bounds, double x, double y, double z, IconCoordinate tex)
    {
        if(overrideBlockTexture != null) tex = overrideBlockTexture;
        if (tex == null) return;
        double uMin = tex.getSubIconU(bounds.minZ);
        double uMax = tex.getSubIconU(bounds.maxZ);
        double vMin = tex.getSubIconV(1 - bounds.maxY);
        double vMax = tex.getSubIconV(1 - bounds.minY);
        if(flipTexture)
        {
            double _uMin = uMin;
            uMin = uMax;
            uMax = _uMin;
        }
        if(bounds.minZ < 0.0D || bounds.maxZ > 1.0D)
        {
            uMin = tex.getIconUMin();
            uMax = tex.getIconUMax();
        }
        if(bounds.minY < 0.0D || bounds.maxY > 1.0D)
        {
            vMin = tex.getIconVMin();
            vMax = tex.getIconVMax();
        }
        double uMax2 = uMax;
        double uMin2 = uMin;
        double vMin2 = vMin;
        double vMax2 = vMax;
        if(uvRotateSouth == 2)
        {
            uMin = tex.getSubIconU(bounds.minY);
            vMin = tex.getSubIconV(1 - bounds.minZ);
            uMax = tex.getSubIconU(bounds.maxY);
            vMax = tex.getSubIconV(1 - bounds.maxZ);
            vMin2 = vMin;
            vMax2 = vMax;
            uMax2 = uMin;
            uMin2 = uMax;
            vMin = vMax;
            vMax = vMin2;
        } else
        if(uvRotateSouth == 1)
        {
            uMin = tex.getSubIconU(1 - bounds.maxY);
            vMin = tex.getSubIconV(bounds.maxZ);
            uMax = tex.getSubIconU(1 - bounds.minY);
            vMax = tex.getSubIconV(bounds.minZ);
            uMax2 = uMax;
            uMin2 = uMin;
            uMin = uMax2;
            uMax = uMin2;
            vMin2 = vMax;
            vMax2 = vMin;
        } else
        if(uvRotateSouth == 3)
        {
            uMin = tex.getSubIconU(1 - bounds.minZ);
            uMax = tex.getSubIconU(1 - bounds.maxZ);
            vMin = tex.getSubIconV(bounds.maxY);
            vMax = tex.getSubIconV(bounds.minY);
            uMax2 = uMax;
            uMin2 = uMin;
            vMin2 = vMin;
            vMax2 = vMax;
        }
        double xMax = x + bounds.maxX;
        double yMin = y + bounds.minY;
        double yMax = y + bounds.maxY;
        double zMin = z + bounds.minZ;
        double zMax = z + bounds.maxZ;

        if(enableAO)
        {
        	if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(xMax, yMin, zMax, uMin2, vMax2);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(xMax, yMin, zMin, uMax, vMax);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(xMax, yMax, zMin, uMax2, vMin2);
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(xMax, yMax, zMax, uMin, vMin);
        } else
        {
            tessellator.addVertexWithUV(xMax, yMin, zMax, uMin2, vMax2);
            tessellator.addVertexWithUV(xMax, yMin, zMin, uMax, vMax);
            tessellator.addVertexWithUV(xMax, yMax, zMin, uMax2, vMin2);
            tessellator.addVertexWithUV(xMax, yMax, zMax, uMin, vMin);
        }
    }
}
