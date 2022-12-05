package dev.sleep.particlecore.client.component.motion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentMotionDynamic extends AbstractComponent {

    public MolangValue[] motionAcceleration = {MolangParser.ZERO, MolangParser.ZERO, MolangParser.ZERO};
    public MolangValue motionDrag = MolangParser.ZERO;
    public MolangValue rotationAcceleration = MolangParser.ZERO;
    public MolangValue rotationDrag = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("linear_acceleration")) {
            JsonArray array = jsonObjectElement.getAsJsonArray("linear_acceleration");
            if (array.size() >= 3) {
                this.motionAcceleration[0] = MolangParser.parseJson(array.get(0));
                this.motionAcceleration[1] = MolangParser.parseJson(array.get(1));
                this.motionAcceleration[2] = MolangParser.parseJson(array.get(2));
            }
        }

        if (jsonObjectElement.has("linear_drag_coefficient")) {
            this.motionDrag = MolangParser.parseJson(jsonObjectElement.get("linear_drag_coefficient"));
        }

        if (jsonObjectElement.has("rotation_acceleration")) {
            this.rotationAcceleration = MolangParser.parseJson(jsonObjectElement.get("rotation_acceleration"));
        }

        if (jsonObjectElement.has("rotation_drag_coefficient")) {
            this.rotationDrag = MolangParser.parseJson(jsonObjectElement.get("rotation_drag_coefficient"));
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray acceleration = new JsonArray();

        for (MolangValue expression : this.motionAcceleration) {
            acceleration.add(expression.toJson());
        }

        object.add("linear_acceleration", acceleration);
        if (!this.motionDrag.isZero()) {
            object.add("linear_drag_coefficient", this.motionDrag.toJson());
        }

        if (!this.rotationAcceleration.isZero()) {
            object.add("rotation_acceleration", this.rotationAcceleration.toJson());
        }

        if (!this.rotationDrag.isZero()) {
            object.add("rotation_drag_coefficient", this.rotationDrag.toJson());
        }

        return object;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }

        particle.acceleration.x += (float) this.motionAcceleration[0].get();
        particle.acceleration.y += (float) this.motionAcceleration[1].get();
        particle.acceleration.z += (float) this.motionAcceleration[2].get();

        particle.drag = (float) this.motionDrag.get();
        particle.rotationAcceleration += (float) this.rotationAcceleration.get() / 20F;
        particle.dragRotation = (float) this.rotationDrag.get();
    }
}
