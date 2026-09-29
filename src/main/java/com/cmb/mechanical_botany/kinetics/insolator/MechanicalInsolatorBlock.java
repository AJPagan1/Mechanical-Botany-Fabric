package com.cmb.mechanical_botany.kinetics.insolator;

import com.cmb.mechanical_botany.registry.ModBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

public class MechanicalInsolatorBlock
        extends KineticBlock
        implements IBE<MechanicalInsolatorBlockEntity>, ICogWheel, IWrenchable {

    public MechanicalInsolatorBlock(
            Properties properties
    ) {
        super(
                properties
        );
    }

    public static boolean hasPipeTowards(
            Direction direction
    ) {
        return direction
                == Direction.DOWN;
    }

    /*
     * ================================================================
     * EMPTY-HAND RETRIEVAL
     * ================================================================
     */

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        ItemStack heldStack =
                player.getItemInHand(
                        hand
                );

        if (!heldStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        withBlockEntityDo(
                level,
                pos,
                insolator -> {
                    boolean emptyOutput = true;

                    for (
                            int slot = 0;
                            slot < insolator.outputInv.getSlotCount();
                            slot++
                    ) {
                        ItemStack stack =
                                insolator.outputInv
                                        .getStackInSlot(
                                                slot
                                        );

                        if (!stack.isEmpty()) {
                            emptyOutput = false;
                        }

                        player
                                .getInventory()
                                .placeItemBackInInventory(
                                        stack
                                );

                        insolator.outputInv
                                .setStackInSlot(
                                        slot,
                                        ItemStack.EMPTY
                                );
                    }

                    if (emptyOutput) {
                        for (
                                int slot = 0;
                                slot < insolator.inputInv.getSlotCount();
                                slot++
                        ) {
                            ItemStack stack =
                                    insolator.inputInv
                                            .getStackInSlot(
                                                    slot
                                            );

                            player
                                    .getInventory()
                                    .placeItemBackInInventory(
                                            stack
                                    );

                            insolator.inputInv
                                    .setStackInSlot(
                                            slot,
                                            ItemStack.EMPTY
                                    );
                        }

                        insolator.timer = 0;
                    }

                    insolator.setChanged();
                    insolator.sendData();
                }
        );

        return InteractionResult.SUCCESS;
    }

    /*
     * ================================================================
     * DROPPED-ITEM INPUT
     * ================================================================
     */

    @Override
    public void updateEntityAfterFallOn(
            BlockGetter world,
            Entity entity
    ) {
        super.updateEntityAfterFallOn(
                world,
                entity
        );

        if (
                entity
                        .level()
                        .isClientSide
        ) {
            return;
        }

        if (
                !(entity
                        instanceof ItemEntity itemEntity)
        ) {
            return;
        }

        if (!entity.isAlive()) {
            return;
        }

        MechanicalInsolatorBlockEntity insolator =
                null;

        for (
                BlockPos checkPos :
                Iterate.hereAndBelow(
                        entity.blockPosition()
                )
        ) {
            if (insolator != null) {
                continue;
            }

            BlockEntity blockEntity =
                    world.getBlockEntity(
                            checkPos
                    );

            if (
                    blockEntity
                            instanceof MechanicalInsolatorBlockEntity found
            ) {
                insolator =
                        found;
            }
        }

        if (insolator == null) {
            return;
        }

        ItemStack original =
                itemEntity.getItem();

        ItemStack remainder =
                insolator.insertInput(
                        original
                );

        if (remainder.isEmpty()) {
            itemEntity.discard();
            return;
        }

        if (
                remainder.getCount()
                        < original.getCount()
        ) {
            itemEntity.setItem(
                    remainder
            );
        }
    }

    /*
     * ================================================================
     * KINETICS
     * ================================================================
     */

    @Override
    public Direction.Axis getRotationAxis(
            BlockState state
    ) {
        return Direction.Axis.Y;
    }

    /*
     * ================================================================
     * BLOCK ENTITY
     * ================================================================
     */

    @Override
    public Class<MechanicalInsolatorBlockEntity>
    getBlockEntityClass() {
        return MechanicalInsolatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalInsolatorBlockEntity>
    getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_INSOLATOR;
    }

    @Override
    public boolean isPathfindable(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            PathComputationType type
    ) {
        return false;
    }
}
