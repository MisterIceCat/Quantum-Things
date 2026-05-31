package lumien.randomthings.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemWaterWalkingBoots extends ItemArmor
{
	public ItemWaterWalkingBoots()
	{
		super(ItemArmor.ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.FEET);
		ItemBase.registerItem("waterWalkingBoots", this);
	}

	@Override
	public EnumRarity getRarity(@Nonnull ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public String getArmorTexture(@Nonnull ItemStack stack, @Nonnull Entity entity, @Nonnull EntityEquipmentSlot slot, @Nonnull String type)
	{
		return "randomthings:textures/models/armor/waterWalkingBoots.png";
	}

	@Override
	public int getMaxDamage()
	{
		return 0;
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}
}
