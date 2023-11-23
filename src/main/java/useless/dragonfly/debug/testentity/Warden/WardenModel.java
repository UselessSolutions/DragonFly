package useless.dragonfly.debug.testentity.Warden;

import net.minecraft.core.util.helper.MathHelper;
import useless.dragonfly.model.entity.BenchEntityModel;

public class WardenModel extends BenchEntityModel {
	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		if (this.getIndexBones().containsKey("head")) {
			this.getIndexBones().get("head").setRotationAngle((float) Math.toRadians(headPitch), (float) Math.toRadians(headYaw), 0);
		}
		if (this.getIndexBones().containsKey("right_arm")) {
			this.getIndexBones().get("right_arm").setRotationAngle(MathHelper.cos(limbSwing * (2f/3) + MathHelper.PI) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("left_arm")) {
			this.getIndexBones().get("left_arm").setRotationAngle(MathHelper.cos(limbSwing * (2f/3)) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("right_leg")) {
			this.getIndexBones().get("right_leg").setRotationAngle(MathHelper.cos(limbSwing * (2f/3)) * 1.4f * limbYaw, 0, 0);
		}
		if (this.getIndexBones().containsKey("left_leg")) {
			this.getIndexBones().get("left_leg").setRotationAngle(MathHelper.cos(limbSwing * (2f/3) + MathHelper.PI) * 1.4f * limbYaw, 0, 0);
		}

		if (this.getIndexBones().containsKey("right_ribcage")) {
			this.getIndexBones().get("right_ribcage").setRotationAngle(0, Math.max(MathHelper.cos(ticksExisted/20), 0), 0);
		}
		if (this.getIndexBones().containsKey("left_ribcage")) {
			this.getIndexBones().get("left_ribcage").setRotationAngle(0, -Math.max(MathHelper.cos(ticksExisted/20), 0), 0);
		}

		if (this.getIndexBones().containsKey("right_tendril")) {
			this.getIndexBones().get("right_tendril").setRotationAngle(0, Math.max(MathHelper.sin(ticksExisted/2) - 0.8f, 0), 0);
		}
		if (this.getIndexBones().containsKey("left_tendril")) {
			this.getIndexBones().get("left_tendril").setRotationAngle(0, -Math.max(MathHelper.sin(ticksExisted/2) - 0.8f, 0), 0);
		}
	}
}
