package lumien.randomthings.block;

import java.awt.Color;
import java.util.Random;

import lumien.randomthings.client.particles.EntityColoredSmokeFX;
import lumien.randomthings.item.block.ItemBlockLuminous;
import lumien.randomthings.lib.ILuminousBlock;
import lumien.randomthings.tileentity.TileEntitySlimeCube;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockSlimeCube extends BlockContainerBase implements ILuminousBlock
{
	public static PropertyBool POWERED = PropertyBool.create("powered");

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F);

	protected BlockSlimeCube()
	{
		super("slimeCube", Material.CLAY, ItemBlockLuminous.class);

		this.setSoundType(SoundType.SLIME);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(@Nonnull IBlockState stateIn, World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntitySlimeCube)
		{
			TileEntitySlimeCube sc = (TileEntitySlimeCube) te;

			Color color;

			if (sc.isRedstonePowered())
			{
				color = Color.RED.darker();
			}
			else
			{
				color = Color.GREEN.darker();
			}

			for (int i = 0; i < 10; i++)
			{
				int posX = pos.getX() + rand.nextInt(11) - 5;
				int posY = pos.getY() + rand.nextInt(11) - 5;
				int posZ = pos.getZ() + rand.nextInt(11) - 5;

				BlockPos target = new BlockPos(posX, posY, posZ);

				IBlockState state = worldIn.getBlockState(target);

				if (state.isFullCube() && worldIn.isAirBlock(target.up()))
				{
					EntityColoredSmokeFX fx = new EntityColoredSmokeFX(worldIn, posX, posY + 0.9, posZ, 0, 0.1, 0);
					fx.setMaxAge(rand.nextInt(40) + 20);
					fx.setRBGColorF(1F / 255F * color.getRed(), 1F / 255F * color.getGreen(), 1F / 255F * color.getBlue());
					Minecraft.getMinecraft().effectRenderer.addEffect(fx);
				}
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		return AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos)
	{
		return AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySlimeCube();
	}

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return tintIndex == 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, POWERED);
	}

	@Override
	public int getMetaFromState(@Nonnull IBlockState state)
	{
		return 0;
	}

	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntitySlimeCube)
		{
			return state.withProperty(POWERED, ((TileEntitySlimeCube) te).isRedstonePowered());
		}
		else
		{
			return super.getActualState(state, worldIn, pos);
		}
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
