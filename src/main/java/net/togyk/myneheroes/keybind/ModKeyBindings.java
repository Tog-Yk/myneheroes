package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.togyk.myneheroes.MyneHeroes;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding first_ability_key;

    public static void registerKeyBinds() {
        MyneHeroes.LOGGER.info("Registering Keybinds for " + MyneHeroes.MOD_ID);
        first_ability_key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R, // Default key of: R
                "category.myneheroes" // keybind category
        ));
    }
}
