package lumien.randomthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ContainerCustomWorkbench extends ContainerWorkbench
{

	public ContainerCustomWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn)
	{
		super(playerInventory, worldIn, posIn);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}
}
