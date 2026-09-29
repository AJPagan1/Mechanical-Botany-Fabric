package com.cmb.mechanical_botany.client;

import com.cmb.mechanical_botany.MechanicalBotany;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public final class ModPartialModels {

    public static final PartialModel MECHANICAL_PUMP_COG_ROT_90 =
            PartialModel.of(
                    new ResourceLocation(
                            MechanicalBotany.MOD_ID,
                            "block/mechanical_pump_cog_rot_90"
                    )
            );

    private ModPartialModels() {
    }

    public static void init() {
    }
}
