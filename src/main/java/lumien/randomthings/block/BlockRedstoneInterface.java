package lumien.randomthings.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

import javax.annotation.Nonnull;

public abstract class BlockRedstoneInterface extends BlockContainerBase
{
	protected BlockRedstoneInterface(String name, Material materialIn)
	{
		super(name, materialIn);
	}

	@Override
	public EnumBlockRenderType getRenderType(@Nonnull IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
}
