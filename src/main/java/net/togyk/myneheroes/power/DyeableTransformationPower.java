package net.togyk.myneheroes.power;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.upgrade.ColorUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;

public class DyeableTransformationPower extends TransformationPower implements UpgradablePower {
    protected final boolean differentColorForEmmisive;

    private List<Upgrade> upgrades = new ArrayList<>();

    public DyeableTransformationPower(Identifier id, int maxTransformationTime, int color, boolean differentColorForEmmisive, List<Ability> abilities, Settings settings, transformationAttributeModifiers attributeModifiers) {
        super(id, maxTransformationTime, color, abilities, settings, attributeModifiers);
        this.differentColorForEmmisive = differentColorForEmmisive;
    }

    @Override
    public int getTintableSkinColor() {
        if (!this.getUpgrades().isEmpty()) {
            return ((ColorUpgrade) this.getUpgrades().getFirst()).getColor(this.getHolder().getWorld());
        }
        return super.getTintableSkinColor();
    }

    @Override
    public int getEmmisiveTintableSkinColor() {
        if (this.getUpgrades().size() > 1) {
            return ((ColorUpgrade) this.getUpgrades().get(1)).getColor(this.getHolder().getWorld());
        } else if (!this.getUpgrades().isEmpty()) {
            return ((ColorUpgrade) this.getUpgrades().getFirst()).getColor(this.getHolder().getWorld());
        }
        return super.getEmmisiveTintableSkinColor();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList upgradesNbt = new NbtList();

        for (Upgrade upgrade : upgrades) {
            upgradesNbt.add(upgrade.writeNbt(new NbtCompound()));
        }

        nbt.put("upgrades", upgradesNbt);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        NbtList upgradesNbt = new NbtList();
        if (nbt.contains("upgrades")) {
            upgradesNbt = nbt.getList("upgrades", NbtElement.COMPOUND_TYPE);
        }

        List<Upgrade> upgradeList = new ArrayList<>();
        for (NbtElement nbtElement : upgradesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Upgrade upgrade = AbilityUtil.nbtToUpgrade(nbtCompound);
                if (upgrade != null) {
                    upgradeList.add(upgrade);
                }
            }
        }

        this.upgrades = upgradeList;

        super.readNbt(nbt);
    }

    @Override
    public boolean canUpgrade(Upgrade upgrade) {
        return differentColorForEmmisive ? this.upgrades.size() < 2 : this.upgrades.isEmpty() && upgrade instanceof ColorUpgrade;
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    @Override
    public void setUpgrades(List<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    @Override
    public DyeableTransformationPower copy() {
        return new DyeableTransformationPower(id, maxTransformationTime, color, differentColorForEmmisive, List.copyOf(this.abilities), settings, attributeModifiers);
    }
}
