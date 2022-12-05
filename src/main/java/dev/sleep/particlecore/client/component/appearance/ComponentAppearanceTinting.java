package dev.sleep.particlecore.client.component.appearance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlecore.util.JsonUtil;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;

public abstract class ComponentAppearanceTinting extends AbstractComponent {
    public ColorTint color = new ColorTint.Solid(MolangParser.ONE, MolangParser.ONE, MolangParser.ONE, MolangParser.ONE);

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("color")) {
            JsonElement color = jsonObjectElement.get("color");

            if (color.isJsonArray() || color.isJsonPrimitive()) {
                this.color = ColorTint.parseColor(color);
            } else if (color.isJsonObject()) {
                this.color = ColorTint.parseGradient(color.getAsJsonObject());
            }
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonElement element = this.color.toJson();

        if (!JsonUtil.isElementEmpty(element)) {
            object.add("color", element);
        }

        return object;
    }
}
