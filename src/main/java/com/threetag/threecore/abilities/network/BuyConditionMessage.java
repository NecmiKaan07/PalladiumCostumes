package com.threetag.threecore.abilities.network;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.IAbilityContainer;
import com.threetag.threecore.abilities.condition.BuyableAbilityCondition;
import com.threetag.threecore.abilities.condition.Condition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BuyConditionMessage {

    public ResourceLocation containerId;
    public String abilityId;
    public UUID conditionId;

    public BuyConditionMessage(ResourceLocation containerId, String abilityId, UUID conditionId) {
        this.containerId = containerId;
        this.abilityId = abilityId;
        this.conditionId = conditionId;
    }

    public BuyConditionMessage(PacketBuffer buffer) {
        this.containerId = new ResourceLocation(buffer.readString(64));
        this.abilityId = buffer.readString(32);
        this.conditionId = buffer.readUniqueId();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
        buffer.writeUniqueId(this.conditionId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(player, this.containerId);

                if (container != null) {
                    Ability ability = container.getAbilityMap().get(this.abilityId);

                    if (ability != null) {
                        Condition condition = ability.getConditionManager().getByUniqueId(this.conditionId);

                        if (condition instanceof BuyableAbilityCondition) {
                            if (((BuyableAbilityCondition) condition).isAvailable(player)) {
                                ((BuyableAbilityCondition) condition).buy(player);
                            }
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
