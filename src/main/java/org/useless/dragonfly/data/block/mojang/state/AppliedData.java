package org.useless.dragonfly.data.block.mojang.state;

public class AppliedData {
	public String model;
	public int x;
	public int y;
	public boolean uvlock;
	public int weight;
	public double weightAccum = 0;
	public AppliedData(){
		this(null, 0, 0, false, 1);
	}
	public AppliedData(String model, int x, int y, boolean uvlock, int weight){
        this.model = model;
        this.x = x;
        this.y = y;
        this.uvlock = uvlock;
        this.weight = weight;
    }

	@Override
	public String toString() {
		String builder =
			"model: " + model + "\n" +
			"x: " + x + "\n" +
			"y: " + y + "\n" +
			"uvlock: " + uvlock + "\n";
		return builder;
	}
}
