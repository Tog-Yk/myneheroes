package net.togyk.myneheroes.upgrade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.StockPile;

import java.util.List;

public class StockPileUpgrade extends AbilityUpgrade implements StockPile {
    private float charge = 0;
    protected final float maxCharge;

    private final Identifier chargeIcon;

    private final Item rechargeItem;
    private final float rechargeValue;

    protected StockPileUpgrade(Ability ability, float maxCharge, Item rechargeItem, float rechargeValue, List<EquipmentType> compatibleTypes, Identifier id) {
        this(List.of(ability), maxCharge, rechargeItem, rechargeValue, compatibleTypes, id);
    }

    protected StockPileUpgrade(List<Ability> abilities, float maxCharge, Item rechargeItem, float rechargeValue, List<EquipmentType> compatibleTypes, Identifier id) {
        super(abilities, compatibleTypes, id);
        this.maxCharge = maxCharge;
        this.chargeIcon = Identifier.of(MyneHeroes.MOD_ID,"textures/upgrade/charge/"+id.getPath()+".png");
        this.rechargeItem = rechargeItem;
        this.rechargeValue = rechargeValue;
    }


    @Override
    public Identifier getStockPileId() {
        return this.getId();
    }

    @Override
    public float getCharge() {
        return this.charge;
    }

    @Override
    public void setCharge(float charge) {
        this.charge = Math.clamp(charge, 0 , this.maxCharge);
        this.save();
    }

    @Override
    public float getMaxCharge() {
        return this.maxCharge;
    }

    @Override
    public Identifier getChargeIcon() {
        return chargeIcon;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.getItem() == this.rechargeItem) {
                if (charge != this.maxCharge) {
                    if (!player.isCreative()) {
                        otherStack.decrement(1);
                    }
                    charge = Math.clamp(charge + this.rechargeValue, 0, this.maxCharge);
                    this.save();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("charge", this.getCharge());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.getFloat("charge").isPresent()) {
            this.charge = nbt.getFloat("charge").get();
        }
    }


    @Override
    public Upgrade copy() {
        return new StockPileUpgrade(abilities, maxCharge, rechargeItem, rechargeValue, compatibleTypes, id);
    }
}
