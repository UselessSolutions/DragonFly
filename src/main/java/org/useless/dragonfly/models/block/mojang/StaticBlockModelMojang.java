package org.useless.dragonfly.models.block.mojang;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element;
import org.useless.dragonfly.models.block.LightingCache;
import org.useless.dragonfly.models.block.StaticBlockModel;

import java.util.Locale;

import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.FLOATS_PER_NORMAL;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.FLOATS_PER_VERTEX;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.NORMAL_FLOAT_X;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.NORMAL_FLOAT_Y;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.NORMAL_FLOAT_Z;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_BOTTOM_LEFT;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_BOTTOM_RIGHT;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_FLOAT_U;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_FLOAT_V;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_FLOAT_X;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_FLOAT_Y;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_FLOAT_Z;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_TOP_LEFT;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_TOP_RIGHT;

public class StaticBlockModelMojang implements StaticBlockModel {
    public static boolean ENABLE_DIRECTIONAL_LIGHTING = true;
    public static final float[] SIDE_LIGHT_MULTIPLIER = {0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f};
    public static final float FULL_CUBE_THRESHOLD = 1.0f / 16.0f;

    protected static final Minecraft MC = Minecraft.getMinecraft();
    protected static final LightingCache LIGHTING_CACHE = new LightingCache();

    protected static final byte[] V_TOP_X = new byte[] {0, 0, -1, 0, 0, 0};
    protected static final byte[] V_TOP_Y = new byte[] {0, 0, 0, 1, 0, 0};
    protected static final byte[] V_TOP_Z = new byte[] {1, 1, 0, 0, 1, 1};

    protected static final byte[] V_LEF_X = new byte[] {-1, 1, 0, -1, 0, 0};
    protected static final byte[] V_LEF_Y = new byte[] {0, 0, 1, 0, 1, -1};
    protected static final byte[] V_LEF_Z = new byte[] {0, 0, 0, 0, 0, 0};

    protected final CompiledBlockModelMojangData compiled;
    public StaticBlockModelMojang(BlockModelMojangData data) {
        this.compiled = new CompiledBlockModelMojangData(data);
    }

    @Override
    public boolean renderStandalone(@NotNull BlockModel<? extends BlockLogic> sourceModel, @NotNull Tessellator tessellator, double x, double y, double z, int metadata, @NotNull BlockColor color, float brightness, float alpha) {
        if (compiled.elements.length == 0) return false;

        GL11.glPushMatrix();
        for (int i = 0; i < compiled.elements.length; i++) {
            C_Element element = compiled.elements[i];

            if (element.shade) {
                GL11.glEnable(GL11.GL_LIGHTING);
            } else {
                GL11.glDisable(GL11.GL_LIGHTING);
            }

            tessellator.startDrawingQuads();
            for (int face = 0; face < element.faces; face++) {
                IconCoordinate coordinate = compiled.textures.getOrDefault(element.textures[face], BlockModelStandard.BLOCK_TEXTURE_UNASSIGNED);

                // Todo fix this needing to be hardcoded to get overbright looking right
                if (LightmapHelper.isLightmapEnabled() && element.lightEmission == 15) {
                    brightness = 1f;
                    LightmapHelper.setLightmapCoord(15, 15);
                } else {
                    brightness = Math.max(brightness, element.lightEmission/15f);
                }

                float r = 1;
                float g = 1;
                float b = 1;
                if (element.tintIndices[face] >= 0) {
                    int c = color.getFallbackColor(metadata/*, element.tintIndices[face]*/);
                    r = Color.redFromInt(c) / 255f;
                    g = Color.greenFromInt(c) / 255f;
                    b = Color.blueFromInt(c) / 255f;
                }

                final int offset = face * 4 * FLOATS_PER_VERTEX;
                for (int v = 0; v < 4; v++) {
                    tessellator.setNormal(element.normalData[face * FLOATS_PER_NORMAL + NORMAL_FLOAT_X], element.normalData[face * FLOATS_PER_NORMAL + NORMAL_FLOAT_Y], element.normalData[face * FLOATS_PER_NORMAL + NORMAL_FLOAT_Z]);
                    tessellator.setColorOpaque_F(r * brightness, g * brightness, b * brightness);
                    tessellator.addVertexWithUV(
                        element.vertexData[offset + v * FLOATS_PER_VERTEX + VERTEX_FLOAT_X] - 0.5f,
                        element.vertexData[offset + v * FLOATS_PER_VERTEX + VERTEX_FLOAT_Y] - 0.5f,
                        element.vertexData[offset + v * FLOATS_PER_VERTEX + VERTEX_FLOAT_Z] - 0.5f,
                        coordinate.getSubIconU(element.vertexData[offset + v * FLOATS_PER_VERTEX + VERTEX_FLOAT_U]),
                        coordinate.getSubIconV(element.vertexData[offset + v * FLOATS_PER_VERTEX + VERTEX_FLOAT_V]));
                }
            }
            tessellator.draw();
        }
        GL11.glPopMatrix();
        return true;
    }

    @Override
    public boolean renderAttached(@NotNull BlockModel<? extends BlockLogic> sourceModel, @NotNull Tessellator tessellator, @NotNull WorldSource worldSource, int x, int y, int z, double xOff, double yOff, double zOff, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
        if (compiled.elements.length == 0) return false;
        boolean didRender = false;
        LIGHTING_CACHE.setupCache(sourceModel.block, worldSource, x, y, z);

        BlockColor color = BlockColorDispatcher.getInstance().getDispatch(sourceModel.block);
        for (int i = 0; i < compiled.elements.length; i++) {
            final C_Element element = compiled.elements[i];
            for (int face = 0; face < element.faces; face++) {
                if (cullFaces && cullSide(worldSource, x, y, z, element.cullfaces[face])) continue;
                final IconCoordinate coordinate = overrideTexture == null ? compiled.textures.getOrDefault(element.textures[face], BlockModelStandard.BLOCK_TEXTURE_UNASSIGNED) : overrideTexture;
                final Direction direction = element.directions[face];

                final float minBrightness = element.lightEmission/15f;

                final int topX = V_TOP_X[direction.getId()];
                final int topY = V_TOP_Y[direction.getId()];
                final int topZ = V_TOP_Z[direction.getId()];
                final float topP;
                final float botP;
                final int lefX = V_LEF_X[direction.getId()];
                final int lefY = V_LEF_Y[direction.getId()];
                final int lefZ = V_LEF_Z[direction.getId()];
                final float lefP;
                final float rigP;
                final float depth;
                switch (direction) {
                    case DOWN:
                        depth = 0 + element.minY;
                        topP = element.maxZ; botP = element.minZ;
                        lefP = 1 - element.minX; rigP = 1 - element.maxX;
                        break;
                    case UP:
                        depth = 1 - element.maxY;
                        topP = element.maxZ; botP = element.minZ;
                        lefP = element.maxX; rigP = element.minX;
                        break;
                    case NORTH:
                        depth = 0 + element.minZ;
                        topP = 1 - element.minX; botP = 1 - element.maxX;
                        lefP = element.maxY; rigP = element.minY;
                        break;
                    case SOUTH:
                        depth = 1 - element.maxZ;
                        topP = element.maxY; botP = element.minY;
                        lefP = 1 - element.minX; rigP = 1 - element.maxX;
                        break;
                    case WEST:
                        depth = 0 + element.minX;
                        topP = element.maxZ; botP = element.minZ;
                        lefP = element.maxY; rigP = element.minY;
                        break;
                    case EAST:
                    default:
                        depth = 1 - element.maxX;
                        topP = element.maxZ; botP = element.minZ;
                        lefP = 1 - element.minY; rigP = 1 - element.maxY;
                        break;
                }

                final boolean useAO = compiled.data.ambientOcclusion && MC.isAmbientOcclusionEnabled() && element.shade;
                final boolean isFullCube = depth <= FULL_CUBE_THRESHOLD && element.shade;

                final float r;
                final float g;
                final float b;
                if (element.tintIndices[face] >= 0) {
                    int c = color.getWorldColor(worldSource, x, y, z/*, element.tintIndices[face]*/);
                    r = Color.redFromInt(c) / 255f;
                    g = Color.greenFromInt(c) / 255f;
                    b = Color.blueFromInt(c) / 255f;
                } else {
                    r = 1;
                    g = 1;
                    b = 1;
                }

                final int dirX = direction.getOffsetX();
                final int dirY = direction.getOffsetY();
                final int dirZ = direction.getOffsetZ();

                final int lightmapCoordTopLeft;
                final int lightmapCoordBottomLeft;
                final int lightmapCoordBottomRight;
                final int lightmapCoordTopRight;

                if(LightmapHelper.isLightmapEnabled()) {
                    if (useAO) {
                        final int dirX2;
                        final int dirY2;
                        final int dirZ2;
                        if (!isFullCube) {
                            dirX2 = 0;
                            dirY2 = 0;
                            dirZ2 = 0;
                        } else {
                            dirX2 = dirX;
                            dirY2 = dirY;
                            dirZ2 = dirZ;
                        }

                        final boolean topT = LIGHTING_CACHE.getOpacity(dirX2 + topX, dirY2 + topY, dirZ2 + topZ);
                        final boolean botT = LIGHTING_CACHE.getOpacity(dirX2 - topX, dirY2 - topY, dirZ2 - topZ);
                        final boolean lefT = LIGHTING_CACHE.getOpacity(dirX2 + lefX, dirY2 + lefY, dirZ2 + lefZ);
                        final boolean rigT = LIGHTING_CACHE.getOpacity(dirX2 - lefX, dirY2 - lefY, dirZ2 - lefZ);

                        final boolean topLefT = LIGHTING_CACHE.getOpacity(dirX2 + topX + lefX, dirY2 + topY + lefY, dirZ2 + topZ + lefZ);
                        final boolean topRigT = LIGHTING_CACHE.getOpacity(dirX2 + topX - lefX, dirY2 + topY - lefY, dirZ2 + topZ - lefZ);
                        final boolean botLefT = LIGHTING_CACHE.getOpacity(dirX2 - topX + lefX, dirY2 - topY + lefY, dirZ2 - topZ + lefZ);
                        final boolean botRigT = LIGHTING_CACHE.getOpacity(dirX2 - topX - lefX, dirY2 - topY - lefY, dirZ2 - topZ - lefZ);

                        final int lmcCen = getLightmap(dirX2, dirY2, dirZ2, element.lightEmission);

                        final int lmcTop = topT ? lmcCen : getLightmap(dirX2 + topX, dirY2 + topY, dirZ2 + topZ, element.lightEmission);
                        final int lmcBot = botT ? lmcCen : getLightmap(dirX2 - topX, dirY2 - topY, dirZ2 - topZ, element.lightEmission);
                        final int lmcLef = lefT ? lmcCen : getLightmap(dirX2 + lefX, dirY2 + lefY, dirZ2 + lefZ, element.lightEmission);
                        final int lmcRig = rigT ? lmcCen : getLightmap(dirX2 - lefX, dirY2 - lefY, dirZ2 - lefZ, element.lightEmission);

                        final int lmcTopLef = topT && lefT ? lmcLef : (topLefT ? lmcCen : getLightmap(dirX2 + topX + lefX, dirY2 + topY + lefY, dirZ2 + topZ + lefZ, element.lightEmission));
                        final int lmcBotLef = botT && lefT ? lmcLef : (botLefT ? lmcCen : getLightmap(dirX2 - topX + lefX, dirY2 - topY + lefY, dirZ2 - topZ + lefZ, element.lightEmission));
                        final int lmcTopRig = topT && rigT ? lmcRig : (topRigT ? lmcCen : getLightmap(dirX2 + topX - lefX, dirY2 + topY - lefY, dirZ2 + topZ - lefZ, element.lightEmission));
                        final int lmcBotRig = botT && rigT ? lmcRig : (botRigT ? lmcCen : getLightmap(dirX2 - topX - lefX, dirY2 - topY - lefY, dirZ2 - topZ - lefZ, element.lightEmission));

                        lightmapCoordTopLeft = LightmapHelper.avg(lmcCen, lmcLef, lmcTop, lmcTopLef);
                        lightmapCoordTopRight = LightmapHelper.avg(lmcCen, lmcRig, lmcTop, lmcTopRig);
                        lightmapCoordBottomLeft = LightmapHelper.avg(lmcCen, lmcLef, lmcBot, lmcBotLef);
                        lightmapCoordBottomRight = LightmapHelper.avg(lmcCen, lmcRig, lmcBot, lmcBotRig);
                    } else {
                        final int lmc;
                        if (!isFullCube) {
                            lmc = getLightmap(0, 0, 0, element.lightEmission);
                        } else {
                            lmc = getLightmap(dirX, dirY,  dirZ, element.lightEmission);
                        }
                        lightmapCoordTopLeft = lightmapCoordBottomLeft = lightmapCoordBottomRight = lightmapCoordTopRight = lmc;
                    }
                } else {
                    lightmapCoordTopLeft = 0;
                    lightmapCoordBottomLeft = 0;
                    lightmapCoordBottomRight = 0;
                    lightmapCoordTopRight = 0;
                }

                float lightTR;
                float lightBR;
                float lightBL;
                float lightTL;

                if (useAO) {
                    {
                        final float dirB = getBrightness(dirX, dirY, dirZ, minBrightness);
                        final boolean lefT = LIGHTING_CACHE.getOpacity(dirX + lefX, dirY + lefY, dirZ + lefZ);
                        final boolean botT = LIGHTING_CACHE.getOpacity(dirX - topX, dirY - topY, dirZ - topZ);
                        final boolean topT = LIGHTING_CACHE.getOpacity(dirX + topX, dirY + topY, dirZ + topZ);
                        final boolean rigT = LIGHTING_CACHE.getOpacity(dirX - lefX, dirY - lefY, dirZ - lefZ);
                        final float lB = getBrightness(dirX + lefX, dirY + lefY, dirZ + lefZ, minBrightness);
                        final float bB = getBrightness(dirX - topX, dirY - topY, dirZ - topZ, minBrightness);
                        final float tB = getBrightness(dirX + topX, dirY + topY, dirZ + topZ, minBrightness);
                        final float rB = getBrightness(dirX - lefX, dirY - lefY, dirZ - lefZ, minBrightness);
                        final float blB = botT && lefT ? lB : getBrightness(dirX + lefX - topX, dirY + lefY - topY, dirZ + lefZ - topZ, minBrightness);
                        final float tlB = topT && lefT ? lB : getBrightness(dirX + lefX + topX, dirY + lefY + topY, dirZ + lefZ + topZ, minBrightness);
                        final float brB = botT && rigT ? rB : getBrightness(dirX - lefX - topX, dirY - lefY - topY, dirZ - lefZ - topZ, minBrightness);
                        final float trB = topT && rigT ? rB : getBrightness(dirX - lefX + topX, dirY - lefY + topY, dirZ - lefZ + topZ, minBrightness);
                        lightTL = (tlB + lB + tB + dirB) / 4.0F;
                        lightTR = (tB + dirB + trB + rB) / 4.0F;
                        lightBR = (dirB + bB + rB + brB) / 4.0F;
                        lightBL = (lB + blB + dirB + bB) / 4.0F;
                    }
                    if (!isFullCube) {
                        final float dirB = getBrightness(0, 0, 0, minBrightness);
                        final boolean lefT = LIGHTING_CACHE.getOpacity(lefX, lefY, lefZ);
                        final boolean botT = LIGHTING_CACHE.getOpacity(-topX, -topY, -topZ);
                        final boolean topT = LIGHTING_CACHE.getOpacity(topX, topY, topZ);
                        final boolean rigT = LIGHTING_CACHE.getOpacity(-lefX, -lefY, -lefZ);
                        final float lB = getBrightness(lefX, lefY, lefZ, minBrightness);
                        final float bB = getBrightness(-topX, -topY, -topZ, minBrightness);
                        final float tB = getBrightness(topX, topY, topZ, minBrightness);
                        final float rB = getBrightness(-lefX, -lefY, -lefZ, minBrightness);
                        final float blB = botT && lefT ? lB : getBrightness(lefX - topX, lefY - topY, lefZ - topZ, minBrightness);
                        final float tlB = topT && lefT ? lB : getBrightness(lefX + topX, lefY + topY, lefZ + topZ, minBrightness);
                        final float brB = botT && rigT ? rB : getBrightness(-lefX - topX, -lefY - topY, -lefZ - topZ, minBrightness);
                        final float trB = topT && rigT ? rB : getBrightness(-lefX + topX, -lefY + topY, -lefZ + topZ, minBrightness);
                        lightTL = (tlB + lB + tB + dirB) / 4.0F * depth + lightTL * (1 - depth);
                        lightTR = (tB + dirB + trB + rB) / 4.0F * depth + lightTR * (1 - depth);
                        lightBR = (dirB + bB + rB + brB) / 4.0F * depth + lightBR * (1 - depth);
                        lightBL = (lB + blB + dirB + bB) / 4.0F * depth + lightBL * (1 - depth);
                    }
                } else {
                    final int dirX2;
                    final int dirY2;
                    final int dirZ2;
                    if (!isFullCube) {
                        dirX2 = 0;
                        dirY2 = 0;
                        dirZ2 = 0;
                    } else {
                        dirX2 = dirX;
                        dirY2 = dirY;
                        dirZ2 = dirZ;
                    }

                    lightTL = lightBL = lightBR = lightTR = getBrightness(dirX2, dirY2, dirZ2, minBrightness);
                }

                final float sideLight = (element.shade && ENABLE_DIRECTIONAL_LIGHTING) ? SIDE_LIGHT_MULTIPLIER[direction.getId()] : 1;
                float colorRedBottomLeft = r * sideLight;
                float colorRedBottomRight = colorRedBottomLeft;
                float colorRedTopRight = colorRedBottomLeft;
                float colorRedTopLeft = colorRedBottomLeft;

                float colorGreenBottomLeft = g * sideLight;
                float colorGreenBottomRight = colorGreenBottomLeft;
                float colorGreenTopRight = colorGreenBottomLeft;
                float colorGreenTopLeft = colorGreenBottomLeft;

                float colorBlueBottomLeft = b * sideLight;
                float colorBlueBottomRight = colorBlueBottomLeft;
                float colorBlueTopRight = colorBlueBottomLeft;
                float colorBlueTopLeft = colorBlueBottomLeft;

                final float tl = topP * lightTL + (1 - topP) * lightBL;
                final float tr = topP * lightTR + (1 - topP) * lightBR;
                final float bl = botP * lightTL + (1 - botP) * lightBL;
                final float br = botP * lightTR + (1 - botP) * lightBR;
                final float ltl = lefP * tl + (1 - lefP) * tr;
                final float lbl = lefP * bl + (1 - lefP) * br;
                final float lbr = rigP * bl + (1 - rigP) * br;
                final float ltr = rigP * tl + (1 - rigP) * tr;
                colorRedTopLeft *= ltl;
                colorGreenTopLeft *= ltl;
                colorBlueTopLeft *= ltl;

                colorRedBottomLeft *= lbl;
                colorGreenBottomLeft *= lbl;
                colorBlueBottomLeft *= lbl;

                colorRedBottomRight *= lbr;
                colorGreenBottomRight *= lbr;
                colorBlueBottomRight *= lbr;

                colorRedTopRight *= ltr;
                colorGreenTopRight *= ltr;
                colorBlueTopRight *= ltr;

                final int offset = face * 4 * FLOATS_PER_VERTEX;

                tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
                if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopLeft);
                tessellator.addVertexWithUV(
                    element.vertexData[offset + VERTEX_TOP_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_X] + x + xOff,
                    element.vertexData[offset + VERTEX_TOP_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Y] + y + yOff,
                    element.vertexData[offset + VERTEX_TOP_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Z] + z + zOff,
                    coordinate.getSubIconU(element.vertexData[offset + VERTEX_TOP_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_U]),
                    coordinate.getSubIconV(element.vertexData[offset + VERTEX_TOP_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_V]));

                tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
                if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomLeft);
                tessellator.addVertexWithUV(
                    element.vertexData[offset + VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_X] + x + xOff,
                    element.vertexData[offset + VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Y] + y + yOff,
                    element.vertexData[offset + VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Z] + z + zOff,
                    coordinate.getSubIconU(element.vertexData[offset + VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_U]),
                    coordinate.getSubIconV(element.vertexData[offset + VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX + VERTEX_FLOAT_V]));

                tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
                if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordBottomRight);
                tessellator.addVertexWithUV(
                    element.vertexData[offset + VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_X] + x + xOff,
                    element.vertexData[offset + VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Y] + y + yOff,
                    element.vertexData[offset + VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Z] + z + zOff,
                    coordinate.getSubIconU(element.vertexData[offset + VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_U]),
                    coordinate.getSubIconV(element.vertexData[offset + VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_V]));

                tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
                if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(lightmapCoordTopRight);
                tessellator.addVertexWithUV(
                    element.vertexData[offset + VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_X] + x + xOff,
                    element.vertexData[offset + VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Y] + y + yOff,
                    element.vertexData[offset + VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_Z] + z + zOff,
                    coordinate.getSubIconU(element.vertexData[offset + VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_U]),
                    coordinate.getSubIconV(element.vertexData[offset + VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX + VERTEX_FLOAT_V]));

                didRender = true;
            }
        }
        return didRender;
    }

    @Override
    public @Nullable IconCoordinate getOverlay() {
        // This breaks the modern mc format of using the "#particle" symbol for the overlay, but due to our changes to the spec this is the best way of going about it
        return compiled.textures.get("#overlay");
    }

    @Override
    public @Nullable IconCoordinate getParticle(@NotNull Side side) {
        return compiled.textures.get("#particle_" + side.getDirection().name().toLowerCase(Locale.ROOT));
    }

    @Override
    public int particleColorIndex(@NotNull Side side) {
        return compiled.particleIndices[side.getId()];
    }

    @Override
    public int renderLayer() {
        return compiled.renderLayer;
    }

    @Override
    public @NotNull DisplayPos getItemDisplayPos(@NotNull String id) {
        return compiled.displayPosMap.getOrDefault(id, DisplayPos.DEFAULT_DISPLAY_POS);
    }

    protected boolean cullSide(WorldSource worldSource, int x, int y, int z, @Nullable Direction direction) {
        return direction != null && worldSource.isBlockOpaqueCube(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ());
    }

    protected float getBrightness(int relX, int relY, int relZ, float min) {
        return Math.max(LIGHTING_CACHE.getBrightness(relX, relY, relZ), min);
    }

    protected int getLightmap(int relX, int relY, int relZ, int min) {
        int coord = LIGHTING_CACHE.getLightmapCoord(relX, relY, relZ);
        int block = getBlockLightFromCoord(coord);
        if (block < min) {
            coord = LightmapHelper.setBlocklightValue(coord, min);
        }
        return coord;
    }

    public static int getBlockLightFromCoord(int lightmapCoord) {
        return  ((lightmapCoord) & 0xFFFF) >> 4;
    }
}
