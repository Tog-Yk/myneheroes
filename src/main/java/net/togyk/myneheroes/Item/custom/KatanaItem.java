package net.togyk.myneheroes.Item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.togyk.myneheroes.util.ModTags;

import java.util.List;
import java.util.Optional;

public class KatanaItem extends ToolItem implements Equipment {
    public KatanaItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings.component(DataComponentTypes.TOOL, createToolComponent()));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    private static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 15.0F), ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            TagKey<Item> katanasTag = ModTags.Items.KATANAS;
            Optional<RegistryEntryList.Named<Item>> katanas = Registries.ITEM.getEntryList(katanasTag);
            if (katanas.isPresent()) {
                for (RegistryEntry<Item> katana : katanas.get()) {
                    player.getItemCooldownManager().set(katana.value(), 40);
                }
            }
        }
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }


    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }
    //shield methods
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.OFFHAND;
    }

    public void onBlocked(PlayerEntity player, float amount) {
        player.stopUsingItem();

        TagKey<Item> katanasTag = ModTags.Items.KATANAS;
        Optional<RegistryEntryList.Named<Item>> katanas = Registries.ITEM.getEntryList(katanasTag);
        if (katanas.isPresent()) {
            for (RegistryEntry<Item> katana : katanas.get()) {
                player.getItemCooldownManager().set(katana.value(), 120);
            }
        }
    }
}
