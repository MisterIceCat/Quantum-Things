package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockBeanStalk extends BlockBase implements IPlantable
{
	private static final int MAX_HEIGHT = 512;
	public static final PropertyBool SHOULD_GROW = PropertyBool.create("should_grow");

	boolean strongMagic;

	protected static final AxisAlignedBB STALK_AABB = new AxisAlignedBB(0.4f, 0, 0.4f, 0.6f, 1, 0.6f);

	protected BlockBeanStalk(boolean strongMagic)
	{
		super(strongMagic ? "beanStalk" : "lesserBeanStalk", Material.PLANTS);

		this.setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SHOULD_GROW, false));

		this.strongMagic = strongMagic;
	}

	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos)
	{
		return STALK_AABB;
	}

	@Override
	public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Entity entityIn)
	{
		if (entityIn.onGround || entityIn.collidedVertically)
			return;

		double speed = strongMagic ? 0.5 : 0.2;

		if (entityIn.motionY >= 0.1)
		{
			Block top = entityIn.world.getBlockState(new BlockPos(MathHelper.floor(entityIn.posX), MathHelper.floor(entityIn.posY) + 3, MathHelper.floor(entityIn.posZ))).getBlock();
			if (top == this)
			{
				entityIn.setPosition(entityIn.posX, entityIn.posY + speed, entityIn.posZ);
			}
		}
		else if (entityIn.motionY <= -0.1)
		{
			Block bottom = entityIn.world.getBlockState(new BlockPos(MathHelper.floor(entityIn.posX), MathHelper.floor(entityIn.posY) - 3, MathHelper.floor(entityIn.posZ))).getBlock();
			if (bottom == null || bottom == this)
			{ // prevent clipping into block
				entityIn.setPosition(entityIn.posX, entityIn.posY - speed, entityIn.posZ);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList list)
	{
    }

	@Override
	public void updateTick(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (!state.getValue(SHOULD_GROW))
				return;

			// set has grown to true
			worldIn.setBlockState(pos, state.withProperty(SHOULD_GROW, false));

			// Grows up to 512 blocks tall, or the height of the world, whichever is lower.
			final int maxHeight = Math.min(512, worldIn.getHeight());
			if (strongMagic)
			{
				if (pos.getY() >= maxHeight - 2)
				{
					IBlockState podReplace = worldIn.getBlockState(pos.up());

					if (podReplace.getBlock().getBlockHardness(podReplace, worldIn, pos.up()) >= 0)
					{
						worldIn.setBlockState(pos.up(), ModBlocks.beanPod.getDefaultState());
					}
					return;
				}
			}
			else
			{
				if (pos.getY() >= maxHeight || !worldIn.isAirBlock(pos.up()))
				{
					return;
				}
			}

			IBlockState upState = worldIn.getBlockState(pos.up());
			if (upState.getBlock().getBlockHardness(upState, worldIn, pos.up()) != -1)
			{
				if (!worldIn.isAirBlock(pos.up()))
				{
					worldIn.playEvent(2001, pos.up(), Block.getStateId(upState));
				}
				else
				{
					worldIn.playEvent(2001, pos,
							Block.getStateId(this.getDefaultState()));
					worldIn.playSound(null, pos, this.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1, 2);
				}

				worldIn.playEvent(2005, pos.up(), 0);
				worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(SHOULD_GROW, true));
				worldIn.scheduleUpdate(pos.up(), this, strongMagic ? 1 : 5);
			}
			else
			{
				worldIn.setBlockState(pos, ModBlocks.beanPod.getDefaultState());
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer)
	{
		if (!worldIn.isRemote)
		{
			worldIn.scheduleUpdate(pos, this, 5);
		}
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	@Override
	public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos changedPos)
	{
		this.checkForDrop(worldIn, pos, state);
	}

	protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.canBlockStay(worldIn, pos))
		{
			return true;
		}
		else
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		if (worldIn.isAirBlock(pos.down()))
		{
			return false;
		}
		IBlockState down = worldIn.getBlockState(pos.down());
		// Allow placement on bean stalks (for stacking)
		// or blocks that can sustain plants
		if (down.getBlock() instanceof BlockBeanStalk)
			return true;

		return down.getBlock().canSustainPlant(down, worldIn, pos.down(), EnumFacing.UP, this);
	}

	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		return this.canPlaceBlockAt(worldIn, pos);
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
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isLadder(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SHOULD_GROW) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(SHOULD_GROW, meta == 1);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHOULD_GROW);
	}

	@Override
	public int quantityDropped(@Nonnull Random random)
	{
		return 0;
	}

	@Override
	public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune)
	{
		return null;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this)
			return getDefaultState();
		return state;
	}
}
