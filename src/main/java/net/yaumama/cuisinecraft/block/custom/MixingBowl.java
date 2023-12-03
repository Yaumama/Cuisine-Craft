package net.yaumama.cuisinecraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yaumama.cuisinecraft.block.entity.FryingPanBlockEntity;
import net.yaumama.cuisinecraft.block.entity.MixingBowlBlockEntity;
import net.yaumama.cuisinecraft.block.entity.ModBlockEntities;
import net.yaumama.cuisinecraft.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class MixingBowl extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty fluid = IntegerProperty.create("fluid", 0, 3);

    public MixingBowl(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE =
            Block.box(0, 0, 0, 16, 10, 16);

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && hand.toString() == "MAIN_HAND") {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof MixingBowlBlockEntity mixingBowlBlockEntity) {
                if (!player.getMainHandItem().is(ModItems.WHISK.get())) {
                    if (player.getMainHandItem().is(Items.AIR)) {
                        level.setBlock(blockPos, state.setValue(fluid, 0), 3);
                    } else if (player.getMainHandItem().is(Items.BUCKET)) {
                        level.setBlock(blockPos, state.setValue(fluid, 0), 3);
                    } else if (player.getMainHandItem().is(Items.WATER_BUCKET)) {
                        level.setBlock(blockPos, state.setValue(fluid, 1), 3);
                    } else if (player.getMainHandItem().is(Items.MILK_BUCKET)) {
                        level.setBlock(blockPos, state.setValue(fluid, 2), 3);
                    } else if (player.getMainHandItem().is(Items.EGG)) {
                        level.setBlock(blockPos, state.setValue(fluid, 3), 3);
                    }
                    mixingBowlBlockEntity.placeFood(player, player.getMainHandItem(), blockPos);
                } else {
                    if (mixingBowlBlockEntity.mix(blockPos, player)) {
                        level.setBlock(blockPos, state.setValue(fluid, 0), 3);
                    }
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(fluid);
    }

    /* BLOCK ENTITY STUFF */


    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MixingBowlBlockEntity) {
                ((MixingBowlBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MixingBowlBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.MIXING_BOWL.get(),
                MixingBowlBlockEntity::tick);
    }
}
