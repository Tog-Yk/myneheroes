package net.togyk.myneheroes.client;

import com.google.common.base.Predicates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AbilityHolding;
import net.togyk.myneheroes.Item.custom.UpgradableItem;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.keybind.ModKeyBinds;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.StockpilePower;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Environment(EnvType.CLIENT)
public class HudOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && !client.options.hudHidden) {
            boolean hasDrawnAbilities = false;
            boolean hasDrawnPowers = false;

            List<Ability> abilities = ((PlayerAbilities) client.player).myneheroes$getAbilities();
            List<HudAbility> hudAbilities = getHudAbilities(abilities);
            for (HudAbility ability : hudAbilities) {
                if (ability.get()) {
                    HudActionResult result = HudTypeRenderer.drawHud(ability.getType(), drawContext, tickCounter);

                    switch (result) {
                        case ABILITIES_AND_POWER_HUD_DRAWN:
                            hasDrawnAbilities = true;
                            hasDrawnPowers = true;
                        case POWER_HUD_DRAWN:
                            hasDrawnPowers = true;
                        case ABILITIES_HUD_DRAWN:
                            hasDrawnAbilities = true;
                    }
                }
            }

            int width = drawContext.getScaledWindowWidth();
            int height = drawContext.getScaledWindowHeight();

            if (!hasDrawnAbilities) {
                //draw Ability Hud
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                boolean hasChatOpen = MinecraftClient.getInstance().currentScreen instanceof ChatScreen;
                Text text;

                int y = 4;

                Ability abilityBeforeFirst = ((PlayerAbilities) client.player).myneheroes$getAbilityBeforeFirst();
                if (abilityBeforeFirst != null) {
                    drawContext.drawTexture(abilityBeforeFirst.icon, width - 20, y, 0, 8, 16, 8, 16, 16);
                    drawContext.fill(width - 20, y, width - 4, y + 8, 0x70000000);
                }
                y += 10;

                Ability firstAbility = ((PlayerAbilities) client.player).myneheroes$getFirstAbility();
                drawAbility(drawContext, firstAbility, ModKeyBinds.useFirstAbility.isPressed(), width - 20, y);
                if (firstAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+firstAbility.getId().toTranslationKey()) : ModKeyBinds.useFirstAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability secondAbility = ((PlayerAbilities) client.player).myneheroes$getSecondAbility();
                drawAbility(drawContext, secondAbility, ModKeyBinds.useSecondAbility.isPressed(), width - 20, y);
                if (secondAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+secondAbility.getId().toTranslationKey()) : ModKeyBinds.useSecondAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability thirdAbility = ((PlayerAbilities) client.player).myneheroes$getThirdAbility();
                drawAbility(drawContext, thirdAbility, ModKeyBinds.useThirdAbility.isPressed(), width - 20, y);
                if (thirdAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+thirdAbility.getId().toTranslationKey()) : ModKeyBinds.useThirdAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability fourthAbility = ((PlayerAbilities) client.player).myneheroes$getFourthAbility();
                drawAbility(drawContext, fourthAbility, ModKeyBinds.useFourthAbility.isPressed(), width - 20, y);
                if (fourthAbility != null) {
                    text = hasChatOpen ? Text.translatable("ability."+fourthAbility.getId().toTranslationKey()) : ModKeyBinds.useFourthAbility.getBoundKeyLocalizedText();
                    drawContext.drawTextWithShadow(textRenderer, text, width - 6 - 18 - textRenderer.getWidth(text), y + 4, 0xFFFFFF);
                }
                y += 18;

                Ability fifthAbility = ((PlayerAbilities) client.player).myneheroes$getFifthAbility();
                if (fifthAbility != null) {
                    drawContext.drawTexture(fifthAbility.icon, width - 20, y, 0, 0, 16, 8, 16, 16);
                    drawContext.fill(width - 20, y, width - 4, y + 8, 0x70000000);
                }
            }
            if (!hasDrawnPowers) {
                List<Power> powers = ((PlayerPowers) client.player).myneheroes$getPowers();
                if (!powers.isEmpty()) {
                    //draw Power Hud
                    int scrolled = ScrollData.getScrolledPowersOffset(client.player);
                    if (powers.size() > scrolled) {
                        Power power = powers.get(scrolled);

                        drawContext.drawTexture(power.getBackground(), width - 112, height - 32, 0, 0, 112, 32, 112, 32);
                        drawPowerInfo(drawContext, tickCounter, power, power.isDampened(), width - 112, height - 32);

                        if (powers.size() > 1) {
                            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal("+" + (powers.size() - 1)), width - 112 + 4, height - 32 + 16 + 4, 0xFFFFFF);
                        }
                    }
                }
            }
        }
    }

    public static void drawEnergyStorage(DrawContext drawContext, RenderTickCounter tickCounter, PlayerEntity player, int x, int y, int width, int height) {
        int i = 0;

        List<Power> powers = PowerData.getPowers(player);
        List<StockPile> stockpiles = new ArrayList<>(powers.stream().filter(Predicates.instanceOf(StockpilePower.class)).map(power -> (StockPile) power).toList());

        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
        abilities.stream().filter(Predicates.instanceOf(StockPile.class)).forEach(ability -> stockpiles.add((StockPile) ability));

        Iterable<ItemStack> armorIterator = player.getArmorItems();
        List<ItemStack> armor = StreamSupport.stream(armorIterator.spliterator(), false)
                .toList();

        for (ItemStack stack : armor) {
            if (stack.getItem() instanceof UpgradableItem upgradableItem) {
                for (Upgrade upgrade : upgradableItem.getUpgrades(stack)) {
                    if (upgrade instanceof StockPile stockPile) {
                        stockpiles.add(stockPile);
                    }
                }
            }
        }

        List<ItemStack> inventory = player.getInventory().main;

        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof UpgradableItem upgradableItem) {
                for (Upgrade upgrade : upgradableItem.getUpgrades(stack)) {
                    if (upgrade instanceof StockPile stockPile) {
                        stockpiles.add(stockPile);
                    }
                }
            }
        }

        List<Identifier> stockpileAbilitiesIds = filterIds(stockpiles);
        for (int a = i; a < stockpileAbilitiesIds.size() + i; a++) {
            Identifier id = stockpileAbilitiesIds.get(a - i);
            float charge = 0;
            float maxCharge = 0;
            
            Identifier chargeIcon = null;
            
            for (StockPile stockPile : AbilityUtil.getStockPilesMatchingId(stockpiles, id)) {
                charge += stockPile.getCharge();
                maxCharge += stockPile.getMaxCharge();

                chargeIcon = stockPile.getChargeIcon();
            }
            
            if (chargeIcon != null) {

                float chargePercentile = charge / maxCharge;

                int currentHeight = (int) (height * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 18 * i, y + height - currentHeight, 0, height - currentHeight, 16, currentHeight, 16, 16); // yellow rectangle
            }
        }
    }
    
    private static List<Identifier> filterIds(List<StockPile> stockPiles) {
        List<Identifier> ids = new ArrayList<>();
        for (Identifier id : stockPiles.stream().map(StockPile::getStockPileId).toList()) {
            if (id != null && !ids.contains(id)) {
                ids.add(id);
            }
        }
        return ids;
    }

    public static void drawAbility(DrawContext drawContext, Ability ability, boolean isPressed, int x, int y) {
        if (ability != null) {
            if (isPressed) {
                drawContext.drawTexture(ability.pressed_icon, x, y, 0, 0, 16, 16, 16, 16);
            } else {
                drawContext.drawTexture(ability.icon, x, y, 0, 0, 16, 16, 16, 16);
            }
            if (ability.getCooldown() != 0) {
                float cooldownPercentile = (float) ability.getCooldown() / ability.getMaxCooldown();
                int maxIconLength = 16;
                int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                drawContext.fill(x, y + maxIconLength - currentCooldownLength, x + 16, y + maxIconLength, 0x88BBBBBB);
            }
        }
    }

    public static void drawPowerInfo(DrawContext drawContext, RenderTickCounter tickCounter, Power power, boolean isDisabled, int x, int y) {
        if (power != null) {
            Text powerName = Text.translatable("power."+power.getId().toTranslationKey());
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, powerName, x + 4, y + 8 + 4, 0xFFFFFF);
        }
    }


    public static List<HudAbility> getHudAbilities(List<Ability> abilityList) {
        List<HudAbility> abilities = new ArrayList<>();
        for (Ability ability : abilityList) {
            if (ability instanceof HudAbility hudAbility) {
                abilities.add(hudAbility);
            }
        }
        return abilities;
    }

    public static List<StockPile> getStockPiles(PlayerEntity player) {

        List<StockPile> stockPiles = new ArrayList<>();
        List<Power> powerList = PowerData.getPowers(player);
        if (!powerList.isEmpty()) {
            for (Power power : powerList) {
                if (power != null) {
                    power.setHolder(player);
                    if (power instanceof StockPile stockPile && !stockPiles.contains(stockPile)) {
                        stockPiles.add(stockPile);
                    }
                }
            }
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack helmetStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if (helmetStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(helmetStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(helmetStack)) {
                    if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                        if (!stockPiles.contains(stockPile)) {
                            stockPiles.add(stockPile);
                        }
                    }
                }
            }
        }
        ItemStack chestplateStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(chestplateStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(chestplateStack)) {
                    if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                        if (!stockPiles.contains(stockPile)) {
                            stockPiles.add(stockPile);
                        }
                    }
                }
            }
        }
        ItemStack leggingsStack = player.getEquippedStack(EquipmentSlot.LEGS);
        if (leggingsStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(leggingsStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(leggingsStack)) {
                    if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                        if (!stockPiles.contains(stockPile)) {
                            stockPiles.add(stockPile);
                        }
                    }
                }
            }
        }
        ItemStack bootsStack = player.getEquippedStack(EquipmentSlot.FEET);
        if (bootsStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(bootsStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(bootsStack)) {
                    if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                        if (!stockPiles.contains(stockPile)) {
                            stockPiles.add(stockPile);
                        }
                    }
                }
            }
        }
        //get the abilities from the main hand
        ItemStack mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
        if (mainHandStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(mainHandStack) != null) {
                for (Ability ability : abilityHolding.getMainHandAbilities(mainHandStack)) {
                    if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                        if (!stockPiles.contains(stockPile)) {
                            stockPiles.add(stockPile);
                        }
                    }
                }
            }
        }
        //get the abilities from the offhand
        ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);
        if (offHandStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(offHandStack) != null) {
                for (Ability ability : abilityHolding.getOffHandAbilities(offHandStack)) {
                    if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                        if (!stockPiles.contains(stockPile)) {
                            stockPiles.add(stockPile);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            //get the abilities in the hotbar
            if (i < 9 && stack.getItem() instanceof AbilityHolding abilityHolding) {
                if (abilityHolding.getAbilities(stack) != null) {
                    for (Ability ability : abilityHolding.getHotbarAbilities(stack)) {
                        if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                            if (!stockPiles.contains(stockPile)) {
                                stockPiles.add(stockPile);
                            }
                        }
                    }
                }
            }
            //get the abilities in the inventory
            if (stack.getItem() instanceof AbilityHolding abilityHolding) {
                if (abilityHolding.getAbilities(stack) != null) {
                    for (Ability ability : abilityHolding.getAbilities(stack)) {
                        if (ability.getHolder() instanceof Upgrade && ability.getHolder() instanceof StockPile stockPile) {
                            if (!stockPiles.contains(stockPile)) {
                                stockPiles.add(stockPile);
                            }
                        }
                    }
                }
            }
        }
        return stockPiles;
    }
}
