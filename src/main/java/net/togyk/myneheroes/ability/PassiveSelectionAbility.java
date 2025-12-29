package net.togyk.myneheroes.ability;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.client.screen.handeler.PassiveSelectionAbilityScreenHandler;
import net.togyk.myneheroes.networking.PassiveScreenAbilityPayload;
import net.togyk.myneheroes.util.SimpleEventResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PassiveSelectionAbility extends AbilityHoldingAbility implements PassiveAbility, ExtendedScreenHandlerFactory<PassiveScreenAbilityPayload> {
    private int selectedPassive;

    public PassiveSelectionAbility(Identifier id, Settings settings, List<Ability> passives) {
        super(id, 0, settings, passives, null);
    }

    @Override
    public void pressed(PlayerEntity player) {
        if (getCooldown() == 0) {
            player.openHandledScreen(this);
            save();
        }
    }

    @Override
    public boolean onGotHit(PlayerEntity player, DamageSource source, float amount) {
        Ability ability = this.getSelectedPassive();
        if (ability instanceof PassiveAbility passive) {
            ability.setHolder(this);
            return passive.onGotHit(player, source, amount);
        }
        return true;
    }

    @Override
    public boolean onDamage(PlayerEntity player, Entity target) {
        Ability ability = this.getSelectedPassive();
        if (ability instanceof PassiveAbility passive) {
            return passive.onDamage(player, target);
        }
        return true;
    }

    @Override
    public boolean onDeath(PlayerEntity player, DamageSource source) {
        Ability ability = this.getSelectedPassive();
        if (ability instanceof PassiveAbility passive) {
            return passive.onDeath(player, source);
        }
        return true;
    }

    @Override
    public SimpleEventResult onMissedAttack(PlayerEntity player) {
        Ability ability = this.getSelectedPassive();
        if (ability instanceof PassiveAbility passive) {
            return passive.onMissedAttack(player);
        }
        return SimpleEventResult.PASS;
    }

    @Override
    public SimpleEventResult onMissedInteraction(PlayerEntity player) {
        Ability ability = this.getSelectedPassive();
        if (ability instanceof PassiveAbility passive) {
            return passive.onMissedInteraction(player);
        }
        return SimpleEventResult.PASS;
    }

    public void setSelectedPassive(int selectedPassive) {
        this.selectedPassive = selectedPassive;
        this.setCooldown(this.getMaxCooldown());
        this.save();
    }

    public Ability getSelectedPassive() {
        if (this.selectedPassive != -1 && this.selectedPassive < this.getAbilities().size()) {
            Ability ability = this.getAbilities().get(this.selectedPassive);
            ability.setHolder(this);
            return ability;
        }
        return null;
    }

    @Override
    public PassiveScreenAbilityPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new PassiveScreenAbilityPayload(this);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("ability."+this.getId());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new PassiveSelectionAbilityScreenHandler(syncId, player, this);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("selected_passive", selectedPassive);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("selected_passive")) {
            this.selectedPassive = nbt.getInt("selected_passive");
        }
    }

    @Override
    public PassiveSelectionAbility copy() {
        return new PassiveSelectionAbility(id, settings, abilities);
    }
}