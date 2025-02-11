package org.useless.dragonfly.models.entity;

public final class BoneTransform {
    public double posX = 0;
    public double posY = 0;
    public double posZ = 0;

    public double rotX = 0;
    public double rotY = 0;
    public double rotZ = 0;

    public double scaleX = 1;
    public double scaleY = 1;
    public double scaleZ = 1;

    public boolean visible = true;
    public void reset() {
        this.posX = 0;
        this.posY = 0;
        this.posZ = 0;

        this.rotX = 0;
        this.rotY = 0;
        this.rotZ = 0;

        this.scaleX = 1;
        this.scaleY = 1;
        this.scaleZ = 1;

        this.visible = true;
    }
}
