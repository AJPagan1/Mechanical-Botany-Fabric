package com.cmb.mechanical_botany.kinetics.insolator;

import com.simibubi.create.foundation.item.SmartInventory;

public class MechanicalInsolatorInventory
        extends SmartInventory {

    public MechanicalInsolatorInventory(
            int slots,
            MechanicalInsolatorBlockEntity blockEntity
    ) {
        super(
                slots,
                blockEntity,
                64,
                true
        );
    }
}