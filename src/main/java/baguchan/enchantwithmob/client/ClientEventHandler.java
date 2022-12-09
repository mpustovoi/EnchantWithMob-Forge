package baguchan.enchantwithmob.client;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;

import static baguchan.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;
import static baguchan.enchantwithmob.client.render.layer.EnchantLayer.enchantBeamSwirl;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);
	protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_GLINT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityGlintShader);
	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENERGY_SWIRL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEnergySwirlShader);
	protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
	protected static final RenderStateShard.TexturingStateShard ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> {
		setupGlintTexturing(0.16F);
	}, () -> {
		RenderSystem.resetTextureMatrix();
	});

	@SubscribeEvent
	public static void renderEnchantBeam(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
		PoseStack matrixStack = event.getPoseStack();
		MultiBufferSource bufferBuilder = event.getMultiBufferSource();
		float particalTick = event.getPartialTick();
		event.getEntity().getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
			if (cap.hasOwner()) {
				LivingEntity entity = cap.getEnchantOwner().get();
				if (entity != null) {
					renderBeam(cap, event.getEntity(), particalTick, matrixStack, bufferBuilder, entity, event.getRenderer());
				}
			}
		});

	}

	private static void renderBeam(@NotNull MobEnchantCapability cap, LivingEntity p_229118_1_, float p_229118_2_, PoseStack p_229118_3_, MultiBufferSource p_229118_4_, Entity p_229118_5_, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
		float tick = (float) p_229118_1_.tickCount + p_229118_2_;
		p_229118_3_.pushPose();
		Vec3 vector3d = p_229118_5_.getRopeHoldPosition(p_229118_2_);
		double d0 = (double) (Mth.lerp(p_229118_2_, p_229118_1_.yBodyRot, p_229118_1_.yBodyRotO) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
		Vector3d vector3d1 = new Vector3d(0.0D, (double) p_229118_1_.getEyeHeight() / 2, 0.0F);
		double d1 = Math.cos(d0) * vector3d1.z + Math.sin(d0) * vector3d1.x;
		double d2 = Math.sin(d0) * vector3d1.z - Math.cos(d0) * vector3d1.x;
		double d3 = Mth.lerp((double) p_229118_2_, p_229118_1_.xo, p_229118_1_.getX()) + d1;
		double d4 = Mth.lerp((double) p_229118_2_, p_229118_1_.yo, p_229118_1_.getY()) + vector3d1.y;
		double d5 = Mth.lerp((double) p_229118_2_, p_229118_1_.zo, p_229118_1_.getZ()) + d2;
		p_229118_3_.translate(d1, vector3d1.y, d2);
		float f = (float) (vector3d.x - d3);
		float f1 = (float) (vector3d.y - d4);
		float f2 = (float) (vector3d.z - d5);
		float f3 = 0.1F;
		VertexConsumer ivertexbuilder = p_229118_4_.getBuffer(enchantBeamSwirl(cap.isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANT_GLINT_LOCATION));
		Matrix4f matrix4f = p_229118_3_.last().pose();
		Matrix3f matrix3f = p_229118_3_.last().normal();
		float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.1F / 2.0F;
		float f5 = f2 * f4;
		float f6 = f * f4;
		BlockPos blockpos = new BlockPos(p_229118_1_.getEyePosition(p_229118_2_));
		BlockPos blockpos1 = new BlockPos(p_229118_5_.getEyePosition(p_229118_2_));
		int i = getBlockLightLevel(p_229118_1_, blockpos);
		int j = getBlockLightLevel(p_229118_5_, blockpos1);
		int k = p_229118_1_.level.getBrightness(LightLayer.SKY, blockpos);
		int l = p_229118_1_.level.getBrightness(LightLayer.SKY, blockpos1);
		renderSide(ivertexbuilder, matrix4f, matrix3f, f, f1, f2, i, j, k, l, 0.05F, 0.1F, f5, f6);
		renderSide(ivertexbuilder, matrix4f, matrix3f, f, f1, f2, i, j, k, l, 0.1F, 0.0F, f5, f6);
		p_229118_3_.popPose();
	}


	public static void renderSide(VertexConsumer p_229119_0_, Matrix4f p_229119_1_, Matrix3f matrix3f, float p_229119_2_, float p_229119_3_, float p_229119_4_, int p_229119_5_, int p_229119_6_, int p_229119_7_, int p_229119_8_, float p_229119_9_, float p_229119_10_, float p_229119_11_, float p_229119_12_) {
		int i = 24;

		for (int j = 0; j < 24; ++j) {
			float f = (float) j / 23.0F;
			int k = (int) Mth.lerp(f, (float) p_229119_5_, (float) p_229119_6_);
			int l = (int) Mth.lerp(f, (float) p_229119_7_, (float) p_229119_8_);
			int i1 = LightTexture.pack(k, l);
			addVertexPair(p_229119_0_, p_229119_1_, matrix3f, i1, p_229119_2_, p_229119_3_, p_229119_4_, p_229119_9_, p_229119_10_, 24, j, false, p_229119_11_, p_229119_12_);
			addVertexPair(p_229119_0_, p_229119_1_, matrix3f, i1, p_229119_2_, p_229119_3_, p_229119_4_, p_229119_9_, p_229119_10_, 24, j + 1, true, p_229119_11_, p_229119_12_);
		}

	}

	public static void addVertexPair(VertexConsumer p_229120_0_, Matrix4f p_229120_1_, Matrix3f matrix3f, int p_229120_2_, float p_229120_3_, float p_229120_4_, float p_229120_5_, float p_229120_6_, float p_229120_7_, int p_229120_8_, int p_229120_9_, boolean p_229120_10_, float p_229120_11_, float p_229120_12_) {
		float f = 0.5F;
		float f1 = 0.4F;
		float f2 = 0.3F;
		if (p_229120_9_ % 2 == 0) {
			f *= 0.7F;
			f1 *= 0.7F;
			f2 *= 0.7F;
		}

		float f3 = (float) p_229120_9_ / (float) p_229120_8_;
		float f4 = p_229120_3_ * f3;
		float f5 = p_229120_4_ > 0.0F ? p_229120_4_ * f3 * f3 : p_229120_4_ - p_229120_4_ * (1.0F - f3) * (1.0F - f3);
		float f6 = p_229120_5_ * f3;
		if (!p_229120_10_) {
			p_229120_0_.vertex(p_229120_1_, f4 + p_229120_11_, f5 + p_229120_6_ - p_229120_7_, f6 - p_229120_12_).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229120_2_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		}

		p_229120_0_.vertex(p_229120_1_, f4 - p_229120_11_, f5 + p_229120_7_, f6 + p_229120_12_).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229120_2_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		if (p_229120_10_) {
			p_229120_0_.vertex(p_229120_1_, f4 + p_229120_11_, f5 + p_229120_6_ - p_229120_7_, f6 - p_229120_12_).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229120_2_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		}

	}

	protected static int getSkyLightLevel(Entity p_239381_1_, BlockPos p_239381_2_) {
		return p_239381_1_.level.getBrightness(LightLayer.SKY, p_239381_2_);
	}

	protected static int getBlockLightLevel(Entity p_225624_1_, BlockPos p_225624_2_) {
		return p_225624_1_.isOnFire() ? 15 : p_225624_1_.level.getBrightness(LightLayer.BLOCK, p_225624_2_);
	}

	private static void setupGlintTexturing(float p_110187_) {
		long i = Util.getMillis() * 8L;
		float f = (float) (i % 110000L) / 110000.0F;
		float f1 = (float) (i % 30000L) / 30000.0F;
		Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
		matrix4f.rotateZ(0.17453292F).scale(p_110187_);
		RenderSystem.setTextureMatrix(matrix4f);
	}
}
