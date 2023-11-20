package net.yaumama.cuisinecraft.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yaumama.cuisinecraft.CuisineCraft;
import net.yaumama.cuisinecraft.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CuisineCraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD =
            BLOCK_ENTITIES.register("cutting_board", () ->
                    BlockEntityType.Builder.of(CuttingBoardBlockEntity::new,
                            ModBlocks.CUTTING_BOARD.get()).build(null));

    public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE =
            BLOCK_ENTITIES.register("plate", () ->
                    BlockEntityType.Builder.of(PlateBlockEntity::new,
                            ModBlocks.PLATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<FryingPanBlockEntity>> FRYING_PAN =
            BLOCK_ENTITIES.register("frying_pan", () ->
                    BlockEntityType.Builder.of(FryingPanBlockEntity::new,
                            ModBlocks.FRYING_PAN.get()).build(null));

    public static final RegistryObject<BlockEntityType<SaucepanBlockEntity>> SAUCEPAN =
            BLOCK_ENTITIES.register("saucepan", () ->
                    BlockEntityType.Builder.of(SaucepanBlockEntity::new,
                            ModBlocks.SAUCEPAN.get()).build(null));

    public static final RegistryObject<BlockEntityType<MixingBowlBlockEntity>> MIXING_BOWL =
            BLOCK_ENTITIES.register("mixing_bowl", () ->
                    BlockEntityType.Builder.of(MixingBowlBlockEntity::new,
                            ModBlocks.MIXING_BOWL.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
