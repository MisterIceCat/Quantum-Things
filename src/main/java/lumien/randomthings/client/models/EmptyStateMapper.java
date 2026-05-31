package lumien.randomthings.client.models;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.IStateMapper;

import javax.annotation.Nonnull;

public class EmptyStateMapper implements IStateMapper
{

	@Override
	public Map putStateModelLocations(@Nonnull Block blockIn)
	{
		return Maps.newLinkedHashMap();
	}

}
