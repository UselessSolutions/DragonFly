package useless.dragonfly.testentity;

import useless.dragonfly.model.entity.BenchEntityModel;

public class ZombieModelTest extends BenchEntityModel {

	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		if (this.getIndexBones().containsKey("bone")) {
			this.getIndexBones().get("bone").setRotationAngle(0, ticksExisted, 0);
		}
	}
}
