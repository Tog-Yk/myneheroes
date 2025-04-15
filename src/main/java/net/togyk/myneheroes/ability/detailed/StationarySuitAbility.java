package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.StationaryArmorEntity;

import java.util.ArrayList;
import java.util.List;

public class StationarySuitAbility extends Ability {
    public StationarySuitAbility(Identifier id, String name, int cooldown) {
        super(id, name, cooldown);
    }

    @Override
    public void Use(PlayerEntity player) {
        if (this.getCooldown() == 0) {
            List<ItemStack> armor = new ArrayList<>();
            for (ItemStack stack : player.getArmorItems()) {
                if (stack.getItem() instanceof AdvancedArmorItem item) {
                    List<Identifier> ids = item.getAbilities(stack).stream().map(Ability::getId).toList();
                    if (ids.contains(this.getId())) {
                        armor.add(stack);
                    }
                }
            }
            if (armor.size() == 4) {
                StationaryArmorEntity entity = new StationaryArmorEntity(ModEntities.STATIONARY_ARMOR, player.getWorld());
                entity.setPosition(player.getX(), player.getY(), player.getZ());
                entity.setAngles(player.getYaw(), player.getPitch());
                entity.setVelocity(player.getVelocity());

                for (ItemStack stack : armor) {
                    entity.equipStack(((AdvancedArmorItem) stack.getItem()).getSlotType() , stack.copyAndEmpty());
                }

                player.getWorld().spawnEntity(entity);
            }
        }
    }

    @Override
    public boolean appearsMultipleTimes() {
        return false;
    }

    @Override
    public StationarySuitAbility copy() {
        return new StationarySuitAbility(id, abilityName, maxCooldown);
    }
}
