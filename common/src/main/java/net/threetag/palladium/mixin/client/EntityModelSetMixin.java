package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityModelSet.class)
public interface EntityModelSetMixin {

    @Accessor
    Map<ModelLayerLocation, LayerDefinition> getRoots();

    @Accessor("roots")
    void setRoots(Map<ModelLayerLocation, LayerDefinition> roots);

}