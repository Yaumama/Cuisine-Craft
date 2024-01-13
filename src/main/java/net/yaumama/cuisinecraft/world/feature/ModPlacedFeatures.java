package net.yaumama.cuisinecraft.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.yaumama.cuisinecraft.CuisineCraft;
import net.yaumama.cuisinecraft.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, CuisineCraft.MOD_ID);


    public static final RegistryObject<PlacedFeature> CINNAMON_CHECKED = PLACED_FEATURES.register("cinnamon_checked",
            () -> new PlacedFeature(ModConfiguredFeatures.CINNAMON.getHolder().get(),
                    List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.CINNAMON_SAPLING.get()))));

    public static final RegistryObject<PlacedFeature> CINNAMON_PLACED = PLACED_FEATURES.register("cinnamon_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.CINNAMON_SPAWN.getHolder().get(), VegetationPlacements.treePlacement(
                    PlacementUtils.countExtra(0, 0.1f, 1))));


    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    public static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}