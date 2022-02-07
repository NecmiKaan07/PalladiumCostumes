package net.threetag.palladium.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class IntegerThreeData extends ThreeData<Integer> {

    public IntegerThreeData(String key) {
        super(key);
    }

    @Override
    public Integer fromJSON(JsonElement jsonElement) {
        return jsonElement.getAsInt();
    }

    @Override
    public JsonElement toJSON(Integer value) {
        return new JsonPrimitive(value);
    }

    @Override
    public Integer fromNBT(Tag tag, Integer defaultValue) {
        if (tag instanceof IntTag intTag) {
            return intTag.getAsInt();
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Integer value) {
        return IntTag.valueOf(value);
    }
}
