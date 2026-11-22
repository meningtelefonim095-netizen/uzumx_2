package uz.uzumx;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.block.BlockState;

public class Hud {
    private static final int R = 16, SCALE = 3;
    private static final int[] map = new int[(R * 2) * (R * 2)];
    private static int lastBuild = -100;

    public static void render(DrawContext ctx, RenderTickCounter tick) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.options.hudHidden) return;
        var tr = mc.textRenderer;

        if (ModuleManager.get("HUD").enabled) {
            int ping = 0;
            var h = mc.getNetworkHandler();
            if (h != null) {
                var e = h.getPlayerListEntry(mc.player.getUuid());
                if (e != null) ping = e.getLatency();
            }
            BlockPos p = mc.player.getBlockPos();
            String[] lines = {
                "UzumX",
                mc.getCurrentFps() + " FPS   " + ping + " ms",
                "XYZ " + p.getX() + " " + p.getY() + " " + p.getZ(),
                Waypoints.nearest(p) == null ? "wp: -" : "wp: " + Waypoints.nearest(p)
            };
            int w = 0;
            for (String s : lines) w = Math.max(w, tr.getWidth(s));
            ctx.fill(4, 4, w + 14, lines.length * 11 + 8, Theme.BG);
            ctx.fill(4, 4, 6, lines.length * 11 + 8, Theme.ACCENT);
            for (int i = 0; i < lines.length; i++)
                ctx.drawText(tr, lines[i], 10, 7 + i * 11, i == 0 ? Theme.ACCENT2 : Theme.TEXT, true);
        }

        if (ModuleManager.get("ArmorHUD").enabled) {
            int x = ctx.getScaledWindowWidth() / 2 + 100, y = ctx.getScaledWindowHeight() - 20;
            for (int i = 0; i < 4; i++) {
                ItemStack s = mc.player.getInventory().armor.get(3 - i);
                if (s.isEmpty()) continue;
                ctx.drawItem(s, x + i * 18, y);
                ctx.drawStackOverlay(tr, s, x + i * 18, y);
            }
        }

        if (ModuleManager.get("Minimap").enabled) drawMinimap(ctx, mc);
    }

    private static void drawMinimap(DrawContext ctx, MinecraftClient mc) {
        if (mc.player.age - lastBuild >= 10) { build(mc); lastBuild = mc.player.age; }
        int size = R * 2 * SCALE;
        int ox = ctx.getScaledWindowWidth() - size - 8, oy = 8;
        ctx.fill(ox - 2, oy - 2, ox + size + 2, oy + size + 2, Theme.ACCENT);
        for (int z = 0; z < R * 2; z++)
            for (int x = 0; x < R * 2; x++)
                ctx.fill(ox + x * SCALE, oy + z * SCALE, ox + x * SCALE + SCALE, oy + z * SCALE + SCALE, map[z * R * 2 + x]);
        int c = size / 2;
        ctx.fill(ox + c - 2, oy + c - 2, ox + c + 2, oy + c + 2, Theme.ACCENT2);
    }

    private static void build(MinecraftClient mc) {
        var w = mc.world;
        int px = mc.player.getBlockX(), pz = mc.player.getBlockZ();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int dz = -R; dz < R; dz++)
            for (int dx = -R; dx < R; dx++) {
                int x = px + dx, z = pz + dz, idx = (dz + R) * R * 2 + (dx + R);
                if (!w.getChunkManager().isChunkLoaded(x >> 4, z >> 4)) { map[idx] = 0xFF000000; continue; }
                int y = w.getTopY(Heightmap.Type.WORLD_SURFACE, x, z) - 1;
                pos.set(x, y, z);
                BlockState st = w.getBlockState(pos);
                map[idx] = 0xFF000000 | st.getMapColor(w, pos).color;
            }
    }
}
