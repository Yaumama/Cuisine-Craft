package net.yaumama.cuisinecraft.utility;

import net.minecraft.world.item.Item;

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
}
