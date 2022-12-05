package dev.sleep.particlecore.client.component.lifetime;

import com.eliotlash.mclib.math.Operation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentLifetimeExpression extends ComponentLifetime {
    public MolangValue expiration = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return super.fromJson(element);
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("expiration_expression")) {
            this.expiration = MolangParser.parseJson(jsonObjectElement.get("expiration_expression"));
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = (JsonObject) super.toJson();

        if (!this.expiration.isZero()) {
            object.add("expiration_expression", this.expiration.toJson());
        }

        return object;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }


        if (!Operation.equals(this.activeTime.get(), 0)) {
            emitter.start();
        }

        if (!Operation.equals(this.expiration.get(), 0)) {
            emitter.stop();
        }
    }

    @Override
    protected String getPropertyName() {
        return "activation_expression";
    }
}
