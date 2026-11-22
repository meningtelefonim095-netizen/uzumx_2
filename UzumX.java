package uz.uzumx;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UzumX implements ClientModInitializer {
    public static KeyBinding zoomKey, guiKey;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void onInitializeClient() {
        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.uzumx.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.uzumx"));
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.uzumx.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.uzumx"));
        ModuleManager.init();

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (guiKey.wasPressed()) mc.setScreen(new ClickGui());
            ModuleManager.tick(mc);
        });
        HudRenderCallback.EVENT.register(Hud::render);
        ClientReceiveMessageEvents.MODIFY_GAME.register((msg, overlay) ->
            overlay || !ModuleManager.get("ChatTime").enabled ? msg
                : Text.literal("§8[" + LocalTime.now().format(FMT) + "] §r").append(msg));
        ClientCommandRegistrationCallback.EVENT.register((d, r) -> Waypoints.register(d));
    }
}
