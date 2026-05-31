package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityLinkOrb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockLinkOrb extends BlockContainerBase
{
	static final AxisAlignedBB BB = FULL_BLOCK_AABB.shrink(0.8);

	protected BlockLinkOrb()
	{
		super("linkOrb", Material.ROCK);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		if (!worldIn.isRemote && placer instanceof EntityPlayer)
		{
			TileEntityLinkOrb lo = (TileEntityLinkOrb) worldIn.getTileEntity(pos);
			lo.setOwner(((EntityPlayer) placer).getGameProfile().getId());
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos)
	{
		return BB;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityLinkOrb();
	}

	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isSideSolid(@Nonnull IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return false;
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
}
