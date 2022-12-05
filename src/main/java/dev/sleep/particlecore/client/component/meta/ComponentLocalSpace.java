package dev.sleep.particlecore.client.component.meta;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;

public class ComponentLocalSpace extends AbstractComponent {
    public boolean position;
    public boolean rotation;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("position")) {
            this.position = jsonObjectElement.get("position").getAsBoolean();
        }

        if (jsonObjectElement.has("rotation")) {
            this.rotation = jsonObjectElement.get("rotation").getAsBoolean();
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (this.position) {
            object.addProperty("position", true);
        }

        if (this.rotation) {
            object.addProperty("rotation", true);
        }

        return object;
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }

        particle.useRelativePosition = this.position;
        particle.useRelativeRotation = this.rotation;
    }
}
