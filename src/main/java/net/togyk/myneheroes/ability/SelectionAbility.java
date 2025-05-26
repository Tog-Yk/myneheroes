package net.togyk.myneheroes.ability;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.client.screen.handeler.SelectionAbilityScreenHandler;
import net.togyk.myneheroes.networking.SelectionScreenAbilityPayload;
import net.togyk.myneheroes.util.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SelectionAbility extends Ability implements ExtendedScreenHandlerFactory<SelectionScreenAbilityPayload> {
    protected List<Ability> abilities;

    public SelectionAbility(Identifier id, Settings settings, List<Ability> abilities) {
        super(id, 0, settings, null);
        this.abilities = abilities;
    }

    @Override
    public void tick() {
        super.tick();
        for (Ability ability : abilities) {
            ability.setHolder(this);
            ability.tick();
        }
    }

    @Override
    public void Use(PlayerEntity player) {
        if (getCooldown() == 0) {
            player.openHandledScreen(this);
            save();
        }
    }

    public void UseAbility(PlayerEntity player, int index) {
        Ability ability = this.abilities.get(index);
        ability.Use(player);
        this.setCooldown(ability.getMaxCooldown());
        this.save();
    }

    @Override
    public int getMaxCooldown() {
        if (!abilities.isEmpty()) {
            return abilities.stream().map(Ability::getMaxCooldown).max(Comparator.naturalOrder()).get();
        }
        return 0;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void saveAbility(Ability ability) {
        this.save();
    }

    @Override
    public void setHolder(@Nullable SelectionAbility holder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SelectionScreenAbilityPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new SelectionScreenAbilityPayload(this);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("ability."+this.getId());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SelectionAbilityScreenHandler(syncId, player, this);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList abilitiesNbt = new NbtList();
        for (Ability ability : this.abilities) {
            if (ability != null) {
                abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
            }
        }

        nbt.put("abilities", abilitiesNbt);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("abilities")) {
            abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
        }

        List<Ability> abilitiesList = new ArrayList<>();
        for (NbtElement nbtElement : abilitiesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Ability ability = AbilityUtil.nbtToAbility(nbtCompound);
                abilitiesList.add(ability);
            }
        }
        this.abilities = abilitiesList;
    }

    @Override
    public Ability copy() {
        return new SelectionAbility(id, settings, abilities);
    }
}
