package com.cmb.mechanical_botany.kinetics.composter;

import com.simibubi.create.foundation.item.SmartInventory;

public class MechanicalComposterInventory
        extends SmartInventory {

    public MechanicalComposterInventory(
            int slots,
            MechanicalComposterBlockEntity blockEntity
    ) {
        super(
                slots,
                blockEntity,
                64,
                true
        );
    }
}