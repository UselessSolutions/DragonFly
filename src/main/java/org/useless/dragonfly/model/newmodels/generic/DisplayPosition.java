package org.useless.dragonfly.model.newmodels.generic;

public final class DisplayPosition {
    public static final String GROUND = "ground";
    public static final String HEAD = "head";
    public static final String FIRSTPERSON_RIGHTHAND = "firstperson_righthand";
    public static final String THRIDPERSON_RIGHTHAND = "thirdperson_righthand";
    public static final String FIXED = "fixed";
    public static final DisplayPosition defaultPos = new DisplayPosition(new double[]{0, 0, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0});
    public final double[] rotation;
    public final double[] translation;
    public final double[] scale;
    public DisplayPosition(double[] rotation, double[] translation, double[] scale){
        assert rotation.length == 3;
        assert translation.length == 3;
        assert scale.length == 3;

        this.rotation = rotation;
        this.translation = translation;
        this.scale = scale;
    }
}
