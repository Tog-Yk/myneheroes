package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;

import java.util.List;

public class AbilityHoldingItem extends Item implements AbilityHolding {
    public Ability ability;
    public AbilityHoldingItem(Ability ability, Settings settings) {
        super(settings);
        this.ability = ability;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Holds ability: " + Text.translatable("ability."+ability.getId().toTranslationKey())));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public List<Ability> getAbilities(ItemStack stack, World world) {
        ability.setHolder(stack);
        return List.of(this.ability);
    }

    @Override
    public void saveAbility(ItemStack stack, World world, Ability ability) {
        this.ability = ability;
    }
}
