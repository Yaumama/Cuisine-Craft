package net.yaumama.cuisinecraft.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yaumama.cuisinecraft.CuisineCraft;
import net.yaumama.cuisinecraft.block.entity.ModBlockEntities;
import net.yaumama.cuisinecraft.block.entity.renderer.*;

public class ClientEvents {
    @Mod.EventBusSubscriber
    public static class ClientForgeEvents {

    }

    @Mod.EventBusSubscriber(modid = CuisineCraft.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.CUTTING_BOARD.get(),
                    CuttingBoardBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.PLATE.get(),
                    PlateBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.FRYING_PAN.get(),
                    FryingPanBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.SAUCEPAN.get(),
                    SaucepanBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.MIXING_BOWL.get(),
                    MixingBowlBlockEntityRenderer::new);
        }
    }
}
