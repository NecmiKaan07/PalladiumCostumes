package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class BrightnessAtPositionCondition extends Condition {

    private final int min, max;

    public BrightnessAtPositionCondition(int minHealth, int maxHealth) {
        this.min = minHealth;
        this.max = maxHealth;
    }

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        var brightness = entity.level.getMaxLocalRawBrightness(entity.blockPosition());
        return brightness >= this.min && brightness <= this.max;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.BRIGHTNESS_AT_POSITION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min_brightness").configurable("Minimum required brightness at entity's position");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max_brightness").configurable("Maximum required brightness at entity's position");

        public Serializer() {
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 16);
        }

        @Override
        public Condition make(JsonObject json) {
            return new BrightnessAtPositionCondition(getProperty(json, MIN), getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity's brightness at it's position is within the given range.";
        }
    }
}
