package dev.sleep.particlecore.client.component.meta;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.Variable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

import java.util.Map;

public class ComponentInitialization extends AbstractComponent {

    public MolangValue creation = MolangParser.ZERO;
    public MolangValue update = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("creation_expression")) {
            this.creation = MolangParser.parseJson(jsonObjectElement.get("creation_expression"));
        }

        if (jsonObjectElement.has("per_update_expression")) {
            this.update = MolangParser.parseJson(jsonObjectElement.get("per_update_expression"));
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (!this.creation.isZero()) {
            object.add("creation_expression", this.creation.toJson());
        }

        if (!this.update.isZero()) {
            object.add("per_update_expression", this.update.toJson());
        }

        return object;
    }

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }

        this.creation.get();
        this.replaceVariables(emitter);
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }

        this.update.get();
        this.replaceVariables(emitter);
    }

    public void replaceVariables(AbstractParticleEmitter emitter) {
        for (Map.Entry<String, IValue> entry : emitter.DATA_LIST.entrySet()) {
            Variable data = MolangParser.VARIABLES.get(entry.getKey());
            if(data == null) {
                continue;
            }

            data.set(entry.getValue().get());
        }
    }
}
