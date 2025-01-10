package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;

import java.util.List;

public class AdvancedArmorItem extends ArmorItem {
    public AdvancedArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    public boolean shouldApplyHud(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        NbtCompound modnbt = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modnbt = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        if (modnbt != null && modnbt.contains("hud_is_active")) {
            return modnbt.getBoolean("hud_is_active");
        } else {
            return true;
        }
    }

    public void toggleHud(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        NbtCompound modnbt = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modnbt = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        if (modnbt.contains("hud_is_active")) {
            boolean shouldApplyHud = modnbt.getBoolean("hud_is_active");
            modnbt.putBoolean("hud_is_active", !shouldApplyHud);
        } else {
            modnbt.putBoolean("hud_is_active", true);
        }
    }

    public void shootRepolserAndReturnLazarEntity(PlayerEntity player, ItemStack reactorStack) {
        if (!player.getWorld().isClient) {
            if (reactorStack.getItem() instanceof ReactorItem reactor) {
                int reactorPower = reactor.getStoredPowerOrDefault(reactorStack, 0);
                if (reactorPower >= 50) {
                    reactor.setStoredPower(reactorStack, reactorPower - 50);
                    // shoot a lazar
                    Vec3d look = player.getRotationVec(1.0F);


                    // Use the player's main hand to mimic shooting
                    player.swingHand(Hand.MAIN_HAND);

                    ArrowEntity projectile = new ArrowEntity(EntityType.ARROW, player.getWorld());
                    projectile.setPosition(player.getX(), player.getEyeY(), player.getZ());
                    projectile.setVelocity(look.x, look.y, look.z, 3.0F, 0.0F);
                    projectile.applyDamageModifier(2.0F);

                    player.getWorld().spawnEntity(projectile);
                }
            }
        }
    }


    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        //using the
        tooltip.add(Text.literal("hud:").formatted(Formatting.BLUE));
        if (this.shouldApplyHud(stack)) {
            tooltip.add(Text.literal(" active").formatted(Formatting.DARK_GREEN));
        } else {
            tooltip.add(Text.literal(" not active").formatted(Formatting.DARK_RED));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    /*
    all the nbt and how they get saved
    shouldApplyHud: nbt.putBoolean("should_apply_hud", !shouldApplyHud);
     */
}
