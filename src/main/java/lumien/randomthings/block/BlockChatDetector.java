package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockChatDetector extends BlockContainerBase
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	protected BlockChatDetector()
	{
		super("chatDetector", Material.ROCK);

		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
		this.setHardness(2);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityChatDetector();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(POWERED, meta == 1 ? true : false);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(POWERED)) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, POWERED);
	}

	@Override
	public boolean isSideSolid(@Nonnull IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.CHAT_DETECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public boolean canProvidePower(@Nonnull IBlockState state)
	{
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		if (!worldIn.isRemote && placer != null && placer instanceof EntityPlayerMP && worldIn.getTileEntity(pos) != null)
		{
			EntityPlayerMP player = (EntityPlayerMP) placer;
			((TileEntityChatDetector) worldIn.getTileEntity(pos)).setPlayerUUID(player.getGameProfile().getId());
		}
	}

	@Override
	public int getStrongPower(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getWeakPower(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
}
