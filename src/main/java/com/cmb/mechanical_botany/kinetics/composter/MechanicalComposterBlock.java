package com.cmb.mechanical_botany.kinetics.composter;

import com.cmb.mechanical_botany.registry.ModBlockEntities;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

public class MechanicalComposterBlock
        extends KineticBlock
        implements IBE<MechanicalComposterBlockEntity>, ICogWheel {

    public MechanicalComposterBlock(
            Properties properties
    ) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(
            LevelReader level,
            BlockPos pos,
            BlockState state,
            Direction face
    ) {
        return face == Direction.DOWN
                || face == Direction.UP;
    }

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
                player.getItemInHand(hand);

        if (!heldStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        withBlockEntityDo(
                level,
                pos,
                composter -> {
                    boolean emptyOutput = true;

                    for (
                            int slot = 0;
                            slot < composter.outputInv.getSlotCount();
                            slot++
                    ) {
                        ItemStack stackInSlot =
                                composter.outputInv
                                        .getStackInSlot(slot);

                        if (!stackInSlot.isEmpty()) {
                            emptyOutput = false;
                        }

                        player.getInventory()
                                .placeItemBackInInventory(
                                        stackInSlot
                                );

                        composter.outputInv
                                .setStackInSlot(
                                        slot,
                                        ItemStack.EMPTY
                                );
                    }

                    if (emptyOutput) {
                        for (
                                int slot = 0;
                                slot < composter.inputInv.getSlotCount();
                                slot++
                        ) {
                            player.getInventory()
                                    .placeItemBackInInventory(
                                            composter.inputInv
                                                    .getStackInSlot(slot)
                                    );

                            composter.inputInv
                                    .setStackInSlot(
                                            slot,
                                            ItemStack.EMPTY
                                    );
                        }
                    }

                    composter.setChanged();
                    composter.sendData();
                }
        );

        return InteractionResult.SUCCESS;
    }

    @Override
    public void updateEntityAfterFallOn(
            BlockGetter world,
            Entity entity
    ) {
        super.updateEntityAfterFallOn(
                world,
                entity
        );

        if (entity.level().isClientSide) {
            return;
        }

        if (!(entity instanceof ItemEntity itemEntity)) {
            return;
        }

        if (!entity.isAlive()) {
            return;
        }

        MechanicalComposterBlockEntity composter = null;

        for (
                BlockPos checkPos :
                Iterate.hereAndBelow(
                        entity.blockPosition()
                )
        ) {
            if (composter == null) {
                BlockEntity blockEntity =
                        world.getBlockEntity(
                                checkPos
                        );

                if (
                        blockEntity
                                instanceof MechanicalComposterBlockEntity found
                ) {
                    composter = found;
                }
            }
        }

        if (composter == null) {
            return;
        }

        ItemStack original =
                itemEntity.getItem();

        ItemStack remainder =
                composter.insertInput(
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

    @Override
    public Direction.Axis getRotationAxis(
            BlockState state
    ) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<MechanicalComposterBlockEntity>
    getBlockEntityClass() {
        return MechanicalComposterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalComposterBlockEntity>
    getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_COMPOSTER;
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