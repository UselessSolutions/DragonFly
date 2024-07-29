package org.useless.dragonfly.model.newmodels.generic;

public class ModelEntry {
    private static final int valuesPerDegree = 360;
    private static final int valuesPer90Degrees = valuesPerDegree * 90;
    private int rotationY = 0;
    private int rotationX = 0;
    private int rotationZ = 0;
    private float offsetX = 0;
    private float offsetY = 0;
    private float offsetZ = 0;
    public final StaticModel model;
    public ModelEntry(StaticModel model){
        this.model = model;
    }
    public void setOffsetX(float offsetX){
        this.offsetX = offsetX;
    }
    public void setOffsetY(float offsetY){
        this.offsetY = offsetY;
    }
    public void setOffsetZ(float offsetZ){
        this.offsetZ = offsetZ;
    }
    public float getOffsetX(){
        return offsetX;
    }
    public float getOffsetY(){
        return offsetY;
    }
    public float getOffsetZ(){
        return offsetZ;
    }
    public void setRotationX(float rotationX){
        this.rotationX = Math.round(rotationX * valuesPerDegree);
    }
    public void setRotationY(float rotationY){
        this.rotationY = Math.round(rotationY * valuesPerDegree);
    }
    public void setRotationZ(float rotationZ){
        this.rotationZ = Math.round(rotationZ * valuesPerDegree);
    }
    public float getRotationX(){
        return (float) rotationX / valuesPerDegree;
    }
    public float getRotationY(){
        return (float) rotationY / valuesPerDegree;
    }
    public float getRotationZ(){
        return (float) rotationZ / valuesPerDegree;
    }
    public int get90DegRotationX(){
        return rotationX/valuesPer90Degrees;
    }
    public int get90DegRotationY(){
        return rotationY/valuesPer90Degrees;
    }
    public int get90DegRotationZ(){
        return rotationZ/valuesPer90Degrees;
    }
}
