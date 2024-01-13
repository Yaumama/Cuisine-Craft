package net.yaumama.cuisinecraft.world.feature.tree;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.yaumama.cuisinecraft.world.feature.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class CinnamonTreeGrower extends AbstractTreeGrower {

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        return ModConfiguredFeatures.CINNAMON.getHolder().get();
    }
}
