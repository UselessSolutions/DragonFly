package org.useless.dragonfly.model.newmodels.generic.components.block;

import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;

public final class BlockFaceBuilder {
    @NotNull
    private Side cullSide = Side.NONE;
    private boolean doubledSided = false;
    private boolean flippedNormal = false;
    private boolean useColor = true;
    private double[] manualUVs = null;
    private final int[] uvSize = new int[]{16, 16};
    private int texRotation = 0;
    private boolean flipU = false;
    private boolean flipV = false;
    private String textureSymbol;
    public BlockFaceBuilder(String textureSymbol){
        this.textureSymbol = textureSymbol;
    }
    public BlockFaceBuilder setIcon(String textureSymbol){
        this.textureSymbol = textureSymbol;
        return this;
    }
    public BlockFaceBuilder setCullSide(Side cullSide){
        this.cullSide = cullSide;
        return this;
    }

    public BlockFaceBuilder setDoubleSided(boolean doubledSided){
        this.doubledSided = doubledSided;
        return this;
    }

    public BlockFaceBuilder setFlippedNormals(boolean flippedNormal){
        this.flippedNormal = flippedNormal;
         return this;
    }

    public BlockFaceBuilder setUseColor(boolean useColor){
        this.useColor = useColor;
        return this;
    }
    public BlockFaceBuilder setTexMirrorX(boolean mirrorX){
        this.flipU = mirrorX;
        return this;
    }
    public BlockFaceBuilder setTexMirrorY(boolean mirrorY){
        this.flipV = mirrorY;
        return this;
    }

    public BlockFaceBuilder setTexRotation(int texRotation){
        this.texRotation = texRotation & 0b11;
        return this;
    }

    public BlockFaceBuilder setUVScale(int iconWidth, int iconHeight){
        this.uvSize[0] = iconWidth;
        this.uvSize[1] = iconHeight;
        return this;
    }

    public BlockFaceBuilder setManualUVs(double minU, double minV, double widthU, double widthV){
        this.manualUVs = new double[]{minU, minV, widthU, widthV};
        return this;
    }

    public BlockFace build(double[] vTopLeft, double[] vTopRight, double[] vBottomRight, double[] vBottomLeft,
                           double[][] defaultUVs){
        double[][] uvs;
        if (manualUVs != null){
            double _minU = manualUVs[0]/uvSize[0];
            double _minV = manualUVs[1]/uvSize[1];
            double _maxU = _minU + manualUVs[2]/uvSize[0];
            double _maxV = _minV + manualUVs[3]/uvSize[1];
            uvs = new double[][]{
                {_minU, _minV},
                {_maxU, _minV},
                {_maxU, _maxV},
                {_minU, _maxV}
            };
        } else {
            uvs = defaultUVs;
        }

        if (flipU){
            double[] _t1 = uvs[0];
            double[] _t2 = uvs[2];
            uvs[0] = uvs[1];
            uvs[2] = uvs[3];
            uvs[1] = _t1;
            uvs[3] = _t2;
        }

        if (flipV){
            double[] _t1 = uvs[0];
            double[] _t2 = uvs[1];
            uvs[0] = uvs[3];
            uvs[1] = uvs[2];
            uvs[2] = _t2;
            uvs[3] = _t1;
        }

        return new BlockFace(textureSymbol, cullSide, doubledSided, flippedNormal, useColor, defaultUVs,
            vTopLeft, vTopRight, vBottomRight, vBottomLeft,
            uvs[(0 + texRotation) & 0b11], uvs[(1 + texRotation) & 0b11], uvs[(2 + texRotation) & 0b11], uvs[(3 + texRotation) & 0b11]);
    }
}
