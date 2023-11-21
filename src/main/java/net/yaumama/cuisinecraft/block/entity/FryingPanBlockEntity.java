package net.yaumama.cuisinecraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.yaumama.cuisinecraft.block.ModBlocks;
import net.yaumama.cuisinecraft.item.ModItems;
import net.yaumama.cuisinecraft.networking.ModMessages;
import net.yaumama.cuisinecraft.networking.packet.ItemStackSyncS2CPacket;
import net.yaumama.cuisinecraft.sound.ModSounds;
import net.yaumama.cuisinecraft.utility.GeneralUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;

public class FryingPanBlockEntity extends BlockEntity {
    private boolean buttered = false;
    private int progress = 0;
    private int maxProgress = 60;
    private Item[] canCook = {Items.BEEF, Items.CHICKEN, Items.COD, Items.MUTTON,
            Items.PORKCHOP, Items.RABBIT, Items.SALMON};
    private Item[] result = {Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON,
            Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON};

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public FryingPanBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FRYING_PAN.get(), pos, state);
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
        nbt.putInt("frying_pan_progress", progress);
        nbt.putBoolean("frying_pan_buttered", buttered);


        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("frying_pan_progress");
        buttered = nbt.getBoolean("frying_pan_buttered");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void placeFood(Player player, ItemStack item, BlockPos blockPos) {
        if (!(player.getLevel().getBlockState(blockPos.below()).getBlock() == ModBlocks.OVEN.get())) {
            player.sendSystemMessage(Component.literal("You need a oven below the frying pan to cook!"));
            return;
        }
        if (item.is(Items.AIR)) {
            ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(0));
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            progress = 0;
            return;
        }

        if (item.is(ModItems.BUTTER.get())) {
            buttered = true;
            item.split(1);
            player.getLevel().playSound(null, blockPos, ModSounds.SIZZLE.get(),
                    SoundSource.BLOCKS, 1f, 1f);
            return;
        }

        if (buttered) {
            if (itemHandler.getStackInSlot(0).isEmpty()) {
                itemHandler.setStackInSlot(0, item.split(1));
                player.getLevel().playSound(null, blockPos, ModSounds.SIZZLE.get(),
                        SoundSource.BLOCKS, 1f, 1f);
                buttered = false;
            } else {
                ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(0));
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                itemHandler.setStackInSlot(0, item.split(1));
                player.getLevel().playSound(null, blockPos, ModSounds.SIZZLE.get(),
                        SoundSource.BLOCKS, 1f, 1f);
                buttered = false;
            }
        } else {
            player.sendSystemMessage(Component.literal("You need to add butter before cooking!"));
        }

    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, FryingPanBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (GeneralUtility.checkItemInArray(pEntity.itemHandler.getStackInSlot(0).getItem(), pEntity.canCook)) {
            if (pEntity.progress < pEntity.maxProgress) {
                pEntity.progress++;
            } else {
                pEntity.itemHandler.setStackInSlot(0, new ItemStack(pEntity.result[GeneralUtility.getIndexFromItemInArray(
                        pEntity.itemHandler.getStackInSlot(0).getItem(), pEntity.canCook)]));
                pEntity.progress = 0;
            }
        }
    }

    public ItemStack getRenderStack() {
        return itemHandler.getStackInSlot(0);
    };

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }
}
