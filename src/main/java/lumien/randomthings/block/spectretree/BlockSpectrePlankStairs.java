package lumien.randomthings.block.spectretree;

import lumien.randomthings.block.BlockBase;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockSpectrePlankStairs extends BlockStairs {
	public BlockSpectrePlankStairs() {
		super(ModBlocks.spectrePlank.getDefaultState());

		this.setTranslationKey("spectreStairs");
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.WOOD);
		this.setLightOpacity(0);
		this.useNeighborBrightness = true;

		BlockBase.registerBlock("spectreStairs", this);
	}

	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(@Nonnull IBlockState state) {
		return false;
	}

	@Override
	public boolean doesSideBlockRendering(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(@Nonnull IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
                                        @Nonnull EnumFacing side) {
		IBlockState neighborState = blockAccess.getBlockState(pos.offset(side));
		Block neighborBlock = neighborState.getBlock();

		if (side.getAxis().isHorizontal()) {
			if (neighborBlock instanceof BlockSpectrePlankStairs) {
				String thisHalf = blockState.getValue(HALF).getName();
				String otherHalf = neighborState.getValue(HALF).getName();
				if (thisHalf.equals(otherHalf)) {
					return false;
				}
			}

			if (neighborBlock instanceof BlockSpectrePlankSlab) {
				BlockSpectrePlankSlab slab = (BlockSpectrePlankSlab) neighborBlock;
				if (!slab.isDouble()) {
					String stairHalf = blockState.getValue(HALF).getName();
					String slabHalf = neighborState.getValue(BlockSlab.HALF).getName();
					if (stairHalf.equals(slabHalf)) {
						return false;
					}
				}
			}
		}

		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public EnumPushReaction getPushReaction(@Nonnull IBlockState state) {
		return EnumPushReaction.NORMAL;
	}
}
