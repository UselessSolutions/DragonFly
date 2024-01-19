package useless.dragonfly.model.blockstates.data;

import java.util.Random;

public class ModelPart {
	public VariantData[] apply;
	public Condition when;
	public double weightAccum = 0;
	public VariantData getRandomModel(Random random){
		double r = random.nextDouble() * this.weightAccum;
		for (VariantData entry : apply) {
			if (!(entry.weightAccum >= r)) continue;
			return entry;
		}
		return apply[0];
	}
}
