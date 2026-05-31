package lumien.randomthings.block.plates;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockContainerBase;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityExtractionPlate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockExtractionPlate extends BlockContainerBase
{
	protected static final AxisAlignedBB AABB = null;
	protected static final AxisAlignedBB VISUAL_AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.03125D, 1D);

	public static final PropertyDirection OUTPUT_FACING = PropertyDirection.create("outputfacing", EnumFacing.Plane.HORIZONTAL);

	public BlockExtractionPlate()
	{
		super("plate_extraction", Material.GROUND);

		this.setHardness(0.3f);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		checkForDrop(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockAt(@Nonnull World worldIn, BlockPos pos)
	{
		return canPlaceOn(worldIn, pos.down());
	}

	private boolean canPlaceOn(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos, EnumFacing.UP);
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this && this.canPlaceOn(worldIn, pos.down()))
		{
			return true;
		}
		else
		{
			if (worldIn.getBlockState(pos).getBlock() == this)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	@Override
	public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos)
	{
		return VISUAL_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		return AABB;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

		return currentOutput.ordinal() - 2;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if (meta > 3)
		{
			meta = 0;
		}
		
		
		EnumFacing output = EnumFacing.values()[meta + 2];
		return this.getDefaultState().withProperty(OUTPUT_FACING, output);
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, OUTPUT_FACING);
	}

	@Override
	public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side == EnumFacing.UP && playerIn.isSneaking())
		{
			EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

			EnumFacing newOutput = EnumFacing.getFacingFromVector(hitX - 0.5F, 0, hitZ - 0.5F);

			if (currentOutput != newOutput)
			{
				if (!worldIn.isRemote)
				{
					worldIn.setBlockState(pos, state.withProperty(OUTPUT_FACING, newOutput));
				}
				return true;
			}
		}
		else
		{
			if (!worldIn.isRemote)
			{
				playerIn.openGui(RandomThings.instance, GuiIds.EXTRACTION_PLATE, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}

			return true;
		}

		return false;
	}

	@Override
	public void onBlockAdded(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state)
	{
		this.setDefaultFacing(worldIn, pos, state);
		this.checkForDrop(worldIn, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(OUTPUT_FACING, placer.getHorizontalFacing());
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{

		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityExtractionPlate();
	}
}
