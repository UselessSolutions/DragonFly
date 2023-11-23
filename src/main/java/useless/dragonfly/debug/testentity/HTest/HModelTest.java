package useless.dragonfly.debug.testentity.HTest;

import useless.dragonfly.model.entity.BenchEntityModel;

public class HModelTest extends BenchEntityModel {
	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		if (this.getIndexBones().containsKey("Stand")) {
			this.getIndexBones().get("Stand").setRotationAngle(ticksExisted/400, 0, 0);
		}
		if (this.getIndexBones().containsKey("Dobble")) {
			this.getIndexBones().get("Dobble").setRotationAngle(0, ticksExisted, 0);
		}
	}
}
