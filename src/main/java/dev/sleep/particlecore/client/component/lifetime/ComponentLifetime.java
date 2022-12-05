package dev.sleep.particlecore.client.component.lifetime;

import com.eliotlash.mclib.math.Constant;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public abstract class ComponentLifetime extends AbstractComponent {
    public static final MolangValue DEFAULT_ACTIVE = new MolangValue(new Constant(10), false);
    public MolangValue activeTime = DEFAULT_ACTIVE;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has(this.getPropertyName())) {
            this.activeTime = MolangParser.parseJson(jsonObjectElement.get(this.getPropertyName()));
        }

        return this;
    }


    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        if (!this.activeTime.is(10)) {
            object.add(this.getPropertyName(), this.activeTime.toJson());
        }

        return object;
    }

    protected String getPropertyName() {
        return "active_time";
    }
}
