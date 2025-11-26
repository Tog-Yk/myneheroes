package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.togyk.myneheroes.component.ModDataComponentTypes;
import net.togyk.myneheroes.gamerule.ModGamerules;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.UpgradablePower;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.PowerData;
import net.togyk.myneheroes.util.ScrollData;

import java.util.ArrayList;
import java.util.List;

public class PowerInjectionItem extends Item implements UpgradableItem {

    public PowerInjectionItem(Settings settings) {
        super(settings);
    }

    public Power getPower(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.POWER,  null);
    }

    public void setPower(ItemStack stack, Power power) {
        stack.set(ModDataComponentTypes.POWER, power);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (getPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks) == 1.0f && user instanceof PlayerEntity player) {
            Power power = this.getPower(stack);

            List<Power> powers = PowerData.getPowers(player);
            List<Power> injectablePowers = powers.stream().filter(Power::isInjectable).toList();
            List<Identifier> powerIds = new ArrayList<>();
            if (!injectablePowers.isEmpty()) {
                powerIds = injectablePowers.stream().map(Power::getId).toList();
            }
            if (power == null && !injectablePowers.isEmpty()) {
                int scrolled = ScrollData.getScrolledPowersOffset(player);
                if (injectablePowers.size() > scrolled) {
                    Power usersPower = injectablePowers.get(scrolled);
                    this.setPower(stack, usersPower);
                    PowerData.removePower(player, usersPower);
                }
            } else if (power != null && !powerIds.contains(power.id)) {
                if (world.getGameRules().getBoolean(ModGamerules.GIVE_POWERS_ABOVE_LIMIT) || injectablePowers.size() < world.getGameRules().getInt(ModGamerules.POWER_LIMIT)) {
                    PowerData.addPower(player, power);
                    this.setPower(stack, null);
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Power power = this.getPower(itemStack);
        List<Power> powers = PowerData.getPowers(user);

        boolean canTake = power == null && !powers.isEmpty();
        boolean canGive = power != null && world.getGameRules().getBoolean(ModGamerules.GIVE_POWERS_ABOVE_LIMIT) || powers.size() < world.getGameRules().getInt(ModGamerules.POWER_LIMIT);
        if (!(canTake || canGive)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (type.isAdvanced()) {
            Power power = this.getPower(stack);
            if (power != null) {
                tooltip.add(Text.literal("Stored Power:").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("power."+power.getId().toTranslationKey()).formatted(Formatting.AQUA));
            } else {
                tooltip.add(Text.literal("Stored Power: none").formatted(Formatting.AQUA));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public Text getName(ItemStack stack) {
        Power power = this.getPower(stack);
        if (power != null) {
            Text name = power.getInjectionName();
            if (name != null) {
                return name;
            }
        }
        if (power == null) {
            //translatable name for empty
            return Text.translatable(this.getTranslationKey(stack) + ".empty");
        }
        //default
        return super.getName(stack);
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

    @Override
    public boolean canUpgrade(ItemStack stack, Upgrade upgrade) {
        Power power = this.getPower(stack);
        return power instanceof UpgradablePower upgradablePower && upgradablePower.canUpgrade(upgrade);
    }

    @Override
    public List<Upgrade> getUpgrades(ItemStack stack) {
        Power power = this.getPower(stack);
        if (power instanceof UpgradablePower upgradablePower) {
            return upgradablePower.getUpgrades();
        }
        return new ArrayList<>();
    }

    @Override
    public void setUpgrades(ItemStack stack, List<Upgrade> upgrades) {
        Power power = this.getPower(stack);
        if (power instanceof UpgradablePower upgradablePower) {
            upgradablePower.setUpgrades(upgrades);
            this.setPower(stack, power);
        }
    }

    @Override
    public void saveUpgrade(ItemStack stack, Upgrade upgrade) {
        Power power = this.getPower(stack);
        if (power instanceof UpgradablePower upgradablePower) {
            upgradablePower.saveUpgrade(upgrade);
            this.setPower(stack, power);
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        for (Upgrade upgrade : this.getUpgrades(stack)) {
            if (upgrade.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference)) {
                return true;
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}
