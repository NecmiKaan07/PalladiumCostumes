package com.threetag.threecore.abilities;

import net.minecraft.entity.EntityLivingBase;

public abstract class AbilityToggle extends Ability {

    public AbilityToggle(AbilityType type) {
        super(type);
    }

    @Override
    public EnumAbilityType getAbilityType() {
        return EnumAbilityType.TOGGLE;
    }

    @Override
    public void tick(EntityLivingBase entity) {
        if (isUnlocked()) {
            if (this.dataManager.get(ENABLED)) {
                if (ticks == 0)
                    firstTick(entity);
                ticks++;
                updateTick(entity);

                if (this.dataManager.has(MAX_COOLDOWN) && this.dataManager.get(MAX_COOLDOWN) > 0) {
                    if (this.dataManager.get(COOLDOWN) >= this.dataManager.get(MAX_COOLDOWN))
                        this.dataManager.set(ENABLED, false);
                    else
                        this.dataManager.set(COOLDOWN, this.getDataManager().get(COOLDOWN) + 1);
                }
            } else {
                if (ticks != 0) {
                    lastTick(entity);
                    ticks = 0;
                }

                if (this.dataManager.has(MAX_COOLDOWN) && this.dataManager.get(MAX_COOLDOWN) > 0) {
                    if (this.dataManager.get(COOLDOWN) > 0)
                        this.dataManager.set(COOLDOWN, this.getDataManager().get(COOLDOWN) - 1);
                }
            }
        } else if (ticks != 0) {
            lastTick(entity);
            ticks = 0;
        }
    }

    @Override
    public void onKeyPressed(EntityLivingBase entity) {
        this.dataManager.set(ENABLED, !this.dataManager.get(ENABLED));
        super.onKeyPressed(entity);
    }

    public void updateTick(EntityLivingBase entity) {

    }

    public void firstTick(EntityLivingBase entity) {

    }

    public void lastTick(EntityLivingBase entity) {

    }

}
