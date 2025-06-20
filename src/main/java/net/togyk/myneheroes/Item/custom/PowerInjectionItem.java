package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.togyk.myneheroes.component.ModDataComponentTypes;
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Power power = this.getPower(user.getStackInHand(hand));

        List<Power> powers = PowerData.getPowers(user);
        List<Identifier> powerIds = new ArrayList<>();
        if (!powers.isEmpty()) {
            powerIds = powers.stream().map(Power::getId).toList();
        }
        if (power == null && !powers.isEmpty()) {
            int scrolled = ScrollData.getScrolledPowersOffset(user);
            if (powers.size() > scrolled) {
                Power usersPower = powers.get(scrolled);
                this.setPower(user.getStackInHand(hand), usersPower);
                PowerData.removePower(user, usersPower);
                user.swingHand(hand);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
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
                tooltip.add(Text.literal("Stored Power:").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("power."+power.getId().toTranslationKey()).formatted(Formatting.AQUA));
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
}
