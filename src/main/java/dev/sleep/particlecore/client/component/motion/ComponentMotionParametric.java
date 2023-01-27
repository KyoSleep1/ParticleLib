package dev.sleep.particlecore.client.component.motion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import org.joml.Vector3f;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentMotionParametric extends AbstractComponent {

    public final MolangValue[] position = {MolangParser.ZERO, MolangParser.ZERO, MolangParser.ZERO};
    public MolangValue rotation = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("relative_position") && jsonObjectElement.get("relative_position").isJsonArray()) {
            JsonArray array = jsonObjectElement.get("relative_position").getAsJsonArray();

            this.position[0] = MolangParser.parseJson(array.get(0));
            this.position[1] = MolangParser.parseJson(array.get(1));
            this.position[2] = MolangParser.parseJson(array.get(2));
        }

        if (jsonObjectElement.has("rotation")) {
            this.rotation = MolangParser.parseJson(jsonObjectElement.get("rotation"));
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray position = new JsonArray();

        for (MolangValue expression : this.position) {
            position.add(expression.toJson());
        }

        object.add("relative_position", position);

        if (!this.rotation.isZero()) {
            object.add("rotation", this.rotation.toJson());
        }

        return object;
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if (updateEmitterOnly) {
            return;
        }

        Vector3f position = new Vector3f((float) this.position[0].get(), (float) this.position[1].get(), (float) this.position[2].get());

        particle.manual = true;
        particle.initialPosition.set(particle.position);

        particle.matrix.transform(position);
        particle.position.x = particle.initialPosition.x + position.x;
        particle.position.y = particle.initialPosition.y + position.y;
        particle.position.z = particle.initialPosition.z + position.z;
        particle.rotation = (float) this.rotation.get();
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if (updateEmitterOnly) {
            return;
        }

        Vector3f position = new Vector3f((float) this.position[0].get(), (float) this.position[1].get(), (float) this.position[2].get());
        particle.matrix.transform(position);
        particle.position.x = particle.initialPosition.x + position.x;
        particle.position.y = particle.initialPosition.y + position.y;
        particle.position.z = particle.initialPosition.z + position.z;
        particle.rotation = (float) this.rotation.get();
    }
}
