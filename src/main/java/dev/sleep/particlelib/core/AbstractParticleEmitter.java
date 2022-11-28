package dev.sleep.particlelib.core;

import lombok.Getter;

import java.util.UUID;

public abstract class AbstractParticleEmitter {

    @Getter
    private final UUID Uuid = UUID.randomUUID();

    @Getter
    private final boolean clientSide;

    public AbstractParticleEmitter(boolean clientSide){
        this.clientSide = clientSide;
    }

    public abstract void tick();

    public abstract double getDistanceSq();
}
