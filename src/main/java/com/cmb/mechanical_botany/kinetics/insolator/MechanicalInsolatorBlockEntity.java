package com.cmb.mechanical_botany.kinetics.insolator;

import com.cmb.mechanical_botany.ModConfigs;
import com.cmb.mechanical_botany.recipe.InsolatingRecipe;
import com.cmb.mechanical_botany.registry.ModBlockEntities;
import com.cmb.mechanical_botany.registry.ModRecipeTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class MechanicalInsolatorBlockEntity
        extends KineticBlockEntity
        implements SidedStorageBlockEntity {

    public final MechanicalInsolatorInventory inputInv;
    public final MechanicalInsolatorInventory outputInv;

    private final Storage<ItemVariant> itemStorage;

    public SmartFluidTankBehaviour tank;

    public int timer;

    private InsolatingRecipe lastRecipe;

    public MechanicalInsolatorBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                ModBlockEntities.MECHANICAL_INSOLATOR,
                pos,
                state
        );

        inputInv =
                new MechanicalInsolatorInventory(
                        1,
                        this
                );

        outputInv =
                new MechanicalInsolatorInventory(
                        9,
                        this
                );

        Storage<ItemVariant> filteredInput =
                new FilteringStorage<ItemVariant>(
                        inputInv
                ) {

                    @Override
                    protected boolean canInsert(
                            ItemVariant resource
                    ) {
                        return MechanicalInsolatorBlockEntity.this
                                .canProcess(
                                        resource.toStack()
                                );
                    }

                    @Override
                    protected boolean canExtract(
                            ItemVariant resource
                    ) {
                        return false;
                    }
                };

        Storage<ItemVariant> extractionOnlyOutput =
                FilteringStorage.extractOnlyOf(
                        outputInv
                );

        itemStorage =
                new CombinedStorage<>(
                        List.of(
                                filteredInput,
                                extractionOnlyOutput
                        )
                );
    }

    @Override
    public Storage<ItemVariant> getItemStorage(
            Direction side
    ) {
        return itemStorage;
    }

    @Override
    public Storage<FluidVariant> getFluidStorage(
            Direction side
    ) {
        if (tank == null) {
            return null;
        }

        if (
                side == null
                        || MechanicalInsolatorBlock.hasPipeTowards(
                        side
                )
        ) {
            return tank.getCapability();
        }

        return null;
    }

    @Override
    public void addBehaviours(
            List<BlockEntityBehaviour> behaviours
    ) {
        behaviours.add(
                new DirectBeltInputBehaviour(
                        this
                )
        );

        /*
         * The original 1.0.5 config stores the tank size in mB.
         *
         * Fabric Transfer API:
         *
         * 1 mB = 81 units
         *
         * Default:
         *
         * 1000 mB * 81 = 81000
         *
         * Maximum:
         *
         * 16000 mB * 81 = 1296000
         */
        long configuredTankCapacity =
                (long) ModConfigs
                        .server()
                        .insolator
                        .tankSize
                        .get()
                        * 81L;

        tank =
                new SmartFluidTankBehaviour(
                        SmartFluidTankBehaviour.INPUT,
                        this,
                        1,
                        configuredTankCapacity,
                        true
                )
                        .allowExtraction()
                        .allowInsertion();

        behaviours.add(
                tank
        );

        super.addBehaviours(
                behaviours
        );
    }

    public FluidStack getCurrentFluidInTank() {
        if (tank == null) {
            return FluidStack.EMPTY;
        }

        return tank
                .getPrimaryHandler()
                .getFluid();
    }

    @Override
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0) {
            return;
        }

        if (
                inputInv
                        .getStackInSlot(0)
                        .isEmpty()
        ) {
            return;
        }

        if (
                tank == null
                        || tank.isEmpty()
        ) {
            return;
        }

        float pitch =
                Mth.clamp(
                        (Math.abs(getSpeed()) / 256F)
                                + 0.45F,
                        0.85F,
                        1.0F
                );

        SoundScapes.play(
                SoundScapes.AmbienceGroup.COG,
                worldPosition,
                pitch
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null) {
            return;
        }

        if (getSpeed() == 0) {
            return;
        }

        for (
                int slot = 0;
                slot < outputInv.getSlotCount();
                slot++
        ) {
            if (
                    outputInv
                            .getStackInSlot(slot)
                            .getCount()
                            == outputInv.getSlotLimit(slot)
            ) {
                return;
            }
        }

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (
                    timer <= 0
                            && !level.isClientSide
            ) {
                process();
            }

            return;
        }

        if (
                inputInv
                        .getStackInSlot(0)
                        .isEmpty()
        ) {
            return;
        }

        if (
                tank == null
                        || tank.isEmpty()
        ) {
            return;
        }

        if (
                lastRecipe == null
                        || !lastRecipe.matches(
                        inputInv,
                        level
                )
        ) {
            Optional<InsolatingRecipe> recipe =
                    findRecipe();

            if (recipe.isEmpty()) {
                timer = 100;

                sendData();

                return;
            }

            lastRecipe =
                    recipe.get();

            timer =
                    lastRecipe
                            .getProcessingDuration();

            sendData();

            return;
        }

        timer =
                lastRecipe
                        .getProcessingDuration();

        sendData();
    }

    private Optional<InsolatingRecipe> findRecipe() {
        if (level == null) {
            return Optional.empty();
        }

        return level
                .getRecipeManager()
                .getRecipeFor(
                        ModRecipeTypes.INSOLATING_TYPE,
                        inputInv,
                        level
                );
    }

    private void process() {
        if (
                level == null
                        || level.isClientSide
        ) {
            return;
        }

        if (
                lastRecipe == null
                        || !lastRecipe.matches(
                        inputInv,
                        level
                )
        ) {
            Optional<InsolatingRecipe> recipe =
                    findRecipe();

            if (recipe.isEmpty()) {
                return;
            }

            lastRecipe =
                    recipe.get();
        }

        ItemStack inputStack =
                inputInv.getStackInSlot(0);

        if (inputStack.isEmpty()) {
            return;
        }

        FluidStack currentFluid =
                getCurrentFluidInTank();

        if (currentFluid.isEmpty()) {
            return;
        }

        if (
                lastRecipe
                        .getIngredients()
                        .isEmpty()
        ) {
            return;
        }

        if (
                !lastRecipe
                        .getIngredients()
                        .get(0)
                        .test(
                                inputStack
                        )
        ) {
            return;
        }

        if (
                !lastRecipe
                        .getRequiredFluid()
                        .test(
                                currentFluid
                        )
        ) {
            return;
        }

        long requiredAmount =
                lastRecipe
                        .getRequiredFluid()
                        .getRequiredAmount();

        if (
                currentFluid.getAmount()
                        < requiredAmount
        ) {
            return;
        }

        try (
                Transaction transaction =
                        Transaction.openOuter()
        ) {
            long extracted =
                    tank
                            .getCapability()
                            .extract(
                                    currentFluid.getType(),
                                    requiredAmount,
                                    transaction
                            );

            if (
                    extracted
                            != requiredAmount
            ) {
                return;
            }

            transaction.commit();
        }

        inputStack.shrink(
                1
        );

        inputInv.setStackInSlot(
                0,
                inputStack
        );

        lastRecipe
                .rollResults()
                .forEach(
                        result ->
                                insertOutput(
                                        result.copy()
                                )
                );

        sendData();
        setChanged();
    }

    private void insertOutput(
            ItemStack stack
    ) {
        if (stack.isEmpty()) {
            return;
        }

        ItemStack remaining =
                stack.copy();

        for (
                int slot = 0;
                slot < outputInv.getSlotCount();
                slot++
        ) {
            ItemStack existing =
                    outputInv.getStackInSlot(
                            slot
                    );

            if (existing.isEmpty()) {
                continue;
            }

            if (
                    !ItemStack.isSameItemSameTags(
                            existing,
                            remaining
                    )
            ) {
                continue;
            }

            int maximum =
                    Math.min(
                            outputInv.getSlotLimit(
                                    slot
                            ),
                            existing.getMaxStackSize()
                    );

            int space =
                    maximum
                            - existing.getCount();

            if (space <= 0) {
                continue;
            }

            int amount =
                    Math.min(
                            space,
                            remaining.getCount()
                    );

            existing.grow(
                    amount
            );

            outputInv.setStackInSlot(
                    slot,
                    existing
            );

            remaining.shrink(
                    amount
            );

            if (remaining.isEmpty()) {
                return;
            }
        }

        for (
                int slot = 0;
                slot < outputInv.getSlotCount();
                slot++
        ) {
            if (
                    !outputInv
                            .getStackInSlot(slot)
                            .isEmpty()
            ) {
                continue;
            }

            int maximum =
                    Math.min(
                            outputInv.getSlotLimit(
                                    slot
                            ),
                            remaining.getMaxStackSize()
                    );

            int amount =
                    Math.min(
                            maximum,
                            remaining.getCount()
                    );

            ItemStack inserted =
                    remaining.copy();

            inserted.setCount(
                    amount
            );

            outputInv.setStackInSlot(
                    slot,
                    inserted
            );

            remaining.shrink(
                    amount
            );

            if (remaining.isEmpty()) {
                return;
            }
        }
    }

    public ItemStack insertInput(
            ItemStack stack
    ) {
        if (
                stack.isEmpty()
                        || level == null
                        || !canProcess(
                        stack
                )
        ) {
            return stack;
        }

        ItemStack current =
                inputInv.getStackInSlot(0);

        if (current.isEmpty()) {
            int amount =
                    Math.min(
                            stack.getCount(),
                            inputInv.getSlotLimit(0)
                    );

            ItemStack inserted =
                    stack.copy();

            inserted.setCount(
                    amount
            );

            inputInv.setStackInSlot(
                    0,
                    inserted
            );

            ItemStack remainder =
                    stack.copy();

            remainder.shrink(
                    amount
            );

            lastRecipe = null;

            sendData();
            setChanged();

            return remainder.isEmpty()
                    ? ItemStack.EMPTY
                    : remainder;
        }

        if (
                !ItemStack.isSameItemSameTags(
                        current,
                        stack
                )
        ) {
            return stack;
        }

        int maximum =
                Math.min(
                        inputInv.getSlotLimit(0),
                        current.getMaxStackSize()
                );

        int available =
                maximum
                        - current.getCount();

        if (available <= 0) {
            return stack;
        }

        int amount =
                Math.min(
                        available,
                        stack.getCount()
                );

        current.grow(
                amount
        );

        inputInv.setStackInSlot(
                0,
                current
        );

        ItemStack remainder =
                stack.copy();

        remainder.shrink(
                amount
        );

        lastRecipe = null;

        sendData();
        setChanged();

        return remainder.isEmpty()
                ? ItemStack.EMPTY
                : remainder;
    }

    public boolean canProcess(
            ItemStack stack
    ) {
        if (
                stack.isEmpty()
                        || level == null
        ) {
            return false;
        }

        SimpleContainer tester =
                new SimpleContainer(
                        1
                );

        tester.setItem(
                0,
                stack.copy()
        );

        return level
                .getRecipeManager()
                .getRecipeFor(
                        ModRecipeTypes.INSOLATING_TYPE,
                        tester,
                        level
                )
                .isPresent();
    }

    @Override
    public void destroy() {
        super.destroy();

        if (level == null) {
            return;
        }

        ItemHelper.dropContents(
                level,
                worldPosition,
                inputInv
        );

        ItemHelper.dropContents(
                level,
                worldPosition,
                outputInv
        );
    }

    @Override
    protected void write(
            CompoundTag compound,
            boolean clientPacket
    ) {
        compound.putInt(
                "Timer",
                timer
        );

        compound.put(
                "InputInventory",
                inputInv.serializeNBT()
        );

        compound.put(
                "OutputInventory",
                outputInv.serializeNBT()
        );

        super.write(
                compound,
                clientPacket
        );
    }

    @Override
    protected void read(
            CompoundTag compound,
            boolean clientPacket
    ) {
        timer =
                compound.getInt(
                        "Timer"
                );

        inputInv.deserializeNBT(
                compound.getCompound(
                        "InputInventory"
                )
        );

        outputInv.deserializeNBT(
                compound.getCompound(
                        "OutputInventory"
                )
        );

        lastRecipe = null;

        super.read(
                compound,
                clientPacket
        );
    }

    public int getProcessingSpeed() {
        return Mth.clamp(
                (int) Math.abs(
                        getSpeed() / 16F
                ),
                1,
                512
        );
    }
}