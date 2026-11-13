package dev.fakehotbar;

import dev.fakehotbar.config.HotbarConfig;
import dev.fakehotbar.screen.HotbarOrderScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class FakeHotbarClient implements ClientModInitializer {
    private static KeyBinding openSettingsKey;

    @Override
    public void onInitializeClient() {
        HotbarConfig.load();

        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fakehotbar.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new HotbarOrderScreen());
                }
            }
        });
    }
}
