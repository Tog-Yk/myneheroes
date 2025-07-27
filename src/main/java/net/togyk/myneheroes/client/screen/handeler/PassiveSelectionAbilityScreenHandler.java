package net.togyk.myneheroes.client.screen.handeler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveSelectionAbility;
import net.togyk.myneheroes.client.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.PassiveScreenAbilityPayload;
import net.togyk.myneheroes.networking.SelectPassivePayload;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

public class PassiveSelectionAbilityScreenHandler extends ScreenHandler {
    private final PassiveSelectionAbility ability;
    private final PlayerEntity player;

    public PassiveSelectionAbilityScreenHandler(int syncId, PlayerEntity player, PassiveSelectionAbility ability) {
        super(ModScreenHandlerTypes.PASSIVE_SELECTION_ABILITY, syncId);
        this.player = player;
        this.ability = ability;
    }

    //Client Constructor
    public PassiveSelectionAbilityScreenHandler(int syncId, PlayerInventory playerInventory, PassiveScreenAbilityPayload payload) {
        this(syncId, playerInventory.player, (PassiveSelectionAbility) payload.ability());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return ability.getCooldown() == 0;
    }

    public PassiveSelectionAbility getAbility() {
        return ability;
    }

    public void selected(int index) {
        List<Ability> abilities = ((PlayerAbilities) this.player).myneheroes$getFilteredAbilities();
        List<Identifier> abilityIds = abilities.stream().map(Ability::getId).toList();
        int selectionIndex = abilityIds.indexOf(this.ability.getId());
        ClientPlayNetworking.send(new SelectPassivePayload(selectionIndex, index));

        if (abilities.get(selectionIndex) instanceof PassiveSelectionAbility ability) {
            ability.setSelectedPassive(index);
        }
    }
}
