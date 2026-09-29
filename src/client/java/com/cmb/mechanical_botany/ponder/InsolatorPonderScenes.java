package com.cmb.mechanical_botany.kinetics.insolator;

import com.cmb.mechanical_botany.registry.ModFluids;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

public final class InsolatorPonderScenes {

    private static final long PONDER_FLUID_AMOUNT =
            FluidConstants.BUCKET * 16L;

    private InsolatorPonderScenes() {
    }

    /*
     * ================================================================
     * PONDER FLUID HELPER
     * ================================================================
     */

    private static void fillPonderTank(
            CreateSceneBuilder scene,
            BlockPos tankPos,
            Fluid fluid
    ) {
        scene.world()
                .modifyBlockEntity(
                        tankPos,
                        FluidTankBlockEntity.class,
                        tank ->
                                tank.getTankInventory()
                                        .setFluid(
                                                new FluidStack(
                                                        fluid,
                                                        PONDER_FLUID_AMOUNT
                                                )
                                        )
                );
    }

    /*
     * ================================================================
     * INTRO
     * ================================================================
     */

    public static class Intro
            implements PonderStoryBoard {

        @Override
        public void program(
                SceneBuilder builder,
                SceneBuildingUtil util
        ) {
            CreateSceneBuilder scene =
                    new CreateSceneBuilder(
                            builder
                    );

            scene.title(
                    "insolator",
                    "Growing Plants in the Mechanical Insolator"
            );

            scene.configureBasePlate(
                    0,
                    0,
                    5
            );

            Selection belt =
                    util.select()
                            .fromTo(
                                    1,
                                    1,
                                    5,
                                    0,
                                    1,
                                    2
                            )
                            .add(
                                    util.select()
                                            .position(
                                                    1,
                                                    2,
                                                    2
                                            )
                            );

            Selection beltCog =
                    util.select()
                            .position(
                                    2,
                                    0,
                                    5
                            );

            scene.world()
                    .showSection(
                            util.select()
                                    .layer(0)
                                    .substract(
                                            beltCog
                                    ),
                            Direction.UP
                    );

            BlockPos insolator =
                    util.grid()
                            .at(
                                    2,
                                    2,
                                    2
                            );

            Selection insolatorSelect =
                    util.select()
                            .position(
                                    insolator
                            );

            BlockPos pump =
                    util.grid()
                            .at(
                                    2,
                                    1,
                                    2
                            );

            Selection pumpSelect =
                    util.select()
                            .position(
                                    pump
                            );

            Selection cogs =
                    util.select()
                            .fromTo(
                                    3,
                                    1,
                                    2,
                                    3,
                                    2,
                                    2
                            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            0
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            0
                    );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            4,
                                            1,
                                            3
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pumpSelect,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .showSection(
                            insolatorSelect,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            Vec3 insolatorTop =
                    util.vector()
                            .topOf(
                                    insolator
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Mechanical Insolators will reprocess certain plants provided to them"
                    )
                    .pointAt(
                            insolatorTop
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            scene.world()
                    .showSection(
                            cogs,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            32
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            32
                    );

            scene.effects()
                    .indicateSuccess(
                            insolator
                    );

            scene.idle(
                    10
            );

            scene.overlay()
                    .showText(
                            60
                    )
                    .attachKeyFrame()
                    .colored(
                            PonderPalette.GREEN
                    )
                    .text(
                            "They can be powered from the side using cogwheels"
                    )
                    .pointAt(
                            util.vector()
                                    .topOf(
                                            insolator.east()
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    70
            );

            scene.overlay()
                    .showText(
                            100
                    )
                    .attachKeyFrame()
                    .text(
                            "mechanical_botany.ponder.insolator.text_3"
                    )
                    .pointAt(
                            util.vector()
                                    .topOf(
                                            pump
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    110
            );

            scene.overlay()
                    .showText(
                            100
                    )
                    .attachKeyFrame()
                    .text(
                            "This fluid is dependent on the plant, but typically will be Water"
                    )
                    .pointAt(
                            util.vector()
                                    .topOf(
                                            pump
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    110
            );

            ItemStack input =
                    new ItemStack(
                            Items.ALLIUM
                    );

            Vec3 entitySpawn =
                    util.vector()
                            .topOf(
                                    insolator.above(
                                            3
                                    )
                            );

            ElementLink<EntityElement> entity =
                    scene.world()
                            .createItemEntity(
                                    entitySpawn,
                                    util.vector()
                                            .of(
                                                    0,
                                                    0.2,
                                                    0
                                            ),
                                    input
                            );

            scene.idle(
                    18
            );

            scene.world()
                    .modifyEntity(
                            entity,
                            Entity::discard
                    );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    input
                                            )
                    );

            scene.idle(
                    10
            );

            scene.overlay()
                    .showControls(
                            insolatorTop,
                            Pointing.DOWN,
                            25
                    )
                    .withItem(
                            input
                    );

            scene.idle(
                    7
            );

            scene.overlay()
                    .showText(
                            40
                    )
                    .attachKeyFrame()
                    .text(
                            "Throw or Insert plants at the top"
                    )
                    .pointAt(
                            insolatorTop
                    )
                    .placeNearTarget();

            scene.idle(
                    60
            );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    ItemStack.EMPTY
                                            )
                    );

            scene.overlay()
                    .showText(
                            70
                    )
                    .text(
                            "After some time, the grown results can be retrieved via Right-click"
                    )
                    .pointAt(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.WEST
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    80
            );

            ItemStack output =
                    Items.ALLIUM
                            .getDefaultInstance();

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.NORTH
                                    ),
                            Pointing.RIGHT,
                            40
                    )
                    .rightClick()
                    .withItem(
                            output
                    );

            scene.idle(
                    50
            );

            scene.addKeyframe();

            scene.world()
                    .showSection(
                            beltCog,
                            Direction.UP
                    );

            scene.world()
                    .showSection(
                            belt,
                            Direction.EAST
                    );

            scene.idle(
                    15
            );

            BlockPos beltPos =
                    util.grid()
                            .at(
                                    1,
                                    1,
                                    2
                            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            output
                    );

            scene.idle(
                    20
            );

            scene.overlay()
                    .showText(
                            50
                    )
                    .text(
                            "The outputs can also be extracted by automation through any side"
                    )
                    .pointAt(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.WEST
                                    )
                                    .add(
                                            -0.5,
                                            0.4,
                                            0
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    60
            );
        }
    }

    /*
     * ================================================================
     * MOLTEN COMPOST
     * ================================================================
     */

    public static class MoltenCompostUsage
            implements PonderStoryBoard {

        @Override
        public void program(
                SceneBuilder builder,
                SceneBuildingUtil util
        ) {
            CreateSceneBuilder scene =
                    new CreateSceneBuilder(
                            builder
                    );

            scene.title(
                    "molten_compost_use",
                    "Using Molten Compost Within a Mechanical Insolator"
            );

            scene.configureBasePlate(
                    0,
                    0,
                    5
            );

            Selection belt =
                    util.select()
                            .fromTo(
                                    1,
                                    2,
                                    5,
                                    0,
                                    1,
                                    2
                            )
                            .add(
                                    util.select()
                                            .position(
                                                    1,
                                                    3,
                                                    2
                                            )
                            );

            Selection beltCog =
                    util.select()
                            .position(
                                    2,
                                    0,
                                    5
                            );

            scene.world()
                    .showSection(
                            util.select()
                                    .layer(0)
                                    .substract(
                                            beltCog
                                    ),
                            Direction.UP
                    );

            BlockPos insolator =
                    util.grid()
                            .at(
                                    2,
                                    3,
                                    2
                            );

            Selection insolatorSelect =
                    util.select()
                            .position(
                                    insolator
                            );

            BlockPos pump =
                    util.grid()
                            .at(
                                    2,
                                    2,
                                    2
                            );

            Selection pumpSelect =
                    util.select()
                            .position(
                                    pump
                            );

            BlockPos fluidTank =
                    util.grid()
                            .at(
                                    3,
                                    1,
                                    2
                            );

            Selection cogs =
                    util.select()
                            .fromTo(
                                    2,
                                    3,
                                    3,
                                    2,
                                    1,
                                    3
                            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            0
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            0
                    );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .fromTo(
                                            5,
                                            1,
                                            3,
                                            3,
                                            1,
                                            3
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            util.select()
                                    .fromTo(
                                            3,
                                            1,
                                            2,
                                            3,
                                            4,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    5
            );

            fillPonderTank(
                    scene,
                    fluidTank,
                    ModFluids.MOLTEN_COMPOST
                            .get()
                            .getSource()
            );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            2,
                                            1,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pumpSelect,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            insolatorSelect,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            Vec3 insolatorTop =
                    util.vector()
                            .topOf(
                                    insolator
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Wither Roses can be produced with the help of Molten Liquid Compost"
                    )
                    .pointAt(
                            insolatorTop
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            scene.world()
                    .showSection(
                            cogs,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            32
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            32
                    );

            scene.effects()
                    .indicateSuccess(
                            insolator
                    );

            scene.idle(
                    10
            );

            ItemStack input =
                    new ItemStack(
                            Items.WITHER_ROSE
                    );

            Vec3 entitySpawn =
                    util.vector()
                            .topOf(
                                    insolator.above(
                                            3
                                    )
                            );

            ElementLink<EntityElement> entity =
                    scene.world()
                            .createItemEntity(
                                    entitySpawn,
                                    util.vector()
                                            .of(
                                                    0,
                                                    0.2,
                                                    0
                                            ),
                                    input
                            );

            scene.idle(
                    18
            );

            scene.world()
                    .modifyEntity(
                            entity,
                            Entity::discard
                    );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    input
                                            )
                    );

            scene.idle(
                    40
            );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    ItemStack.EMPTY
                                            )
                    );

            ItemStack output =
                    Items.WITHER_ROSE
                            .getDefaultInstance();

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.NORTH
                                    ),
                            Pointing.RIGHT,
                            40
                    )
                    .rightClick()
                    .withItem(
                            output
                    );

            scene.idle(
                    50
            );

            scene.addKeyframe();

            scene.world()
                    .showSection(
                            beltCog,
                            Direction.UP
                    );

            scene.world()
                    .showSection(
                            belt,
                            Direction.EAST
                    );

            scene.idle(
                    15
            );

            BlockPos beltPos =
                    util.grid()
                            .at(
                                    1,
                                    2,
                                    2
                            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            output
                    );

            scene.idle(
                    15
            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            new ItemStack(
                                    Items.WITHER_ROSE
                            )
                    );

            scene.idle(
                    20
            );
        }
    }

    /*
     * ================================================================
     * VOID COMPOST
     * ================================================================
     */

    public static class VoidCompostUsage
            implements PonderStoryBoard {

        @Override
        public void program(
                SceneBuilder builder,
                SceneBuildingUtil util
        ) {
            CreateSceneBuilder scene =
                    new CreateSceneBuilder(
                            builder
                    );

            scene.title(
                    "void_compost_use",
                    "Using Liquid Void Compost Within a Mechanical Insolator"
            );

            scene.configureBasePlate(
                    0,
                    0,
                    5
            );

            Selection belt =
                    util.select()
                            .fromTo(
                                    1,
                                    2,
                                    5,
                                    0,
                                    1,
                                    2
                            )
                            .add(
                                    util.select()
                                            .position(
                                                    1,
                                                    3,
                                                    2
                                            )
                            );

            Selection beltCog =
                    util.select()
                            .position(
                                    2,
                                    0,
                                    5
                            );

            scene.world()
                    .showSection(
                            util.select()
                                    .layer(0)
                                    .substract(
                                            beltCog
                                    ),
                            Direction.UP
                    );

            BlockPos insolator =
                    util.grid()
                            .at(
                                    2,
                                    3,
                                    2
                            );

            Selection insolatorSelect =
                    util.select()
                            .position(
                                    insolator
                            );

            BlockPos pump =
                    util.grid()
                            .at(
                                    2,
                                    2,
                                    2
                            );

            Selection pumpSelect =
                    util.select()
                            .position(
                                    pump
                            );

            BlockPos fluidTank =
                    util.grid()
                            .at(
                                    3,
                                    1,
                                    2
                            );

            Selection cogs =
                    util.select()
                            .fromTo(
                                    2,
                                    3,
                                    3,
                                    2,
                                    1,
                                    3
                            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            0
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            0
                    );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .fromTo(
                                            5,
                                            1,
                                            3,
                                            3,
                                            1,
                                            3
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            util.select()
                                    .fromTo(
                                            3,
                                            1,
                                            2,
                                            3,
                                            4,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    5
            );

            fillPonderTank(
                    scene,
                    fluidTank,
                    ModFluids.VOID_COMPOST
                            .get()
                            .getSource()
            );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            2,
                                            1,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pumpSelect,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            insolatorSelect,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            Vec3 insolatorTop =
                    util.vector()
                            .topOf(
                                    insolator
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Chorus Fruit can be produced with the help of Void Liquid Compost"
                    )
                    .pointAt(
                            insolatorTop
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            scene.world()
                    .showSection(
                            cogs,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            32
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            32
                    );

            scene.effects()
                    .indicateSuccess(
                            insolator
                    );

            scene.idle(
                    10
            );

            ItemStack input =
                    new ItemStack(
                            Items.CHORUS_FLOWER
                    );

            Vec3 entitySpawn =
                    util.vector()
                            .topOf(
                                    insolator.above(
                                            3
                                    )
                            );

            ElementLink<EntityElement> entity =
                    scene.world()
                            .createItemEntity(
                                    entitySpawn,
                                    util.vector()
                                            .of(
                                                    0,
                                                    0.2,
                                                    0
                                            ),
                                    input
                            );

            scene.idle(
                    18
            );

            scene.world()
                    .modifyEntity(
                            entity,
                            Entity::discard
                    );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    input
                                            )
                    );

            scene.idle(
                    40
            );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    ItemStack.EMPTY
                                            )
                    );

            ItemStack output =
                    Items.CHORUS_FLOWER
                            .getDefaultInstance();

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.NORTH
                                    ),
                            Pointing.RIGHT,
                            40
                    )
                    .rightClick()
                    .withItem(
                            output
                    );

            scene.idle(
                    50
            );

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.NORTH
                                    ),
                            Pointing.RIGHT,
                            40
                    )
                    .rightClick()
                    .withItem(
                            input
                    );

            scene.idle(
                    50
            );

            scene.addKeyframe();

            scene.world()
                    .showSection(
                            beltCog,
                            Direction.UP
                    );

            scene.world()
                    .showSection(
                            belt,
                            Direction.EAST
                    );

            scene.idle(
                    15
            );

            BlockPos beltPos =
                    util.grid()
                            .at(
                                    1,
                                    2,
                                    2
                            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            new ItemStack(
                                    Items.CHORUS_FRUIT
                            )
                    );

            scene.idle(
                    15
            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            new ItemStack(
                                    Items.CHORUS_FRUIT
                            )
                    );

            scene.idle(
                    20
            );
        }
    }

    /*
     * ================================================================
     * LIQUID COMPOST
     * ================================================================
     */

    public static class CompostUsage
            implements PonderStoryBoard {

        @Override
        public void program(
                SceneBuilder builder,
                SceneBuildingUtil util
        ) {
            CreateSceneBuilder scene =
                    new CreateSceneBuilder(
                            builder
                    );

            scene.title(
                    "compost_use",
                    "Using Liquid Compost Within a Mechanical Insolator"
            );

            scene.configureBasePlate(
                    0,
                    0,
                    5
            );

            Selection belt =
                    util.select()
                            .fromTo(
                                    1,
                                    2,
                                    5,
                                    0,
                                    1,
                                    2
                            )
                            .add(
                                    util.select()
                                            .position(
                                                    1,
                                                    3,
                                                    2
                                            )
                            );

            Selection beltCog =
                    util.select()
                            .position(
                                    2,
                                    0,
                                    5
                            );

            scene.world()
                    .showSection(
                            util.select()
                                    .layer(0)
                                    .substract(
                                            beltCog
                                    ),
                            Direction.UP
                    );

            BlockPos insolator =
                    util.grid()
                            .at(
                                    2,
                                    3,
                                    2
                            );

            Selection insolatorSelect =
                    util.select()
                            .position(
                                    insolator
                            );

            BlockPos pump =
                    util.grid()
                            .at(
                                    2,
                                    2,
                                    2
                            );

            Selection pumpSelect =
                    util.select()
                            .position(
                                    pump
                            );

            BlockPos fluidTank =
                    util.grid()
                            .at(
                                    3,
                                    1,
                                    2
                            );

            Selection cogs =
                    util.select()
                            .fromTo(
                                    2,
                                    3,
                                    3,
                                    2,
                                    1,
                                    3
                            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            0
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            0
                    );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .fromTo(
                                            5,
                                            1,
                                            3,
                                            3,
                                            1,
                                            3
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            util.select()
                                    .fromTo(
                                            3,
                                            1,
                                            2,
                                            3,
                                            4,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    5
            );

            fillPonderTank(
                    scene,
                    fluidTank,
                    ModFluids.COMPOST
                            .get()
                            .getSource()
            );

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            2,
                                            1,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pumpSelect,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            insolatorSelect,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            Vec3 insolatorTop =
                    util.vector()
                            .topOf(
                                    insolator
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Plants gathered from Sniffers require Liquid Compost in order to be reprocessed"
                    )
                    .pointAt(
                            insolatorTop
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            scene.world()
                    .showSection(
                            cogs,
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .setKineticSpeed(
                            insolatorSelect,
                            32
                    );

            scene.world()
                    .setKineticSpeed(
                            pumpSelect,
                            32
                    );

            scene.effects()
                    .indicateSuccess(
                            insolator
                    );

            scene.idle(
                    10
            );

            ItemStack input =
                    new ItemStack(
                            Items.TORCHFLOWER_SEEDS
                    );

            Vec3 entitySpawn =
                    util.vector()
                            .topOf(
                                    insolator.above(
                                            3
                                    )
                            );

            ElementLink<EntityElement> entity =
                    scene.world()
                            .createItemEntity(
                                    entitySpawn,
                                    util.vector()
                                            .of(
                                                    0,
                                                    0.2,
                                                    0
                                            ),
                                    input
                            );

            scene.idle(
                    18
            );

            scene.world()
                    .modifyEntity(
                            entity,
                            Entity::discard
                    );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    input
                                            )
                    );

            scene.idle(
                    40
            );

            scene.world()
                    .modifyBlockEntity(
                            insolator,
                            MechanicalInsolatorBlockEntity.class,
                            blockEntity ->
                                    blockEntity.inputInv
                                            .setStackInSlot(
                                                    0,
                                                    ItemStack.EMPTY
                                            )
                    );

            ItemStack output =
                    Items.TORCHFLOWER
                            .getDefaultInstance();

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.NORTH
                                    ),
                            Pointing.RIGHT,
                            40
                    )
                    .rightClick()
                    .withItem(
                            output
                    );

            scene.idle(
                    50
            );

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            insolator,
                                            Direction.NORTH
                                    ),
                            Pointing.RIGHT,
                            40
                    )
                    .rightClick()
                    .withItem(
                            input
                    );

            scene.idle(
                    50
            );

            scene.addKeyframe();

            scene.world()
                    .showSection(
                            beltCog,
                            Direction.UP
                    );

            scene.world()
                    .showSection(
                            belt,
                            Direction.EAST
                    );

            scene.idle(
                    15
            );

            BlockPos beltPos =
                    util.grid()
                            .at(
                                    1,
                                    2,
                                    2
                            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            output
                    );

            scene.idle(
                    15
            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            new ItemStack(
                                    Items.TORCHFLOWER_SEEDS
                            )
                    );

            scene.idle(
                    20
            );
        }
    }
}
