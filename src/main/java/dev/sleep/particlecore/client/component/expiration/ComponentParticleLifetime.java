package dev.sleep.particlecore.client.component.expiration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentParticleLifetime extends AbstractComponent {

    public MolangValue expression = MolangParser.ZERO;
    public boolean max;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        JsonElement expression;

        if (jsonObjectElement.has("expiration_expression")) {
            expression = jsonObjectElement.get("expiration_expression");
            this.max = false;
        } else if (jsonObjectElement.has("max_lifetime")) {
            expression = jsonObjectElement.get("max_lifetime");
            this.max = true;
        } else {
            throw new JsonParseException("No expiration_expression or max_lifetime was found in minecraft:particle_lifetime_expression component");
        }

        this.expression = MolangParser.parseJson(expression);
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.add(this.max ? "max_lifetime" : "expiration_expression", this.expression.toJson());
        return object;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        if (!this.max && this.expression.get() != 0) {
            particle.dead = true;
        }
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        if (this.max) {
            particle.lifetime = (int) (this.expression.get() * 20);
        } else {
            particle.lifetime = -1;
        }
    }
}
