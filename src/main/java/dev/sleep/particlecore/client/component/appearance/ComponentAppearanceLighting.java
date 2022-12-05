package dev.sleep.particlecore.client.component.appearance;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;

public class ComponentAppearanceLighting extends AbstractComponent {

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }
    }

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        return this;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.toString());
    }

    @Override
    public boolean canBeEmpty() {
        return true;
    }
}
