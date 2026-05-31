package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BlockContainerBase extends BlockBase
{
	protected BlockContainerBase(String name, Material materialIn)
	{
		super(name, materialIn);
	}

	protected BlockContainerBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
	{
		super(name, materialIn, itemBlock);
	}

	@Override
	public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityBase)
		{
			((TileEntityBase) te).breakBlock(worldIn, pos, state);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void neighborChanged(@Nonnull IBlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos changedPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityBase)
		{
			((TileEntityBase) te).neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
		}
	}

	@Override
	public boolean hasTileEntity(@Nonnull IBlockState state)
	{
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state);

	@Override
	public boolean eventReceived(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
}
