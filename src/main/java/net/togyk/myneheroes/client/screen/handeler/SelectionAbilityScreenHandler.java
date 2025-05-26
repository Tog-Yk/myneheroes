package net.togyk.myneheroes.client.screen.handeler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.SelectionAbility;
import net.togyk.myneheroes.client.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.SelectionScreenAbilityPayload;
import net.togyk.myneheroes.networking.UseSelectedAbilityPayload;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

public class SelectionAbilityScreenHandler extends ScreenHandler {
    private final SelectionAbility ability;
    private final PlayerEntity player;

    public SelectionAbilityScreenHandler(int syncId, PlayerEntity player, SelectionAbility ability) {
        super(ModScreenHandlerTypes.SELECTION_ABILITY, syncId);
        this.ability = ability;
        this.player = player;
    }

    //Client Constructor
    public SelectionAbilityScreenHandler(int syncId, PlayerInventory playerInventory, SelectionScreenAbilityPayload payload) {
        this(syncId, playerInventory.player, (SelectionAbility) payload.ability());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return ability.getCooldown() == 0;
    }

    public SelectionAbility getAbility() {
        List<Ability> abilities = ((PlayerAbilities) this.player).getFilteredAbilities();
        List<Identifier> abilityIds = abilities.stream().map(Ability::getId).toList();
        int selectionIndex = abilityIds.indexOf(this.ability.getId());

        return (SelectionAbility) abilities.get(selectionIndex);
    }

    public void useAbility(int index) {
        List<Ability> abilities = ((PlayerAbilities) this.player).getFilteredAbilities();
        List<Identifier> abilityIds = abilities.stream().map(Ability::getId).toList();
        int selectionIndex = abilityIds.indexOf(this.ability.getId());

        if (abilities.get(selectionIndex) instanceof SelectionAbility ability) {
            ability.UseAbility(player, index);
        }
        ClientPlayNetworking.send(new UseSelectedAbilityPayload(selectionIndex, index));
    }
}
