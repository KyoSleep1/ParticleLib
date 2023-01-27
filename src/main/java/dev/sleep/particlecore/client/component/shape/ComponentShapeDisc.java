package dev.sleep.particlecore.client.component.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentShapeDisc extends ComponentShapeSphere {
    public final MolangValue[] normal = {MolangParser.ZERO, MolangParser.ONE, MolangParser.ZERO};

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return super.fromJson(element);
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("plane_normal")) {
            JsonArray array = jsonObjectElement.getAsJsonArray("plane_normal");

            if (array.size() >= 3) {
                this.normal[0] = MolangParser.parseJson(array.get(0));
                this.normal[1] = MolangParser.parseJson(array.get(1));
                this.normal[2] = MolangParser.parseJson(array.get(2));
            }
        }

        return super.fromJson(element);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = (JsonObject) super.toJson();
        JsonArray array = new JsonArray();

        for (MolangValue expression : this.normal) {
            array.add(expression.toJson());
        }

        object.add("plane_normal", array);
        return object;
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if (updateEmitterOnly) {
            return;
        }

        float centerX = (float) this.offset[0].get();
        float centerY = (float) this.offset[1].get();
        float centerZ = (float) this.offset[2].get();

        Vector3f normal = new Vector3f((float) this.normal[0].get(), (float) this.normal[1].get(), (float) this.normal[2].get());
        normal.normalize();

        Quaternionf quaternion = new Quaternionf(normal.x, normal.y, normal.z, 1);
        Matrix4f rotation = new Matrix4f();
        rotation.set(quaternion);

        Vector4f position = new Vector4f((float) Math.random() - 0.5F, 0, (float) Math.random() - 0.5F, 0);
        position.normalize();
        rotation.transform(position);

        position.mul((float) (this.radius.get() * (this.surface ? 1 : Math.random())));
        position.add(new Vector4f(centerX, centerY, centerZ, 0));

        particle.position.x += position.x;
        particle.position.y += position.y;
        particle.position.z += position.z;

        this.direction.applyDirection(particle, centerX, centerY, centerZ);
    }
}
