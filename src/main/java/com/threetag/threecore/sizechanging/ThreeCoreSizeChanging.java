package com.threetag.threecore.sizechanging;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import com.threetag.threecore.sizechanging.capability.ISizeChanging;
import com.threetag.threecore.sizechanging.command.SizeChangeCommand;
import com.threetag.threecore.sizechanging.network.SyncSizeMessage;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

public class ThreeCoreSizeChanging {

    public ThreeCoreSizeChanging() {
        MinecraftForge.EVENT_BUS.register(new SizeChangingEventHandler());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    }

    public void serverStarting(FMLServerStartingEvent e) {
        SizeChangeCommand.register(e.getCommandDispatcher());
    }

    public void setup(FMLCommonSetupEvent e) {
        // Network
        ThreeCore.registerMessage(SyncSizeMessage.class, SyncSizeMessage::toBytes, SyncSizeMessage::new, SyncSizeMessage::handle);

        // Capability
        CapabilityManager.INSTANCE.register(ISizeChanging.class, new Capability.IStorage<ISizeChanging>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<ISizeChanging> capability, ISizeChanging instance, Direction side) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");

                    }

                    @Override
                    public void readNBT(Capability<ISizeChanging> capability, ISizeChanging instance, Direction side, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                () -> new CapabilitySizeChanging());
    }
}
