package org.useless.dragonfly.model.newmodels.generic;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.model.newmodels.generic.components.ModelComponent;

import java.util.HashMap;
import java.util.Map;

public final class StaticModel {
    private final Map<String, DisplayPosition> displayPositionMap = new HashMap<>();
    private final Map<String, String> textureSymbolMap = new HashMap<>();
    private final Map<Side, Boolean> particleColorMap = new HashMap<>();
    private final boolean useAo;
    private final boolean useOverbright;
    @Nullable
    private final ModelComponent[] components;
    @Nullable
    public final StaticModel parentModel;
    public StaticModel(@Nullable StaticModel parentModel, @Nullable Boolean useAmbientOcclusion, @Nullable ModelComponent ... components){
        this.parentModel = parentModel;

        if (parentModel != null){
            displayPositionMap.putAll(parentModel.displayPositionMap);
            textureSymbolMap.putAll(parentModel.textureSymbolMap);
            particleColorMap.putAll(parentModel.particleColorMap);
        }

        if (useAmbientOcclusion != null){
            this.useAo = useAmbientOcclusion;
        } else {
            if (parentModel != null){
                this.useAo = parentModel.useAO();
            } else {
                this.useAo = true;
            }
        }

        if (components.length == 0){
            this.components = null;
        } else {
            this.components = components;
        }

        boolean _overbright = false;
        ModelComponent[] _comps = getComponents();
        if (_comps != null){
            for (ModelComponent c : _comps){
                if (c.doOverbright()) {
                    _overbright = true;
                    break;
                }
            }
        }
        useOverbright = _overbright;
    }

    public StaticModel setDisplayPosition(String id, @NotNull DisplayPosition position){
        displayPositionMap.put(id, position);
        return this;
    }
    @NotNull
    public DisplayPosition getDisplayPosition(String id){
        return displayPositionMap.getOrDefault(id, DisplayPosition.defaultPos);
    }

    public StaticModel setTexture(String varName, String value){
        assert !value.isEmpty();
        textureSymbolMap.put(varName, value);
        try {
            if (!value.startsWith("#")){
                if (!value.contains(":")) value = "minecraft:" + value;
                TextureRegistry.getTexture(value);
            }
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return this;
    }
    public StaticModel setParticleColor(Side side, boolean useColor){
        particleColorMap.put(side, useColor);
        return this;
    }

    @Nullable
    public ModelComponent[] getComponents(){
        if (this.components != null) return components;
        if (parentModel != null) return parentModel.getComponents();
        return null;
    }

    @Nullable
    public IconCoordinate getTexture(@NotNull String textureSymbol, boolean isNullable){
        if (textureSymbol.isEmpty()) {
            if (isNullable) return null;
            return TextureRegistry.getTexture("builtin:block/missing");
        }
        String texVar = textureSymbol;

        // Texture variable
        if (textureSymbol.startsWith("#")){
            assert !textureSymbol.substring(1).isEmpty();
            texVar = textureSymbolMap.get(textureSymbol.substring(1));

            // Variable doesn't have an assigned texture id
            if (texVar == null){
                if (isNullable) return null;
                return TextureRegistry.getTexture("builtin:block/unassigned");
            }

            // Variable links to another variable
            if (texVar.startsWith("#")){
                return getTexture(texVar, isNullable);
            }
        }

        // Texture ID
        if (!texVar.contains(":")) texVar = "minecraft:" + texVar;

        NamespaceID texID;
        try {
            texID = new NamespaceID(texVar);
        } catch (IllegalArgumentException e){
            if (isNullable) return null;
            return TextureRegistry.getTexture("builtin:block/missing");
        }

        IconCoordinate icon;
        try {
            if (TextureRegistry.hasTexture(texID)){
                icon = TextureRegistry.getTexture(texID);
                if (icon != null) return icon;
            }
            if (isNullable) return null;
            return TextureRegistry.getTexture("builtin:block/missing");
        } catch (IllegalArgumentException e){
            if (isNullable) return null;
            return TextureRegistry.getTexture("builtin:block/missing");
        }
    }

    @Nullable
    public IconCoordinate getParticle(Side side){
        return getTexture("#particle_" + String.valueOf(side).toLowerCase(), true);
    }
    @Nullable
    public IconCoordinate getOverlay(){
        return getTexture("#overlay", false);
    }
    public boolean useAO(){
        return useAo;
    }
    public boolean hasOverbright() {
        return useOverbright;
    }
    @Nullable
    public Boolean useParticleColor(Side side){
        return particleColorMap.get(side);
    }
}
