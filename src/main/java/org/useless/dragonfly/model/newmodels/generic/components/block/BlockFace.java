package org.useless.dragonfly.model.newmodels.generic.components.block;

import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BlockFace {
    public final double[] v1;
    public final double[] v2;
    public final double[] v3;
    public final double[] v4;
    public final double[] t1;
    public final double[] t2;
    public final double[] t3;
    public final double[] t4;
    public final double[] normal = new double[3];
    public final double[] top = new double[3];
    public final double[] left = new double[3];
    public final double[][] defaultUVs;
    @NotNull
    public final Side cullSide;
    public final boolean doubledSided;
    public final boolean flippedNormal;

    public final boolean useColor;
    public final String textureSymbol;
    public BlockFace(String textureSymbol, @Nullable Side cullSide, boolean doubledSided, boolean flippedNormal, boolean useColor, double[][] defaultUVs,
                     double[] v1, double[] v2, double[] v3, double[] v4,
                     double[] t1, double[] t2, double[] t3, double[] t4){

        this.textureSymbol = textureSymbol;

        if (cullSide == null || doubledSided) cullSide = Side.NONE;
        this.cullSide = cullSide;

        this.doubledSided = doubledSided;
        this.flippedNormal = flippedNormal;
        this.useColor = useColor;

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;

        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;

        this.defaultUVs = defaultUVs;

        double[] _J = new double[]{
            v3[0] - v1[0],
            v3[1] - v1[1],
            v3[2] - v1[2]};

        double[] _K = new double[]{
            v2[0] - v1[0],
            v2[1] - v1[1],
            v2[2] - v1[2]};

        normal[0] = (_J[1] * _K[2]) - (_J[2] * _K[1]);
        normal[1] = (_J[2] * _K[0]) - (_J[0] * _K[2]);
        normal[2] = (_J[0] * _K[1]) - (_J[1] * _K[0]);

        makeUnit(normal);

        top[0] = v1[0] - v4[0];
        top[1] = v1[1] - v4[1];
        top[2] = v1[2] - v4[2];

//        if (flipTop) {
//            top[0] *= -1;
//            top[1] *= -1;
//            top[2] *= -1;
//        }

        makeUnit(top);

        left[0] = v1[0] - v2[0];
        left[1] = v1[1] - v2[1];
        left[2] = v1[2] - v2[2];

//        if (flipLeft) {
//            left[0] *= -1;
//            left[1] *= -1;
//            left[2] *= -1;
//        }

        makeUnit(left);
    }
    private void makeUnit(double[] vector){
        double scalar = 1d/Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);

        vector[0] *= scalar;
        vector[1] *= scalar;
        vector[2] *= scalar;
    }
}
