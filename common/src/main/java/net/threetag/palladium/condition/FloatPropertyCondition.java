package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.concurrent.atomic.AtomicBoolean;

public class FloatPropertyCondition extends Condition {

    private final String propertyKey;
    private final float min, max;

    public FloatPropertyCondition(String propertyKey, float min, float max) {
        this.propertyKey = propertyKey;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        AtomicBoolean result = new AtomicBoolean(false);

        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property instanceof FloatProperty floatProperty) {
                float value = handler.get(floatProperty);
                result.set(value >= this.min && value <= this.max);
            }
        });

        return result.get();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.FLOAT_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the float property in the entity");
        public static final PalladiumProperty<Float> MIN = new FloatProperty("min").configurable("Minimum required amount of the property value");
        public static final PalladiumProperty<Float> MAX = new FloatProperty("max").configurable("Maximum required amount of the property value");

        public Serializer() {
            this.withProperty(PROPERTY, "value");
            this.withProperty(MIN, 0F);
            this.withProperty(MAX, 0F);
        }

        @Override
        public Condition make(JsonObject json) {
            return new FloatPropertyCondition(
                    this.getProperty(json, PROPERTY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a float property with a value between the given minimum and maximum.";
        }
    }
}
