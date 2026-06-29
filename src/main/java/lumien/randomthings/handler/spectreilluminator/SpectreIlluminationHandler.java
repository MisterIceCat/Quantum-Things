package lumien.randomthings.handler.spectreilluminator;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.client.MessageSpectreIllumination;
import lumien.randomthings.util.WorldUtil;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;

public class SpectreIlluminationHandler extends WorldSavedData
{
	final static String ID = "RTSIH";

	LongSet illuminatedChunks;

	public SpectreIlluminationHandler()
	{
		this(ID);
	}

	public SpectreIlluminationHandler(String id)
	{
		super(id);
		
		illuminatedChunks = new LongOpenHashSet();
	}

	public static SpectreIlluminationHandler get(World worldObj)
	{
		SpectreIlluminationHandler instance = (SpectreIlluminationHandler) worldObj.getPerWorldStorage().getOrLoadData(SpectreIlluminationHandler.class, ID);
		if (instance == null)
		{
			instance = new SpectreIlluminationHandler();
			worldObj.getPerWorldStorage().setData(ID, instance);
		}

		return instance;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.illuminatedChunks.clear();
		NBTTagList list = nbt.getTagList("illuminatedChunks", 4);

		for (int i = 0; i < list.tagCount(); i++)
		{
			this.illuminatedChunks.add(((NBTTagLong)list.get(i)).getLong());
		}
	}

	@Override
	public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();

		for (long cp : illuminatedChunks)
		{
			list.appendTag(new NBTTagLong(cp));
		}

		compound.setTag("illuminatedChunks", list);

		return compound;
	}
	
	public void startWatching(Chunk c, EntityPlayerMP player)
	{
		long cp = ChunkPos.asLong(c.x, c.z);
		if (illuminatedChunks.contains(cp))
		{
			MessageSpectreIllumination msg = new MessageSpectreIllumination(c.getWorld().provider.getDimension(), cp, true);
			
			PacketHandler.instance().sendTo(msg, player);
		}
	}

	public void syncToPlayer(EntityPlayerMP player)
	{
		World world = player.world;
		int viewDistance = player.getServer().getPlayerList().getViewDistance();
		ChunkPos playerChunk = new ChunkPos(player.getPosition());

		for (long cpLong : illuminatedChunks)
		{
			ChunkPos cp = WorldUtil.getChunkPosFromLong(cpLong);
			if (Math.abs(cp.x - playerChunk.x) <= viewDistance && Math.abs(cp.z - playerChunk.z) <= viewDistance)
			{
				MessageSpectreIllumination msg = new MessageSpectreIllumination(world.provider.getDimension(), cpLong, true);
				PacketHandler.instance().sendTo(msg, player);
			}
		}
	}

	public boolean isIlluminated(BlockPos pos)
	{
		return isIlluminatedChunk(ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4));
	}

	public boolean isIlluminatedChunk(long chunkLong)
	{
		return illuminatedChunks.contains(chunkLong);
	}

	public boolean hasIlluminatedChunks()
	{
		return !illuminatedChunks.isEmpty();
	}

	@SuppressWarnings("null")
	public void toggleChunk(World world, BlockPos pos)
	{
		long cpLong = ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);

		boolean newValue;
		
		if (illuminatedChunks.contains(cpLong))
		{
			illuminatedChunks.remove(cpLong);
			
			newValue = false;
		}
		else
		{
			illuminatedChunks.add(cpLong);
			
			newValue = true;
		}
		
		SpectreIlluminationHelper.lightUpdateChunk(world,
				new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4));
		
		MessageSpectreIllumination msg = new MessageSpectreIllumination(world.provider.getDimension(), cpLong, newValue);
		
		MessageUtil.sendToAllWatchingPos(world, pos, msg);
		
		
		world.markChunkDirty(pos, null);
		this.markDirty();
	}
}
