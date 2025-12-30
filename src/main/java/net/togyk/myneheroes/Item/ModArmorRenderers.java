package net.togyk.myneheroes.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.UpgradeItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.armor.AdvancedArmorModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModelRegistry;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModArmorRenderers {
    private static void registerToolbelt(Item item) {
        ArmorRenderer.register(
            (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                    Upgrade upgrade = upgradeItem.getUpgrade(stack);
                    UpgradeModel upgradeModel = UpgradeModelRegistry.get(upgrade, MinecraftClient.getInstance().getEntityModelLoader());
                    if (contextModel != null) {
                        contextModel.copyBipedStateTo(upgradeModel);

                        upgradeModel.setEquipmentSlotVisible(equipmentSlot);
                        Identifier texture = upgradeModel.getTexture(upgrade);

                        ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, upgradeModel, texture);
                    }
                }
        }, item);
    }

    private static void registerAdvancedArmor(Item item) {
        ArmorRenderer.register(
                (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                    //render armor
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                        AdvancedArmorModel armorModel = new AdvancedArmorModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(AdvancedArmorModel.ADVANCED_ARMOR));

                        if (contextModel != null) {
                            armorModel.setRotation(contextModel);
                            armorModel.setEquipmentSlotVisible(equipmentSlot);
                            for (int layer = 0; layer < armorItem.getMaterial().value().layers().size(); layer++) {
                                Identifier texture = armorModel.getTexture(stack, layer);

                                VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(texture), stack.hasGlint());
                                armorModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
                            }
                        }
                    }

                    //render upgrades
                    if (stack.getItem() instanceof AdvancedArmorItem armor) {
                        List<Upgrade> upgrades = armor.getUpgrades(stack);
                        for (Upgrade upgrade: upgrades) {
                            UpgradeModel upgradeModel = UpgradeModelRegistry.get(upgrade, MinecraftClient.getInstance().getEntityModelLoader());
                            if (contextModel != null) {
                                contextModel.copyBipedStateTo(upgradeModel);

                                upgradeModel.setEquipmentSlotVisible(equipmentSlot);
                                Identifier texture = upgradeModel.getTexture(upgrade);

                                ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, upgradeModel, texture);
                            }
                        }
                    }
                }, item);
    }

    private static void registerDyeableAdvancedArmor(Item item) {
        ArmorRenderer.register(
                (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                    //render armor
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                        AdvancedArmorModel armorModel = new AdvancedArmorModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(AdvancedArmorModel.ADVANCED_ARMOR));

                        if (contextModel != null) {
                            armorModel.setRotation(contextModel);
                            armorModel.setEquipmentSlotVisible(equipmentSlot);
                            for (int layer = 0; layer < armorItem.getMaterial().value().layers().size(); layer++) {
                                int armor_light = armorItem.layerIsLightable(stack, layer) ? (armorItem.getLightLevel(stack, layer) / 15) * 15728880 : light;
                                int color = armorItem.layerIsDyed(stack, layer) ? armorItem.getColor(stack, layer) : 0xFFFFFFFF;
                                Identifier texture = armorModel.getTexture(stack, layer);

                                VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(texture), stack.hasGlint());
                                armorModel.render(matrixStack, vertexConsumer, armor_light, OverlayTexture.DEFAULT_UV, color);
                            }
                        }
                    }

                    //render upgrades
                    if (stack.getItem() instanceof AdvancedArmorItem armor) {
                        List<Upgrade> upgrades = armor.getUpgrades(stack);
                        for (Upgrade upgrade: upgrades) {
                            UpgradeModel upgradeModel = UpgradeModelRegistry.get(upgrade, MinecraftClient.getInstance().getEntityModelLoader());
                            if (contextModel != null) {
                                contextModel.copyBipedStateTo(upgradeModel);

                                upgradeModel.setEquipmentSlotVisible(equipmentSlot);
                                Identifier texture = upgradeModel.getTexture(upgrade);

                                ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, upgradeModel, texture);
                            }
                        }
                    }
                }, item);
    }

    public static void registerArmorRenderers() {
        MyneHeroes.LOGGER.info("Registering Armor Renderers for " + MyneHeroes.MOD_ID);

        registerToolbelt(ModItems.TOOLBELT);
        registerToolbelt(ModItems.IRON_TOOLBELT);
        registerToolbelt(ModItems.DIAMOND_TOOLBELT);
        registerToolbelt(ModItems.NETHERITE_TOOLBELT);
        
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_HELMET);
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_BOOTS);

        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_HELMET);
        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_BOOTS);

        registerDyeableAdvancedArmor(ModItems.MARK45_NETHERITE_HELMET);
        registerDyeableAdvancedArmor(ModItems.MARK45_NETHERITE_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.MARK45_NETHERITE_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.MARK45_NETHERITE_BOOTS);

        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_GOLD_TITANIUM_HELMET);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_GOLD_TITANIUM_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_GOLD_TITANIUM_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_GOLD_TITANIUM_BOOTS);

        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_VIBRANIUM_HELMET);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_VIBRANIUM_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_VIBRANIUM_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_VIBRANIUM_BOOTS);
    }
}
