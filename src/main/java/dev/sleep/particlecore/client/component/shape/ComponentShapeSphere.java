package dev.sleep.particlecore.client.component.shape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import org.joml.Vector3f;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentShapeSphere extends ComponentShape {
    public MolangValue radius = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return super.fromJson(element);
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("radius")) {
            this.radius = MolangParser.parseJson(jsonObjectElement.get("radius"));
        }

        return super.fromJson(element);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = (JsonObject) super.toJson();

        if (!this.radius.isZero()) {
            object.add("radius", this.radius.toJson());
        }

        return object;
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        float centerX = (float) this.offset[0].get();
        float centerY = (float) this.offset[1].get();
        float centerZ = (float) this.offset[2].get();
        float radius = (float) this.radius.get();

        Vector3f direction = new Vector3f((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
        direction.normalize();

        if (!this.surface) {
            radius *= Math.random();
        }

        direction.mul(radius);

        particle.position.x = centerX + direction.x;
        particle.position.y = centerY + direction.y;
        particle.position.z = centerZ + direction.z;

        this.direction.applyDirection(particle, centerX, centerY, centerZ);
    }
}
