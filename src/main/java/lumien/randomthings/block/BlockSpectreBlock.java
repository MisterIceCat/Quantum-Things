package lumien.randomthings.block;

import lumien.randomthings.lib.IExplosionImmune;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockSpectreBlock extends BlockBase implements IExplosionImmune
{
	public BlockSpectreBlock()
	{
		super("spectreBlock", Material.ROCK);

		this.setBlockUnbreakable();
		this.setSoundType(SoundType.GLASS);
		this.blockResistance = Float.MAX_VALUE - 1000f;
	}

	@Override
	public float getExplosionResistance(@Nonnull World world, @Nonnull BlockPos pos, Entity exploder, @Nonnull Explosion explosion)
	{
		return blockResistance;
	}

	@Override
	public boolean canEntityDestroy(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull Entity entity)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
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

		if (block == this || iblockstate.getBlock() == ModBlocks.spectreCore)
		{
			return false;
		}

		if (state != iblockstate)
		{
			return true;
		}

		return false;
	}
}
