package dev.sleep.particlecore.client.component.rate;

import com.eliotlash.mclib.math.Constant;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentRateSteady extends ComponentRate {

    public static final MolangValue DEFAULT_PARTICLES = new MolangValue(new Constant(50), false);
    public MolangValue spawnRate = MolangParser.ONE;

    public ComponentRateSteady() {
        this.particles = DEFAULT_PARTICLES;
    }

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();

        if (jsonObjectElement.has("spawn_rate")) {
            this.spawnRate = MolangParser.parseJson(jsonObjectElement.get("spawn_rate"));
        }
        if (jsonObjectElement.has("max_particles")) {
            this.particles = MolangParser.parseJson(jsonObjectElement.get("max_particles"));
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (!this.spawnRate.isOne()) {
            object.add("spawn_rate", this.spawnRate.toJson());
        }

        if (!this.particles.is(50)) {
            object.add("max_particles", this.particles.toJson());
        }

        return object;
    }

    @Override
    public void postRender(AbstractParticleEmitter emitter, CachedParticleScheme scheme, float partialTicks) {
        if (emitter.playing) {
            double particles = emitter.getAge(partialTicks) * this.spawnRate.get();
            double diff = particles - emitter.spawnedParticles;
            double spawn = Math.ceil(diff);

            if (spawn > 0) {
                for (int i = 0; i < spawn; i++) {
                    if (emitter.PARTICLES_LIST.size() < this.particles.get()) {
                        emitter.spawnParticle(scheme);
                    }
                }

                emitter.spawnedParticles += spawn;
            }
        }
    }
}
