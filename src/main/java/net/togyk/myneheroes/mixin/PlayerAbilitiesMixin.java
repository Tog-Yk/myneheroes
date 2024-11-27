package net.togyk.myneheroes.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.togyk.myneheroes.Item.custom.AbilityHoldingItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.registry.ModRegistries;
import net.togyk.myneheroes.util.AbilityInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAbilitiesMixin implements AbilityInterface {
    @Shadow @Final
    public PlayerInventory inventory;


    // List. Colum. Ability
    private List<List<Ability>> Abilities;
    private int selectedColum = 0;
    private int selectedDisplacement = 0;

    public void cycleSelectedColum(Boolean backwards) {
        int buffer = backwards? 1:-1;
        if (selectedColum + buffer < 0) {
            selectedColum = 0;
        }else if (selectedColum + buffer > Abilities.size()) {
            selectedColum = Abilities.size();
        } else {
            selectedColum += buffer;
        }
    }

    public void cycleSelectedDisplacement(Boolean backwards) {
        int buffer = backwards? 1:-1;
        if (selectedDisplacement + buffer < 0) {
            buffer += Abilities.get(selectedColum).size();
        }else if (selectedDisplacement + buffer > Abilities.get(selectedColum).size()) {
            buffer -= Abilities.get(selectedColum).size();
        }
        selectedDisplacement += buffer;
    }

    public Ability getFirstSelectedAbility() {
        return Abilities.get(selectedColum).get(selectedDisplacement);
    }
    public Ability getSecondSelectedAbility() {
        int displacement = selectedDisplacement + 1;
        if (displacement > Abilities.get(selectedColum).size()) {
            displacement -= Abilities.get(selectedColum).size();
        }
        return Abilities.get(selectedColum).get(displacement);
    }
    public Ability getThirdSelectedAbility() {
        int displacement = selectedDisplacement + 2;
        if (displacement > Abilities.get(selectedColum).size()) {
            displacement -= Abilities.get(selectedColum).size();
        }
        return Abilities.get(selectedColum).get(displacement);
    }
    public Ability getFourthSelectedAbility() {
        int displacement = selectedDisplacement + 3;
        if (displacement > Abilities.get(selectedColum).size()) {
            displacement -= Abilities.get(selectedColum).size();
        }
        return Abilities.get(selectedColum).get(displacement);
    }

    public List<List<Ability>> getAbilities() {
        return Abilities;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        //update the owned Abilities
        List<ItemStack> itemList = inventory.main.stream()
                .filter(stack -> stack.getItem() instanceof AbilityHoldingItem)
                .toList();
        List<Ability> abilities = new ArrayList<>();
        for (ItemStack itemStack : itemList) {
            if (itemStack.getItem() instanceof AbilityHoldingItem abilityHoldingItem) {
                for (int l = 0; l < abilityHoldingItem.getAbilities().size(); ++l) {
                    if (!abilities.contains(abilityHoldingItem.getAbilities().get(l))) {
                        abilities.add(abilityHoldingItem.getAbilities().get(l));
                    }
                }
            }
        }
        Abilities = compareAbilities(abilities);

        //tick the abilities
        for (List<Ability> list : Abilities) {
            for (Ability ability : list) {
                ability.tick();
            }
        }
    }

    @Inject(at = @At("HEAD"),method = "writeCustomDataToNbt")
    public void writeNbt(NbtCompound nbt, CallbackInfo info) {
        NbtCompound modidNbt = new NbtCompound();
        modidNbt.putInt("selectedColum",selectedColum);
        modidNbt.putInt("selectedDisplacement", selectedDisplacement);
        if (this.Abilities != null) {
            modidNbt.put("Abilities",this.genNbt(new NbtList()));
        }
        nbt.put(MyneHeroes.MOD_ID, modidNbt);
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            if (nbt.getCompound(MyneHeroes.MOD_ID).contains("selectedColum")) {
                selectedColum = nbt.getCompound(MyneHeroes.MOD_ID).getInt("selectedColum");
            }
            if (nbt.getCompound(MyneHeroes.MOD_ID).contains("selectedDisplacement")) {
                selectedDisplacement = nbt.getCompound(MyneHeroes.MOD_ID).getInt("selectedDisplacement");
            }
            if (nbt.getCompound(MyneHeroes.MOD_ID).contains("Abilities")) {

                NbtList nbtList = (NbtList) nbt.getCompound(MyneHeroes.MOD_ID).get("Abilities");
                List<List<Ability>> list = new ArrayList<>();
                assert nbtList != null;
                for (NbtElement nbtElement : nbtList) {
                    List<Ability> colum = new ArrayList<>();
                    if (nbtElement instanceof NbtList abilityCompoundList) {
                        for (int s = 0; s < abilityCompoundList.size(); ++s) {
                            NbtCompound nbtCompound = abilityCompoundList.getCompound(s);
                            Ability ability = ModRegistries.ABILITY.get(nbtCompound.getInt("Index"));
                            ability.setCooldown(nbtCompound.getInt("cooldown"));
                            colum.add(ability);
                        }
                    }
                    list.add(colum);
                }
                Abilities = list;
            }
        }
    }
    private List<List<Ability>> compareAbilities(List<Ability> list) {
        List<Ability> missingAbilities = new ArrayList<>();
        List<Ability> passedAbilities = new ArrayList<>();

        List<List<Ability>> newAbilities = new ArrayList<>();
        for (List<Ability> preferredAbilityColum : Abilities) {
            List<Ability> colum = new ArrayList<>();
            for (Ability ability : preferredAbilityColum) {
                if (list.contains(ability)) {
                    colum.add(ability);

                    passedAbilities.add(ability);
                    missingAbilities.remove(ability);
                } else if (!passedAbilities.contains(ability)) {
                    missingAbilities.add(ability);
                }
            }
            newAbilities.add(colum);
        }
        newAbilities.add(missingAbilities);
        return newAbilities;
    }

    private NbtList genNbt(NbtList nbtList) {
        int i;
        for(i = 0; i < this.Abilities.size(); ++i) {
            NbtList setNbtList = new NbtList();
            int s;
            for(s = 0; s < i; ++s) {
                NbtCompound nbt = new NbtCompound();
                MyneHeroes.LOGGER.info("index is now: " + ModRegistries.ABILITY.getRawId(this.Abilities.get(i).get(s)));
                nbt.putInt("index",ModRegistries.ABILITY.getRawId(this.Abilities.get(i).get(s)));
                setNbtList.add(this.Abilities.get(i).get(s).getNbt(nbt));
            }
            nbtList.add(setNbtList);
        }

        return nbtList;
    }
}