package net.togyk.myneheroes.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.ToolbeltModel;

@Environment(EnvType.CLIENT)
public class ModArmorRenderers {

    private static void registerToolbelt(Item item) {
        ArmorRenderer.register(
            (matrixStack, vertexConsumerProvider, stack, livingEntity, equipmentSlot, light, model) -> {
                BipedEntityModel<LivingEntity> toolbeltModel = new ToolbeltModel(
                        MinecraftClient.getInstance().getEntityModelLoader()
                                .getModelPart(ToolbeltModel.TOOLBELT)
                );
                model.copyBipedStateTo(toolbeltModel);

                Identifier itemId = Registries.ITEM.getId(item);
                Identifier texture = Identifier.of(itemId.getNamespace(), "textures/models/toolbelt/" + itemId.getPath() + ".png");

                ArmorRenderer.renderPart(matrixStack, vertexConsumerProvider, light, stack, toolbeltModel, texture);
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
