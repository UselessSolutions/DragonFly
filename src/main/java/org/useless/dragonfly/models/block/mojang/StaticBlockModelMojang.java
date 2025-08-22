package org.useless.dragonfly.models.block.mojang;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element;
import org.useless.dragonfly.models.block.LightingCache;
import org.useless.dragonfly.models.block.RenderBlocks;
import org.useless.dragonfly.models.block.StaticBlockModel;

import java.util.Locale;

import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_BOTTOM_LEFT;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_BOTTOM_RIGHT;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_TOP_LEFT;
import static org.useless.dragonfly.data.block.mojang.CompiledBlockModelMojangData.C_Element.VERTEX_TOP_RIGHT;

public class StaticBlockModelMojang implements StaticBlockModel {
    protected static final Minecraft MC = Minecraft.getMinecraft();
    protected static final LightingCache LIGHTING_CACHE = new LightingCache();

    protected final @NotNull CompiledBlockModelMojangData compiled;

    public StaticBlockModelMojang(@NotNull final BlockModelMojangData data) {
        this.compiled = new CompiledBlockModelMojangData(data);
    }

    @Override
    public boolean renderStandalone(@NotNull final BlockModel<? extends BlockLogic> sourceModel, @NotNull final Tessellator tessellator, final double x, final double y, final double z, final int metadata, @NotNull final BlockColor color, final float brightness, final float alpha) {
        if (this.compiled.elements.length == 0) return false;

        tessellator.startDrawingQuads();
        for (int i = 0; i < this.compiled.elements.length; i++) {
            final C_Element element = this.compiled.elements[i];

//            tessellator.setLightmapCoord(lightmapCoord);
            for (int face = 0; face < element.faces; face++) {
                final IconCoordinate coordinate = element.textures[face];

                if (element.tintIndices[face] >= 0) {
					final int c = color.getFallbackColor(metadata);
					final float r = Color.redFromInt(c)/255f;
					final float g = Color.greenFromInt(c)/255f;
					final float b = Color.blueFromInt(c)/255f;
                    tessellator.setColorRGBA_F(r * brightness, g * brightness, b * brightness, alpha);
                } else {
					tessellator.setColorRGBA_F(brightness, brightness, brightness, alpha);
                }

                final int offset = face * 4;
                for (int v = 0; v < 4; v++) {
                    if (element.shade) {
                        final Vector3fc normal = element.faceNormals[face];
                        tessellator.setNormal(normal.x(), normal.y(), normal.z());
                    } else {
                        tessellator.setNormal(0, 1, 0);
                    }
                    final Vector3dc pos = element.vertexPoses[offset + v];
                    final Vector2fc uvs = element.vertexUvs[offset + v];
                    tessellator.addVertexWithUV(
                        pos.x() - 0.5f,
                        pos.y() - 0.5f,
                        pos.z() - 0.5f,
                        coordinate.getSubIconU(uvs.x()),
                        coordinate.getSubIconV(uvs.y()));
                }
            }
        }
        tessellator.draw();
        return true;
    }

    @Override
    public boolean renderAttached(@NotNull final BlockModel<? extends BlockLogic> sourceModel, @NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource,
								  final int x, final int y, final int z,
								  final int rotX, final int rotY, final int rotZ,
								  final double xOff, final double yOff, final double zOff,
								  final boolean uvlock, final boolean cullFaces, @Nullable final IconCoordinate overrideTexture) {
        if (this.compiled.elements.length == 0) return false;
        boolean didRender = false;
        LIGHTING_CACHE.setupCache(sourceModel.block, worldSource, x, y, z);
        byte cullCache = 0;

        final BlockColor color = BlockColorDispatcher.getInstance().getDispatch(sourceModel.block);
        for (int i = 0; i < this.compiled.elements.length; i++) {
            final C_Element element = this.compiled.elements[i];
            for (int face = 0; face < element.faces; face++) {
                Direction cullDir = element.cullfaces[face];
                if (cullFaces && cullDir != null) {
                    cullDir = rotateZ(rotateY(rotateX(element.cullfaces[face], rotX), rotY), rotZ);
                    if ((cullCache & (1 << cullDir.getId())) != 0) {
                        continue;
                    }
                    if (cullSide(worldSource, x, y, z, cullDir)) {
                        cullCache |= (byte) (1 << cullDir.getId());
                        continue;
                    }
                }
                final IconCoordinate coordinate = overrideTexture == null ? element.textures[face] : overrideTexture;
                final Direction direction = rotateZ(rotateY(rotateX(element.directions[face], rotX), rotY), rotZ);

                final float minBrightness = element.lightEmission / 15f;

                final Vector3f q = new Vector3f();
                final Vector3f top = q.set(element.faceUps[face]);
                if (rotX != 0) { top.rotateX(rotX * org.joml.Math.PI_OVER_2_f); }
                if (rotY != 0) { top.rotateY(rotY * org.joml.Math.PI_OVER_2_f); }
                if (rotZ != 0) { top.rotateZ(rotZ * org.joml.Math.PI_OVER_2_f); }
                final int topX = org.joml.Math.round(top.x());
                final int topY = org.joml.Math.round(top.y());
                final int topZ = org.joml.Math.round(top.z());
                final float topP;
                final float botP;

                final Vector3f left = q.set(element.faceLefts[face]);
                if (rotX != 0) { left.rotateX(rotX * org.joml.Math.PI_OVER_2_f); }
                if (rotY != 0) { left.rotateY(rotY * org.joml.Math.PI_OVER_2_f); }
                if (rotZ != 0) { left.rotateZ(rotZ * org.joml.Math.PI_OVER_2_f); }
                final int lefX = org.joml.Math.round(left.x());
                final int lefY = org.joml.Math.round(left.y());
                final int lefZ = org.joml.Math.round(left.z());
                final float lefP;
                final float rigP;
                final float depth;
                if (element.flip) {
                    switch (element.directions[face]) {
                        case DOWN:
                            depth = 1 - element.minY;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = 1 - element.minX;
                            rigP = 1 - element.maxX;
                            break;
                        case UP:
                            depth = 0 + element.maxY;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = element.maxX;
                            rigP = element.minX;
                            break;
                        case NORTH:
                            depth = 1 - element.minZ;
                            topP = 1 - element.minX;
                            botP = 1 - element.maxX;
                            lefP = element.maxY;
                            rigP = element.minY;
                            break;
                        case SOUTH:
                            depth = 0 + element.maxZ;
                            topP = element.maxY;
                            botP = element.minY;
                            lefP = 1 - element.minX;
                            rigP = 1 - element.maxX;
                            break;
                        case WEST:
                            depth = 1 - element.maxX;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = element.maxY;
                            rigP = element.minY;
                            break;
                        case EAST:
                        default:
                            depth = 0 + element.minX;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = 1 - element.minY;
                            rigP = 1 - element.maxY;
                            break;
                    }
                } else {
                    switch (element.directions[face]) {
                        case DOWN:
                            depth = 0 + element.minY;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = 1 - element.minX;
                            rigP = 1 - element.maxX;
                            break;
                        case UP:
                            depth = 1 - element.maxY;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = element.maxX;
                            rigP = element.minX;
                            break;
                        case NORTH:
                            depth = 0 + element.minZ;
                            topP = 1 - element.minX;
                            botP = 1 - element.maxX;
                            lefP = element.maxY;
                            rigP = element.minY;
                            break;
                        case SOUTH:
                            depth = 1 - element.maxZ;
                            topP = element.maxY;
                            botP = element.minY;
                            lefP = 1 - element.minX;
                            rigP = 1 - element.maxX;
                            break;
                        case WEST:
                            depth = 0 + element.minX;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = element.maxY;
                            rigP = element.minY;
                            break;
                        case EAST:
                        default:
                            depth = 1 - element.maxX;
                            topP = element.maxZ;
                            botP = element.minZ;
                            lefP = 1 - element.minY;
                            rigP = 1 - element.maxY;
                            break;
                    }
                }

                final boolean useAO = this.compiled.data.ambientOcclusion && MC.isAmbientOcclusionEnabled() && element.shade;
                final boolean isFullCube = depth <= RenderBlocks.FULL_CUBE_THRESHOLD && element.shade;

                final float r;
                final float g;
                final float b;
                if (element.tintIndices[face] >= 0) {
                    final int c = color.getWorldColor(worldSource, x, y, z);
                    r = Color.redFromInt(c) / 255f;
                    g = Color.greenFromInt(c) / 255f;
                    b = Color.blueFromInt(c) / 255f;
                } else {
                    r = 1;
                    g = 1;
                    b = 1;
                }

                final int dirX = element.flip ? -direction.getOffsetX() : direction.getOffsetX();
                final int dirY = element.flip ? -direction.getOffsetY() : direction.getOffsetY();
                final int dirZ = element.flip ? -direction.getOffsetZ() : direction.getOffsetZ();

                final int lightmapCoordTopLeft;
                final int lightmapCoordBottomLeft;
                final int lightmapCoordBottomRight;
                final int lightmapCoordTopRight;

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
                        lmc = getLightmap(dirX, dirY, dirZ, element.lightEmission);
                    }
                    lightmapCoordTopLeft = lightmapCoordBottomLeft = lightmapCoordBottomRight = lightmapCoordTopRight = lmc;
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

                float colorRedBottomLeft = r;
                float colorRedBottomRight = colorRedBottomLeft;
                float colorRedTopRight = colorRedBottomLeft;
                float colorRedTopLeft = colorRedBottomLeft;

                float colorGreenBottomLeft = g;
                float colorGreenBottomRight = colorGreenBottomLeft;
                float colorGreenTopRight = colorGreenBottomLeft;
                float colorGreenTopLeft = colorGreenBottomLeft;

                float colorBlueBottomLeft = b;
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

                final int offset = face * 4;

                final Vector3f norm = q.set(element.faceNormals[face]);
                if (rotX != 0) { norm.rotateX(rotX * org.joml.Math.PI_OVER_2_f); }
                if (rotY != 0) { norm.rotateY(rotY * org.joml.Math.PI_OVER_2_f); }
                if (rotZ != 0) { norm.rotateZ(rotZ * org.joml.Math.PI_OVER_2_f); }

//                if (element.shade) {
//                    tessellator.setNormal(norm.x(), norm.y(), norm.z());
//                } else {
//                    tessellator.setNormal(0, 1, 0);
//                }

                final Vector3d v = new Vector3d();

                final Vector3d posTL = v.set(element.vertexPoses[offset + VERTEX_TOP_LEFT]);
                if (rotX != 0 || rotY != 0 || rotZ != 0) {
                    posTL.sub(0.5, 0.5, 0.5)
                        .rotateX(rotX * org.joml.Math.PI_OVER_2)
                        .rotateY(rotY * org.joml.Math.PI_OVER_2)
                        .rotateZ(rotZ * org.joml.Math.PI_OVER_2)
                        .add(0.5, 0.5, 0.5);
                }
                Vector2fc uvsTL = element.vertexUvs[offset + VERTEX_TOP_LEFT];
                if (uvlock) uvsTL = rotateUV(uvsTL, rotX, rotY, rotZ, element.directions[face], new Vector2f());
                tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
                tessellator.setLightmapCoord(lightmapCoordTopLeft);
                tessellator.addVertexWithUV(
                    posTL.x() + x + xOff,
                    posTL.y() + y + yOff,
                    posTL.z() + z + zOff,
                    coordinate.getSubIconU(uvsTL.x()),
                    coordinate.getSubIconV(uvsTL.y()));

                final Vector3d posBL = v.set(element.vertexPoses[offset + VERTEX_BOTTOM_LEFT]);
                if (rotX != 0 || rotY != 0 || rotZ != 0) {
                    posBL.sub(0.5, 0.5, 0.5)
                        .rotateX(rotX * org.joml.Math.PI_OVER_2)
                        .rotateY(rotY * org.joml.Math.PI_OVER_2)
                        .rotateZ(rotZ * org.joml.Math.PI_OVER_2)
                        .add(0.5, 0.5, 0.5);
                }
                Vector2fc uvsBL = element.vertexUvs[offset + VERTEX_BOTTOM_LEFT];
                if (uvlock) uvsBL = rotateUV(uvsBL, rotX, rotY, rotZ, element.directions[face], new Vector2f());
                tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
                tessellator.setLightmapCoord(lightmapCoordBottomLeft);
                tessellator.addVertexWithUV(
                    posBL.x() + x + xOff,
                    posBL.y() + y + yOff,
                    posBL.z() + z + zOff,
                    coordinate.getSubIconU(uvsBL.x()),
                    coordinate.getSubIconV(uvsBL.y()));

                final Vector3d posBR = v.set(element.vertexPoses[offset + VERTEX_BOTTOM_RIGHT]);
                if (rotX != 0 || rotY != 0 || rotZ != 0) {
                    posBR.sub(0.5, 0.5, 0.5)
                        .rotateX(rotX * org.joml.Math.PI_OVER_2)
                        .rotateY(rotY * org.joml.Math.PI_OVER_2)
                        .rotateZ(rotZ * org.joml.Math.PI_OVER_2)
                        .add(0.5, 0.5, 0.5);
                }
                Vector2fc uvsBR = element.vertexUvs[offset + VERTEX_BOTTOM_RIGHT];
                if (uvlock) uvsBR = rotateUV(uvsBR, rotX, rotY, rotZ, element.directions[face], new Vector2f());
                tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
                tessellator.setLightmapCoord(lightmapCoordBottomRight);
                tessellator.addVertexWithUV(
                    posBR.x() + x + xOff,
                    posBR.y() + y + yOff,
                    posBR.z() + z + zOff,
                    coordinate.getSubIconU(uvsBR.x()),
                    coordinate.getSubIconV(uvsBR.y()));

                final Vector3d posTR = v.set(element.vertexPoses[offset + VERTEX_TOP_RIGHT]);
                if (rotX != 0 || rotY != 0 || rotZ != 0) {
                    posTR.sub(0.5, 0.5, 0.5)
                        .rotateX(rotX * org.joml.Math.PI_OVER_2)
                        .rotateY(rotY * org.joml.Math.PI_OVER_2)
                        .rotateZ(rotZ * org.joml.Math.PI_OVER_2)
                        .add(0.5, 0.5, 0.5);
                }
                Vector2fc uvsTR = element.vertexUvs[offset + VERTEX_TOP_RIGHT];
                if (uvlock) uvsTR = rotateUV(uvsTR, rotX, rotY, rotZ, element.directions[face], new Vector2f());
                tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
                tessellator.setLightmapCoord(lightmapCoordTopRight);
                tessellator.addVertexWithUV(
                    posTR.x() + x + xOff,
                    posTR.y() + y + yOff,
                    posTR.z() + z + zOff,
                    coordinate.getSubIconU(uvsTR.x()),
                    coordinate.getSubIconV(uvsTR.y()));

                didRender = true;
            }
        }
        return didRender;
    }

    public @NotNull Vector2f rotateUV(@NotNull final Vector2fc uvs, int rotX, int rotY, int rotZ, @NotNull final Direction srcDirection, @NotNull final Vector2f dest) {
        Direction dir = srcDirection;
        float u = uvs.x() - 0.5f;
        float v = uvs.y() - 0.5f;
        rotX = (rotX & 0b11);
        while (rotX > 0) {
            if (dir.getAxis() == Axis.X) {
                if (dir == Direction.WEST) {
                    final float _u = u;
                    u = -v;
                    v = _u;
                } else {
                    final float _u = u;
                    u = v;
                    v = -_u;
                }
            } else {
                if (dir == Direction.NORTH || dir == Direction.DOWN) {
                    v = -v;
                    u = -u;
                }
            }
            dir = rotateX(dir, 1);
            rotX--;
        }

        rotY = (rotY & 0b11);
        while (rotY > 0) {
            if (dir.getAxis() == Axis.Y) {
                if (dir == Direction.DOWN) {
                    final float _v = v;
                    v = u;
                    u = -_v;
                } else {
                    final float _u = u;
                    u = v;
                    v = -_u;
                }
            }
            dir = rotateY(dir, 1);
            rotY--;
        }

        rotZ = (rotZ & 0b11);
        while (rotZ > 0) {
            if (dir.getAxis() == Axis.Z) {
                if (dir == Direction.NORTH) {
                    final float _u = u;
                    u = -v;
                    v = _u;
                } else {
                    final float _u = u;
                    u = v;
                    v = -_u;
                }
            } else {
                if (dir == Direction.DOWN || dir == Direction.UP || dir == Direction.WEST || dir == Direction.EAST) {
                    final float _v = v;
                    v = -u;
                    u = _v;
                }
            }
            dir = rotateZ(dir, 1);
            rotZ--;
        }

        return dest.set(u + 0.5f, v + 0.5f);
    }

    @Override
    public @Nullable IconCoordinate getOverlay() {
        // This breaks the modern mc format of using the "#particle" symbol for the overlay, but due to our changes to the spec this is the best way of going about it
        return this.compiled.textures.get("#overlay");
    }

    @Override
    public @Nullable IconCoordinate getParticle(@NotNull final Side side) {
        return this.compiled.textures.get("#particle_" + side.getDirection().name().toLowerCase(Locale.ROOT));
    }

    @Override
    public int particleColorIndex(@NotNull final Side side) {
        return this.compiled.particleIndices[side.getId()];
    }

    @Override
    public int renderLayer() {
        return this.compiled.renderLayer;
    }

    @Override
    public @NotNull DisplayPos getItemDisplayPos(@NotNull final String id) {
        return this.compiled.displayPosMap.getOrDefault(id, DisplayPos.DEFAULT_DISPLAY_POS);
    }

    protected float getBrightness(final int relX, final int relY, final int relZ, final float min) {
        return Math.max(LIGHTING_CACHE.getBrightness(relX, relY, relZ), min);
    }

    protected int getLightmap(final int relX, final int relY, final int relZ, final int min) {
        int coord = LIGHTING_CACHE.getLightmapCoord(relX, relY, relZ);
        final int block = (coord & 0xFFFF) >> 4;
        if (block < min) {
            coord = LightmapHelper.setBlocklightValue(coord, min);
        }
        return coord;
    }

	protected boolean cullSide(final WorldSource worldSource, final int x, final int y, final int z, @Nullable final Direction direction) {
		return direction != null && worldSource.isBlockOpaqueCube(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ());
	}

	public static @NotNull Direction[] rotXMap = {
		Direction.DOWN, Direction.NORTH, Direction.UP, Direction.SOUTH,
		Direction.UP, Direction.SOUTH, Direction.DOWN, Direction.NORTH,
		Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN,
		Direction.SOUTH, Direction.DOWN, Direction.NORTH, Direction.UP,
		Direction.WEST, Direction.WEST, Direction.WEST, Direction.WEST,
		Direction.EAST, Direction.EAST, Direction.EAST, Direction.EAST,
	};

	public static @NotNull Direction rotateX(@NotNull final Direction direction, final int amount) {
		return rotXMap[direction.getId() * 4 + (amount & 0b11)];
	}

	public static @NotNull Direction[] rotYMap = {
		Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN,
		Direction.UP, Direction.UP, Direction.UP, Direction.UP,
		Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST,
		Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST,
		Direction.WEST, Direction.SOUTH, Direction.EAST, Direction.NORTH,
		Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH,
	};

	public static @NotNull Direction rotateY(@NotNull final Direction direction, final int amount) {
		return rotYMap[direction.getId() * 4 + (amount & 0b11)];
	}

	public static @NotNull Direction[] rotZMap = {
		Direction.DOWN, Direction.EAST, Direction.UP, Direction.WEST,
		Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST,
		Direction.NORTH, Direction.NORTH, Direction.NORTH, Direction.NORTH,
		Direction.SOUTH, Direction.SOUTH, Direction.SOUTH, Direction.SOUTH,
		Direction.WEST, Direction.DOWN, Direction.EAST, Direction.UP,
		Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN,
	};

	public static @NotNull Direction rotateZ(@NotNull final Direction direction, final int amount) {
		return rotZMap[direction.getId() * 4 + (amount & 0b11)];
	}
}
