package dev.sleep.particlecore;

import lombok.Getter;

import java.util.UUID;

public abstract class AbstractParticleEmitter {

    @Getter
    private final UUID Uuid = UUID.randomUUID();

    public abstract void tick();

    public abstract double getDistanceSq();
}
