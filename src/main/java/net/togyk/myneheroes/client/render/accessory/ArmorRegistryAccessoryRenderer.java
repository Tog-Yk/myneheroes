package net.togyk.myneheroes.client.render.accessory;

import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ArmorRegistryAccessoryRenderer implements AccessoryRenderer {

    @Override
    public <M extends LivingEntity> void render(ItemStack stack, SlotReference reference, MatrixStack matrices, EntityModel<M> model, VertexConsumerProvider vertexConsumers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ArmorRenderer renderer = ArmorRendererRegistryImpl.get(stack.getItem());

        if (renderer != null && model instanceof BipedEntityModel<M> bipedEntityModel) {
            renderer.render(matrices, vertexConsumers, stack, reference.entity(), toEquipmentSlot(reference), light, ((BipedEntityModel<LivingEntity>) bipedEntityModel));
        }
    }

    public static EquipmentSlot toEquipmentSlot(SlotReference reference) {
        String slotName = reference.slotName();

        return switch (slotName) {
            case "hat", "head" -> EquipmentSlot.HEAD;
            case "chest", "back" -> EquipmentSlot.CHEST;
            case "belt", "legs" -> EquipmentSlot.LEGS;
            case "feet" -> EquipmentSlot.FEET;
            case "hand", "ring" -> EquipmentSlot.MAINHAND;
            default -> EquipmentSlot.CHEST; // safe fallback
        };
    }
}
