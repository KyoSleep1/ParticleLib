package software.bernie.geckolib.core.molang.expressions;

import com.eliotlash.mclib.math.Constant;
import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.Operation;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import software.bernie.geckolib.core.molang.MolangParser;

/**
 * Molang extension for the {@link IValue} system.
 * Used to handle values and expressions specific to Molang deserialization
 */
public class MolangValue implements IValue {
    private final IValue value;
    private final boolean returns;

    public MolangValue(IValue value) {
        this(value, false);
    }

    public MolangValue(IValue value, boolean isReturn) {
        this.value = value;
        this.returns = isReturn;
    }

    @Override
    public double get() {
        return this.value.get();
    }

    public IValue getValueHolder() {
        return this.value;
    }

    public boolean isReturnValue() {
        return this.returns;
    }

    public boolean isConstant() {
        return getClass() == MolangValue.class && value instanceof Constant;
    }

    public boolean is(int compareValue){
        return this.isConstant() && Operation.equals(value.get(), compareValue);
    }

    public boolean isZero() {
        return this.value instanceof Constant && Operation.equals(this.value.get(), 0);
    }

    public boolean isOne() {
        return this.value instanceof Constant && Operation.equals(this.value.get(), 1);
    }

    @Override
    public String toString() {
        return (this.returns ? MolangParser.RETURN : "") + this.value.toString();
    }

    public JsonElement toJson() {
        if (this.value instanceof Constant) {
            return new JsonPrimitive(this.value.get());
        }

        return new JsonPrimitive(this.toString());
    }
}
