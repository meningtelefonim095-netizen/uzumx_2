package uz.uzumx;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new Module("HUD", true));
        modules.add(new Module("ArmorHUD", true));
        modules.add(new Module("Minimap", true));
        modules.add(new Module("Zoom", true));
        modules.add(new Module("ChatTime", true));
        modules.add(new Module("Fullbright", false) {
            @Override public void tick(MinecraftClient mc) {
                if (enabled && mc.player != null && !mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                    mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1_000_000, 0, false, false, false));
                }
            }
            @Override public void onDisable() {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            }
        });
    }

    public static Module get(String name) {
        for (Module m : modules) if (m.name.equals(name)) return m;
        throw new IllegalArgumentException(name);
    }

    public static void tick(MinecraftClient mc) { for (Module m : modules) m.tick(mc); }
}
