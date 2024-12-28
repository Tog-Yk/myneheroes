package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PlayerPowers;

import java.util.List;

public class PowerInjectionItem extends Item {

    public PowerInjectionItem(Settings settings) {
        super(settings);
    }

    public Identifier getPowerId(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        if (nbt.contains(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            var modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            if (modNbt.contains("power_id")) {
                return Identifier.of(modNbt.getString("power_id"));
            }
        }
        return null;
    }

    public void setPowerId(ItemStack stack, Identifier powerId) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        var modidData = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modidData = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        if (powerId != null) {
            modidData.putString("power_id", powerId.toString());
        } else {
            modidData.remove("power_id");
        }
        nbt.put(MyneHeroes.MOD_ID, modidData);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Identifier powerId = this.getPowerId(user.getStackInHand(hand));

        PlayerPowers playerPowersI = ((PlayerPowers) user);
        List<Power> powers = playerPowersI.getPowers();
        if (powerId == null && !powers.isEmpty()) {
            Power usersLastPower = powers.getLast();
            this.setPowerId(user.getStackInHand(hand), Powers.getFirstMatchingId(usersLastPower));
            playerPowersI.removePower(usersLastPower);
            user.swingHand(Hand.MAIN_HAND);
            return TypedActionResult.success(user.getStackInHand(hand));
        } else if (Powers.containsId(powerId) && !powers.contains(Powers.get(powerId))) {
            Power power = Powers.get(powerId);
            playerPowersI.addPower(power);
            this.setPowerId(user.getStackInHand(hand), Powers.getFirstMatchingId(null));
            user.swingHand(Hand.MAIN_HAND);
            return TypedActionResult.success(user.getStackInHand(hand));
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (type.isAdvanced()) {
            Identifier storedPowerId = this.getPowerId(stack);
            if (storedPowerId != null) {
                tooltip.add(Text.literal("Stored Power: " + storedPowerId).formatted(Formatting.AQUA));
            } else {
                tooltip.add(Text.literal("Stored Power: none").formatted(Formatting.AQUA));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.isSneaking()) {
            this.use(user.getWorld(),user,hand);
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();
        if (user != null && user.isSneaking()) {
            this.use(user.getWorld(),user,context.getHand());
        }
        return super.useOnBlock(context);
    }
}
