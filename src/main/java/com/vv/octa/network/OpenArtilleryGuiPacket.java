package com.vv.octa.network;

import com.vv.octa.Octa;
import com.vv.octa.modular.ModularLaserArtilleryItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import se.mickelus.mutil.network.AbstractPacket;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class OpenArtilleryGuiPacket extends AbstractPacket {

    public OpenArtilleryGuiPacket() {
    }

    public void toBytes(FriendlyByteBuf buffer) {
    }

    public void fromBytes(FriendlyByteBuf buffer) {
    }

    public void handle(Player player) {
        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ModularLaserArtilleryItem artilleryItem) {
            Octa.debug("Open Gui Step2");
            NetworkHooks.openScreen((ServerPlayer)player, artilleryItem);
        }
    }
}
