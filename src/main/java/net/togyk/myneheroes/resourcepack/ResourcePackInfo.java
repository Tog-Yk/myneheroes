package net.togyk.myneheroes.resourcepack;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class ResourcePackInfo {
    private boolean active = false;
    private final Identifier id;
    private final ResourcePackActivationType activationType;

    public ResourcePackInfo(Identifier id, ResourcePackActivationType activationType) {
        this.id = id;
        this.activationType = activationType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Identifier getId() {
        return id;
    }

    public ResourcePackActivationType getActivationType() {
        return activationType;
    }

    public void register() {
        Identifier id = this.getId();
        ModContainer container = FabricLoader.getInstance().getModContainer(id.getNamespace()).orElseThrow();

        ResourceManagerHelper.registerBuiltinResourcePack(
                id,
                container,
                Text.translatable("dataPack." + id.getNamespace() + "." + id.getPath()),
                this.getActivationType()
        );

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Collection<String> enabled = server.getDataPackManager().getEnabledIds();
            this.setActive(enabled.contains(id.toString()));
        });
    }
}
