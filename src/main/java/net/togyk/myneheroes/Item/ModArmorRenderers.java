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
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorWithFaceplateItem;
import net.togyk.myneheroes.Item.custom.UpgradeItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.armor.AdvancedArmorModel;
import net.togyk.myneheroes.client.render.armor.AdvancedHelmetWithFaceplateModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModelRegistry;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.HexConsumer;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModArmorRenderers {
    private static void registerToolbelt(Item item) {
        ArmorRenderer.register(
            (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, contextModel) -> {
                if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                    Upgrade upgrade = upgradeItem.getUpgrade(stack);
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
                (armorModel, layer, stack, matrixStack, vertexConsumerProvider, light) -> {
                    Identifier texture = armorModel.getTexture(stack, layer);

                    VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(texture), stack.hasGlint());
                    armorModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
                }
        );
    }

    private static void registerDyeableAdvancedArmor(Item item) {
        registerArmor(item, AdvancedArmorModel::new, AdvancedArmorModel.ADVANCED_ARMOR,
                (armorModel, layer, stack, matrixStack, vertexConsumerProvider, light) -> {
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
                        int armor_light = armorItem.layerIsLightable(stack, layer) ? (armorItem.getLightLevel(stack, layer) / 15) * 15728880 : light;
                        int color = armorItem.layerIsDyed(stack, layer) ? armorItem.getColor(stack, layer) : 0xFFFFFFFF;
                        Identifier texture = armorModel.getTexture(stack, layer);

                        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(texture), stack.hasGlint());
                        armorModel.render(matrixStack, vertexConsumer, armor_light, OverlayTexture.DEFAULT_UV, color);
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
                (armorModel, layer, stack, matrixStack, vertexConsumerProvider, light) -> {
                    if (stack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {

                        int armor_light = armorItem.layerIsLightable(stack, layer) ? (armorItem.getLightLevel(stack, layer) / 15) * 15728880 : light;
                        int color = armorItem.layerIsDyed(stack, layer) ? armorItem.getColor(stack, layer) : 0xFFFFFFFF;
                        Identifier texture = armorModel.getTexture(stack, layer);

                        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(texture), stack.hasGlint());
                        armorModel.render(matrixStack, vertexConsumer, armor_light, OverlayTexture.DEFAULT_UV, color);
                    }
                }
        );
    }

    public static void registerArmorRenderers() {
        MyneHeroes.LOGGER.info("Registering Armor Renderers for " + MyneHeroes.MOD_ID);

        registerToolbelt(ModItems.TOOLBELT);
        registerToolbelt(ModItems.IRON_TOOLBELT);
        registerToolbelt(ModItems.DIAMOND_TOOLBELT);
        registerToolbelt(ModItems.NETHERITE_TOOLBELT);

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
