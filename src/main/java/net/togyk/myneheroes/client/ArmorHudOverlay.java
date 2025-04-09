package net.togyk.myneheroes.client;

import com.google.common.base.Predicates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.ReactorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AbilityUtil;
import net.togyk.myneheroes.ability.HudAbility;
import net.togyk.myneheroes.ability.StockpileAbility;
import net.togyk.myneheroes.keybind.ModKeyBindings;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.StockpilePower;
import net.togyk.myneheroes.util.HudActionResult;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ArmorHudOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            boolean hasDrawnAbilities = false;
            boolean hasDrawnPowers = false;

            List<Ability> abilities = ((PlayerAbilities) client.player).getAbilities();
            List<HudAbility> hudAbilities = getHudAbilities(abilities);
            for (HudAbility ability : hudAbilities) {
                HudActionResult result = ability.drawHud(drawContext, tickCounter);

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

            if (!hasDrawnAbilities) {
                //draw Ability Hud
            }
            if (!hasDrawnPowers) {
                //draw Power Hud
            }
        }
    }

    public static void drawEnergyStorage(DrawContext drawContext, RenderTickCounter tickCounter, PlayerEntity player, int x, int y) {
        List<Power> powers = PowerData.getPowers(player);
        List<Power> stockpilePowers = powers.stream().filter(Predicates.instanceOf(StockpilePower.class)).toList();
        int i;
        for (i = 0; i < stockpilePowers.size(); i++) {
            if (stockpilePowers.get(i) instanceof StockpilePower power) {
                int charge = power.getCharge();
                int maxCharge = power.getMaxCharge();

                float chargePercentile = (float) charge / maxCharge;

                int maxHeight = 120;
                int currentHeight = (int) (maxHeight * chargePercentile);

                drawContext.drawTexture(power.getChargeIcon(), x + 16 * i, y + maxHeight - currentHeight, 0, maxHeight - currentHeight,14,currentHeight,14,14); // yellow rectangle
            }
        }
        List<Ability> abilities = ((PlayerAbilities) player).getAbilities();
        List<Ability> stockpileAbilities = abilities.stream().filter(Predicates.instanceOf(StockpileAbility.class)).toList();
        List<Identifier> stockpileAbilitiesIds = filterIds(stockpileAbilities);
        for (int a = i; a < stockpileAbilitiesIds.size() + i; a++) {
            Identifier id = stockpileAbilitiesIds.get(a - i);
            float charge = 0;
            float maxCharge = 0;
            
            Identifier chargeIcon = null;
            
            for (Ability ability : AbilityUtil.getAbilitiesMatchingId(stockpileAbilities, id)) {
                if (ability instanceof StockpileAbility stockpileAbility) {
                    charge += stockpileAbility.getCharge();
                    maxCharge += stockpileAbility.getMaxCharge();
                    
                    chargeIcon = stockpileAbility.getChargeIcon();
                }
            }
            
            if (chargeIcon != null) {

                float chargePercentile = (float) charge / maxCharge;

                int maxHeight = 120;
                int currentHeight = (int) (maxHeight * chargePercentile);

                drawContext.drawTexture(chargeIcon, x + 16 * i, y + maxHeight - currentHeight, 0, maxHeight - currentHeight, 14, currentHeight, 14, 14); // yellow rectangle
            }
        }
    }
    
    private static List<Identifier> filterIds(List<Ability> abilities) {
        List<Identifier> ids = new ArrayList<>();
        for (Identifier id : abilities.stream().map(Ability::getId).toList()) {
            if (id != null && !ids.contains(id)) {
                ids.add(id);
            }
        }
        return ids;
    }

    public static void drawAbility(DrawContext drawContext, RenderTickCounter tickCounter, Ability ability, boolean isDisabled, int x, int y) {
        if (ability != null) {
            if (isDisabled) {
                drawContext.drawTexture(ability.disabled_icon, x, y, 0, 0, 8, 8, 8, 8);
            } else {
                drawContext.drawTexture(ability.icon, x, y, 0, 0, 8, 8, 8, 8);
            }
            if (ability.getCooldown() != 0) {
                float cooldownPercentile = (float) ability.getCooldown() / ability.getMaxCooldown();
                int maxIconLength = 8;
                int currentCooldownLength = (int) (maxIconLength * cooldownPercentile);
                drawContext.fill(x, y + maxIconLength - currentCooldownLength, x + 8, y + maxIconLength, 0x88BBBBBB);
            }
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(ability.getName()), x +10, y, 0xFFFFFF);
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
}
