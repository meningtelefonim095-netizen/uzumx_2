package uz.uzumx;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import java.util.LinkedHashMap;
import java.util.Map;

public class Waypoints {
    public static final Map<String, BlockPos> points = new LinkedHashMap<>();

    public static void register(CommandDispatcher<FabricClientCommandSource> d) {
        d.register(ClientCommandManager.literal("wp")
            .then(ClientCommandManager.literal("add").then(ClientCommandManager.argument("name", StringArgumentType.word())
                .executes(c -> {
                    var p = MinecraftClient.getInstance().player;
                    if (p == null) return 0;
                    String n = StringArgumentType.getString(c, "name");
                    points.put(n, p.getBlockPos());
                    c.getSource().sendFeedback(Text.literal("§d[UzumX] §fwaypoint §b" + n + " §fsaqlandi"));
                    return 1;
                })))
            .then(ClientCommandManager.literal("del").then(ClientCommandManager.argument("name", StringArgumentType.word())
                .executes(c -> {
                    points.remove(StringArgumentType.getString(c, "name"));
                    c.getSource().sendFeedback(Text.literal("§d[UzumX] §fo'chirildi"));
                    return 1;
                })))
            .then(ClientCommandManager.literal("list").executes(c -> {
                points.forEach((n, b) -> c.getSource().sendFeedback(
                    Text.literal("§b" + n + " §7" + b.getX() + " " + b.getY() + " " + b.getZ())));
                return 1;
            })));
    }

    public static String nearest(BlockPos from) {
        String best = null; double bd = Double.MAX_VALUE;
        for (var e : points.entrySet()) {
            double dist = Math.sqrt(e.getValue().getSquaredDistance(from));
            if (dist < bd) { bd = dist; best = e.getKey() + " " + (int) dist + "m"; }
        }
        return best;
    }
}
