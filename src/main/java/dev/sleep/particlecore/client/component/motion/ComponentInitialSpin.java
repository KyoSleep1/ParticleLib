package dev.sleep.particlecore.client.component.motion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentInitialSpin extends AbstractComponent {

    public MolangValue rotation = MolangParser.ZERO;
    public MolangValue rate = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("rotation")) {
            this.rotation = MolangParser.parseJson(jsonObjectElement.get("rotation"));
        }

        if (jsonObjectElement.has("rotation_rate")) {
            this.rate = MolangParser.parseJson(jsonObjectElement.get("rotation_rate"));
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (!this.rotation.isZero()) {
            object.add("rotation", this.rotation.toJson());
        }

        if (!this.rate.isZero()) {
            object.add("rotation_rate", this.rate.toJson());
        }

        return object;
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        particle.initialRotation = (float) this.rotation.get();
        particle.rotationVelocity = (float) this.rate.get() / 20;
    }
}
