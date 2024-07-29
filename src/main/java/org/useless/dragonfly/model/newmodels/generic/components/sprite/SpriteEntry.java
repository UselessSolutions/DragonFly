package org.useless.dragonfly.model.newmodels.generic.components.sprite;

public class SpriteEntry {
    public final String textureID;
    public final RenderType renderType;
    public final double[] position;
    public final double[] rotation;
    public final boolean overbright;
    public SpriteEntry(String textureID, boolean overbright, RenderType renderType, double[] position, double[] rotation){
        this.textureID = textureID;
        this.renderType = renderType;
        this.position = position;
        this.rotation = rotation;
        this.overbright = overbright;
    }
}
