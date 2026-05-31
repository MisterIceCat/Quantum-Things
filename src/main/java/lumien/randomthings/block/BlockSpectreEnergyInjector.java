package lumien.randomthings.block;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.tileentity.TileEntitySpectreEnergyInjector;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockSpectreEnergyInjector extends BlockContainerBase
{

	protected BlockSpectreEnergyInjector()
	{
		super("spectreEnergyInjector", Material.ROCK);
		
		this.setHardness(3.0F);
		this.setSoundType(SoundType.GLASS);
	}
	
	public boolean isOpaqueCube(@Nonnull IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(@Nonnull IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySpectreEnergyInjector();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack)
	{
		if (!worldIn.isRemote && placer instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) placer;
			
			GameProfile profile = player.getGameProfile();

            TileEntitySpectreEnergyInjector injector = (TileEntitySpectreEnergyInjector) worldIn.getTileEntity(pos);

            injector.setOwner(profile.getId());
        }
	}
}
