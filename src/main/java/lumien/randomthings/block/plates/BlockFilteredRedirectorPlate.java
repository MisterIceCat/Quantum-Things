package lumien.randomthings.block.plates;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockContainerBase;
import lumien.randomthings.lib.EntityFilterItemStack;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityFilteredRedirectorPlate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockFilteredRedirectorPlate extends BlockContainerBase
{
	protected static final AxisAlignedBB AABB = null;
	protected static final AxisAlignedBB VISUAL_AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.03125D, 1D);

	public static final PropertyDirection INPUT_FACING = PropertyDirection.create("inputfacing", EnumFacing.Plane.HORIZONTAL);

	public BlockFilteredRedirectorPlate()
	{
		super("plate_filteredredirector", Material.GROUND);

		this.setHardness(0.3f);
		this.setSoundType(SoundType.STONE);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity teRaw = worldIn.getTileEntity(pos);
		if (teRaw instanceof TileEntityFilteredRedirectorPlate)
			InventoryHelper.dropInventoryItems(worldIn, pos,
					((TileEntityFilteredRedirectorPlate) teRaw).getInventory());

		super.breakBlock(worldIn, pos, state);
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
		EnumFacing currentInput = state.getValue(INPUT_FACING);

		return currentInput.ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing input = EnumFacing.values()[meta];

		if (INPUT_FACING.getAllowedValues().contains(input))
		{
			return this.getDefaultState().withProperty(INPUT_FACING, input);
		}
		else
		{
			return this.getDefaultState();
		}
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, INPUT_FACING);
	}

	@Override
	public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn)
	{
		super.onEntityCollision(worldIn, pos, state, entityIn);

		Vec3d motionVec = new Vec3d(entityIn.motionX, entityIn.motionY, entityIn.motionZ);

		EnumFacing roughMovingFacing = EnumFacing.getFacingFromVector((float) motionVec.x, (float) motionVec.y, (float) motionVec.z).getOpposite();

		Vec3d center = new Vec3d(pos).add(0.5, 0, 0.5);
		Vec3d difVec = center.subtract(entityIn.getPositionVector());

		EnumFacing facing = EnumFacing.getFacingFromVector((float) difVec.x, (float) difVec.y, (float) difVec.z).getOpposite();

		EnumFacing inputSide = state.getValue(INPUT_FACING);

		if ((facing == inputSide || facing == inputSide.getOpposite()) && facing == roughMovingFacing)
		{
			TileEntity teRaw = worldIn.getTileEntity(pos);
			if (!(teRaw instanceof TileEntityFilteredRedirectorPlate))
			{
				return;
			}
			TileEntityFilteredRedirectorPlate te = (TileEntityFilteredRedirectorPlate) teRaw;

			EntityFilterItemStack[] filter = te.getFilter();

			EnumFacing output = facing.getOpposite();

			if (filter[0] != null)
			{
				if (filter[0].apply(entityIn))
				{
					output = inputSide.rotateY();
				}
			}

			if (filter[1] != null)
			{
				if (filter[1].apply(entityIn))
				{
					output = inputSide.rotateYCCW();
				}
			}

			Vec3d facingVec = new Vec3d(output.getDirectionVec()).scale(0.4).add(center);

			float dif = facing.getOpposite().getHorizontalAngle() - output.getHorizontalAngle();

			Vec3d outputMotionVec = motionVec.rotateYaw((float) Math.toRadians(dif));
			entityIn.setPosition(facingVec.x, facingVec.y, facingVec.z);

			entityIn.motionX = outputMotionVec.x;
			entityIn.motionY = outputMotionVec.y;
			entityIn.motionZ = outputMotionVec.z;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.FILTERED_REDIRECTOR_PLATE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
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
		return this.getDefaultState().withProperty(INPUT_FACING, placer.getHorizontalFacing().getOpposite());
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(INPUT_FACING);

			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
			{
				enumfacing = EnumFacing.NORTH;
			}
			else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
			{
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(INPUT_FACING, enumfacing), 2);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityFilteredRedirectorPlate();
	}
}
