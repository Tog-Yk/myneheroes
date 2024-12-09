package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.togyk.myneheroes.MyneHeroes;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding fuelReactor;
    public static KeyBinding useFirstAbility;
    public static KeyBinding useSecondAbility;
    public static KeyBinding useThirdAbility;
    public static KeyBinding useForthAbility;

    public static void registerKeyBinds() {
        MyneHeroes.LOGGER.info("Registering Keybinds for " + MyneHeroes.MOD_ID);
        useFirstAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.use_first_ability", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O, // Default key of: O
                "key.category.myneheroes" // keybind category
        ));
        useSecondAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.use_second_ability", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H, // Default key of: H
                "key.category.myneheroes" // keybind category
        ));
        useThirdAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.use_third_ability", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R, // Default key of: R
                "key.category.myneheroes" // keybind category
        ));
        useForthAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.use_forth_ability", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B, // Default key of: B
                "key.category.myneheroes" // keybind category
        ));
        fuelReactor = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.fuel_reactor", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V, // Default key of: V
                "key.category.myneheroes" // keybind category
        ));
    }
}
