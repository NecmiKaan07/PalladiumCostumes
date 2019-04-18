package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.client.EnumAbilityColor;
import com.threetag.threecore.abilities.data.*;
import com.threetag.threecore.util.render.IIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Ability implements INBTSerializable<NBTTagCompound> {

    public static final AbilityData<Boolean> ENABLED = new AbilityDataBoolean("enabled").disableSaving();
    public static final AbilityData<Integer> MAX_COOLDOWN = new AbilityDataInteger("max_cooldown").disableSaving().setSyncType(EnumSync.SELF).enableSetting("cooldown", "Maximum cooldown for using this ability");
    public static final AbilityData<Integer> COOLDOWN = new AbilityDataInteger("cooldown").setSyncType(EnumSync.SELF);
    public static final AbilityData<Boolean> SHOW_IN_BAR = new AbilityDataBoolean("show_in_bar").disableSaving().setSyncType(EnumSync.SELF).enableSetting("show_in_bar", "Determines if this ability should be displayed in the ability bar");
    public static final AbilityData<Boolean> HIDDEN = new AbilityDataBoolean("hidden").setSyncType(EnumSync.SELF);
    public static final AbilityData<ITextComponent> TITLE = new AbilityDataTextComponent("title").disableSaving().setSyncType(EnumSync.SELF).enableSetting("title", "Allows you to set a custom title for this ability");
    public static final AbilityData<IIcon> ICON = new AbilityDataIcon("icon").disableSaving().setSyncType(EnumSync.SELF).enableSetting("icon", "Lets you customize the icon for the ability");

    protected final AbilityType type;
    String id;
    public IAbilityContainer container;
    protected AbilityDataManager dataManager = new AbilityDataManager(this);
    protected int ticks = 0;
    public EnumSync sync = EnumSync.NONE;
    public boolean dirty = false;
    protected NBTTagCompound additionalData;

    public Ability(AbilityType type) {
        this.type = type;

        registerData();
    }

    public abstract EnumAbilityType getAbilityType();

    public void registerData() {
        if (this.getAbilityType() != EnumAbilityType.CONSTANT) {
            if (this.getAbilityType() == EnumAbilityType.HELD || this.getAbilityType() == EnumAbilityType.TOGGLE)
                this.dataManager.register(ENABLED, false);
            this.dataManager.register(MAX_COOLDOWN, 0);
            this.dataManager.register(COOLDOWN, 0);
        }
        this.dataManager.register(SHOW_IN_BAR, getAbilityType() != EnumAbilityType.CONSTANT);
        this.dataManager.register(HIDDEN, false);
        this.dataManager.register(TITLE, new TextComponentTranslation("ability." + this.type.getRegistryName().getNamespace() + "." + this.type.getRegistryName().getPath()));
    }

    public NBTTagCompound getAdditionalData() {
        if (this.additionalData == null)
            this.additionalData = new NBTTagCompound();
        return additionalData;
    }

    @OnlyIn(Dist.CLIENT)
    public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
        if (this.getDataManager().has(ICON))
            this.getDataManager().get(ICON).draw(mc, x, y);
    }

    public void tick(EntityLivingBase entity) {

    }

    public void onKeyPressed(EntityLivingBase entity) {
        for (Ability entityAbilities : AbilityHelper.getAbilities(entity).stream().filter(entityAbilities -> entityAbilities.getParentAbility() == this).toArray(Ability[]::new)) {
            entityAbilities.onKeyPressed(entity);
        }
    }

    public void onKeyReleased(EntityLivingBase entity) {

    }

    public boolean needsKey() {
        return true;
    }

    // TODO Ability Conditions
    public boolean isUnlocked() {
        return true;
    }

    // TODO Parent ability
    public Ability getParentAbility() {
        return null;
    }

    public AbilityDataManager getDataManager() {
        return dataManager;
    }

    public final String getId() {
        return id;
    }

    public final IAbilityContainer getContainer() {
        return container;
    }

    @OnlyIn(Dist.CLIENT)
    public EnumAbilityColor getColor() {
        return EnumAbilityColor.LIGHT_GRAY;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.putString("AbilityType", this.type.getRegistryName().toString());
        nbt.put("Data", this.dataManager.serializeNBT());
        if (this.additionalData != null)
            nbt.put("AdditionalData", this.additionalData);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.dataManager.deserializeNBT(nbt.getCompound("Data"));
        this.additionalData = nbt.getCompound("AdditionalData");
    }

    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.putString("AbilityType", this.type.getRegistryName().toString());
        nbt.put("Data", this.dataManager.getUpdatePacket());
        if (this.additionalData != null)
            nbt.put("AdditionalData", this.additionalData);
        return nbt;
    }

    public void readUpdateTag(NBTTagCompound nbt) {
        this.dataManager.readUpdatePacket(nbt.getCompound("Data"));
        this.additionalData = nbt.getCompound("AdditionalData");
    }
}
