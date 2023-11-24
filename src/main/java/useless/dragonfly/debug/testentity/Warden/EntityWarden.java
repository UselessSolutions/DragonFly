package useless.dragonfly.debug.testentity.Warden;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityHuman;
import net.minecraft.core.world.World;
import useless.dragonfly.model.entity.AnimationState;

public class EntityWarden extends EntityHuman {
	public AnimationState emergeState = new AnimationState();
	public AnimationState diggingState = new AnimationState();
	public AnimationState attackState = new AnimationState();
	public EntityWarden(World world) {
		super(world);
	}

	@Override
	public void spawnInit() {
		super.spawnInit();
		this.emergeState.start(this.tickCount);
	}

	@Override
	protected void attackEntity(Entity entity, float distance) {
		if (this.attackTime <= 0 && distance < 2.0f && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
			this.attackState.start(this.tickCount);
		}
		super.attackEntity(entity, distance);
	}

	@Override
	public void handleEntityEvent(byte byte0, float attackedAtYaw) {
		if (byte0 == 4) {

		} else if (byte0 == 5) {

		} else {
			super.handleEntityEvent(byte0, attackedAtYaw);
		}
	}

	@Override
	public String getEntityTexture() {
		return "/assets/dragonfly/entity/warden.png";
	}

	@Override
	public String getDefaultEntityTexture() {
		return "/assets/dragonfly/entity/warden.png";
	}
}
