package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.item.block.ItemBlockBlockDiaphanous;
import lumien.randomthings.tileentity.TileEntityBlockDiaphanous;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockBlockDiaphanous extends BlockContainerBase
{
	static final AxisAlignedBB EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	protected BlockBlockDiaphanous()
	{
		super("diaphanousBlock", Material.GLASS, ItemBlockBlockDiaphanous.class);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3F);
	}
	
	@Override
	public void getSubBlocks(@Nonnull CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items)
	{
		
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
	}

	private ItemStack createDiaphanousStack(World worldIn, BlockPos pos)
	{
		ItemStack drop = new ItemStack(this);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityBlockDiaphanous))
		{
			return drop;
		}

		TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) tileEntity;
		IBlockState displayState = te.getDisplayState();
		NBTTagCompound tagCompound = new NBTTagCompound();
		drop.setTagCompound(tagCompound);

		ResourceLocation registryName = displayState.getBlock().getRegistryName();
		tagCompound.setString("block", registryName != null ? registryName.toString() : "minecraft:stone");
		tagCompound.setInteger("meta", displayState.getBlock().getMetaFromState(displayState));
		tagCompound.setBoolean("inverted", te.isInverted());

		return drop;
	}

	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest)
		{
			WorldUtil.spawnItemStack(world, pos, createDiaphanousStack(world, pos));
		}

		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
	}
	
	@Override
	public boolean addLandingEffects(@Nonnull IBlockState state, WorldServer worldObj, @Nonnull BlockPos blockPosition, @Nonnull IBlockState iblockstate, @Nonnull EntityLivingBase entity, int numberOfParticles)
	{
		TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldObj.getTileEntity(blockPosition);

		IBlockState display = te.getDisplayState();
		Block b = display.getBlock();

		worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), numberOfParticles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(display));

		return true;
	}

	@Override
	public boolean addDestroyEffects(World world, @Nonnull BlockPos pos, @Nonnull ParticleManager manager)
	{
		TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) world.getTileEntity(pos);

		IBlockState display = te.getDisplayState();
		Block b = display.getBlock();

		try
		{
			manager.addBlockDestroyEffects(pos, display);
		}
		catch (Exception e)
		{

		}

		return true;
	}

	static final Random rand = new Random();

	@Override
	public boolean addHitEffects(@Nonnull IBlockState state, @Nonnull World worldObj, RayTraceResult target, @Nonnull ParticleManager manager)
	{
		if (target.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldObj.getTileEntity(target.getBlockPos());

			if (te instanceof TileEntityBlockDiaphanous)
			{
				IBlockState display = te.getDisplayState();
				Block b = display.getBlock();

				BlockPos pos = target.getBlockPos();

				try
				{
					int i = pos.getX();
					int j = pos.getY();
					int k = pos.getZ();
					float f = 0.1F;
					AxisAlignedBB axisalignedbb = display.getBoundingBox(worldObj, pos);
					double d0 = (double) i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
					double d1 = (double) j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
					double d2 = (double) k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;

					EnumFacing side = target.sideHit;

					if (side == EnumFacing.DOWN)
					{
						d1 = (double) j + axisalignedbb.minY - 0.10000000149011612D;
					}

					if (side == EnumFacing.UP)
					{
						d1 = (double) j + axisalignedbb.maxY + 0.10000000149011612D;
					}

					if (side == EnumFacing.NORTH)
					{
						d2 = (double) k + axisalignedbb.minZ - 0.10000000149011612D;
					}

					if (side == EnumFacing.SOUTH)
					{
						d2 = (double) k + axisalignedbb.maxZ + 0.10000000149011612D;
					}

					if (side == EnumFacing.WEST)
					{
						d0 = (double) i + axisalignedbb.minX - 0.10000000149011612D;
					}

					if (side == EnumFacing.EAST)
					{
						d0 = (double) i + axisalignedbb.maxX + 0.10000000149011612D;
					}

					manager.addEffect(((ParticleDigging) new ParticleDigging.Factory().createParticle(0, worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(display))).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
				}
				catch (Exception e)
				{

				}
			}
		}
		return true;
	}

	@Override
	public boolean isFullCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		TileEntity te =  worldIn.getTileEntity(pos);

		return (te instanceof TileEntityBlockDiaphanous && ((TileEntityBlockDiaphanous)te).isInverted()) ? Block.FULL_BLOCK_AABB : Block.NULL_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos)
	{
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;

		if (thePlayer != null)
		{
			for (EnumHand hand : EnumHand.values())
			{
				ItemStack held = thePlayer.getHeldItem(hand);

				if (!held.isEmpty() && held.getItem() == Item.getItemFromBlock(ModBlocks.blockDiaphanous))
				{
					return Block.FULL_BLOCK_AABB.offset(pos);
				}
			}
		}

		return EMPTY;
	}

	@Override
	public void onBlockPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("block"))
		{
			NBTTagCompound compound = stack.getTagCompound();
			IBlockState toDisplay;

			Block b = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("block")));
			int meta = compound.getInteger("meta");

			try
			{
				toDisplay = b.getStateFromMeta(meta);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				toDisplay = Blocks.STONE.getDefaultState();
			}

			TileEntityBlockDiaphanous te = (TileEntityBlockDiaphanous) worldIn.getTileEntity(pos);
			te.setDisplayState(toDisplay);
			te.setInverted(compound.getBoolean("inverted"));
		}

		if (!worldIn.isRemote)
		{
			neighborChanged(state, worldIn, pos, this, pos);
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityBlockDiaphanous();
	}
}
