package dev.sleep.particlelib.client.loading.object;

import com.eliotlash.mclib.math.Variable;
import com.eliotlash.mclib.utils.Interpolations;
import com.eliotlash.mclib.utils.MathUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;
import software.bernie.geckolib.core.molang.expressions.MolangVariableHolder;
import dev.sleep.particlelib.client.loading.object.type.ParticleCurveType;

public class ParticleCurve {

    public ParticleCurveType type = ParticleCurveType.LINEAR;

    public MolangValue[] nodes = {MolangParser.ZERO, MolangParser.ONE, MolangParser.ZERO};
    public MolangValue input;
    public MolangValue range;

    public Variable variable;

    public double compute() {
        return this.computeCurve(this.input.get() / this.range.get());
    }

    private double computeCurve(double factor) {
        int length = this.nodes.length;

        if (length == 0) {
            return 0;
        } else if (length == 1) {
            return this.nodes[0].get();
        }

        if (factor < 0) {
            factor = -(1 + factor);
        }

        factor = MathUtils.clamp(factor, 0, 1);

        if (this.type == ParticleCurveType.HERMITE) {
            if (length <= 3) {
                return this.nodes[length - 2].get();
            }

            factor *= (length - 3);
            int index = (int) factor + 1;

            MolangValue beforeFirst = this.getNode(index - 1);
            MolangValue first = this.getNode(index);
            MolangValue next = this.getNode(index + 1);
            MolangValue afterNext = this.getNode(index + 2);

            return Interpolations.cubicHermite(beforeFirst.get(), first.get(), next.get(), afterNext.get(), factor % 1);
        }

        factor *= length - 1;
        int index = (int) factor;

        MolangValue first = this.getNode(index);
        MolangValue next = this.getNode(index + 1);

        return Interpolations.lerp(first.get(), next.get(), factor % 1);
    }

    private MolangValue getNode(int index) {
        if (index < 0) {
            return this.nodes[0];
        } else if (index >= this.nodes.length) {
            return this.nodes[this.nodes.length - 1];
        }

        return this.nodes[index];
    }

    public void fromJson(JsonObject object) throws MolangException {
        if (object.has("type")) {
            this.type = ParticleCurveType.valueOf(object.get("type").getAsString());
        }

        if (object.has("input")) {
            this.input = MolangParser.parseJson(object.get("input"));
        }

        if (object.has("horizontal_range")) {
            this.range = MolangParser.parseJson(object.get("horizontal_range"));
        }

        if (object.has("nodes")) {
            JsonArray nodes = object.getAsJsonArray("nodes");
            MolangValue[] result = new MolangVariableHolder[nodes.size()];

            for (int i = 0, c = result.length; i < c; i++) {
                result[i] = MolangParser.parseJson(nodes.get(i));
            }

            this.nodes = result;
        }
    }

    public JsonElement toJson() {
        JsonObject curve = new JsonObject();
        JsonArray nodes = new JsonArray();

        curve.addProperty("type", this.type.curveName);
        curve.add("nodes", nodes);
        curve.add("input", this.molangToJson(input.toString()));
        curve.add("horizontal_range", this.molangToJson(range.toString()));

        for (MolangValue holder : this.nodes) {
            nodes.add(this.molangToJson(holder.toString()));
        }

        return curve;
    }

    private JsonPrimitive molangToJson(String molangValueString){
        return new JsonPrimitive(molangValueString);
    }
}
