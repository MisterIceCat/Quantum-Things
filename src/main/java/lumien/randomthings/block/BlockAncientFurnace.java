package lumien.randomthings.block;

import lumien.randomthings.config.Features;
import lumien.randomthings.tileentity.TileEntityAncientFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockAncientFurnace extends BlockContainerBase
{

	protected BlockAncientFurnace()
	{
		super("ancientFurnace", Material.ROCK);
	}

	@Override
	public float getBlockHardness(@Nonnull IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos) {
		return Features.ANCIENT_BRICK_DROP_ITEMS ? 2.0F : -1.0F;
	}

	@Override
	public float getExplosionResistance(@Nonnull Entity exploder) {
		return Features.ANCIENT_BRICK_DROP_ITEMS ? 2.0F : (this.blockResistance / 5.0F);
	}

	@Override
	public float getExplosionResistance(@Nonnull World world, @Nonnull BlockPos pos, Entity exploder, @Nonnull Explosion explosion) {
		return Features.ANCIENT_BRICK_DROP_ITEMS ? 2.0F : 6000000.0F;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityAncientFurnace();
	}
}
