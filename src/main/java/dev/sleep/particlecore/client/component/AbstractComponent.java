package dev.sleep.particlecore.client.component;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import software.bernie.geckolib.core.molang.MolangException;

public abstract class AbstractComponent {

    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
    }

    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
    }

    public void preRender(AbstractParticleEmitter emitter, float partialTicks) {
    }

    public void render(AbstractParticleEmitter particleEmitter, EnhancedParticle particle, PoseStack poseStack, BufferBuilder tesselator, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
    }

    public void postRender(AbstractParticleEmitter emitter, CachedParticleScheme scheme, float partialTicks) {
    }

    public abstract AbstractComponent fromJson(JsonElement element) throws MolangException;

    public abstract JsonElement toJson();

    public boolean canBeEmpty() {
        return false;
    }
}
