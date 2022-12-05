package dev.sleep.particlecore.client.component.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public abstract class ComponentShape extends AbstractComponent {

    public MolangValue[] offset = {MolangParser.ZERO, MolangParser.ZERO, MolangParser.ZERO};
    public ShapeDirection direction = ShapeDirection.OUTWARDS;
    public boolean surface = false;

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonElementObject = element.getAsJsonObject();
        if (jsonElementObject.has("offset")) {
            JsonArray array = jsonElementObject.getAsJsonArray("offset");

            if (array.size() >= 3) {
                this.offset[0] = MolangParser.parseJson(array.get(0));
                this.offset[1] = MolangParser.parseJson(array.get(1));
                this.offset[2] = MolangParser.parseJson(array.get(2));
            }
        }

        if (jsonElementObject.has("direction")) {
            JsonElement direction = jsonElementObject.get("direction");

            if (direction.isJsonPrimitive()) {
                String name = direction.getAsString();

                if (name.equals("inwards")) this.direction = ShapeDirection.INWARDS;
                else this.direction = ShapeDirection.OUTWARDS;
            } else if (direction.isJsonArray()) {
                JsonArray array = direction.getAsJsonArray();

                if (array.size() >= 3) {
                    this.direction = new ShapeDirection.Vector(
                            MolangParser.parseJson(array.get(0)),
                            MolangParser.parseJson(array.get(1)),
                            MolangParser.parseJson(array.get(2))
                    );
                }
            }
        }

        if (jsonElementObject.has("surface_only")) {
            this.surface = jsonElementObject.get("surface_only").getAsBoolean();
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray offset = new JsonArray();

        for (MolangValue expression : this.offset) {
            offset.add(expression.toJson());
        }

        object.add("offset", offset);

        if (this.direction != ShapeDirection.OUTWARDS) {
            object.add("direction", this.direction.toJson());
        }

        if (this.surface) {
            object.addProperty("surface_only", true);
        }

        return object;
    }
}
