package dev.sleep.particlecore.client.component.appearance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlecore.util.JsonUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;

public abstract class ComponentAppearanceTinting extends AbstractComponent {
    public ColorTint color = new ColorTint.Solid(MolangParser.ONE, MolangParser.ONE, MolangParser.ONE, MolangParser.ONE);

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("color")) {
            JsonElement color = jsonObjectElement.get("color");

            if (color.isJsonArray() || color.isJsonPrimitive()) {
                this.color = ColorTint.parseColor(color);
            } else if (color.isJsonObject()) {
                this.color = ColorTint.parseGradient(color.getAsJsonObject());
            }
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonElement element = this.color.toJson();

        if (!JsonUtil.isElementEmpty(element)) {
            object.add("color", element);
        }

        return object;
    }

    @Override
    public void render(AbstractParticleEmitter particleEmitter, EnhancedParticle particle, PoseStack poseStack, BufferBuilder tesselator, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        if (this.color != null) {
            this.color.compute(particle);
        } else {
            particle.color.x = particle.color.y = particle.color.z = particle.color.w = 1;
        }
    }
}
