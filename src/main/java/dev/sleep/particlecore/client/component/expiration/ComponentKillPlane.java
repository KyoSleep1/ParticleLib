package dev.sleep.particlecore.client.component.expiration;

import com.eliotlash.mclib.math.Operation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import org.joml.Vector3d;
import software.bernie.geckolib.core.molang.MolangException;

public class ComponentKillPlane extends AbstractComponent {

    public float a, b, c, d;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonArray()) {
            return this;
        }

        JsonArray array = element.getAsJsonArray();
        if (array.size() >= 4) {
            this.a = array.get(0).getAsFloat();
            this.b = array.get(1).getAsFloat();
            this.c = array.get(2).getAsFloat();
            this.d = array.get(3).getAsFloat();
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonArray array = new JsonArray();
        if (Operation.equals(this.a, 0) && Operation.equals(this.b, 0) && Operation.equals(this.c, 0) && Operation.equals(this.d, 0)) {
            return array;
        }

        array.add(this.a);
        array.add(this.b);
        array.add(this.c);
        array.add(this.d);

        return array;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        if (particle.dead) {
            return;
        }

        Vector3d prevLocal = new Vector3d(particle.prevPosition);
        Vector3d local = new Vector3d(particle.position);

        if (!particle.useRelativePosition) {
            local.sub(emitter.lastGlobal);
            prevLocal.sub(emitter.lastGlobal);
        }

        double prev = this.a * prevLocal.x + this.b * prevLocal.y + this.c * prevLocal.z + this.d;
        double now = this.a * local.x + this.b * local.y + this.c * local.z + this.d;

        if ((prev > 0 && now < 0) || (prev < 0 && now > 0)) {
            particle.dead = true;
        }
    }
}
