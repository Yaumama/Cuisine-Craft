package net.yaumama.cuisinecraft.utility;

import net.minecraft.world.item.Item;
import net.minecraftforge.items.ItemStackHandler;

public class GeneralUtility {
    public static boolean checkItemInArray(Item item, Item[] array) {
        boolean inArray = false;
        for (Item element : array) {
            if (element == item) {
                inArray = true;
                break;
            }
        }

        return inArray;
    }

    public static int getIndexFromItemInArray(Item item, Item[] array) {
        int i = 0;
        for (Item element : array) {
            i++;
            if (element == item) {
                break;
            }
        }

        return i - 1;
    }

    public static int getIndexFromItemsInArray(Item[] item, Item[][] array) {
        int i = 0;
        for (Item[] element : array) {
            i++;
            if (element == item) {
                break;
            }
        }

        return i - 1;
    }

    public static int getItemHandlerLength(ItemStackHandler itemHandler) {
        int length = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                length++;
            }
        }
        return length;
    }

    public static boolean checkItemInItemHandler(Item item, ItemStackHandler itemHandler) {
        boolean inHandler = false;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).is(item)) {
                inHandler = true;
                break;
            }
        }

        return inHandler;
    }
}
