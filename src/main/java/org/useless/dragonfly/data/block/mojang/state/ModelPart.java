
package org.useless.dragonfly.data.block.mojang.state;

import java.util.Random;

public class ModelPart {
	public AppliedData[] apply;
	public Condition when;
	public double weightAccum = 0;
	public AppliedData getRandomModel(Random random){
		double r = random.nextDouble() * this.weightAccum;
		for (AppliedData entry : apply) {
			if (!(entry.weightAccum >= r)) continue;
			return entry;
		}
		return null;
	}
}
