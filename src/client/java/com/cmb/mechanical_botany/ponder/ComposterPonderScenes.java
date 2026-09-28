package com.cmb.mechanical_botany.kinetics.composter;

import com.cmb.mechanical_botany.registry.ModFluids;
import com.cmb.mechanical_botany.registry.ModItems;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public final class ComposterPonderScenes {

    /*
     * ================================================================
     * FLUID AMOUNTS
     * ================================================================
     *
     * NeoForge 1.0.5 used millibuckets:
     *
     * 50 mB   -> 4,050 Fabric units
     * 100 mB  -> 8,100 Fabric units
     * 200 mB  -> 16,200 Fabric units
     * 500 mB  -> 40,500 Fabric units
     * 1000 mB -> 81,000 Fabric units
     */

    private static final long FIFTY_MB =
            FluidConstants.BUCKET * 50L / 1000L;

    private static final long ONE_HUNDRED_MB =
            FluidConstants.BUCKET * 100L / 1000L;

    private static final long TWO_HUNDRED_MB =
            FluidConstants.BUCKET * 200L / 1000L;

    private static final long FIVE_HUNDRED_MB =
            FluidConstants.BUCKET * 500L / 1000L;

    private static final long ONE_BUCKET =
            FluidConstants.BUCKET;

    /*
     * The original Creative Fluid Tank in compost_creation.nbt contains
     * 32 buckets of Lava.
     */
    private static final long LAVA_TANK_AMOUNT =
            FluidConstants.BUCKET * 32L;

    private ComposterPonderScenes() {
    }

    /*
     * ================================================================
     * FLUID HELPERS
     * ================================================================
     */

    private static void setFluidTank(
            CreateSceneBuilder scene,
            BlockPos position,
            FluidStack fluid
    ) {
        scene.world()
                .modifyBlockEntity(
                        position,
                        FluidTankBlockEntity.class,
                        tank ->
                                tank.getTankInventory()
                                        .setFluid(
                                                fluid
                                        )
                );
    }

    private static void setBasinInputs(
            CreateSceneBuilder scene,
            BlockPos position,
            long compostAmount,
            long lavaAmount
    ) {
        scene.world()
                .modifyBlockEntity(
                        position,
                        BasinBlockEntity.class,
                        basin -> {

                            if (
                                    compostAmount > 0
                            ) {
                                basin.inputTank
                                        .getTanks()[0]
                                        .getTank()
                                        .setFluid(
                                                new FluidStack(
                                                        ModFluids.COMPOST
                                                                .get()
                                                                .getSource(),
                                                        compostAmount
                                                )
                                        );
                            } else {
                                basin.inputTank
                                        .getTanks()[0]
                                        .getTank()
                                        .setFluid(
                                                FluidStack.EMPTY
                                        );
                            }

                            if (
                                    lavaAmount > 0
                            ) {
                                basin.inputTank
                                        .getTanks()[1]
                                        .getTank()
                                        .setFluid(
                                                new FluidStack(
                                                        Fluids.LAVA,
                                                        lavaAmount
                                                )
                                        );
                            } else {
                                basin.inputTank
                                        .getTanks()[1]
                                        .getTank()
                                        .setFluid(
                                                FluidStack.EMPTY
                                        );
                            }

                            basin.notifyChangeOfContents();
                        }
                );
    }

    private static void setMoltenCompostResult(
            CreateSceneBuilder scene,
            BlockPos position
    ) {
        scene.world()
                .modifyBlockEntity(
                        position,
                        BasinBlockEntity.class,
                        basin -> {

                            basin.inputTank
                                    .getTanks()[0]
                                    .getTank()
                                    .setFluid(
                                            new FluidStack(
                                                    ModFluids.MOLTEN_COMPOST
                                                            .get()
                                                            .getSource(),
                                                    ONE_BUCKET
                                            )
                                    );

                            basin.inputTank
                                    .getTanks()[1]
                                    .getTank()
                                    .setFluid(
                                            FluidStack.EMPTY
                                    );

                            basin.notifyChangeOfContents();
                        }
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
                    "composter",
                    "Breaking Down Plants Into Compost"
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
                                    .layer(
                                            0
                                    )
                                    .substract(
                                            beltCog
                                    ),
                            Direction.UP
                    );

            BlockPos composter =
                    util.grid()
                            .at(
                                    2,
                                    2,
                                    2
                            );

            Selection composterSelect =
                    util.select()
                            .position(
                                    composter
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
                            composterSelect,
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
                            util.select()
                                    .position(
                                            2,
                                            1,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            composter
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            Vec3 composterTop =
                    util.vector()
                            .topOf(
                                    composter
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Mechanical Composter will turn compostables into Compost"
                    )
                    .pointAt(
                            composterTop
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
                            composterSelect,
                            32
                    );

            scene.effects()
                    .indicateSuccess(
                            composter
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
                                            composter.east()
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    70
            );

            ItemStack input =
                    new ItemStack(
                            Items.ROSE_BUSH
                    );

            Vec3 entitySpawn =
                    util.vector()
                            .topOf(
                                    composter.above(
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
                            composter,
                            MechanicalComposterBlockEntity.class,
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
                            composterTop,
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
                            "Throw or Insert compostables at the top"
                    )
                    .pointAt(
                            composterTop
                    )
                    .placeNearTarget();

            scene.idle(
                    60
            );

            scene.world()
                    .modifyBlockEntity(
                            composter,
                            MechanicalComposterBlockEntity.class,
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
                            "After some time, compost can be retrieved via Right-click"
                    )
                    .pointAt(
                            util.vector()
                                    .blockSurface(
                                            composter,
                                            Direction.WEST
                                    )
                    )
                    .placeNearTarget();

            scene.idle(
                    80
            );

            ItemStack output =
                    new ItemStack(
                            ModItems.COMPOST
                    );

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            composter,
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
                    15
            );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            new ItemStack(
                                    ModItems.COMPOST
                            )
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
                                            composter,
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
     * LAYERED COMPOSTERS
     * ================================================================
     */

    public static class LayeredComposters
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
                    "layered_composters",
                    "Transporting Rotational Power With Layered Composters"
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
                                    .layer(
                                            0
                                    )
                                    .substract(
                                            beltCog
                                    ),
                            Direction.UP
                    );

            BlockPos composter =
                    util.grid()
                            .at(
                                    2,
                                    2,
                                    2
                            );

            Selection composterSelect =
                    util.select()
                            .position(
                                    composter
                            );

            BlockPos composter2 =
                    util.grid()
                            .at(
                                    2,
                                    4,
                                    2
                            );

            Selection cogs =
                    util.select()
                            .fromTo(
                                    5,
                                    1,
                                    2,
                                    3,
                                    1,
                                    2
                            );

            scene.world()
                    .setKineticSpeed(
                            composterSelect,
                            0
                    );

            scene.idle(
                    5
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
                            composterSelect,
                            -32
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
                            util.select()
                                    .position(
                                            2,
                                            1,
                                            2
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            composter
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            scene.effects()
                    .indicateSuccess(
                            composter
                    );

            Vec3 composterTop =
                    util.vector()
                            .topOf(
                                    composter
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Mechanical Composters can also be powered from the bottom"
                    )
                    .pointAt(
                            composterTop
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "Rotational Power flows through the Mechanical Composter's cog"
                    )
                    .pointAt(
                            composterTop
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            composter2
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            composter2.below()
                                    ),
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            util.select()
                                    .position(
                                            composter2.west()
                                    ),
                            Direction.DOWN
                    );

            scene.idle(
                    10
            );

            Vec3 composterTop2 =
                    util.vector()
                            .topOf(
                                    composter2
                            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .attachKeyFrame()
                    .text(
                            "This offers for some unique automation potential"
                    )
                    .pointAt(
                            composterTop2
                    )
                    .placeNearTarget();

            scene.idle(
                    90
            );

            ItemStack compost =
                    new ItemStack(
                            ModItems.COMPOST
                    );

            Vec3 entitySpawn =
                    util.vector()
                            .of(
                                    1,
                                    4,
                                    2.2
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

            BlockPos beltPos2 =
                    util.grid()
                            .at(
                                    0,
                                    1,
                                    2
                            );

            ElementLink<EntityElement> entity =
                    scene.world()
                            .createItemEntity(
                                    entitySpawn,
                                    util.vector()
                                            .of(
                                                    0,
                                                    -0.1,
                                                    0
                                            ),
                                    compost
                            );

            scene.world()
                    .flapFunnel(
                            util.grid()
                                    .at(
                                            1,
                                            4,
                                            2
                                    ),
                            true
                    );

            scene.world()
                    .createItemOnBelt(
                            beltPos,
                            Direction.EAST,
                            compost
                    );

            scene.idle(
                    9
            );

            scene.world()
                    .modifyEntity(
                            entity,
                            Entity::discard
                    );

            scene.idle(
                    2
            );

            scene.world()
                    .createItemOnBelt(
                            beltPos2,
                            Direction.DOWN,
                            compost
                    );

            scene.idle(
                    20
            );
        }
    }

    /*
     * ================================================================
     * CREATING LIQUID COMPOST
     * ================================================================
     *
     * This is the Fabric port of the full 1.0.5 NeoForge storyboard.
     *
     * Original structure:
     *
     * Compost
     *   -> Item Drain
     *   -> Liquid Compost tank
     *   -> Basin
     *
     * Lava Creative Tank
     *   -> Basin
     *
     * Heated Basin + Mechanical Mixer
     *   -> Molten Liquid Compost
     */

    public static class CreatingLiquidCompost
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
                    "compost_creation",
                    "Processing Compost Into Usable Fluids"
            );

            scene.configureBasePlate(
                    0,
                    0,
                    5
            );

            /*
             * ============================================================
             * STRUCTURE SELECTIONS
             * ============================================================
             */

            // Powers the Item Drain belt.
            Selection cogs0 =
                    util.select()
                            .fromTo(
                                    0,
                                    0,
                                    1,
                                    0,
                                    1,
                                    0
                            );

            // Powers the Lava-side pump.
            Selection cogs1 =
                    util.select()
                            .fromTo(
                                    6,
                                    0,
                                    1,
                                    6,
                                    1,
                                    0
                            );

            // Powers the Basin-side pump / Mixer network.
            Selection cogs2 =
                    util.select()
                            .fromTo(
                                    3,
                                    0,
                                    5,
                                    4,
                                    1,
                                    5
                            );

            Selection selection0 =
                    util.select()
                            .fromTo(
                                    4,
                                    1,
                                    4,
                                    4,
                                    4,
                                    3
                            );

            Selection selection1 =
                    util.select()
                            .position(
                                    5,
                                    1,
                                    0
                            );

            Selection selection2 =
                    util.select()
                            .fromTo(
                                    4,
                                    4,
                                    1,
                                    4,
                                    4,
                                    2
                            );

            // Powers Liquid Compost extraction.
            Selection cogs3 =
                    util.select()
                            .fromTo(
                                    0,
                                    0,
                                    4,
                                    0,
                                    1,
                                    2
                            );

            Selection pump0 =
                    util.select()
                            .fromTo(
                                    1,
                                    1,
                                    3,
                                    2,
                                    1,
                                    4
                            );

            Selection pump1 =
                    util.select()
                            .fromTo(
                                    3,
                                    2,
                                    2,
                                    4,
                                    1,
                                    2
                            );

            Selection pump2 =
                    util.select()
                            .fromTo(
                                    4,
                                    2,
                                    1,
                                    4,
                                    1,
                                    0
                            );

            Selection tank0 =
                    util.select()
                            .fromTo(
                                    3,
                                    1,
                                    3,
                                    3,
                                    4,
                                    3
                            );

            Selection tank1 =
                    util.select()
                            .fromTo(
                                    5,
                                    1,
                                    1,
                                    5,
                                    4,
                                    1
                            );

            BlockPos tank0Position =
                    util.grid()
                            .at(
                                    3,
                                    1,
                                    3
                            );

            BlockPos tank1Position =
                    util.grid()
                            .at(
                                    5,
                                    1,
                                    1
                            );

            Selection belt =
                    util.select()
                            .fromTo(
                                    1,
                                    1,
                                    0,
                                    1,
                                    1,
                                    2
                            );

            BlockPos drain =
                    util.grid()
                            .at(
                                    1,
                                    1,
                                    2
                            );

            Selection basinAndCo =
                    util.select()
                            .fromTo(
                                    3,
                                    1,
                                    1,
                                    3,
                                    4,
                                    1
                            );

            BlockPos basinPosition =
                    util.grid()
                            .at(
                                    3,
                                    2,
                                    1
                            );

            BlockPos burnerPosition =
                    util.grid()
                            .at(
                                    3,
                                    1,
                                    1
                            );

            BlockPos mixerPosition =
                    util.grid()
                            .at(
                                    3,
                                    4,
                                    1
                            );

            /*
             * ============================================================
             * ITEM DRAIN
             * ============================================================
             */

            scene.idle(
                    5
            );

            scene.world()
                    .showSection(
                            util.select()
                                    .layer(
                                            0
                                    )
                                    .substract(
                                            cogs1
                                    )
                                    .substract(
                                            cogs2
                                    )
                                    .substract(
                                            cogs3
                                    ),
                            Direction.UP
                    );

            scene.world()
                    .showSection(
                            cogs0,
                            Direction.DOWN
                    );

            scene.idle(
                    7
            );

            scene.world()
                    .showSection(
                            belt,
                            Direction.DOWN
                    );

            ItemStack compost0 =
                    new ItemStack(
                            ModItems.COMPOST
                    );

            ItemStack compost1 =
                    new ItemStack(
                            ModItems.COMPOST
                    );

            ItemStack compost2 =
                    new ItemStack(
                            ModItems.COMPOST
                    );

            ItemStack compost3 =
                    new ItemStack(
                            ModItems.COMPOST
                    );

            scene.overlay()
                    .showText(
                            80
                    )
                    .text(
                            "Putting Compost into an Item Drain will provide small amounts of Liquid Compost"
                    )
                    .attachKeyFrame()
                    .placeNearTarget()
                    .pointAt(
                            util.vector()
                                    .blockSurface(
                                            drain.west(),
                                            Direction.UP
                                    )
                    );

            scene.idle(
                    20
            );

            BlockPos beltStart =
                    util.grid()
                            .at(
                                    1,
                                    1,
                                    0
                            );

            scene.world()
                    .createItemOnBelt(
                            beltStart,
                            Direction.NORTH,
                            compost0
                    );

            scene.idle(
                    24
            );

            scene.world()
                    .createItemOnBelt(
                            beltStart,
                            Direction.NORTH,
                            compost1
                    );

            scene.idle(
                    24
            );

            scene.world()
                    .createItemOnBelt(
                            beltStart,
                            Direction.NORTH,
                            compost2
                    );

            scene.idle(
                    24
            );

            scene.world()
                    .createItemOnBelt(
                            beltStart,
                            Direction.NORTH,
                            compost3
                    );

            scene.idle(
                    50
            );

            /*
             * ============================================================
             * LIQUID COMPOST STORAGE
             * ============================================================
             */

            scene.addKeyframe();

            scene.idle(
                    7
            );

            scene.world()
                    .showSection(
                            cogs3,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pump0,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            tank0,
                            Direction.DOWN
                    );

            scene.idle(
                    24
            );

            /*
             * Original 1.0.5 adds 500 mB four times.
             *
             * On Fabric that is:
             *
             * 500 mB = 40,500 transfer units.
             */

            for (
                    int i = 1;
                    i <= 4;
                    i++
            ) {
                long amount =
                        FIVE_HUNDRED_MB
                                * i;

                setFluidTank(
                        scene,
                        tank0Position,
                        new FluidStack(
                                ModFluids.COMPOST
                                        .get()
                                        .getSource(),
                                amount
                        )
                );

                scene.idle(
                        7
                );
            }

            /*
             * ============================================================
             * MIXING SETUP
             * ============================================================
             */

            scene.addKeyframe();

            scene.idle(
                    14
            );

            scene.world()
                    .showSection(
                            tank1,
                            Direction.DOWN
                    );

            /*
             * The original structure contains 32 buckets of Lava in the
             * Creative Fluid Tank.
             *
             * Its old NeoForge NBT amount is not reliable on Fabric, so
             * populate it explicitly.
             */

            setFluidTank(
                    scene,
                    tank1Position,
                    new FluidStack(
                            Fluids.LAVA,
                            LAVA_TANK_AMOUNT
                    )
            );

            scene.world()
                    .showSection(
                            basinAndCo,
                            Direction.DOWN
                    );

            scene.idle(
                    7
            );

            scene.world()
                    .showSection(
                            cogs1,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pump2,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            selection1,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            selection2,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            cogs2,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            selection0,
                            Direction.DOWN
                    );

            scene.world()
                    .showSection(
                            pump1,
                            Direction.DOWN
                    );

            scene.idle(
                    14
            );

            /*
             * Original basin state:
             *
             * 1000 mB Lava
             *  500 mB Liquid Compost
             */

            setBasinInputs(
                    scene,
                    basinPosition,
                    FIVE_HUNDRED_MB,
                    ONE_BUCKET
            );

            scene.idle(
                    7
            );

            scene.overlay()
                    .showText(
                            80
                    )
                    .text(
                            "Mixing Liquid Compost and Lava together provides Molten Liquid Compost"
                    )
                    .attachKeyFrame()
                    .placeNearTarget()
                    .pointAt(
                            util.vector()
                                    .blockSurface(
                                            basinPosition.west(),
                                            Direction.UP
                                    )
                    );

            scene.idle(
                    90
            );

            /*
             * ============================================================
             * HEAT THE BLAZE BURNER
             * ============================================================
             */

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            burnerPosition,
                                            Direction.WEST
                                    ),
                            Pointing.LEFT,
                            15
                    )
                    .rightClick()
                    .withItem(
                            new ItemStack(
                                    Items.OAK_PLANKS
                            )
                    );

            scene.idle(
                    3
            );

            scene.world()
                    .modifyBlock(
                            burnerPosition,
                            state ->
                                    state.setValue(
                                            BlazeBurnerBlock.HEAT_LEVEL,
                                            BlazeBurnerBlock.HeatLevel.KINDLED
                                    ),
                            false
                    );

            scene.idle(
                    7
            );

            /*
             * ============================================================
             * RUN THE MIXER
             * ============================================================
             */

            scene.world()
                    .modifyBlockEntity(
                            mixerPosition,
                            MechanicalMixerBlockEntity.class,
                            MechanicalMixerBlockEntity::startProcessingBasin
                    );

            scene.idle(
                    40
            );

            /*
             * ============================================================
             * CONSUME THE INGREDIENTS
             * ============================================================
             *
             * Original 1.0.5 drains both ingredients over five stages:
             *
             * Liquid Compost:
             * 500 mB / 5 = 100 mB per stage
             *
             * Lava:
             * 1000 mB / 5 = 200 mB per stage
             */

            scene.addKeyframe();

            scene.idle(
                    4
            );

            for (
                    int i = 1;
                    i <= 5;
                    i++
            ) {
                scene.idle(
                        7
                );

                long compostRemaining =
                        FIVE_HUNDRED_MB
                                - ONE_HUNDRED_MB * i;

                long lavaRemaining =
                        ONE_BUCKET
                                - TWO_HUNDRED_MB * i;

                setBasinInputs(
                        scene,
                        basinPosition,
                        Math.max(
                                0,
                                compostRemaining
                        ),
                        Math.max(
                                0,
                                lavaRemaining
                        )
                );
            }

            /*
             * ============================================================
             * MOLTEN LIQUID COMPOST RESULT
             * ============================================================
             */

            setMoltenCompostResult(
                    scene,
                    basinPosition
            );

            scene.idle(
                    14
            );

            scene.overlay()
                    .showControls(
                            util.vector()
                                    .blockSurface(
                                            basinPosition,
                                            Direction.WEST
                                    ),
                            Pointing.LEFT,
                            25
                    )
                    .withItem(
                            new ItemStack(
                                    ModFluids.MOLTEN_COMPOST
                                            .get()
                                            .getBucket()
                            )
                    );

            scene.idle(
                    20
            );
        }
    }
}