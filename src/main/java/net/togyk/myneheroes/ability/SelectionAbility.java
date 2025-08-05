package net.togyk.myneheroes.ability;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.client.screen.handeler.SelectionAbilityScreenHandler;
import net.togyk.myneheroes.networking.SelectionScreenAbilityPayload;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class SelectionAbility extends AbilityHoldingAbility implements ExtendedScreenHandlerFactory<SelectionScreenAbilityPayload> {
    public SelectionAbility(Identifier id, Settings settings, List<Ability> abilities) {
        super(id, 0, settings, abilities, null);
    }

    @Override
    public void use(PlayerEntity player) {
        if (getCooldown() == 0) {
            player.openHandledScreen(this);
            save();
        }
    }

    public void UseAbility(PlayerEntity player, int index) {
        Ability ability = this.getAbilities().get(index);
        ability.use(player);
        this.setCooldown(ability.getMaxCooldown());
        this.save();
    }

    @Override
    public int getMaxCooldown() {
        if (!this.getAbilities().isEmpty()) {
            return abilities.stream().map(Ability::getMaxCooldown).max(Comparator.naturalOrder()).get();
        }
        return 0;
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
    public Ability copy() {
        return new SelectionAbility(id, settings, abilities);
    }
}
