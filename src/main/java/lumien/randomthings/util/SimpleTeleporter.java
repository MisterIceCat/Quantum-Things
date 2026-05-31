package lumien.randomthings.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class SimpleTeleporter extends Teleporter
{
	public SimpleTeleporter(WorldServer worldIn)
	{
		super(worldIn);
	}

	@Override
	public void placeInPortal(@Nonnull Entity entityIn, float rotationYaw)
	{

	}

	@Override
	public void removeStalePortalLocations(long worldTime)
	{

	}
}
