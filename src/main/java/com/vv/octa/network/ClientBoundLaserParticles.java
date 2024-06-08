package com.vv.octa.network;

import com.vv.octa.Octa;
import com.vv.octa.init.OctaParticlesRegistry;
import com.vv.octa.util.OctaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundLaserParticles {
    private Vec3 pos1;
    private Vec3 pos2;

    public ClientBoundLaserParticles(Vec3 pos1, Vec3 pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public ClientBoundLaserParticles(FriendlyByteBuf buf) {
        pos1 = readVec3(buf);
        pos2 = readVec3(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        writeVec3(pos1, buf);
        writeVec3(pos2, buf);
    }

    public Vec3 readVec3(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3(x, y, z);
    }

    public void writeVec3(Vec3 vec3, FriendlyByteBuf buf) {
        buf.writeDouble(vec3.x);
        buf.writeDouble(vec3.y);
        buf.writeDouble(vec3.z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            handleClientBoundLaserParticles(pos1, pos2);
            Octa.debug("handle: succeed");
        });
        return true;
    }

    public static void handleClientBoundLaserParticles(Vec3 pos1, Vec3 pos2) {
        if (Minecraft.getInstance().player == null)
            return;
        var level = Minecraft.getInstance().player.level();
        Vec3 direction = pos2.subtract(pos1).scale(.1f);
        for (int i = 0; i < 40; i++) {
            Octa.debug("Particle: succeed");
            Vec3 scaledDirection = direction.scale(1 + OctaUtils.getRandomScaled(.35));
            Vec3 random = new Vec3(OctaUtils.getRandomScaled(.08f), OctaUtils.getRandomScaled(.08f), OctaUtils.getRandomScaled(.08f));
            level.addParticle(OctaParticlesRegistry.LASER_PARTICLE.get(), pos1.x, pos1.y, pos1.z, scaledDirection.x + random.x, scaledDirection.y + random.y, scaledDirection.z + random.z);
            level.addParticle(ParticleTypes.EXPLOSION, pos1.x, pos1.y, pos1.z, scaledDirection.x + random.x, scaledDirection.y + random.y, scaledDirection.z + random.z);
        }
    }
}
