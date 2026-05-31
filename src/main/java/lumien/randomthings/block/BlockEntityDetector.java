package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityEntityDetector;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockEntityDetector extends BlockContainerBase
{

	public BlockEntityDetector()
	{
		super("entityDetector", Material.ROCK);

		this.setHardness(1.5F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityEntityDetector();
	}

	@Override
	public boolean isSideSolid(@Nonnull IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return true;
	}

	@Override
	public int getWeakPower(@Nonnull IBlockState blockState, IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		TileEntity teRaw = blockAccess.getTileEntity(pos);
		if (!(teRaw instanceof TileEntityEntityDetector))
			return 0;
		TileEntityEntityDetector te = (TileEntityEntityDetector) teRaw;

		// Return the power level based on the current power mode
		return te.getPowerLevel();
	}

	@Override
	public int getStrongPower(@Nonnull IBlockState blockState, IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		TileEntity teRaw = blockAccess.getTileEntity(pos);
		if (!(teRaw instanceof TileEntityEntityDetector))
			return 0;
		TileEntityEntityDetector te = (TileEntityEntityDetector) teRaw;

		if (te.isPowered() && te.getPowerMode() == TileEntityEntityDetector.POWER_MODE.STRONG)
		{
			// Return the power level based on the current power mode
			return te.getPowerLevel();
		}
		else
		{
			return super.getStrongPower(blockState, blockAccess, pos, side);
		}
	}

	@Override
	public boolean isNormalCube(@Nonnull IBlockState state)
	{
		return true;
	}

	@Override
	public boolean shouldCheckWeakPower(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return false;
	}

	@Override
	public boolean canProvidePower(@Nonnull IBlockState state)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ENTITY_DETECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity teRaw = worldIn.getTileEntity(pos);
		if (teRaw instanceof TileEntityEntityDetector)
		{
			TileEntityEntityDetector tileentity = (TileEntityEntityDetector) teRaw;
			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity.getInventory());

			if (tileentity.getPowerMode() == TileEntityEntityDetector.POWER_MODE.STRONG)
			{
				for (EnumFacing facing : EnumFacing.VALUES)
				{
					worldIn.notifyNeighborsOfStateChange(pos.offset(facing), ModBlocks.entityDetector, false);
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}
}
