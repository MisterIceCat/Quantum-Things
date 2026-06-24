package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.BlockGlowingMushroom;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;

public class WorldGenPatches extends WorldGenerator
{
	private final Block block;

	public WorldGenPatches(Block blockIn)
	{
		this.block = blockIn;
	}

	@Override
	public boolean generate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos position)
	{
		for (int i = 0; i < 64; ++i)
		{
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < worldIn.getHeight() - 1) && !worldIn.canSeeSky(blockpos)
					&& canPlaceDuringWorldGen(worldIn, blockpos))
			{
				worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
			}
		}

		return true;
	}

	private boolean canPlaceDuringWorldGen(World worldIn, BlockPos pos)
	{
		if (this.block instanceof BlockGlowingMushroom)
			return ((BlockGlowingMushroom) this.block).canGenerateAt(worldIn, pos);

		return this.block.canPlaceBlockAt(worldIn, pos);
	}
}