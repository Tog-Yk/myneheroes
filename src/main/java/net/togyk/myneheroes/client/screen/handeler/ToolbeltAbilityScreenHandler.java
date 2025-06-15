package net.togyk.myneheroes.client.screen.handeler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.ToolbeltAbility;
import net.togyk.myneheroes.client.screen.ModScreenHandlerTypes;
import net.togyk.myneheroes.networking.ToolBeltScreenAbilityPayload;
import net.togyk.myneheroes.networking.SwapFromToolbeltPayload;
import net.togyk.myneheroes.util.PlayerAbilities;

import java.util.List;

public class ToolbeltAbilityScreenHandler extends ScreenHandler {
    private final ToolbeltAbility ability;
    private final PlayerEntity player;

    public ToolbeltAbilityScreenHandler(int syncId, PlayerEntity player, ToolbeltAbility ability) {
        super(ModScreenHandlerTypes.TOOLBELT_ABILITY, syncId);
        this.player = player;
        this.ability = ability;
    }

    //Client Constructor
    public ToolbeltAbilityScreenHandler(int syncId, PlayerInventory playerInventory, ToolBeltScreenAbilityPayload payload) {
        this(syncId, playerInventory.player, (ToolbeltAbility) payload.ability());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return ability.getCooldown() == 0;
    }

    public ToolbeltAbility getAbility() {
        return ability;
    }

    public void selected(int index) {
        List<Ability> abilities = ((PlayerAbilities) this.player).myneheroes$getFilteredAbilities();
        List<Identifier> abilityIds = abilities.stream().map(Ability::getId).toList();
        int toolbeltIndex = abilityIds.indexOf(this.ability.getId());
        ClientPlayNetworking.send(new SwapFromToolbeltPayload(toolbeltIndex, index));

        if (abilities.get(toolbeltIndex) instanceof ToolbeltAbility ability) {
            ability.swapItem(player, index);
        }
    }
}
