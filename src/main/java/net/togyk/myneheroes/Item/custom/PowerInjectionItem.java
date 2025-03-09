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
import net.togyk.myneheroes.util.PowerData;

import java.util.ArrayList;
import java.util.List;

public class PowerInjectionItem extends Item {

    public PowerInjectionItem(Settings settings) {
        super(settings);
    }

    public Power getPower(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        if (nbt.contains(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            var modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            if (modNbt.contains("power")) {
                return PowerData.nbtToPower(modNbt.getCompound("power"));
            }
        }
        return null;
    }

    public void setPower(ItemStack stack, Power power) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        var modidData = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modidData = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        if (power != null) {
            modidData.put("power", power.getNbt());
        } else {
            modidData.remove("power");
        }
        nbt.put(MyneHeroes.MOD_ID, modidData);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Power power = this.getPower(user.getStackInHand(hand));

        List<Power> powers = PowerData.getPowers(user);
        List<Identifier> powerIds = new ArrayList<>();
        if (!powers.isEmpty() && powers != null) {
            powerIds = powers.stream().map(Power::getId).toList();
        }
        if (power == null && !powers.isEmpty()) {
            Power usersLastPower = powers.getLast();
            this.setPower(user.getStackInHand(hand), usersLastPower);
            PowerData.removePower(user, usersLastPower);
            user.swingHand(hand);
            return TypedActionResult.success(user.getStackInHand(hand));
        } else if (power != null && !powerIds.contains(power.id)) {
            PowerData.addPower(user, power);
            this.setPower(user.getStackInHand(hand), null);
            user.swingHand(hand);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (type.isAdvanced()) {
            Power power = this.getPower(stack);
            if (power != null) {
                tooltip.add(Text.literal("Stored Power: " + power.getName()).formatted(Formatting.AQUA));
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
