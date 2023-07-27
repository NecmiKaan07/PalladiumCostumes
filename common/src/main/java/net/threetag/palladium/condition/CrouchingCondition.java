package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;

public class CrouchingCondition extends Condition {

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);
        return entity != null && entity.isCrouching();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.CROUCHING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new CrouchingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is crouching.";
        }
    }

}
