package net.yaumama.cuisinecraft.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yaumama.cuisinecraft.CuisineCraft;

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
            () -> new Item(new Item.Properties().stacksTo(64).tab(ModCreativeModeTab.CUISINECRAFT_TAB)));

    public static final RegistryObject<Item> CUT_GREEN_ONION = ITEMS.register("cut_green_onion",
            () -> new Item(new Item.Properties().stacksTo(64).tab(ModCreativeModeTab.CUISINECRAFT_TAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
