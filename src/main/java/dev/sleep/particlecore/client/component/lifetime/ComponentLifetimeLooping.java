package dev.sleep.particlecore.client.component.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentLifetimeLooping extends ComponentLifetime {
    public MolangValue sleepTime = MolangParser.ZERO;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return super.fromJson(element);
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("sleep_time")) {
            this.sleepTime = MolangParser.parseJson(jsonObjectElement.get("sleep_time"));
        }

        return super.fromJson(element);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = (JsonObject) super.toJson();

        if (!this.sleepTime.isZero()) {
            object.add("sleep_time", this.sleepTime.toJson());
        }

        return object;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(!updateEmitterOnly){
            return;
        }

        double active = this.activeTime.get();
        double sleep = this.sleepTime.get();
        double age = emitter.getAge();

        emitter.lifetime = (int) (active * 20);
        if (age >= active && emitter.playing) {
            emitter.stop();
        }

        if (age >= sleep && !emitter.playing) {
            emitter.start();
        }
    }
}
