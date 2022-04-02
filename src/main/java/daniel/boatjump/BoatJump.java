package daniel.boatjump;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class BoatJump implements ClientModInitializer, DedicatedServerModInitializer {
    public static boolean CAN_BE_USED = false;
    private static final Identifier IS_MOD_ON_SERVER = new Identifier("boat_jump", "mod_on_server");

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.isInSingleplayer()) {
                CAN_BE_USED = true;
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(IS_MOD_ON_SERVER, (client, handler, buf, responseSender) -> {
           CAN_BE_USED = true;
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            CAN_BE_USED = false;
        });
    }

    @Override
    public void onInitializeServer() {
        ServerPlayConnectionEvents.INIT.register((handler, server) ->
                ServerPlayNetworking.send(handler.getPlayer(), IS_MOD_ON_SERVER, new PacketByteBuf(Unpooled.buffer()))
        );
    }
}
