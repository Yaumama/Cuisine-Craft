package net.yaumama.cuisinecraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.yaumama.cuisinecraft.block.custom.MixingBowl;
import net.yaumama.cuisinecraft.block.entity.MixingBowlBlockEntity;
import net.yaumama.cuisinecraft.utility.GeneralUtility;

public class MixingBowlBlockEntityRenderer implements BlockEntityRenderer<MixingBowlBlockEntity> {
    public MixingBowlBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MixingBowlBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStack[] items = pBlockEntity.getItems();

        for (int i = 0; i < items.length; i++) {
            if (!GeneralUtility.checkItemInArray(items[i].getItem(), pBlockEntity.getFluidsList()) && !GeneralUtility.checkItemInArray(items[i].getItem(), pBlockEntity.getDontRenderList())) {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

                ItemStack itemStack = items[i];
//            ItemStack itemStack = pBlockEntity.getRenderStack();
                pPoseStack.pushPose();
                pPoseStack.translate(0.5f, 0.1f, 0.5f);
                pPoseStack.scale(0.6f, 0.6f, 0.6f);
                pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90));

                switch (pBlockEntity.getBlockState().getValue(MixingBowl.FACING)) {
                    case NORTH -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(0));
                    case EAST -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(90));
                    case SOUTH -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
                    case WEST -> pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(270));
                }

                itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GUI, getLightLevel(pBlockEntity.getLevel(),
                                pBlockEntity.getBlockPos()),
                        OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 1);
                pPoseStack.popPose();
            }
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
