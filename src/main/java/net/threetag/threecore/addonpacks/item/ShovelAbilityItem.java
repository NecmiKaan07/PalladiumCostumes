package net.threetag.threecore.addonpacks.item;

import com.google.common.collect.Lists;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.threetag.threecore.abilities.AbilityGenerator;
import net.threetag.threecore.abilities.AbilityMap;
import net.threetag.threecore.abilities.IAbilityProvider;
import net.threetag.threecore.abilities.capability.ItemAbilityContainerProvider;

import javax.annotation.Nullable;
import java.util.List;

public class ShovelAbilityItem extends ShovelItem implements IAbilityProvider {

    private List<AbilityGenerator> abilityGenerators;

    public ShovelAbilityItem(IItemTier itemTier, int attackDamage, float attackSpeed, Properties properties) {
        super(itemTier, attackDamage, attackSpeed, properties);
    }

    public ShovelAbilityItem setAbilities(List<AbilityGenerator> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public ShovelAbilityItem addAbility(AbilityGenerator abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (this.abilityGenerators != null && !this.abilityGenerators.isEmpty())
            return new ItemAbilityContainerProvider(stack);
        else
            return super.initCapabilities(stack, nbt);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }

}
