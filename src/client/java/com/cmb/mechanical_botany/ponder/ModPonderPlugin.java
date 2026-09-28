package com.cmb.mechanical_botany.ponder;

import com.cmb.mechanical_botany.MechanicalBotany;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonderPlugin
        implements PonderPlugin {

    @Override
    public void registerScenes(
            PonderSceneRegistrationHelper<ResourceLocation> helper
    ) {
        ModPonderScenes.register(
                helper
        );
    }

    @Override
    public void registerTags(
            PonderTagRegistrationHelper<ResourceLocation> helper
    ) {
        ModPonderTags.register(
                helper
        );
    }

    @Override
    public String getModId() {
        return MechanicalBotany.MOD_ID;
    }
}