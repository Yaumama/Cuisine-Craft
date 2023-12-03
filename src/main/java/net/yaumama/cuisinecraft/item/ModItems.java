package net.yaumama.cuisinecraft.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yaumama.cuisinecraft.CuisineCraft;
import net.yaumama.cuisinecraft.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CuisineCraft.MOD_ID);

    public static final RegistryObject<Item> BUTTER = ITEMS.register("butter",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CUISINECRAFT_TAB)));

    public static final RegistryObject<Item> WHISK = ITEMS.register("whisk",
            () -> new Item(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.CUISINECRAFT_TAB)));

    public static final RegistryObject<Item> KNIFE = ITEMS.register("knife",
            () -> new Item(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.CUISINECRAFT_TAB)));

    public static final RegistryObject<Item> GREEN_ONION = ITEMS.register("green_onion",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CUISINECRAFT_TAB)
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(2f).build())));

    public static final RegistryObject<Item> CUT_GREEN_ONION = ITEMS.register("cut_green_onion",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CUISINECRAFT_TAB)));

    public static final RegistryObject<Item> GREEN_ONION_SEEDS = ITEMS.register("green_onion_seeds",
            () -> new ItemNameBlockItem(ModBlocks.GREEN_ONION_CROP.get(),
                    new Item.Properties().tab(ModCreativeModeTab.CUISINECRAFT_TAB)));
    public static final RegistryObject<Item> SCRAMBLED_EGGS = ITEMS.register("scrambled_eggs",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CUISINECRAFT_TAB)
                    .food(new FoodProperties.Builder().nutrition(10).saturationMod(10f).build())));
    public static final RegistryObject<Item> RAW_SCRAMBLED_EGGS = ITEMS.register("raw_scrambled_eggs",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CUISINECRAFT_TAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
