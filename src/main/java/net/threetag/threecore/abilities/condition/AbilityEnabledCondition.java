package net.threetag.threecore.abilities.condition;

import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.StringThreeData;
import net.threetag.threecore.util.threedata.ThreeData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AbilityEnabledCondition extends Condition {

    public static final ThreeData<String> ABILITY_ID = new StringThreeData("ability_id").setSyncType(EnumSync.SELF).enableSetting("ability_id", "The id for the ability that must be enabled for the condition to be true.");

    public AbilityEnabledCondition(Ability ability) {
        super(ConditionType.ABILITY_ENABLED, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ABILITY_ID, "");
    }

    @Override
    public boolean test(LivingEntity entity) {
        Ability dependentAbility = AbilityHelper.getAbilityById(entity, this.dataManager.get(ABILITY_ID), ability.container);
        // Not the best way to handle the name, but it works ¯\_(ツ)_/¯
        this.dataManager.set(TITLE, dependentAbility == null ? new StringTextComponent("") : new TranslationTextComponent("ability.condition.threecore.ability_enabled", dependentAbility.getDataManager().get(Ability.TITLE)));
        return dependentAbility != null && dependentAbility != ability && dependentAbility.getConditionManager().isEnabled();

    }
}