package dev.sleep.particlecore.client.component.motion;

import com.eliotlash.mclib.math.Operation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentMotionCollision extends AbstractComponent {

    public MolangValue enabled = MolangParser.ONE;
    public float collissionDrag = 0;
    public float bounciness = 1;

    public float radius = 0.01F;
    public boolean expireOnImpact;

    private final Vector3d PREVIOUS = new Vector3d();
    private final Vector3d CURRENT = new Vector3d();
    private final BlockPos POSITION = new BlockPos(0, 0, 0);

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("enabled")) {
            this.enabled = MolangParser.parseJson(jsonObjectElement.get("enabled"));
        }
        if (jsonObjectElement.has("collision_drag")) {
            this.collissionDrag = jsonObjectElement.get("collision_drag").getAsFloat();
        }
        if (jsonObjectElement.has("coefficient_of_restitution")) {
            this.bounciness = jsonObjectElement.get("coefficient_of_restitution").getAsFloat();
        }

        if (jsonObjectElement.has("collision_radius")) {
            this.radius = jsonObjectElement.get("collision_radius").getAsFloat();
        }
        if (jsonObjectElement.has("expire_on_contact")) {
            this.expireOnImpact = jsonObjectElement.get("expire_on_contact").getAsBoolean();
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (this.enabled.isZero()) {
            return object;
        }

        if (!this.enabled.isOne()) {
            object.add("enabled", this.enabled.toJson());
        }

        if (this.collissionDrag != 0) {
            object.addProperty("collision_drag", this.collissionDrag);
        }

        if (this.bounciness != 1) {
            object.addProperty("coefficient_of_restitution", this.bounciness);
        }

        if (this.radius != 0.01F) {
            object.addProperty("collision_radius", this.radius);
        }

        if (this.expireOnImpact) {
            object.addProperty("expire_on_contact", true);
        }

        return object;
    }

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        if (emitter.world == null) {
            return;
        }

        if (!Operation.equals(this.enabled.get(), 0)) {
            BlockPos.MutableBlockPos mutableBlockPos = this.POSITION.mutable();
            float r = this.radius;

            this.PREVIOUS.set(particle.getGlobalPosition(emitter, particle.prevPosition));
            this.CURRENT.set(particle.getGlobalPosition(emitter));

            Vector3d prev = this.PREVIOUS;
            Vector3d now = this.CURRENT;

            double x = now.x - prev.x;
            double y = now.y - prev.y;
            double z = now.z - prev.z;
            boolean isCollisionBig = Math.abs(x) > 10 || Math.abs(y) > 10 || Math.abs(z) > 10;

            mutableBlockPos.set(now.x, now.y, now.z);
            if (isCollisionBig || !emitter.world.isLoaded(mutableBlockPos)) {
                return;
            }

            AABB aabb = new AABB(prev.x - r, prev.y - r, prev.z - r, prev.x + r, prev.y + r, prev.z + r);

            double d0 = y;
            double origX = x;
            double origZ = z;

            Iterable<VoxelShape> iterable = emitter.world.getCollisions(null, aabb.expandTowards(x, y, z));
            for (VoxelShape voxelShape : iterable) {
                x = voxelShape.collide(Direction.Axis.X, aabb, x);
                y = voxelShape.collide(Direction.Axis.Y, aabb, y);
                z = voxelShape.collide(Direction.Axis.Z, aabb, z);
            }

            if (d0 != y || origX != x || origZ != z) {
                if (this.expireOnImpact) {
                    particle.dead = true;
                    return;
                }

                if (particle.useRelativePosition) {
                    particle.useRelativePosition = false;
                    particle.prevPosition.set(prev);
                }

                now.set(aabb.minX + r, aabb.minY + r, aabb.minZ + r);
                if (d0 != y) {
                    particle.accelerationFactor.y *= -this.bounciness;
                    now.y += d0 < y ? r : -r;
                }

                if (origX != x) {
                    particle.accelerationFactor.x *= -this.bounciness;
                    now.x += origX < x ? r : -r;
                }

                if (origZ != z) {
                    particle.accelerationFactor.z *= -this.bounciness;
                    now.z += origZ < z ? r : -r;
                }

                particle.position.set(now);
                particle.dragFactor += this.collissionDrag;
            }
        }
    }
}
