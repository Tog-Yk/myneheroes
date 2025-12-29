package net.togyk.myneheroes.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.UpgradeItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModel;
import net.togyk.myneheroes.client.render.upgrade.UpgradeModelRegistry;
import net.togyk.myneheroes.upgrade.Upgrade;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModArmorRenderers {
    private static void registerToolbelt(Item item) {
        ArmorRenderer.register(
            (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, model) -> {
                if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                    Upgrade upgrade = upgradeItem.getUpgrade(stack);
                    UpgradeModel upgradeModel = UpgradeModelRegistry.get(upgrade, MinecraftClient.getInstance().getEntityModelLoader());
                    if (model != null) {
                        model.copyBipedStateTo(upgradeModel);

                        upgradeModel.setEquipmentSlotVisible(equipmentSlot);
                        Identifier texture = upgradeModel.getTexture(upgrade);

                        ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, upgradeModel, texture);
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
    }
}
