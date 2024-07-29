package org.useless.dragonfly.model.newmodels.generic.components.block;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.model.newmodels.generic.ModelEntry;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;
import org.useless.dragonfly.model.newmodels.generic.components.ModelComponent;
import org.useless.dragonfly.utilities.SideUtils;

public class BlockComponent extends ModelComponent {
    private static final double[] quickRotationOrigin = new double[]{0.5, 0.5, 0.5};
    public final static double[] bufferVec1 = new double[3];
    public final static double[] bufferVec2 = new double[3];
    public final static double[] bufferVec3 = new double[3];
    public final static double[] bufferVec4 = new double[3];
    public final static double[] bufferVec5 = new double[3];
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    private boolean hasOverbright = false;
    public final BlockFace[] faces = new BlockFace[6];
    public final BlockFace[] overBrightFaces = new BlockFace[6];
    public final double[][][] defaultUVs = new double[6][4][3];
    public final boolean shade;
    public final boolean forceInnerLighting;
    public final boolean supportsComplexLighting;
    public BlockComponent(double minX, double minY, double minZ,
                          double maxX, double maxY, double maxZ,
                          double rotationX, double rotationY, double rotationZ,
                          boolean rescaleX, boolean rescaleY, boolean rescaleZ,
                          double @NotNull [] rotationOffset, boolean shade, boolean forceInnerLighting,
                          BlockFaceBuilder[] builders, BlockFaceBuilder[] overbrightBuilders){
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

        while (rotationX < 0) rotationX += 360;
        while (rotationY < 0) rotationY += 360;
        while (rotationZ < 0) rotationZ += 360;

        final double zeroCheck = 0.0001d;

        this.shade = shade;
        this.forceInnerLighting = forceInnerLighting;

        supportsComplexLighting = rotationX < zeroCheck && rotationY < zeroCheck && rotationZ < zeroCheck;

        double[] tfl = slowRotate(rotationX, rotationY, rotationZ, new double[]{minX, maxY, minZ}, null, rotationOffset);
        double[] tbl = slowRotate(rotationX, rotationY, rotationZ, new double[]{minX, maxY, maxZ}, null, rotationOffset);
        double[] tfr = slowRotate(rotationX, rotationY, rotationZ, new double[]{maxX, maxY, minZ}, null, rotationOffset);
        double[] tbr = slowRotate(rotationX, rotationY, rotationZ, new double[]{maxX, maxY, maxZ}, null, rotationOffset);

        double[] bfl = slowRotate(rotationX, rotationY, rotationZ, new double[]{minX, minY, minZ}, null, rotationOffset);
        double[] bbl = slowRotate(rotationX, rotationY, rotationZ, new double[]{minX, minY, maxZ}, null, rotationOffset);
        double[] bfr = slowRotate(rotationX, rotationY, rotationZ, new double[]{maxX, minY, minZ}, null, rotationOffset);
        double[] bbr = slowRotate(rotationX, rotationY, rotationZ, new double[]{maxX, minY, maxZ}, null, rotationOffset);

        if (rescaleX && rotationX > zeroCheck){
            double scalar = getReScalar(rotationX);

            for (double[] v : new double[][]{tfl, tbl, tfr, tbr, bfl, bbl, bfr, bbr}){
                double[] newVec = addVector(scaleVector(scalar, subVector(v, rotationOffset, bufferVec1), bufferVec2), rotationOffset, bufferVec3);
                v[1] = newVec[1];
                v[2] = newVec[2];
            }
        }
        if (rescaleY && rotationY > zeroCheck){
            double scalar = getReScalar(rotationY);
            for (double[] v : new double[][]{tfl, tbl, tfr, tbr, bfl, bbl, bfr, bbr}){
                double[] newVec = addVector(scaleVector(scalar, subVector(v, rotationOffset, bufferVec1), bufferVec2), rotationOffset, bufferVec3);
                v[0] = newVec[0];
                v[2] = newVec[2];
            }
        }
        if (rescaleZ && rotationZ > zeroCheck){
            double scalar = getReScalar(rotationZ);
            for (double[] v : new double[][]{tfl, tbl, tfr, tbr, bfl, bbl, bfr, bbr}){
                double[] newVec = addVector(scaleVector(scalar, subVector(v, rotationOffset, bufferVec1), bufferVec2), rotationOffset, bufferVec3);
                v[0] = newVec[0];
                v[1] = newVec[1];
            }
        }

        defaultUVs[Side.BOTTOM.getId()] = generateUVs(Side.BOTTOM, true, true);
        defaultUVs[Side.TOP.getId()] = generateUVs(Side.TOP, false, true);
        defaultUVs[Side.NORTH.getId()] = generateUVs(Side.NORTH, false, true);
        defaultUVs[Side.SOUTH.getId()] = generateUVs(Side.SOUTH, false, true);
        defaultUVs[Side.WEST.getId()] = generateUVs(Side.WEST, false, true);
        defaultUVs[Side.EAST.getId()] = generateUVs(Side.EAST, false, true);

        if (builders[0] != null){
            faces[Side.BOTTOM.getId()] = builders[0].build(bfr, bfl, bbl, bbr,
                defaultUVs[Side.BOTTOM.getId()]);
        }
        if (builders[1] != null){
            faces[Side.TOP.getId()] = builders[1].build(tfl, tfr, tbr, tbl,
                defaultUVs[Side.TOP.getId()]);
        }
        if (builders[2] != null){
            faces[Side.NORTH.getId()] = builders[2].build(tfr, tfl, bfl, bfr,
                defaultUVs[Side.NORTH.getId()]);
        }
        if (builders[3] != null){
            faces[Side.SOUTH.getId()] = builders[3].build(tbl, tbr, bbr, bbl,
                defaultUVs[Side.SOUTH.getId()]);
        }
        if (builders[4] != null){
            faces[Side.WEST.getId()] = builders[4].build(tfl, tbl, bbl, bfl,
                defaultUVs[Side.WEST.getId()]);
        }
        if (builders[5] != null){
            faces[Side.EAST.getId()] = builders[5].build(tbr, tfr, bfr, bbr,
                defaultUVs[Side.EAST.getId()]);
        }

        if (overbrightBuilders[0] != null){
            overBrightFaces[Side.BOTTOM.getId()] = overbrightBuilders[0].build(bfr, bfl, bbl, bbr,
                defaultUVs[Side.BOTTOM.getId()]);
        }
        if (overbrightBuilders[1] != null){
            overBrightFaces[Side.TOP.getId()] = overbrightBuilders[1].build(tfl, tfr, tbr, tbl,
                defaultUVs[Side.TOP.getId()]);
        }
        if (overbrightBuilders[2] != null){
            overBrightFaces[Side.NORTH.getId()] = overbrightBuilders[2].build(tfr, tfl, bfl, bfr,
                defaultUVs[Side.NORTH.getId()]);
        }
        if (overbrightBuilders[3] != null){
            overBrightFaces[Side.SOUTH.getId()] = overbrightBuilders[3].build(tbl, tbr, bbr, bbl,
                defaultUVs[Side.SOUTH.getId()]);
        }
        if (overbrightBuilders[4] != null){
            overBrightFaces[Side.WEST.getId()] = overbrightBuilders[4].build(tfl, tbl, bbl, bfl,
                defaultUVs[Side.WEST.getId()]);
        }
        if (overbrightBuilders[5] != null){
            overBrightFaces[Side.EAST.getId()] = overbrightBuilders[5].build(tbr, tfr, bfr, bbr,
                defaultUVs[Side.EAST.getId()]);
        }

        for (BlockFace face : overBrightFaces){
            if (face != null){
                hasOverbright = true;
                break;
            }
        }
    }
    public double[][] generateUVs(Side side, boolean flipTexU, boolean flipTexV){
        double minU;
        double minV;
        double widthU;
        double widthV;
        switch (side){
            case BOTTOM:
                minU =   minX;
                minV =   minZ;
                widthU = maxX - minX;
                widthV = maxZ - minZ;
                break;
            case TOP:
                minU =   minX;
                minV =   1 - maxZ;
                widthU = maxX - minX;
                widthV = maxZ - minZ;
                break;
            case NORTH:
                minU =   1 - maxX;
                minV =   minY;
                widthU = maxX - minX;
                widthV = maxY - minY;
                break;
            case SOUTH:
                minU =   minX;
                minV =   minY;
                widthU = maxX - minX;
                widthV = maxY - minY;
                break;
            case WEST:
                minU =   minZ;
                minV =   minY;
                widthU = maxZ - minZ;
                widthV = maxY - minY;
                break;
            case EAST:
                minU =   1 - maxZ;
                minV =   minY;
                widthU = maxZ - minZ;
                widthV = maxY - minY;
                break;
            default:
                throw new IllegalArgumentException("Illegal Side " + side + "!");
        }
        minU = MathHelper.clamp(minU, 0, 1);
        minV = MathHelper.clamp(minV, 0, 1);
        widthU = MathHelper.clamp(minU + widthU, 0, 1) - minU;
        widthV = MathHelper.clamp(minV + widthV, 0, 1) - minV;
        return new double[][]{
            {(flipTexU ? 1 - minU : minU),                      (flipTexV ? 1 - (minV + widthV): minV + widthV)},
            {(flipTexU ? 1 - (minU + widthU) : minU + widthU),  (flipTexV ? 1 - (minV + widthV): minV + widthV)},
            {(flipTexU ? 1 - (minU + widthU) : minU + widthU),  (flipTexV ? 1 - minV: minV)},
            {(flipTexU ? 1 - minU : minU),                      (flipTexV ? 1 - minV: minV)}};
    }
    @Override
    public void drawComponent(ModelEntry modelEntry, Tessellator tessellator, int meta, double x, double y, double z, float r, float g, float b, float brightness, float alpha) {
        StaticModel staticModel = modelEntry.model;
        for (BlockFace face : faces){
            if (face == null) continue;
            if (face.useColor){
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            IconCoordinate texture = staticModel.getTexture(face.textureSymbol, false);

            drawFaceRaw(tessellator, face, texture, x, y, z, false, face.flippedNormal);
            if (face.doubledSided){
                drawFaceRaw(tessellator, face, texture, x, y, z, false, !face.flippedNormal);
            }
        }
        for (BlockFace face : overBrightFaces){
            if (face == null) continue;
            if (face.useColor){
                GL11.glColor4f(r, g, b, alpha);
            } else {
                GL11.glColor4f(1, 1, 1, alpha);
            }

            IconCoordinate texture = staticModel.getTexture(face.textureSymbol, false);

            drawFaceRaw(tessellator, face, texture, x, y, z, true, face.flippedNormal);
            if (face.doubledSided){
                drawFaceRaw(tessellator, face, texture, x, y, z, true, !face.flippedNormal);
            }
        }
    }
    private void drawFaceRaw(Tessellator tessellator, BlockFace face, IconCoordinate tex, double x, double y, double z, boolean overbright, boolean flipped){
        if (tex == null) return;
        tessellator.startDrawingQuads();
        if (overbright && LightmapHelper.isLightmapEnabled()){
            tessellator.setLightmapCoord(LightmapHelper.getOverbrightLightmapCoord(15));
        }
        if (flipped) {
            tessellator.setNormal((float) -face.normal[0], (float) -face.normal[1], (float) -face.normal[2]);
            tessellator.addVertexWithUV(x + face.v1[0], y + face.v1[1], z + face.v1[2], tex.getSubIconU(face.t1[0]), tex.getSubIconV(face.t1[1]));
            tessellator.addVertexWithUV(x + face.v2[0], y + face.v2[1], z + face.v2[2], tex.getSubIconU(face.t2[0]), tex.getSubIconV(face.t2[1]));
            tessellator.addVertexWithUV(x + face.v3[0], y + face.v3[1], z + face.v3[2], tex.getSubIconU(face.t3[0]), tex.getSubIconV(face.t3[1]));
            tessellator.addVertexWithUV(x + face.v4[0], y + face.v4[1], z + face.v4[2], tex.getSubIconU(face.t4[0]), tex.getSubIconV(face.t4[1]));
        } else {
            tessellator.setNormal((float) face.normal[0], (float) face.normal[1], (float) face.normal[2]);
            tessellator.addVertexWithUV(x + face.v1[0], y + face.v1[1], z + face.v1[2], tex.getSubIconU(face.t1[0]), tex.getSubIconV(face.t1[1]));
            tessellator.addVertexWithUV(x + face.v4[0], y + face.v4[1], z + face.v4[2], tex.getSubIconU(face.t4[0]), tex.getSubIconV(face.t4[1]));
            tessellator.addVertexWithUV(x + face.v3[0], y + face.v3[1], z + face.v3[2], tex.getSubIconU(face.t3[0]), tex.getSubIconV(face.t3[1]));
            tessellator.addVertexWithUV(x + face.v2[0], y + face.v2[1], z + face.v2[2], tex.getSubIconU(face.t2[0]), tex.getSubIconV(face.t2[1]));
        }
        tessellator.draw();
    }

    @Override
    public boolean drawComponentInWorld(BlockModel<?> model, ModelEntry modelEntry, Tessellator tessellator, WorldSource worldSource, Block block, int meta, int x, int y, int z, float r, float g, float b) {
        BlockFace[] _faces = BlockModel.renderBlocks.overbright ? overBrightFaces : faces;
        StaticModel staticModel = modelEntry.model;
        int rotX = modelEntry.get90DegRotationX();
        int rotY = modelEntry.get90DegRotationY();
        int rotZ = modelEntry.get90DegRotationZ();

        boolean flag = false;
        for (int i = 0; i < _faces.length; i++) {
            BlockFace face = _faces[i];
            if (face == null) continue;
            Side side = Side.getSideById(i);

            if (face.cullSide != Side.NONE && !model.shouldSideBeRendered(worldSource, x, y, z,
                SideUtils.rotateSide(rotX, rotY, rotZ, face.cullSide).getId(), meta)){
                continue;
            }

            setupLighting(face, side, block, meta,
                x, y, z,
                r, g, b,
                rotX, rotY, rotZ,
                shade, supportsComplexLighting && staticModel.useAO(), face.flippedNormal);

            IconCoordinate texture = staticModel.getTexture(face.textureSymbol, false);

            drawFaceLighting(tessellator, face, texture,
                x, y, z,
                modelEntry.getOffsetX(), modelEntry.getOffsetY(), modelEntry.getOffsetZ(),
                rotX, rotY, rotZ, face.flippedNormal);

            if (face.doubledSided){
                setupLighting(face, side, block, meta,
                    x, y, z,
                    r, g, b,
                    rotX, rotY, rotZ,
                    shade, supportsComplexLighting && staticModel.useAO(), !face.flippedNormal);
                drawFaceLighting(tessellator, face, texture,
                    x, y, z,
                    modelEntry.getOffsetX(), modelEntry.getOffsetY(), modelEntry.getOffsetZ(),
                    rotX, rotY, rotZ, !face.flippedNormal);
            }

            flag = true;
        }
        return flag;
    }

    @Override
    public boolean doOverbright() {
        return hasOverbright;
    }

    private void drawFaceLighting(Tessellator tessellator, BlockFace face, IconCoordinate tex, int x, int y, int z, float offX, float offY, float offZ, int rotX, int rotY, int rotZ, boolean flipped){
        RenderBlocks rb = BlockModel.renderBlocks;

        double[] t1;
        double[] t2;
        double[] t3;
        double[] t4;
        if (rb.overrideBlockTexture != null) {
            tex = rb.overrideBlockTexture;
            t1 = face.defaultUVs[0];
            t2 = face.defaultUVs[1];
            t3 = face.defaultUVs[2];
            t4 = face.defaultUVs[3];
        } else {
            t1 = face.t1;
            t2 = face.t2;
            t3 = face.t3;
            t4 = face.t4;
        }

        quickRotate(rotX, rotY, rotZ, new double[]{face.v1[0], face.v1[1], face.v1[2]}, bufferVec1, quickRotationOrigin);
        quickRotate(rotX, rotY, rotZ, new double[]{face.v2[0], face.v2[1], face.v2[2]}, bufferVec2, quickRotationOrigin);
        quickRotate(rotX, rotY, rotZ, new double[]{face.v3[0], face.v3[1], face.v3[2]}, bufferVec3, quickRotationOrigin);
        quickRotate(rotX, rotY, rotZ, new double[]{face.v4[0], face.v4[1], face.v4[2]}, bufferVec4, quickRotationOrigin);

        quickRotate(rotX, rotY, rotZ, new double[]{offX, offY, offZ}, bufferVec5, null);


        if (flipped) {
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(rb.colorRedTopLeft, rb.colorGreenTopLeft, rb.colorBlueTopLeft);
            tessellator.addVertexWithUV(x + bufferVec1[0] + bufferVec5[0], y + bufferVec1[1] + bufferVec5[1], z + bufferVec1[2] + bufferVec5[2], tex.getSubIconU(t1[0]), tex.getSubIconV(t1[1]));
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordTopRight);
            tessellator.setColorOpaque_F(rb.colorRedTopRight, rb.colorGreenTopRight, rb.colorBlueTopRight);
            tessellator.addVertexWithUV(x + bufferVec2[0] + bufferVec5[0], y + bufferVec2[1] + bufferVec5[1], z + bufferVec2[2] + bufferVec5[2], tex.getSubIconU(t2[0]), tex.getSubIconV(t2[1]));
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(rb.colorRedBottomRight, rb.colorGreenBottomRight, rb.colorBlueBottomRight);
            tessellator.addVertexWithUV(x + bufferVec3[0] + bufferVec5[0], y + bufferVec3[1] + bufferVec5[1], z + bufferVec3[2] + bufferVec5[2], tex.getSubIconU(t3[0]), tex.getSubIconV(t3[1]));
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(rb.colorRedBottomLeft, rb.colorGreenBottomLeft, rb.colorBlueBottomLeft);
            tessellator.addVertexWithUV(x + bufferVec4[0] + bufferVec5[0], y + bufferVec4[1] + bufferVec5[1], z + bufferVec4[2] + bufferVec5[2], tex.getSubIconU(t4[0]), tex.getSubIconV(t4[1]));
        } else {
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordTopLeft);
            tessellator.setColorOpaque_F(rb.colorRedTopLeft, rb.colorGreenTopLeft, rb.colorBlueTopLeft);
            tessellator.addVertexWithUV(x + bufferVec1[0] + bufferVec5[0], y + bufferVec1[1] + bufferVec5[1], z + bufferVec1[2] + bufferVec5[2], tex.getSubIconU(t1[0]), tex.getSubIconV(t1[1]));
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordBottomLeft);
            tessellator.setColorOpaque_F(rb.colorRedBottomLeft, rb.colorGreenBottomLeft, rb.colorBlueBottomLeft);
            tessellator.addVertexWithUV(x + bufferVec4[0] + bufferVec5[0], y + bufferVec4[1] + bufferVec5[1], z + bufferVec4[2] + bufferVec5[2], tex.getSubIconU(t4[0]), tex.getSubIconV(t4[1]));
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordBottomRight);
            tessellator.setColorOpaque_F(rb.colorRedBottomRight, rb.colorGreenBottomRight, rb.colorBlueBottomRight);
            tessellator.addVertexWithUV(x + bufferVec3[0] + bufferVec5[0], y + bufferVec3[1] + bufferVec5[1], z + bufferVec3[2] + bufferVec5[2], tex.getSubIconU(t3[0]), tex.getSubIconV(t3[1]));
            if(LightmapHelper.isLightmapEnabled()) tessellator.setLightmapCoord(rb.lightmapCoordTopRight);
            tessellator.setColorOpaque_F(rb.colorRedTopRight, rb.colorGreenTopRight, rb.colorBlueTopRight);
            tessellator.addVertexWithUV(x + bufferVec2[0] + bufferVec5[0], y + bufferVec2[1] + bufferVec5[1], z + bufferVec2[2] + bufferVec5[2], tex.getSubIconU(t2[0]), tex.getSubIconV(t2[1]));
        }

//        drawDebugVectors(tessellator, face, x, y, z, rotX, rotY, rotZ, flipped, true, true, true, 0.05, 0.4, TextureRegistry.getTexture("minecraft:block/wool_white"));
    }
    @SuppressWarnings("unused") // here for whenever someone needs to make the faces draw their vectors for debugging, do not remove
    private void drawDebugVectors(Tessellator tessellator, BlockFace face, int x, int y, int z, int rotX, int rotY, int rotZ, boolean flipped,
                                  boolean drawNorm, boolean drawTop, boolean drawLeft, double offset, double scale, IconCoordinate texture){
        // Debug Vector drawing

        double midX = (bufferVec1[0] + bufferVec2[0] + bufferVec3[0] + bufferVec4[0])/4 + 0.5;
        double midY = (bufferVec1[1] + bufferVec2[1] + bufferVec3[1] + bufferVec4[1])/4 + 0.5;
        double midZ = (bufferVec1[2] + bufferVec2[2] + bufferVec3[2] + bufferVec4[2])/4 + 0.5;

        double[] norm = quickRotate(rotX, rotY, rotZ, face.normal, null, null);
        double[] topp = quickRotate(rotX, rotY, rotZ, face.top, null, null);
        double[] left = quickRotate(rotX, rotY, rotZ, face.left, null, null);

        if (flipped){
            norm[0] *= -1;
            norm[1] *= -1;
            norm[2] *= -1;
        }

        final double minU = texture.getIconUMin();
        final double maxU = texture.getIconUMax();
        final double minV = texture.getIconVMin();
        final double maxV = texture.getIconVMax();

        if (drawNorm){
            tessellator.setColorOpaque_F(1, 0, 0);
            tessellator.addVertexWithUV(
                x + midX + norm[0] * scale - offset,
                y + midY + norm[1] * scale - offset,
                z + midZ + norm[2] * scale - offset, minU, maxV);
            tessellator.addVertexWithUV(
                x + midX + norm[0] * scale + offset,
                y + midY + norm[1] * scale + offset,
                z + midZ + norm[2] * scale + offset, maxU, maxV);
            tessellator.addVertexWithUV(
                x + midX + offset,
                y + midY + offset,
                z + midZ + offset, maxU, minV);
            tessellator.addVertexWithUV(
                x + midX - offset,
                y + midY - offset,
                z + midZ - offset, minU, minV);
            tessellator.addVertexWithUV(
                x + midX + norm[0] * scale - offset,
                y + midY + norm[1] * scale - offset,
                z + midZ + norm[2] * scale - offset, minU, maxV);
            tessellator.addVertexWithUV(
                x + midX - offset,
                y + midY - offset,
                z + midZ - offset, minU, minV);
            tessellator.addVertexWithUV(
                x + midX + offset,
                y + midY + offset,
                z + midZ + offset, maxU, minV);
            tessellator.addVertexWithUV(
                x + midX + norm[0] * scale + offset,
                y + midY + norm[1] * scale + offset,
                z + midZ + norm[2] * scale + offset, maxU, maxV);
        }

        if (drawTop){
            tessellator.setColorOpaque_F(0, 1, 0);
            tessellator.addVertexWithUV(
                x + midX + topp[0] * scale - offset,
                y + midY + topp[1] * scale - offset,
                z + midZ + topp[2] * scale - offset, minU, maxV);
            tessellator.addVertexWithUV(
                x + midX + topp[0] * scale + offset,
                y + midY + topp[1] * scale + offset,
                z + midZ + topp[2] * scale + offset, maxU, maxV);
            tessellator.addVertexWithUV(
                x + midX + offset,
                y + midY + offset,
                z + midZ + offset, maxU, minV);
            tessellator.addVertexWithUV(
                x + midX - offset,
                y + midY - offset,
                z + midZ - offset, minU, minV);
            tessellator.addVertexWithUV(
                x + midX + topp[0] * scale - offset,
                y + midY + topp[1] * scale - offset,
                z + midZ + topp[2] * scale - offset, minU, maxV);
            tessellator.addVertexWithUV(
                x + midX - offset,
                y + midY - offset,
                z + midZ - offset, minU, minV);
            tessellator.addVertexWithUV(
                x + midX + offset,
                y + midY + offset,
                z + midZ + offset, maxU, minV);
            tessellator.addVertexWithUV(
                x + midX + topp[0] * scale + offset,
                y + midY + topp[1] * scale + offset,
                z + midZ + topp[2] * scale + offset, maxU, maxV);
        }

        if (drawLeft){
            tessellator.setColorOpaque_F(0, 0, 1);
            tessellator.addVertexWithUV(
                x + midX + left[0] * scale - offset,
                y + midY + left[1] * scale - offset,
                z + midZ + left[2] * scale - offset, minU, maxV);
            tessellator.addVertexWithUV(
                x + midX + left[0] * scale + offset,
                y + midY + left[1] * scale + offset,
                z + midZ + left[2] * scale + offset, maxU, maxV);
            tessellator.addVertexWithUV(
                x + midX + offset,
                y + midY + offset,
                z + midZ + offset, maxU, minV);
            tessellator.addVertexWithUV(
                x + midX - offset,
                y + midY - offset,
                z + midZ - offset, minU, minV);
            tessellator.addVertexWithUV(
                x + midX + left[0] * scale - offset,
                y + midY + left[1] * scale - offset,
                z + midZ + left[2] * scale - offset, minU, maxV);
            tessellator.addVertexWithUV(
                x + midX - offset,
                y + midY - offset,
                z + midZ - offset, minU, minV);
            tessellator.addVertexWithUV(
                x + midX + offset,
                y + midY + offset,
                z + midZ + offset, maxU, minV);
            tessellator.addVertexWithUV(
                x + midX + left[0] * scale + offset,
                y + midY + left[1] * scale + offset,
                z + midZ + left[2] * scale + offset, maxU, maxV);
        }
    }
    private void setupLighting(BlockFace face, Side side, Block block, int meta, int x, int y, int z, float r, float g, float b, int rotX, int rotY, int rotZ, boolean shade, boolean useAO, boolean flip){
        if (!face.useColor){
            r = g = b = 1f;
        }
        RenderBlocks rb = BlockModel.renderBlocks;
        if (flip) side = side.getOpposite();

        rb.enableAO = useAO;

        quickRotate(rotX, rotY, rotZ, face.normal, bufferVec1, null);

        int dirX = (int)Math.round(bufferVec1[0]);
        int dirY = (int)Math.round(bufferVec1[1]);
        int dirZ = (int)Math.round(bufferVec1[2]);

        if (!shade){
            float brightness = 1.0f;
            if(!LightmapHelper.isLightmapEnabled() && !rb.overbright) {
                brightness = rb.getBlockBrightness(rb.blockAccess, x, y, z);
            }
            rb.colorRedTopLeft = rb.colorRedBottomLeft = rb.colorRedBottomRight = rb.colorRedTopRight = r * brightness;
            rb.colorGreenTopLeft = rb.colorGreenBottomLeft = rb.colorGreenBottomRight = rb.colorGreenTopRight = g * brightness;
            rb.colorBlueTopLeft = rb.colorBlueBottomLeft = rb.colorBlueBottomRight = rb.colorBlueTopRight = b * brightness;
            if (LightmapHelper.isLightmapEnabled()){
                int lmc;
                if (rb.overbright){
                    int dirX2 = forceInnerLighting ? 0 : dirX;
                    int dirY2 = forceInnerLighting ? 0 : dirY;
                    int dirZ2 = forceInnerLighting ? 0 : dirZ;

                    lmc = LightmapHelper.getOverbrightLightmapCoord(rb.blockAccess.getSavedLightValue(LightLayer.Sky, x + dirX2, y + dirY2, z + dirZ2));
                } else {
                    lmc = block.getLightmapCoord(rb.blockAccess, x, y, z);
                }
                rb.lightmapCoordTopLeft = rb.lightmapCoordBottomLeft = rb.lightmapCoordBottomRight = rb.lightmapCoordTopRight = lmc;
            }
            return;
        }

        quickRotate(rotX, rotY, rotZ, face.top, bufferVec1, null);

        int topX = (int)Math.round(bufferVec1[0]);
        int topY = (int)Math.round(bufferVec1[1]);
        int topZ = (int)Math.round(bufferVec1[2]);

        quickRotate(rotX, rotY, rotZ, face.left, bufferVec1, null);

        int lefX = (int)Math.round(bufferVec1[0]);
        int lefY = (int)Math.round(bufferVec1[1]);
        int lefZ = (int)Math.round(bufferVec1[2]);

        float depth;
        float topP;
        float botP;
        float lefP;
        float rigP;

        switch (side){
            case BOTTOM:
                depth = (float)minY;
                topP = 1-(float)minZ;
                botP = 1-(float)maxZ;
                lefP = (float)maxX;
                rigP = (float)minX;
                break;
            case TOP:
                depth = 1-(float)maxY;
                topP = 1-(float)minZ;
                botP = 1-(float)maxZ;
                lefP = 1-(float)minX;
                rigP = 1-(float)maxX;
                break;
            case NORTH:
                depth = (float)minZ;
                topP = (float)maxY;
                botP = (float)minY;
                lefP = (float)maxX;
                rigP = (float)minX;
                break;
            case SOUTH:
                depth = 1-(float)maxZ;
                topP = (float)maxY;
                botP = (float)minY;
                lefP = 1-(float)minX;
                rigP = 1-(float)maxX;
                break;
            case WEST:
                depth = (float)minX;
                topP = (float)maxY;
                botP = (float)minY;
                lefP = 1-(float)minZ;
                rigP = 1-(float)maxZ;
                break;
            case EAST:
                depth = 1 - (float)maxX;
                topP = (float)maxY;
                botP = (float)minY;
                lefP = (float)maxZ;
                rigP = (float)minZ;
                break;
            default:
                throw new IllegalArgumentException("Side " + side + " not expected!");
        }
        rb.setupLighting(block, x, y, z,
            r, g, b, SideUtils.rotateSide(rotX, rotY, rotZ, side).getId(), meta,
            dirX, dirY, dirZ, forceInnerLighting ? 1 : depth,
            topX, topY, topZ, topP, botP,
            lefX, lefY, lefZ, lefP, rigP);
        rb.enableAO = false;
    }


    /**
     * @param rotX Degrees to rotate around the X-Axis
     * @param rotY Degrees to rotate around the Y-Axis
     * @param rotZ Degrees to rotate around the Z-Axis
     * @param inputVec Vector to rotate, formatted as a double array of length 3
     * @param outputVec Vector to store the result in, generates a new double array if set to null
     * @return Rotated vector
     */
    public static double @NotNull [] slowRotate(double rotX, double rotY, double rotZ, double @NotNull [] inputVec, double @Nullable [] outputVec, double @Nullable [] offset){
        double[] output = outputVec == null ? new double[3]: outputVec;

        output[0] = inputVec[0];
        output[1] = inputVec[1];
        output[2] = inputVec[2];

        if (offset != null){
            output[0] -= offset[0];
            output[1] -= offset[1];
            output[2] -= offset[2];
        }

        rotX = Math.toRadians(rotX);
        rotY = Math.toRadians(rotY);
        rotZ = Math.toRadians(rotZ);

        double cos = Math.cos(rotX);
        double sin = Math.sin(rotX);

        double val = output[2];
        output[2] = output[2] * cos - output[1] * sin;
        output[1] = output[1] * cos + val * sin;

        cos = Math.cos(rotY);
        sin = Math.sin(rotY);

        val = output[0];
        output[0] = output[0] * cos - output[2] * sin;
        output[2] = output[2] * cos + val * sin;

        cos = Math.cos(rotZ);
        sin = Math.sin(rotZ);

        val = output[0];
        output[0] = output[0] * cos - output[1] * sin;
        output[1] = output[1] * cos + val * sin;

        if (offset != null){
            output[0] += offset[0];
            output[1] += offset[1];
            output[2] += offset[2];
        }

        return output;
    }

    /**
     * @param rotX Quantity of 90 degree rotations to do around the X-Axis
     * @param rotY Quantity of 90 degree rotations to do around the Y-Axis
     * @param rotZ Quantity of 90 degree rotations to do around the Z-Axis
     * @param inputVec Vector to rotate, formatted as a double array of length 3
     * @param outputVec Vector to store the result in, generates a new double array if set to null
     * @return Rotated vector
     */
    public static double @NotNull [] quickRotate(int rotX, int rotY, int rotZ, double @NotNull [] inputVec, double @Nullable [] outputVec, double @Nullable [] offset){
        double[] output = outputVec == null ? new double[3]: outputVec;

        output[0] = inputVec[0];
        output[1] = inputVec[1];
        output[2] = inputVec[2];

        if (offset != null){
            output[0] -= offset[0];
            output[1] -= offset[1];
            output[2] -= offset[2];
        }

        rotX &= 0b11;
        rotY &= 0b11;
        rotZ &= 0b11;

        double temp;

        switch (rotX){
            case 0:
                break;
            case 2:
                output[2] *= -1;
                output[1] *= -1;
                break;
            case 3:
                output[2] *= -1;
                output[1] *= -1;
            case 1:
                temp = output[2];
                output[2] = -output[1];
                output[1] = temp;
                break;
        }

        switch (rotY){
            case 0:
                break;
            case 2:
                output[0] *= -1;
                output[2] *= -1;
                break;
            case 3:
                output[0] *= -1;
                output[2] *= -1;
            case 1:
                temp = output[0];
                output[0] = -output[2];
                output[2] = temp;
                break;
        }

        switch (rotZ){
            case 0:
                break;
            case 2:
                output[0] *= -1;
                output[1] *= -1;
                break;
            case 3:
                output[0] *= -1;
                output[1] *= -1;
            case 1:
                temp = output[0];
                output[0] = -output[1];
                output[1] = temp;
                break;
        }

        if (offset != null){
            output[0] += offset[0];
            output[1] += offset[1];
            output[2] += offset[2];
        }

        return output;
    }
    public static double[] scaleVector(double scalar, double @NotNull [] inputVec, double @Nullable [] outputVec){
        double[] output = outputVec == null ? new double[3]: outputVec;
        output[0] = inputVec[0] * scalar;
        output[1] = inputVec[1] * scalar;
        output[2] = inputVec[2] * scalar;

        return output;
    }
    public static double[] addVector(double @NotNull [] leftVec, double @NotNull [] rightVec, double @Nullable [] outputVec){
        double[] output = outputVec == null ? new double[3]: outputVec;
        output[0] = leftVec[0] + rightVec[0];
        output[1] = leftVec[1] + rightVec[1];
        output[2] = leftVec[2] + rightVec[2];

        return output;
    }
    public static double[] subVector(double @NotNull [] leftVec, double @NotNull [] rightVec, double @Nullable [] outputVec){
        double[] output = outputVec == null ? new double[3]: outputVec;
        output[0] = leftVec[0] - rightVec[0];
        output[1] = leftVec[1] - rightVec[1];
        output[2] = leftVec[2] - rightVec[2];

        return output;
    }
    // Copied directly from dragonfly so probably shite
    private static double getReScalar(double angle){
        final double modRange = 90f;
        double v = (Math.abs(angle) - (modRange/2));
        if (v < 0) {
            v += modRange;
        }
        double x = (v % modRange) - (modRange/2);
        double slope = Math.tan(Math.toRadians(x));
        double scalar = (Math.sqrt(Math.pow((-0.5 * slope + 0.5), 2) + Math.pow((0.5 * slope + 0.5), 2)) * Math.sqrt(2));
        if (scalar > Math.sqrt(2)){
            throw new IllegalArgumentException(String.valueOf(scalar));
        }
        return scalar;
    }
}
