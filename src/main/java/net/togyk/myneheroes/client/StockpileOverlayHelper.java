package net.togyk.myneheroes.client;

import com.google.common.base.Predicates;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AbilityHolding;
import net.togyk.myneheroes.Item.custom.UpgradableItem;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import net.togyk.myneheroes.util.StockPile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class StockpileOverlayHelper {
    public static void drawStockpiles(DrawContext drawContext, PlayerEntity player, int x, int y, int width, int height) {
        List<StockPile> stockpiles = getStockPiles(player);
        drawStockpiles(drawContext, stockpiles, x, y, width, height, 2);
    }

    public static void drawStockpiles(DrawContext drawContext, PlayerEntity player, int x, int y, int width, int height, int spacing) {
        List<StockPile> stockpiles = getStockPiles(player);
        drawStockpiles(drawContext, stockpiles, x, y, width, height, spacing);
    }

    public static void drawStockpiles(DrawContext drawContext, List<StockPile> stockpiles, int x, int y, int width, int height) {
        drawStockpiles(drawContext, stockpiles, x, y, width, height, 2);
    }

    public static void drawStockpiles(DrawContext drawContext, List<StockPile> stockpiles, int x, int y, int width, int height, int spacing) {
        List<Identifier> stockpileIds = filterIds(stockpiles);
        for (int a = 0; a < stockpileIds.size(); a++) {
            Identifier id = stockpileIds.get(a);
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

                drawContext.drawTexture(chargeIcon, x + (width + spacing) * a, y + height - currentHeight, 0, height - currentHeight, width, currentHeight, 16, 16);
            }
        }
    }


    public static void drawStockpilesWithBackground(DrawContext drawContext, PlayerEntity player, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY) {
        List<StockPile> stockpiles = getStockPiles(player);
        drawStockpilesWithBackground(drawContext, stockpiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, width - stockpileOffsetX * 2, height - stockpileOffsetY * 2);
    }
    public static void drawStockpilesWithBackground(DrawContext drawContext, PlayerEntity player, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY, int spacing) {
        List<StockPile> stockpiles = getStockPiles(player);
        drawStockpilesWithBackground(drawContext, stockpiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, width - stockpileOffsetX * 2, height - stockpileOffsetY * 2, spacing);
    }

    public static void drawStockpilesWithBackground(DrawContext drawContext, PlayerEntity player, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY, int stockpileWidth, int stockpileHeight) {
        List<StockPile> stockpiles = getStockPiles(player);
        drawStockpilesWithBackground(drawContext, stockpiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, stockpileWidth, stockpileHeight);
    }


    public static void drawStockpilesWithBackground(DrawContext drawContext, PlayerEntity player, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY, int stockpileWidth, int stockpileHeight, int spacing) {
        List<StockPile> stockpiles = getStockPiles(player);
        drawStockpilesWithBackground(drawContext, stockpiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, stockpileWidth, stockpileHeight, spacing);
    }

    public static void drawStockpilesWithBackground(DrawContext drawContext, List<StockPile> stockPiles, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY) {
        drawStockpilesWithBackground(drawContext, stockPiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, width - stockpileOffsetX * 2, height - stockpileOffsetY * 2);
    }
    public static void drawStockpilesWithBackground(DrawContext drawContext, List<StockPile> stockPiles, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY, int spacing) {
        drawStockpilesWithBackground(drawContext, stockPiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, width - stockpileOffsetX * 2, height - stockpileOffsetY * 2, spacing);
    }
    public static void drawStockpilesWithBackground(DrawContext drawContext, List<StockPile> stockPiles, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY, int stockpileWidth, int stockpileHeight) {
        drawStockpilesWithBackground(drawContext, stockPiles, background, x, y, width, height, stockpileOffsetX, stockpileOffsetY, stockpileWidth, stockpileHeight, 2);
    }

    public static void drawStockpilesWithBackground(DrawContext drawContext, List<StockPile> stockPiles, Identifier background, int x, int y, int width, int height, int stockpileOffsetX, int stockpileOffsetY, int stockpileWidth, int stockpileHeight, int spacing) {
        List<Identifier> stockpileIds = filterIds(stockPiles);
        for (int a = 0; a < stockpileIds.size(); a++) {
            //draw backgrounds
            drawContext.drawGuiTexture(background, width, height, 0, 0, x + (width + spacing) * a, y, width, height);
        }
        drawStockpiles(drawContext, stockPiles, x + stockpileOffsetX, y + stockpileOffsetY, stockpileWidth, stockpileHeight, width - stockpileWidth + spacing);
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

        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
        abilities.stream().filter(Predicates.instanceOf(StockPile.class)).forEach(ability -> stockPiles.add((StockPile) ability));

        PlayerInventory inventory = player.getInventory();

        Iterable<ItemStack> armorIterator = player.getArmorItems();
        List<ItemStack> armor = StreamSupport.stream(armorIterator.spliterator(), false)
                .toList();

        for (ItemStack stack : armor) {
            if (stack.getItem() instanceof UpgradableItem upgradableItem) {
                for (Upgrade upgrade : upgradableItem.getUpgrades(stack)) {
                    if (upgrade instanceof StockPile stockPile) {
                        stockPiles.add(stockPile);
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
