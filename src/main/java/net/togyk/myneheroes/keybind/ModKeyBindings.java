package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.togyk.myneheroes.MyneHeroes;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding toggleHudKeybinding;
    public static KeyBinding shootRepolser;

    public static void registerKeyBinds() {
        MyneHeroes.LOGGER.info("Registering Keybinds for " + MyneHeroes.MOD_ID);
        toggleHudKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.togglehud", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H, // Default key of: H
                "category.myneheroes" // keybind category
        ));
        shootRepolser = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.shoot_repolser", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R, // Default key of: R
                "category.myneheroes" // keybind category
        ));
    }
}
