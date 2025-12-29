package net.togyk.myneheroes.client.render.upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.upgrade.Upgrade;

@Environment(EnvType.CLIENT)
public class ToolbeltModel extends UpgradeModel {
    public static final EntityModelLayer TOOLBELT =
            new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "toolbelt"), "main");

    public ToolbeltModel(EntityModelLoader loader) {
        super(loader.getModelPart(TOOLBELT));
    }

    @Override
    public Identifier getTexture(ItemStack stack) {
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        Identifier texture = Identifier.of(itemId.getNamespace(), "textures/models/toolbelt/" + itemId.getPath() + ".png");

        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        if (resourceManager.getResource(texture).isEmpty()) {
            texture = Identifier.of(MyneHeroes.MOD_ID, "textures/models/toolbelt/toolbelt.png");
        }
        return texture;
    }

    public Identifier getTexture(Upgrade upgrade) {
        Identifier id = upgrade.getId();
        Identifier texture = Identifier.of(id.getNamespace(), "textures/models/toolbelt/" + id.getPath() + ".png");

        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        if (resourceManager.getResource(texture).isEmpty()) {
            texture = Identifier.of(MyneHeroes.MOD_ID, "textures/models/toolbelt/toolbelt.png");
        }
        return texture;
    }
}