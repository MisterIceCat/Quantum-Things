package lumien.randomthings.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockQuartzGlass extends BlockBase
{
	protected BlockQuartzGlass()
	{
		super("quartzGlass", Material.GROUND);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullBlock(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean causesSuffocation(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos, @Nonnull EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (state != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return false;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState)
	{
		AxisAlignedBB blockBox = state.getCollisionBoundingBox(worldIn, pos);
		AxisAlignedBB axisalignedbb = blockBox.offset(pos);

		if (entityBox.intersects(axisalignedbb) && entityIn != null && !(entityIn instanceof EntityPlayer))
		{
			collidingBoxes.add(axisalignedbb);
		}
	}
}
