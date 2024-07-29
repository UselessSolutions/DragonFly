package org.useless.dragonfly.model.newmodels.generic.components.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class BlockComponentBuilder {
    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;
    private double rotX = 0;
    private double rotY = 0;
    private double rotZ = 0;
    private boolean rescaleX = false;
    private boolean rescaleY = false;
    private boolean rescaleZ = false;
    private final double[] rotationOrigin = new double[]{0.5, 0.5, 0.5};
    private boolean shade = true;
    private boolean forceInnerLighting = false;
    public final Map<Side, BlockFaceBuilder> faceBuilderMap = new HashMap<>();
    public final Map<Side, BlockFaceBuilder> overbrightFaceBuilderMap = new HashMap<>();
    public BlockComponentBuilder(Block referenceBlock){
        this(referenceBlock.minX, referenceBlock.minY, referenceBlock.minZ, referenceBlock.maxX, referenceBlock.maxY, referenceBlock.maxZ);
    }
    public BlockComponentBuilder(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    public BlockComponentBuilder setRotationX(double rotationX){
        this.rotX = rotationX;
        return this;
    }
    public BlockComponentBuilder setRotationY(double rotationY){
        this.rotY = rotationY;
        return this;
    }
    public BlockComponentBuilder setRotationZ(double rotationZ){
        this.rotZ = rotationZ;
        return this;
    }
    public BlockComponentBuilder setRescaleX(boolean rescaleX){
        this.rescaleX = rescaleX;
        return this;
    }
    public BlockComponentBuilder setRescaleY(boolean rescaleY){
        this.rescaleY = rescaleY;
        return this;
    }
    public BlockComponentBuilder setRescaleZ(boolean rescaleZ){
        this.rescaleZ = rescaleZ;
        return this;
    }
    public BlockComponentBuilder setUseShade(boolean shade){
        this.shade = shade;
        return this;
    }
    public BlockComponentBuilder setForceInnerLighting(boolean forceInnerLighting){
        this.forceInnerLighting = forceInnerLighting;
        return this;
    }
    public BlockComponentBuilder setRotationOrigin(double x, double y, double z){
        rotationOrigin[0] = x;
        rotationOrigin[1] = y;
        rotationOrigin[2] = z;
        return this;
    }
    public BlockComponentBuilder setFace(@NotNull Side side, @Nullable BlockFaceBuilder faceBuilder){
        if (side == Side.NONE) throw new IllegalArgumentException("Cannot set face to side NONE!");
        faceBuilderMap.put(side, faceBuilder);
        return this;
    }
    public BlockComponentBuilder setFaces(@Nullable BlockFaceBuilder faceBuilder){
        for (Side side : Side.sides){
            setFace(side, faceBuilder);
        }
        return this;
    }
    public BlockComponentBuilder setOverbrightFace(@NotNull Side side, @Nullable BlockFaceBuilder faceBuilder){
        if (side == Side.NONE) throw new IllegalArgumentException("Cannot set face to side NONE!");
        overbrightFaceBuilderMap.put(side, faceBuilder);
        return this;
    }
    public BlockComponentBuilder setOverbrightFaces(@Nullable BlockFaceBuilder faceBuilder){
        for (Side side : Side.sides){
            setOverbrightFace(side, faceBuilder);
        }
        return this;
    }
    public BlockComponent build(){
        return new BlockComponent(
            minX, minY, minZ,
            maxX, maxY, maxZ,
            rotX, rotY, rotZ,
            rescaleX, rescaleY, rescaleZ,
            rotationOrigin, shade, forceInnerLighting,
            new BlockFaceBuilder[]{
                faceBuilderMap.getOrDefault(Side.BOTTOM, null),
                faceBuilderMap.getOrDefault(Side.TOP, null),
                faceBuilderMap.getOrDefault(Side.NORTH, null),
                faceBuilderMap.getOrDefault(Side.SOUTH, null),
                faceBuilderMap.getOrDefault(Side.WEST, null),
                faceBuilderMap.getOrDefault(Side.EAST, null),
            },
            new BlockFaceBuilder[]{
                overbrightFaceBuilderMap.getOrDefault(Side.BOTTOM, null),
                overbrightFaceBuilderMap.getOrDefault(Side.TOP, null),
                overbrightFaceBuilderMap.getOrDefault(Side.NORTH, null),
                overbrightFaceBuilderMap.getOrDefault(Side.SOUTH, null),
                overbrightFaceBuilderMap.getOrDefault(Side.WEST, null),
                overbrightFaceBuilderMap.getOrDefault(Side.EAST, null),
            });
    }
}
