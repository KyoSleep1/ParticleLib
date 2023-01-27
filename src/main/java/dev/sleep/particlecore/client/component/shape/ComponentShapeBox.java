package dev.sleep.particlecore.client.component.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentShapeBox extends ComponentShape {

    public final MolangValue[] halfDimensions = {MolangParser.ZERO, MolangParser.ZERO, MolangParser.ZERO};

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject())
            return super.fromJson(element);

        JsonObject jssonObjectElement = element.getAsJsonObject();

        if (jssonObjectElement.has("half_dimensions")) {
            JsonArray array = jssonObjectElement.getAsJsonArray("half_dimensions");

            if (array.size() >= 3) {
                this.halfDimensions[0] = MolangParser.parseJson(array.get(0));
                this.halfDimensions[1] = MolangParser.parseJson(array.get(1));
                this.halfDimensions[2] = MolangParser.parseJson(array.get(2));
            }
        }

        return super.fromJson(element);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = (JsonObject) super.toJson();
        JsonArray array = new JsonArray();

        for (MolangValue expression : this.halfDimensions) {
            array.add(expression.toJson());
        }

        object.add("half_dimensions", array);
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

        float w = (float) this.halfDimensions[0].get();
        float h = (float) this.halfDimensions[1].get();
        float d = (float) this.halfDimensions[2].get();

        particle.position.x = centerX + ((float) Math.random() * 2 - 1F) * w;
        particle.position.y = centerY + ((float) Math.random() * 2 - 1F) * h;
        particle.position.z = centerZ + ((float) Math.random() * 2 - 1F) * d;

        if (this.surface) {
            int roll = (int) (Math.random() * 6 * 100) % 6;

            if (roll == 0) particle.position.x = centerX + w;
            else if (roll == 1) particle.position.x = centerX - w;
            else if (roll == 2) particle.position.y = centerY + h;
            else if (roll == 3) particle.position.y = centerY - h;
            else if (roll == 4) particle.position.z = centerZ + d;
            else if (roll == 5) particle.position.z = centerZ - d;
        }

        this.direction.applyDirection(particle, centerX, centerY, centerZ);
    }
}
