package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    // Declare the keybinding as a public static field
    public static KeyBinding toggleHudKeybinding;
    public static KeyBinding shootRepolser;

    // Initialize and register the keybinding
    public static void registerModKeybindings() {
        toggleHudKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.togglehud", // Translation key
                InputUtil.Type.KEYSYM, // Input type
                GLFW.GLFW_KEY_H, // Default key (H)
                "key.category.myneheroes" // Translation key for the category
        ));
        shootRepolser = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.shoot_repolser", // Translation key
                InputUtil.Type.KEYSYM, // Input type
                GLFW.GLFW_KEY_R, // Default key (R)
                "key.category.myneheroes" // Translation key for the category
        ));
    }
}
