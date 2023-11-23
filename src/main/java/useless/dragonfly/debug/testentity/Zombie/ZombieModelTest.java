package useless.dragonfly.debug.testentity.Zombie;

import net.minecraft.core.util.helper.MathHelper;
import useless.dragonfly.helper.AnimationHelper;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.entity.animation.Animation;

import static useless.dragonfly.DragonFly.MOD_ID;

public class ZombieModelTest extends BenchEntityModel {
	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		// If you need play some animation. you should reset with this
		this.getIndexBones().forEach((s, benchEntityBones) -> benchEntityBones.resetPose());
		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		if (this.getIndexBones().containsKey("Head")) {
			this.getIndexBones().get("Head").setRotationAngle((float) Math.toRadians(headPitch), (float) Math.toRadians(headYaw), 0);
		}
		if (this.getIndexBones().containsKey("bone")) {
			this.getIndexBones().get("bone").setRotationAngle(0, ticksExisted, 0);
		}
		if (this.getIndexBones().containsKey("RightArm")) {
			this.getIndexBones().get("RightArm").setRotationAngle(MathHelper.cos(limbSwing * (2f/3) + MathHelper.PI) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("LeftArm")) {
			this.getIndexBones().get("LeftArm").setRotationAngle(MathHelper.cos(limbSwing * (2f/3)) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		Animation testAnimation = AnimationHelper.getOrCreateEntityAnimation(MOD_ID, "zombie_test.animation");
		animateWalk(testAnimation.getAnimations().get("test"), limbSwing, limbYaw * 0.5F, 2.0F, 2.5F);
	}
}
