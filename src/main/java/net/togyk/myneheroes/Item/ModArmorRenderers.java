package net.togyk.myneheroes.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorWithFaceplateItem;
import net.togyk.myneheroes.Item.custom.UpgradeItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.client.render.ReactorModel;
import net.togyk.myneheroes.client.render.armor.AdvancedArmorModel;
import net.togyk.myneheroes.client.render.armor.AdvancedHelmetWithFaceplateModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModelRegistry;
import net.togyk.myneheroes.upgrade.AbilityUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.HexConsumer;
import net.togyk.myneheroes.util.ModTags;
import net.togyk.myneheroes.util.StockPile;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModArmorRenderers {private static void registerUpgrade(Item item) {
        ArmorRenderer.register(
            (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                    Upgrade upgrade = upgradeItem.getUpgrade(stack, null);
                    UpgradeModel upgradeModel = UpgradeModelRegistry.get(upgrade, MinecraftClient.getInstance().getEntityModelLoader());
                    if (upgradeModel != null && contextModel != null) {
                        contextModel.copyBipedStateTo(upgradeModel);

                        upgradeModel.setEquipmentSlotVisible(equipmentSlot);
                        Identifier texture = upgradeModel.getTexture(upgrade);

                        ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, upgradeModel, texture);
                    }
                }
        }, item);
    }

    private static void registerArcReactor(Item item) {
        ArmorRenderer.register(
                (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                    ReactorModel reactorModel = new ReactorModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(ReactorModel.REACTOR));
                    if (contextModel != null) {
                        contextModel.copyBipedStateTo(reactorModel);

                        reactorModel.setEquipmentSlotVisible(equipmentSlot);
                        Identifier texture = reactorModel.getTexture(stack);

                        ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, reactorModel, texture);
                    }
                }, item);
    }

    private static <M extends AdvancedArmorModel> void registerArmor(Item item, Function<EntityModelLayer, M> modelProvider, EntityModelLayer part, BiConsumer<M, ItemStack> prepareModel, HexConsumer<M, Integer, ItemStack, MatrixStack, VertexConsumerProvider, Integer> drawLayer) {
        ArmorRenderer.register(
                (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                    //render armor
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                        M armorModel = modelProvider.apply(part);

                        if (contextModel != null) {
                            armorModel.setRotation(contextModel);
                            armorModel.setEquipmentSlotVisible(equipmentSlot);
                            prepareModel.accept(armorModel, stack);
                            for (int layer = 0; layer < armorItem.getMaterial().value().layers().size(); layer++) {
                                drawLayer.accept(armorModel, layer, stack, matrixStack, vertexConsumerProvider, light);
                            }
                        }
                    }

                    //render upgrades
                    if (stack.getItem() instanceof AdvancedArmorItem armor) {
                        List<Upgrade> upgrades = armor.getUpgrades(stack);
                        for (Upgrade upgrade: upgrades) {
                            UpgradeModel upgradeModel = UpgradeModelRegistry.get(upgrade, MinecraftClient.getInstance().getEntityModelLoader());
                            if (upgradeModel != null && contextModel != null) {
                                contextModel.copyBipedStateTo(upgradeModel);

                                upgradeModel.setEquipmentSlotVisible(equipmentSlot);
                                Identifier texture = upgradeModel.getTexture(upgrade);

                                ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, upgradeModel, texture);
                            }
                        }
                    }
                }, item);

    }
    private static <M extends AdvancedArmorModel> void registerArmor(Item item, Function<EntityModelLayer, M> modelProvider, EntityModelLayer part, HexConsumer<M, Integer, ItemStack, MatrixStack, VertexConsumerProvider, Integer> drawLayer) {
        registerArmor(item, modelProvider, part, (armorModel, stack) -> {}, drawLayer);
    }

    private static void registerAdvancedArmor(Item item) {
        registerArmor(item, AdvancedArmorModel::new, AdvancedArmorModel.ADVANCED_ARMOR,
                (armorModel, layerIndex, stack, matrixStack, vertexConsumerProvider, light) -> {
                    Identifier texture = armorModel.getTexture(stack, layerIndex);

                    int alpha = 0xFF;

                    if (stack.getItem() instanceof AdvancedArmorItem armorItem) {
                        ArmorMaterial.Layer layer = armorItem.getMaterial().value().layers().get(layerIndex);
                        String layerName = layer.getTexture(true).getPath().substring("textures/models/armor/".length(), layer.getTexture(true).getPath().length() - "_layer_2.png".length());
                        if (layerName.endsWith("kinetic_energy")) {
                            List<Upgrade> upgrades = armorItem.getUpgrades(stack);
                            List<Upgrade> kineticEnergyUpgrades = upgrades.stream().filter(upgrade -> upgrade instanceof StockPile && upgrade.isIn(ModTags.Upgrades.KINETIC_ENERGY)).toList();
                            if (!kineticEnergyUpgrades.isEmpty()) {
                                float charge = 1.0F;
                                for (Upgrade upgrade : kineticEnergyUpgrades) {
                                    StockPile stockPile = (StockPile) upgrade;
                                    charge = charge * stockPile.getCharge() / stockPile.getMaxCharge();
                                }

                                alpha = (int) (0xFF * charge);
                            } else {
                                alpha = 0;
                            }
                        }
                    }

                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(texture));

                    armorModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(alpha, 0xFFFFFF));
                }
        );
    }

    private static void registerDyeableAdvancedArmor(Item item) {
        registerArmor(item, AdvancedArmorModel::new, AdvancedArmorModel.ADVANCED_ARMOR,
                (armorModel, layerIndex, stack, matrixStack, vertexConsumerProvider, light) -> {
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                        int armor_light = armorItem.layerIsLightable(stack, layerIndex) ? (armorItem.getLightLevel(stack, layerIndex) / 15) * 15728880 : light;
                        int color = armorItem.layerIsDyed(stack, layerIndex) ? armorItem.getColor(stack, layerIndex) : 0xFFFFFFFF;
                        Identifier texture = armorModel.getTexture(stack, layerIndex);

                        int alpha = 0xFF;

                        ArmorMaterial.Layer layer = armorItem.getMaterial().value().layers().get(layerIndex);
                        String layerName = layer.getTexture(true).getPath().substring("textures/models/armor/".length(), layer.getTexture(true).getPath().length() - "_layer_2.png".length());
                        if (layerName.endsWith("kinetic_energy")) {
                            List<Upgrade> upgrades = armorItem.getUpgrades(stack);
                            List<Upgrade> kineticEnergyUpgrades = upgrades.stream().filter(upgrade -> upgrade.isIn(ModTags.Upgrades.KINETIC_ENERGY)).toList();
                            if (!kineticEnergyUpgrades.isEmpty()) {
                                float charge = 1.0F;
                                for (Upgrade upgrade : kineticEnergyUpgrades) {
                                    if (upgrade instanceof StockPile stockPile) {
                                        charge = charge * stockPile.getCharge() / stockPile.getMaxCharge();
                                    } else if (upgrade instanceof AbilityUpgrade abilityUpgrade) {
                                        for (Ability ability : abilityUpgrade.getAbilities()) {
                                            if (ability instanceof StockPile stockPile) {
                                                charge = charge * stockPile.getCharge() / stockPile.getMaxCharge();
                                            }
                                        }
                                    }
                                }

                                alpha = (int) (0xFF * charge);
                            } else {
                                alpha = 0;
                            }
                        }

                        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(texture));

                        armorModel.render(matrixStack, vertexConsumer, armor_light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(alpha, color));
                    }
                }
        );
    }

    private static void registerDyeableAdvancedHelmetWithFaceplate(Item item) {
        registerArmor(item, AdvancedHelmetWithFaceplateModel::new, AdvancedHelmetWithFaceplateModel.ADVANCED_HELMET_W_FACEPLATE,
                (armorModel, stack) -> {
                    if (stack.getItem() instanceof DyeableAdvancedArmorWithFaceplateItem armorItem) {
                        armorModel.setFaceplateOpenProgress(armorItem.getOpenProgress(stack));
                    }
                },
                (armorModel, layerIndex, stack, matrixStack, vertexConsumerProvider, light) -> {
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                        int armor_light = armorItem.layerIsLightable(stack, layerIndex) ? (armorItem.getLightLevel(stack, layerIndex) / 15) * 15728880 : light;
                        int color = armorItem.layerIsDyed(stack, layerIndex) ? armorItem.getColor(stack, layerIndex) : 0xFFFFFFFF;
                        Identifier texture = armorModel.getTexture(stack, layerIndex);

                        int alpha = 0xFF;

                        ArmorMaterial.Layer layer = armorItem.getMaterial().value().layers().get(layerIndex);
                        String layerName = layer.getTexture(true).getPath().substring("textures/models/armor/".length(), layer.getTexture(true).getPath().length() - "_layer_2.png".length());
                        if (layerName.endsWith("kinetic_energy")) {
                            List<Upgrade> upgrades = armorItem.getUpgrades(stack);
                            List<Upgrade> kineticEnergyUpgrades = upgrades.stream().filter(upgrade -> upgrade.isIn(ModTags.Upgrades.KINETIC_ENERGY)).toList();
                            if (!kineticEnergyUpgrades.isEmpty()) {
                                float charge = 1.0F;
                                for (Upgrade upgrade : kineticEnergyUpgrades) {
                                    if (upgrade instanceof StockPile stockPile) {
                                        charge = charge * stockPile.getCharge() / stockPile.getMaxCharge();
                                    } else if (upgrade instanceof AbilityUpgrade abilityUpgrade) {
                                        for (Ability ability : abilityUpgrade.getAbilities()) {
                                            if (ability instanceof StockPile stockPile) {
                                                charge = charge * stockPile.getCharge() / stockPile.getMaxCharge();
                                            }
                                        }
                                    }
                                }

                                alpha = (int) (0xFF * charge);
                            } else {
                                alpha = 0;
                            }
                        }

                        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(texture));

                        armorModel.render(matrixStack, vertexConsumer, armor_light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.withAlpha(alpha, color));
                    }
                }
        );
    }

    public static void registerArmorRenderers() {
        MyneHeroes.LOGGER.info("Registering Armor Renderers for " + MyneHeroes.MOD_ID);

        registerUpgrade(ModItems.TOOLBELT);
        registerUpgrade(ModItems.IRON_TOOLBELT);
        registerUpgrade(ModItems.DIAMOND_TOOLBELT);
        registerUpgrade(ModItems.NETHERITE_TOOLBELT);

        registerArcReactor(ModItems.ARC_REACTOR);

        registerDyeableAdvancedHelmetWithFaceplate(ModItems.MARK6_VIBRANIUM_HELMET);
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.MARK6_VIBRANIUM_BOOTS);

        registerDyeableAdvancedHelmetWithFaceplate(ModItems.MARK3_GOLD_TITANIUM_HELMET);
        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.MARK3_GOLD_TITANIUM_BOOTS);

        registerDyeableAdvancedHelmetWithFaceplate(ModItems.MARK45_NETHERITE_HELMET);
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

        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_NETHERITE_HELMET);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_NETHERITE_CHESTPLATE);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_NETHERITE_LEGGINGS);
        registerDyeableAdvancedArmor(ModItems.SPEEDSTER_NETHERITE_BOOTS);
    }
}
