package com.threetag.threecore.abilities.condition;

import com.google.gson.JsonObject;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.client.gui.AbilityScreen;
import com.threetag.threecore.abilities.client.gui.BuyAbilityScreen;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.abilities.data.ExperienceThreeData;
import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.util.render.ExperienceIcon;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class XPBuyableAbilityCondition extends BuyableAbilityCondition {

    public static ThreeData<ExperienceThreeData.Experience> EXPERIENCE = new ExperienceThreeData("experience").setSyncType(EnumSync.SELF).enableSetting("experience", "Determines the amount of XP the player has to spend to activate this condition.");

    public XPBuyableAbilityCondition(Ability ability) {
        super(ConditionType.XP_BUY, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(EXPERIENCE, new ExperienceThreeData.Experience(false, 5));
    }

    @Override
    public void readFromJson(JsonObject json) {
        super.readFromJson(json);
        this.dataManager.set(TITLE, new TranslationTextComponent(this.dataManager.get(EXPERIENCE).isLevels() ? "ability.condition.threecore.xp_buy.levels" : "ability.condition.threecore.xp_buy.points", this.dataManager.get(EXPERIENCE).getValue()));
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        return entity instanceof PlayerEntity && this.dataManager.get(EXPERIENCE).has((PlayerEntity) entity);
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        boolean available = isAvailable(entity);
        if (entity instanceof PlayerEntity)
            this.dataManager.get(EXPERIENCE).take((PlayerEntity) entity);
        return available;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Screen getScreen(AbilityScreen screen) {
        return this.dataManager.get(BOUGHT) ? null : new BuyAbilityScreen(this.ability, this,
                new ExperienceIcon(this.dataManager.get(EXPERIENCE)),
                new TranslationTextComponent("ability.condition.threecore.xp_buy.info." + (this.dataManager.get(EXPERIENCE).isLevels() ? "levels" : "points"),
                        this.dataManager.get(EXPERIENCE).getValue()), screen);
    }
}
