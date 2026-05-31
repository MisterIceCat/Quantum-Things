package lumien.randomthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ContainerEmptyContainer extends Container
{
	public ContainerEmptyContainer(EntityPlayer player, World world, int x, int y, int z)
	{

	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player)
	{
		return true;
	}

}
