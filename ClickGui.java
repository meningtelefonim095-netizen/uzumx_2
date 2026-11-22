package uz.uzumx;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    private static final int X = 24, Y = 24, W = 140, ROW = 20;

    public ClickGui() { super(Text.literal("UzumX")); }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        ctx.fillGradient(0, 0, width, height, 0x90000000, 0xB0300A55);
        int h = 26 + ModuleManager.modules.size() * ROW;
        ctx.fill(X, Y, X + W, Y + h, Theme.PANEL);
        ctx.fill(X, Y, X + W, Y + 2, Theme.ACCENT);
        ctx.drawText(textRenderer, "UzumX", X + 8, Y + 9, Theme.ACCENT2, true);
        int y = Y + 24;
        for (Module m : ModuleManager.modules) {
            boolean hover = mx >= X && mx < X + W && my >= y && my < y + ROW;
            if (hover) ctx.fill(X, y, X + W, y + ROW, 0x40B65CFF);
            ctx.drawText(textRenderer, m.name, X + 8, y + 6, Theme.TEXT, false);
            ctx.fill(X + W - 22, y + 6, X + W - 8, y + 14, m.enabled ? Theme.ON : Theme.OFF);
            y += ROW;
        }
        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int y = Y + 24;
        for (Module m : ModuleManager.modules) {
            if (mx >= X && mx < X + W && my >= y && my < y + ROW) { m.toggle(); return true; }
            y += ROW;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override public boolean shouldPause() { return false; }
}
