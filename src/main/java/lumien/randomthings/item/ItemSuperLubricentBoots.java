package lumien.randomthings.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemSuperLubricentBoots extends ItemArmor
{
	public ItemSuperLubricentBoots()
	{
		super(ItemArmor.ArmorMaterial.IRON, 0, EntityEquipmentSlot.FEET);

		ItemBase.registerItem("superLubricentBoots", this);
	}

	@Override
	public EnumRarity getRarity(@Nonnull ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}

	@Override
	public String getArmorTexture(@Nonnull ItemStack stack, @Nonnull Entity entity, @Nonnull EntityEquipmentSlot slot, @Nonnull String type)
	{
		return "randomthings:textures/models/armor/superLubricentBoots.png";
	}
}
