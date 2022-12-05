package dev.sleep.particlecore.client.component.motion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentInitialSpeed extends AbstractComponent {

    public MolangValue speed = MolangParser.ONE;
    public MolangValue[] direction;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();

            if (array.size() >= 3) {
                this.direction = new MolangValue[]{MolangParser.parseJson(array.get(0)), MolangParser.parseJson(array.get(1)), MolangParser.parseJson(array.get(2))};
            }
        } else if (element.isJsonPrimitive()) {
            this.speed = MolangParser.parseJson(element);
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        if (this.direction != null) {
            JsonArray array = new JsonArray();

            for (MolangValue expression : this.direction) {
                array.add(expression.toJson());
            }

            return array;
        }

        return this.speed.toJson();
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        if (this.direction == null) {
            float speed = (float) this.speed.get();
            particle.speed.mul(speed);
            return;
        }

        particle.speed.set((float) this.direction[0].get(), (float) this.direction[1].get(), (float) this.direction[1].get());
    }

    @Override
    public boolean canBeEmpty() {
        return true;
    }
}
