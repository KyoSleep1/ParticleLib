package dev.sleep.particlelib.client;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlecore.client.component.appearance.ComponentAppearanceBillboard;
import dev.sleep.particlecore.client.component.appearance.ComponentAppearanceLighting;
import dev.sleep.particlecore.client.component.appearance.ComponentAppearanceTinting;
import dev.sleep.particlecore.client.component.expiration.ComponentExpireInBlocks;
import dev.sleep.particlecore.client.component.expiration.ComponentExpireNotInBlocks;
import dev.sleep.particlecore.client.component.expiration.ComponentKillPlane;
import dev.sleep.particlecore.client.component.expiration.ComponentParticleLifetime;
import dev.sleep.particlecore.client.component.lifetime.ComponentLifetimeExpression;
import dev.sleep.particlecore.client.component.lifetime.ComponentLifetimeLooping;
import dev.sleep.particlecore.client.component.lifetime.ComponentLifetimeOnce;
import dev.sleep.particlecore.client.component.meta.ComponentInitialization;
import dev.sleep.particlecore.client.component.meta.ComponentLocalSpace;
import dev.sleep.particlecore.client.component.motion.*;
import dev.sleep.particlecore.client.component.rate.ComponentRateInstant;
import dev.sleep.particlecore.client.component.rate.ComponentRateSteady;
import dev.sleep.particlecore.client.component.shape.*;
import lombok.Getter;

public class ParticleComponentRegistry {

    @Getter
    private final static BiMap<String, Class<? extends AbstractComponent>> ComponentsList = HashBiMap.create();

     public static void registerAll() {
         ComponentsList.put("minecraft:emitter_local_space", ComponentLocalSpace.class);
         ComponentsList.put("minecraft:emitter_initialization", ComponentInitialization.class);

         ComponentsList.put("minecraft:emitter_rate_instant", ComponentRateInstant.class);
         ComponentsList.put("minecraft:emitter_rate_steady", ComponentRateSteady.class);

         ComponentsList.put("minecraft:emitter_lifetime_looping", ComponentLifetimeLooping.class);
         ComponentsList.put("minecraft:emitter_lifetime_once", ComponentLifetimeOnce.class);
         ComponentsList.put("minecraft:emitter_lifetime_expression", ComponentLifetimeExpression.class);

         ComponentsList.put("minecraft:emitter_shape_disc", ComponentShapeDisc.class);
         ComponentsList.put("minecraft:emitter_shape_box", ComponentShapeBox.class);
         ComponentsList.put("minecraft:emitter_shape_entity_aabb", ComponentShapeEntityAABB.class);
         ComponentsList.put("minecraft:emitter_shape_point", ComponentShapePoint.class);
         ComponentsList.put("minecraft:emitter_shape_sphere", ComponentShapeSphere.class);

         ComponentsList.put("minecraft:particle_lifetime_expression", ComponentParticleLifetime.class);
         ComponentsList.put("minecraft:particle_expire_if_in_blocks", ComponentExpireInBlocks.class);
         ComponentsList.put("minecraft:particle_expire_if_not_in_blocks", ComponentExpireNotInBlocks.class);
         ComponentsList.put("minecraft:particle_kill_plane", ComponentKillPlane.class);

         ComponentsList.put("minecraft:particle_appearance_billboard", ComponentAppearanceBillboard.class);
         //ComponentsList.put("minecraft:particle_appearance_lighting", ComponentAppearanceLighting.class);
         ComponentsList.put("minecraft:particle_appearance_tinting", ComponentAppearanceTinting.class);

         ComponentsList.put("minecraft:particle_initial_speed", ComponentInitialSpeed.class);
         ComponentsList.put("minecraft:particle_initial_spin", ComponentInitialSpin.class);
         ComponentsList.put("minecraft:particle_motion_collision", ComponentMotionCollision.class);
         ComponentsList.put("minecraft:particle_motion_dynamic", ComponentMotionDynamic.class);
         ComponentsList.put("minecraft:particle_motion_parametric", ComponentMotionParametric.class);
    }
}
