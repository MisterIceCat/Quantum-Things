package lumien.randomthings.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBottleOfAir extends ItemBase
{
	public ItemBottleOfAir()
	{
		super("bottleOfAir");
	}

	@Override
	public int getMaxItemUseDuration(@Nonnull ItemStack stack)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(@Nonnull ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldObj, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack itemStack = player.getHeldItem(hand);
		if (player.isInsideOfMaterial(Material.WATER))
		{
			player.setActiveHand(hand);

			return new ActionResult(EnumActionResult.SUCCESS, itemStack);
		}
		return new ActionResult(EnumActionResult.FAIL, itemStack);
	}

	@Override
	public EnumRarity getRarity(@Nonnull ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public void onUsingTick(@Nonnull ItemStack stack, @Nonnull EntityLivingBase livingEntity, int count)
	{
		super.onUsingTick(stack, livingEntity, count);

		if (livingEntity.isInsideOfMaterial(Material.WATER) || livingEntity.getAir() < 270)
		{
			// Restore air every 5 ticks while using the item
			if (count % 5 == 0)
			{
				int currentAir = livingEntity.getAir();
				livingEntity.setAir(Math.min(currentAir + 20, 300));
			}
		}
	}
}
