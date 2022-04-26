package net.voidz.dimension;

import net.adventurez.entity.VoidShadowEntity;
import net.adventurez.init.EntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.voidz.access.ServerPlayerAccess;
import net.voidz.init.BlockInit;

public class VoidPlacementHandler {

    public static final BlockPos VOID_SPAWN_POS = new BlockPos(0, 100, 0);

    public static TeleportTarget enter(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, final BlockPos portalPos) {
        ((ServerPlayerAccess) serverPlayerEntity).setVoidPortingBlockPos(portalPos);
        spawnVoidPlatform(serverWorld, VOID_SPAWN_POS.down());
        return new TeleportTarget(Vec3d.of(VOID_SPAWN_POS).add(0.5, 0, 0.5), Vec3d.ZERO, 0, 0);
    }

    public static TeleportTarget leave(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, final BlockPos portalPos) {
        return new TeleportTarget(Vec3d.of(((ServerPlayerAccess) serverPlayerEntity).getVoidPortingBlockPos()).add(0.5, 0, 0.5), Vec3d.ZERO, 0, 0);
    }

    private static void spawnVoidPlatform(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() != BlockInit.PORTAL_BLOCK) {
            BlockState platformBlock = BlockInit.VOID_BLOCK.getDefaultState();
            for (float u = 0.0F; u < Math.PI * 2; u += (float) Math.PI / 256F) {
                for (int i = 0; i < 40; i++) {
                    world.setBlockState(pos.add(Math.sin(u) * i, 0, Math.cos(u) * i), platformBlock);
                }
            }
            // Pretty good centered
            world.setBlockState(pos, BlockInit.PORTAL_BLOCK.getDefaultState());
            spawnVoidBoss((ServerWorld) world, pos.up());
        }
    }

    public static void spawnVoidBoss(ServerWorld world, BlockPos spawnPos) {
        VoidShadowEntity voidShadowEntity = (VoidShadowEntity) EntityInit.VOID_SHADOW_ENTITY.create((World) world);
        voidShadowEntity.setVoidMiddle(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        ((Entity) voidShadowEntity).refreshPositionAndAngles(spawnPos.up().north(40), 0.0F, 0.0F);
        ((ServerWorld) world).spawnEntity(voidShadowEntity);
    }
}
