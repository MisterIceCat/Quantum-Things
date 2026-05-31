package lumien.randomthings.item;

import lumien.randomthings.entitys.EntityThrownWeatherEgg;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemWeatherEgg extends ItemBase
{
	public enum TYPE
	{
		SUN, RAIN, STORM;
	}

	public ItemWeatherEgg()
	{
		super("weatherEgg");

		this.setHasSubtypes(true);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorProjectileDispense()
		{
			/**
			 * Return the projectile entity spawned by this dispense behavior.
			 */
			protected IProjectile getProjectileEntity(@Nonnull World worldIn, @Nonnull IPosition position, @Nonnull ItemStack stackIn)
			{
				return new EntityThrownWeatherEgg(worldIn, position.getX(), position.getY(), position.getZ(), TYPE.values()[stackIn.getItemDamage()]);
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (!playerIn.capabilities.isCreativeMode)
		{
			itemstack.shrink(1);
		}

		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote)
		{
			EntityThrownWeatherEgg entityegg = new EntityThrownWeatherEgg(worldIn, playerIn, TYPE.values()[itemstack.getItemDamage()]);
			entityegg.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntity(entityegg);
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			for (TYPE t : TYPE.values())
			{
				items.add(new ItemStack(this, 1, t.ordinal()));
			}
		}
	}

	@Override
	public String getTranslationKey(@Nonnull ItemStack stack)
	{
		return super.getTranslationKey(stack) + "_" + TYPE.values()[stack.getItemDamage()].toString().toLowerCase();
	}
}
