package dev.sleep.particlelib.core.impl.client.loading.raw;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import dev.sleep.particlelib.core.impl.client.loading.raw.object.ParticleCurve;
import dev.sleep.particlelib.core.impl.client.loading.raw.object.component.*;
import dev.sleep.particlelib.core.impl.client.loading.raw.object.type.ParticleMaterialType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RawParticleScheme {

    public String identifier = "";
    public ParticleMaterialType material = null;

    public HashMap<String, ParticleCurve> curvesList = new HashMap<>();
    public List<AbstractComponent> componentsList = new ArrayList<>();

    public List<IComponentEmitterInitialize> emitterInitializes;
    public List<IComponentEmitterUpdate> emitterUpdates;

    public List<IComponentParticleInitialize> particleInitializes;
    public List<IComponentParticleUpdate> particleUpdates;

    public List<IComponentParticleRenderBase> particleRender;

    public static JsonDeserializer<RawParticleScheme> deserializer() throws JsonParseException {
        return (json, type, context) -> new RawParticleScheme();
    }
}
