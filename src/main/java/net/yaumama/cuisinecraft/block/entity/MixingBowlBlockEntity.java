package net.yaumama.cuisinecraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.yaumama.cuisinecraft.item.ModItems;
import net.yaumama.cuisinecraft.networking.ModMessages;
import net.yaumama.cuisinecraft.networking.packet.ItemStackSyncS2CPacket;
import net.yaumama.cuisinecraft.sound.ModSounds;
import net.yaumama.cuisinecraft.utility.GeneralUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MixingBowlBlockEntity extends BlockEntity {
    private Item[][] canMix = {{ModItems.CUT_GREEN_ONION.get(), Items.EGG}};
    private Item[] result = {ModItems.RAW_SCRAMBLED_EGGS.get()};
    private Item[] dontRender = {Items.EGG};
    private Item[] fluids = {Items.WATER_BUCKET, Items.MILK_BUCKET};

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MixingBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIXING_BOWL.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() ->itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public boolean mix(BlockPos blockPos, Player player) {
        int itemHandlerLength = GeneralUtility.getItemHandlerLength(itemHandler);

        for (int i = 0; i < canMix.length; i++) {
            if (itemHandlerLength == canMix[i].length) {
                int checked = 0;

                for (int k = 0; k < canMix[i].length; k++) {
                    if (GeneralUtility.checkItemInItemHandler(canMix[i][k], itemHandler)) {
                        checked++;
                    }
                }
                if (checked == canMix[i].length) {

                    for (int k = 0; k < itemHandler.getSlots(); k++) {
                        itemHandler.setStackInSlot(k, ItemStack.EMPTY);
                    }
                    itemHandler.setStackInSlot(0, new ItemStack(result[GeneralUtility.getIndexFromItemsInArray(
                            canMix[i], canMix)]));
                    player.getLevel().playSound(null, blockPos, SoundEvents.COW_MILK,
                            SoundSource.BLOCKS, 1f, 1f);
                    return true;
                }
            }
        }
        return false;
    }

    public void placeFood(Player player, ItemStack item, BlockPos blockPos) {
        if (item.is(Items.AIR)) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (!GeneralUtility.checkItemInArray(itemHandler.getStackInSlot(i).getItem(), fluids)) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(i));
                    itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
            return;
        }

        if (item.is(Items.BUCKET)) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (GeneralUtility.checkItemInArray(itemHandler.getStackInSlot(i).getItem(), fluids)) {
                    item.split(1);
                    ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(i));
                    itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    player.getLevel().playSound(null, blockPos, SoundEvents.BUCKET_FILL,
                            SoundSource.BLOCKS, 1f, 1f);
                    return;
                }
            }
            return;
        }

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (item.is(itemHandler.getStackInSlot(i).getItem())) {
                return;
            }
           if (itemHandler.getStackInSlot(i).isEmpty()) {
               Item itemType = item.getItem();
               Item fluidItem = null;

               if (GeneralUtility.checkItemInArray(itemType, fluids)) {
                   for (int k = 0; k < itemHandler.getSlots(); k++) {
                       if (GeneralUtility.checkItemInArray(itemHandler.getStackInSlot(k).getItem(), fluids)) {
                           fluidItem = itemHandler.getStackInSlot(k).getItem();
                           itemHandler.setStackInSlot(k, ItemStack.EMPTY);

                       }
                   }
               }
               itemHandler.setStackInSlot(i, item.split(1));
               if (GeneralUtility.checkItemInArray(itemType, fluids)) {
                   player.getLevel().playSound(null, blockPos, SoundEvents.BUCKET_EMPTY,
                           SoundSource.BLOCKS, 1f, 1f);
                   if (fluidItem != null) {
                       ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(fluidItem));
                       player.getLevel().playSound(null, blockPos, SoundEvents.BUCKET_FILL,
                               SoundSource.BLOCKS, 1f, 1f);
                   } else {
                       ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.BUCKET));
                   }
               }
               break;
           }
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, MixingBowlBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }
    }

    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[itemHandler.getSlots()];

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            items[i] = itemHandler.getStackInSlot(i);
        }

        return items;
    }

    public Item[] getFluidsList() {
        return fluids;
    }
    public Item[] getDontRenderList() {
        return dontRender;
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }
}
