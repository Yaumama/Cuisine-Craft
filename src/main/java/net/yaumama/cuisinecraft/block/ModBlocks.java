package net.yaumama.cuisinecraft.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yaumama.cuisinecraft.CuisineCraft;
import net.yaumama.cuisinecraft.block.custom.*;
import net.yaumama.cuisinecraft.item.ModCreativeModeTab;
import net.yaumama.cuisinecraft.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CuisineCraft.MOD_ID);

    public static final RegistryObject<Block> KITCHEN_FLOOR = registerBlock("kitchen_floor",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4f).requiresCorrectToolForDrops()), ModCreativeModeTab.CUISINECRAFT_TAB, 64);

    public static final RegistryObject<Block> MIXING_BOWL = registerBlock("mixing_bowl",
            () -> new MixingBowl(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(1f).noOcclusion()), ModCreativeModeTab.CUISINECRAFT_TAB, 1);

    public static final RegistryObject<Block> OVEN = registerBlock("oven",
            () -> new Oven(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6f).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.CUISINECRAFT_TAB, 1);

    public static final RegistryObject<Block> FRYING_PAN = registerBlock("frying_pan",
            () -> new FryingPan(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(5f).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.CUISINECRAFT_TAB, 1);

    public static final RegistryObject<Block> CUTTING_BOARD = registerBlock("cutting_board",
            () -> new CuttingBoard(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(1f).noOcclusion()), ModCreativeModeTab.CUISINECRAFT_TAB, 1);
    public static final RegistryObject<Block> PLATE = registerBlock("plate",
            () -> new Plate(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(1f).noOcclusion()), ModCreativeModeTab.CUISINECRAFT_TAB, 4);

    public static final RegistryObject<Block> SAUCEPAN = registerBlock("saucepan",
            () -> new Saucepan(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(5f).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.CUISINECRAFT_TAB, 1);

    public static final RegistryObject<Block> GREEN_ONION_CROP = BLOCKS.register("green_onion_crop",
            () -> new GreenOnionCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab, int stacksTo) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab, stacksTo);
        return toReturn;
    }

    private static<T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab, int stacksTo) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(stacksTo).tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
