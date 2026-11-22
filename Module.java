package uz.uzumx;

public class Module {
    public final String name;
    public boolean enabled;
    public Module(String name, boolean enabled) { this.name = name; this.enabled = enabled; }
    public void toggle() { setEnabled(!enabled); }
    public void setEnabled(boolean v) {
        if (v == enabled) return;
        enabled = v;
        if (v) onEnable(); else onDisable();
    }
    public void onEnable() {}
    public void onDisable() {}
    public void tick(net.minecraft.client.MinecraftClient mc) {}
}
