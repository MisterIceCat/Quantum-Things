package lumien.randomthings.client.mesh;

import lumien.randomthings.item.ModItems;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RedstoneActivatorMesh implements ItemMeshDefinition
{

	@Override
	public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack)
	{
		return new ModelResourceLocation("randomthings:redstoneactivator_" + ModItems.redstoneActivator.getDurationIndex(stack));
	}

}
