package uz.uzumx.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uz.uzumx.ModuleManager;
import uz.uzumx.UzumX;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void uzumx$zoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (ModuleManager.get("Zoom").enabled && UzumX.zoomKey.isPressed()) {
            cir.setReturnValue(cir.getReturnValueD() / 4.0);
        }
    }
}
