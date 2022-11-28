package dev.sleep.particlelib.core.impl.mixin.server;

import dev.sleep.particlelib.core.impl.server.ServerParticleManager;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class MixinServerLevel {

    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "TAIL"))
    public void injectParticleTick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        ServerParticleManager.INSTANCE.tickParticles(false);
    }

}
