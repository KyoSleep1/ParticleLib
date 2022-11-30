package dev.sleep.particlelib.core.impl.client.loading.raw.object.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlelib.core.geckolib.molang.MolangException;

public abstract class AbstractComponent {

    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        return this;
    }

    public JsonElement toJson() {
        return new JsonObject();
    }

    public boolean canBeEmpty() {
        return false;
    }
}
