package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class BlockFertilizedDirt extends BlockBase
{
	boolean tilled;

	protected static final AxisAlignedBB TILLED_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);

	protected BlockFertilizedDirt(boolean tilled)
	{
		super("fertilizedDirt" + (tilled ? "Tilled" : ""), Material.GROUND);

		this.tilled = tilled;
		this.setTickRandomly(true);
		this.setHardness(0.6F);
		this.setSoundType(SoundType.GROUND);

		if (tilled)
		{
			this.setLightOpacity(255);
			this.setCreativeTab(null);
			this.useNeighborBrightness = true;
		}
		else {
			OreDictionary.registerOre("fertilizedDirt", this);
			OreDictionary.registerOre("dirt", this);
		}
	}

	@Override
	protected ItemStack getSilkTouchDrop(@Nonnull IBlockState state)
	{
		return new ItemStack(ModBlocks.fertilizedDirt);
	}

	@Override
	public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list)
	{
		if (!tilled)
		{
			super.getSubBlocks(tab, list);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos)
	{
		if (tilled)
		{
			return TILLED_AABB;
		}
		else
		{
			return super.getBoundingBox(state, source, pos);
		}
	}

	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player)
	{
		return new ItemStack(ModBlocks.fertilizedDirt, 1, 0);
	}

	@Override
	public String getTranslationKey()
	{
		return "tile.fertilizedDirt";
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		return FULL_BLOCK_AABB;
	}

	@Override
	public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.fertilizedDirt);
	}

	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		return !tilled;
	}

	@Override
	public boolean isFertile(@Nonnull World world, @Nonnull BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable)
	{
		EnumPlantType plantType = plantable.getPlantType(world, pos.up());

		switch (plantType)
		{
			case Desert:
			case Cave:
			case Beach:
				return !tilled;
			case Nether:
			case Water:
				return false;
			case Crop:
				return tilled;
			case Plains:
				return !tilled || (tilled && world.getBlockState(pos.up()).getBlock() == Blocks.BEETROOTS);
		}

		return false;
	}

	@Override
	public void updateTick(World worldObj, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand)
	{
		if (!worldObj.isRemote)
		{
			IBlockState plantState;
			Block toBoost;
			for (int i = 0; i < 3; i++)
			{
				plantState = worldObj.getBlockState(pos.up());
				toBoost = plantState.getBlock();
				if (toBoost != Blocks.AIR && toBoost instanceof IPlantable)
				{
					toBoost.updateTick(worldObj, pos.up(), plantState, rand);
				}
			}
		}
	}
}
