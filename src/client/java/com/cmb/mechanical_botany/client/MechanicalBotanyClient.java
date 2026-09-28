package com.cmb.mechanical_botany.client;

import com.cmb.mechanical_botany.kinetics.composter.MechanicalComposterRenderer;
import com.cmb.mechanical_botany.kinetics.composter.MechanicalComposterVisual;
import com.cmb.mechanical_botany.kinetics.insolator.MechanicalInsolatorRenderer;
import com.cmb.mechanical_botany.kinetics.insolator.MechanicalInsolatorVisual;
import com.cmb.mechanical_botany.ponder.ModPonderPlugin;
import com.cmb.mechanical_botany.registry.ModBlockEntities;
import com.cmb.mechanical_botany.registry.ModBlocks;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class MechanicalBotanyClient
		implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		/*
		 * ================================================================
		 * PARTIAL MODELS
		 * ================================================================
		 */

		ModPartialModels.init();

		/*
		 * ================================================================
		 * PONDER
		 * ================================================================
		 */

		PonderIndex.addPlugin(
				new ModPonderPlugin()
		);

		/*
		 * ================================================================
		 * BLOCK RENDER LAYERS
		 * ================================================================
		 */

		BlockRenderLayerMap.INSTANCE.putBlock(
				ModBlocks.MECHANICAL_COMPOSTER,
				RenderType.cutout()
		);

		BlockRenderLayerMap.INSTANCE.putBlock(
				ModBlocks.MECHANICAL_INSOLATOR,
				RenderType.cutout()
		);

		/*
		 * ================================================================
		 * FLUID RENDERING
		 * ================================================================
		 */

		MechanicalBotanyFluidClient.register();

		/*
		 * ================================================================
		 * PARTICLES
		 * ================================================================
		 */

		MechanicalBotanyParticleClient.register();

		/*
		 * ================================================================
		 * BLOCK ENTITY RENDERERS
		 * ================================================================
		 */

		BlockEntityRenderers.register(
				ModBlockEntities.MECHANICAL_COMPOSTER,
				MechanicalComposterRenderer::new
		);

		BlockEntityRenderers.register(
				ModBlockEntities.MECHANICAL_INSOLATOR,
				MechanicalInsolatorRenderer::new
		);

		/*
		 * ================================================================
		 * FLYWHEEL VISUALS
		 * ================================================================
		 */

		SimpleBlockEntityVisualizer
				.builder(
						ModBlockEntities.MECHANICAL_COMPOSTER
				)
				.factory(
						MechanicalComposterVisual::new
				)
				.apply();

		SimpleBlockEntityVisualizer
				.builder(
						ModBlockEntities.MECHANICAL_INSOLATOR
				)
				.factory(
						MechanicalInsolatorVisual::new
				)
				.apply();
	}
}