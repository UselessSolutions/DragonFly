package useless.dragonfly.debug.testentity.Zombie;

import net.minecraft.core.entity.monster.EntityHuman;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class EntityZombieTest extends EntityHuman {
	public EntityZombieTest(World world) {
		super(world);
	}

	public ItemStack getHeldItem() {
		return new ItemStack(Item.toolSwordWood);
	}

	@Override
	public String getEntityTexture() {
		return "/assets/dragonfly/entity/zombie_test.png";
	}

	@Override
	public String getDefaultEntityTexture() {
		return "/assets/dragonfly/entity/zombie_test.png";
	}
}
