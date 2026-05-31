package lumien.randomthings.handler.spectre;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;

public class SpectreChunkProvider implements IChunkGenerator
{
	World worldObj;

	public SpectreChunkProvider(World worldObj)
	{
		this.worldObj = worldObj;
	}

	@Override
	public Chunk generateChunk(int x, int z)
	{
		ChunkPrimer chunkprimer = new ChunkPrimer();

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
		chunk.resetRelightChecks();

		return chunk;
	}

	@Override
	public void populate(int x, int z)
	{

	}

	@Override
	public boolean generateStructures(@Nonnull Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType creatureType, @Nonnull BlockPos pos)
	{
		return new ArrayList<>();
	}

	@Override
	public void recreateStructures(@Nonnull Chunk chunkIn, int x, int z)
	{
	}

	@Override
	public BlockPos getNearestStructurePos(@Nonnull World worldIn, @Nonnull String structureName, @Nonnull BlockPos position, boolean findUnexplored)
	{
		return null;
	}

	@Override
	public boolean isInsideStructure(@Nonnull World worldIn, @Nonnull String structureName, @Nonnull BlockPos pos)
	{
		return false;
	}
}
