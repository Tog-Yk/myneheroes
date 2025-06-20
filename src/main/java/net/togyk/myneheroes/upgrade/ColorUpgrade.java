package net.togyk.myneheroes.upgrade;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.SimpleDyeableItem;

import java.util.List;

public class ColorUpgrade extends Upgrade {

    protected ColorUpgrade(List<ArmorItem.Type> compatibleTypes, Identifier id) {
        super(compatibleTypes, id);
    }

    public int getColor(World world) {
        ItemStack stack = this.getItemStack(world);
        if (stack.getItem() instanceof SimpleDyeableItem dyeableItem) {
            return dyeableItem.getColor(stack, 0);
        }
        return 0xFFFFFFFF;
    }

    public void setColor(World world, int color) {
        ItemStack stack = this.getItemStack(world);
        if (stack.getItem() instanceof SimpleDyeableItem dyeableItem) {
            dyeableItem.setColor(stack, 0, color);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public Upgrade copy() {
        return new ColorUpgrade(compatibleTypes, id);
    }
}
