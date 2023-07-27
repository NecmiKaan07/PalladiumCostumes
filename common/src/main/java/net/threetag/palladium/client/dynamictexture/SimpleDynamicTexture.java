package net.threetag.palladium.client.dynamictexture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;

public class SimpleDynamicTexture extends DynamicTexture {

    private final ResourceLocation texture;

    public SimpleDynamicTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTexture(Entity entity) {
        return this.texture;
    }

    @Override
    public DynamicTexture transform(ITextureTransformer textureTransformer) {
        throw new IllegalStateException("Cant transform simple textures");
    }

    @Override
    public DynamicTexture addVariable(String name, ITextureVariable variable) {
        throw new IllegalStateException("Cant add variables to simple textures");
    }
}
