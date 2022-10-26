package com.ferox.net.codec;

import com.ferox.GameServer;
import com.ferox.net.NetworkConstants;
import com.ferox.net.PlayerSession;
import com.ferox.net.packet.ClientToServerPackets;
import com.ferox.net.packet.Packet;
import com.ferox.net.security.IsaacRandom;
import com.ferox.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


/**
 * Decodes packets that are received from the player's channel.
 * These packets are received from the client C2S packets.
 * These are the packets from the Clients "PacketSender".
 * Those packets need to have their sizes written in the array below.
 *
 * Size calculations: byte is 1, short is 2, int is 4, long is 8, String is -1 and -3 is skip.
 *
 * @author Swiffy
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LogManager.getLogger(PacketDecoder.class);
    private int opcode = -1;
    private int size = -1;
    private final IsaacRandom random;


    public PacketDecoder(IsaacRandom random) {
        this.random = random;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        PlayerSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
        if (session == null || session.getPlayer() == null) {
            return;
        }
        buffer.markReaderIndex();
        int opcode = this.opcode;
        int size = this.size;

        if (opcode == -1) {
            if (buffer.isReadable()) {
                opcode = buffer.readUnsignedByte();
                opcode = opcode - random.nextInt() & 0xFF;
                size = ClientToServerPackets.PACKET_SIZES[opcode];
                this.opcode = opcode;
                this.size = size;
            } else {
                return;
            }
        }

        if (size == -1 || size == -2) {
            if (buffer.isReadable()) {
                size = buffer.readUnsignedByte() & 0xFF;
                this.size = size;
            } else {
                return;
            }
        }
        String packetName = ClientToServerPackets.PACKET_NAMES[opcode];
        if (ClientToServerPackets.PACKET_SIZES[opcode] == -3) {
            logger.error("Client to server packet opcode not whitelisted, skipping opcode "+ opcode +" with size "+ size  + " for " + session.getPlayer().toString() +", logging this player out. (If packet size not fixed use "+ (size > 512 ? -2 : -1) +", if the opcode has multiple packet size prints then it's not fixed packet size)");
            ctx.close();
            return;
        }
        if (buffer.isReadable(size)) {
            byte[] data = new byte[size];
            buffer.readBytes(data);
            this.opcode = -1;
            this.size = -1;
            final Packet packet = new Packet(opcode, Unpooled.copiedBuffer(data));
            session.setPacketCounter(session.getPacketCounter() + 1);
            //logger.info("Packet counter for player " + session.getPlayer().getUsername() + ": " + session.getPacketCounter()); //The packet counter seems to reset every cycle.
            if (session.getPacketCounter() < GameServer.properties().packetProcessLimit) { // This getPacketCounter is set to check < 100 on ikov.
                session.queuePacket(packet);
            }
            if (GameServer.properties().logSuccessfulPackets && opcode != 0) {
                //logger.info("Reading client to server packet " + opcode + " (" + packetName + ") with size " + size);
                Utils.sendDiscordInfoLog("Reading client to server packet " + opcode + " (" + packetName + ") with size " + size);
            }
        } else {
            //We don't really need to log this since packet fragmentation is a thing over the Internet.
            //We should just log this locally in development mode since packet fragmentation doesn't happen locally.
            if (GameServer.properties().logUnderflowPacketsProduction || !GameServer.properties().production) {
                logger.error("Client to server packet size mismatch with " + opcode + " (" + packetName + ")! Expected size " + size + " but received " + buffer.readableBytes() + " for " + session.getPlayer().toString() + ".");
            }
        }
    }
}
