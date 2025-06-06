package net.togyk.myneheroes.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.togyk.myneheroes.MyneHeroes;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding useFirstAbility;
    public static KeyBinding useSecondAbility;
    public static KeyBinding useThirdAbility;
    public static KeyBinding useFourthAbility;

    public static KeyBinding abilitiesScrollUp;
    public static KeyBinding abilitiesScrollDown;

    public static KeyBinding powersScrollUp;
    public static KeyBinding powersScrollDown;

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
        useFourthAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.use_fourth_ability", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B, // Default key of: B
                "key.category.myneheroes" // keybind category
        ));
        abilitiesScrollUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.scroll_up_abilities", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP, // Default key of: up
                "key.category.myneheroes" // keybind category
        ));
        abilitiesScrollDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.scroll_down_abilities", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN, // Default key of: down
                "key.category.myneheroes" // keybind category
        ));
        powersScrollUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.scroll_up_powers", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT, // Default key of: up
                "key.category.myneheroes" // keybind category
        ));
        powersScrollDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myneheroes.scroll_down_powers", //translatable key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT, // Default key of: down
                "key.category.myneheroes" // keybind category
        ));
    }
}
