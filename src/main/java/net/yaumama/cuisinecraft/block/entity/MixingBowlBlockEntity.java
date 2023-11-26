package net.yaumama.cuisinecraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
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
import net.yaumama.cuisinecraft.networking.ModMessages;
import net.yaumama.cuisinecraft.networking.packet.ItemStackSyncS2CPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MixingBowlBlockEntity extends BlockEntity {
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

    public void placeFood(Player player, ItemStack item) {
        if (item.is(Items.AIR)) {
            ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(0));
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            return;
        }
        if (itemHandler.getStackInSlot(0).isEmpty()) {
            itemHandler.setStackInSlot(0, item.split(1));
        } else {
            if (!item.is(itemHandler.getStackInSlot(0).getItem())) {
                ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(0));
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                itemHandler.setStackInSlot(0, item.split(1));
            }
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, MixingBowlBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
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
