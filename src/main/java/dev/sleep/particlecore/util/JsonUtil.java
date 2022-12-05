package dev.sleep.particlecore.util;

import com.eliotlash.mclib.math.Operation;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class JsonUtil {

    public static boolean isElementEmpty(JsonElement element) {
        if (element.isJsonArray()) {
            return element.getAsJsonArray().size() == 0;
        } else if (element.isJsonObject()) {
            return element.getAsJsonObject().size() == 0;
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if (primitive.isString()) {
                return primitive.getAsString().isEmpty();
            } else if (primitive.isNumber()) {
                return Operation.equals(primitive.getAsDouble(), 0);
            }
        }

        return element.isJsonNull();
    }
}
