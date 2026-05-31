package lumien.randomthings.block;

import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.render.MessageLightRedirector;
import lumien.randomthings.tileentity.TileEntityLightRedirector;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockLightRedirector extends BlockContainerBase
{
	public static PropertyBool[] enabledProperties;

	static
	{
		enabledProperties = new PropertyBool[EnumFacing.VALUES.length];
		for (int i = 0; i < EnumFacing.VALUES.length; i++)
		{
			enabledProperties[i] = PropertyBool.create(EnumFacing.VALUES[i].getName());
		}
	}

	public BlockLightRedirector()
	{
		super("lightRedirector", Material.GROUND);

		this.setSoundType(SoundType.WOOD);
		this.setHardness(2);
	}

	@Override
	public int getMetaFromState(@Nonnull IBlockState state)
	{
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			TileEntity te = worldIn.getTileEntity(pos);

			if (te instanceof TileEntityLightRedirector)
			{
				((TileEntityLightRedirector) te).toggleSide(facing);
			}
		}

		return true;
	}

	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityLightRedirector)
		{
			return ((TileEntityLightRedirector) te).makeState(state);
		}
		else
		{
			return super.getActualState(state, worldIn, pos);
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, enabledProperties);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityLightRedirector)
			((TileEntityLightRedirector) te).broken();

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (!worldIn.isRemote)
		{
			MessageLightRedirector message = new MessageLightRedirector(worldIn.provider.getDimension(), pos);

			MessageUtil.sendToAllWatchingPos(worldIn, pos, message);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityLightRedirector();
	}
}
