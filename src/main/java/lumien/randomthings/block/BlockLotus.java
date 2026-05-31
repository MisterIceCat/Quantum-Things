package lumien.randomthings.block;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lumien.randomthings.config.Lotus;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLotus extends BlockBase implements IPlantable, IGrowable
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.3125D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5625D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.6875D, 0.9375D), new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D) };

	public BlockLotus()
	{
		super("lotus", Material.PLANTS);

		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
		this.setHardness(0.0F);

		this.setCreativeTab(null);
	}

	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player)
	{
		return new ItemStack(ModItems.lotusSeeds);
	}

	@Override
	public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune)
	{
		return ModItems.lotusSeeds;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos)
	{
		return CROPS_AABB[state.getValue(AGE)];
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AGE);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(AGE, meta);
	}

	@Override
	public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (state.getValue(AGE) == 3)
		{
			if (worldIn.isRemote)
			{
				return true;
			}
			else
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, 0));
				playerIn.addItemStackToInventory(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.LOTUS_BLOSSOM.id));
			}
		}

		return false;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this)
			return getDefaultState();
		return state;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		return NULL_AABB;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
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

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND;
	}

	@Override
	public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, IBlockState state, @Nonnull Random rand)
	{
		int i = state.getValue(AGE);

		if (i < 3)
		{
			if (rand.nextInt(10) == 0)
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
			}
		}

		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this)
		{
			IBlockState soil = worldIn.getBlockState(pos.down());
			return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
		}
		return this.canSustainBush(worldIn.getBlockState(pos.down()));
	}

	@Override
	public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(AGE) < 3;
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state)
	{
		return Lotus.CAN_BONEMEAL;
	}

	// Grow by bonemeal
	@Override
	public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, IBlockState state)
	{
		int i = state.getValue(AGE);
		if (i < 3) {
			worldIn.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
		}
	}
}
