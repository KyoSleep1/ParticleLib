package dev.sleep.particlecore.client.component.rate;

import com.eliotlash.mclib.math.Constant;
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

public class ComponentRateInstant extends ComponentRate {
    public static final MolangValue DEFAULT_PARTICLES = new MolangValue(new Constant(10), false);

    public ComponentRateInstant() {
        this.particles = DEFAULT_PARTICLES;
    }

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("num_particles")) {
            this.particles = MolangParser.parseJson(jsonObjectElement.get("num_particles"));
        }

        return this;
    }


    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (!this.particles.isConstant()) {
            object.add("num_particles", this.particles.toJson());
        }

        return object;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }

        double age = emitter.getAge();
        if (emitter.playing && Operation.equals(age, 0)) {
            for (int i = 0, c = (int) this.particles.get(); i < c; i++) {
                emitter.spawnParticle(scheme);
            }
        }
    }
}
