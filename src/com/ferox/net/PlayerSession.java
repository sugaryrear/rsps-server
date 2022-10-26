package com.ferox.net;

import com.ferox.GameServer;
import com.ferox.game.TimesCycle;
import com.ferox.game.task.impl.PlayerTask;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.channel.DummyChannelHandlerContext;
import com.ferox.net.codec.PacketDecoder;
import com.ferox.net.login.LoginDetailsMessage;
import com.ferox.net.login.LoginResponsePacket;
import com.ferox.net.login.LoginResponses;
import com.ferox.net.packet.ClientToServerPackets;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketBuilder;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Utils;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

import com.ferox.game.service.LoginRequest;
import static com.ferox.game.world.entity.AttributeKey.MAC_ADDRESS;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author Lare96
 * @author Swiffy
 * @editor Professor Oak
 */
public class PlayerSession {

    private static final Logger logger = LogManager.getLogger(PlayerSession.class);

    /**
     * The queue of packets that will be handled on the next sequence.
     */
    private final LinkedList<Packet> packetsQueue = new LinkedList<>();

    /**
     * The channel that will manage the connection for this player.
     */
    private final Channel channel;

    /**
     * The player I/O operations will be executed for.
     */
    private final Player player;
    public ChannelHandlerContext ctx;

    /**
     * The amount of packets read this cycle.
     */
    private int packetCounter;

    /**
     * The current state of this I/O session.
     */
    private SessionState state = SessionState.CONNETED;

    public LoginDetailsMessage getMsg() {
        return msg;
    }

    private LoginDetailsMessage msg;

    /**
     * Creates a new {@link PlayerSession}.
     */
    public PlayerSession(Channel channel) {
        this.channel = channel;
        this.player = new Player(this);
    }

    /**
     * Attempts to finalize a player's login.
     *
     * @param msg The player's login information.
     */
    public void finalizeLogin(LoginDetailsMessage msg) {
        this.msg = msg;
        state = SessionState.LOGGING_IN;
        String username = msg.getUsername();
        String password = msg.getPassword();

        // Passed initial check, submit login request.
        player.setUsername(username).setLongUsername(Utils.stringToLong(username)).setHostAddress(msg.getHost());
        player.putAttrib(MAC_ADDRESS, msg.getMac());
        ctx = msg.getContext();
        World.getWorld().ls.enqueue(new LoginRequest(player, msg));
    }

    public ChannelFuture sendOkLogin(int response) {
        if (channel == null)
            return DummyChannelHandlerContext.DUMMY_FUTURE;
        ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(response, player.getPlayerRights()));
        if (response != LoginResponses.LOGIN_SUCCESSFUL) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        return future;
    }

    /**
     * Queues a recently decoded packet received from the channel.
     *
     * @param msg The packet that should be queued.
     */
    public void queuePacket(Packet msg) {

        //Are our queues already full?
        //A player may be packet flooding.
        //Simply don't add more packets to the queues.
        //This check seems to be a bit redundant thanks to our check of getPacketCounter < PACKET_PROCESS_LIMIT (it checks getPacketCounter < 100 on ikov) in ChannelEventHandler.
        //Even though this check is redundant, let's keep it just in case we need it in the future.
        int total_size = (packetsQueue.size());
        if (total_size >= GameServer.properties().packetProcessLimit) {
            logger.error("Packet limit reached for " + getPlayer().getUsername() + ", disconnecting this player.  (Packet limit: " + GameServer.properties().packetProcessLimit + " )");
            getPlayer().requestLogout();
            return;
        }

        //Add the packet to the queue.
        //If it's prioritized, add it to the prioritized queue instead.
        /*if (msg.getOpcode() == ClientToServerPackets.EQUIP_ITEM_OPCODE ||
            msg.getOpcode() == ClientToServerPackets.SPECIAL_ATTACK_OPCODE) {
            packetsQueue.addFirst(msg);
        } else {*/
            packetsQueue.add(msg);
        //}
    }

    private static final DecimalFormat df = new DecimalFormat("#.##");
    public static long threshold = 50_500_000;

    /**
     * Processes all of the queued messages from the {@link PacketDecoder} by
     * polling the internal queue, and then handling them via the handleInputMessage.
     * This method is called EACH GAME CYCLE.
     */
    public void handleQueuedPackets() {
        setPacketCounter(0);
        final int size = packetsQueue.size();
        for (int i = 0; i < GameServer.properties().packetProcessLimit; i++) {
            Packet packet = packetsQueue.poll();
            if (packet == null) {
                //Skip the null packet.
                //continue;
                //We may want to stop processing all packets for the player if they have a null one,
                //rather than just skipping the null packet, as this is faster.
                break;
            }
            try {
                PacketListener listener = ClientToServerPackets.PACKETS[packet.getOpcode()];

                if (packet.getOpcode() != 0) {
                    //logger.info("Packet " + packet.getOpcode() + " is: " + ClientToServerPackets.PACKETS[packet.getOpcode()].toString());
                }
                int finalI = i;
                Mob.accumulateRuntimeTo(() -> {
                    try {
                        listener.handleMessage(player, packet);
                    } catch (Throwable t) {
                        logger.catching(t);
                    }
                    if (player.getCurrentTask() != null) {
                        if (player.getCurrentTask() instanceof PlayerTask) {
                            PlayerTask task = (PlayerTask) player.getCurrentTask();
                            if (task.stops(listener.getClass())) {
                                task.stop();
                            }
                        }
                    }
                }, taken -> {
                    if (!TimesCycle.BENCHMARKING_ENABLED)
                        return;
                    if (taken.toNanos() > threshold) { // 0.5ms
                        final double taken2 = taken.toNanos() / 1_000_000.;
                        final String frm = df.format(taken2);
                        final String time = frm.equals("0") || frm.equals("0.0") ? taken2 + "" : frm;
                        final String name = ClientToServerPackets.PACKETS[packet.getOpcode()].getClass().getSimpleName();
                        final String data = Arrays.toString(packet.getBuffer().array()); // cant release before calling this
                        //packet.getBuffer().readerIndex(0);
                       // logger.warn(time + " ms to process packet " + finalI + "/" + size + " in queue id " + name + " by {}. data: {}", player, data);
                        //logger.trace(time + " ms to process packet " + finalI + "/" + size + " in queue id " + name + " by {}. data: {}", player, data);
                        Utils.sendDiscordInfoLog(time + " ms to process packet id " + name, "warning");
                    }
                });
            } catch (Throwable t) {
                logger.catching(t);
            } finally {
                packet.getBuffer().release();
            }
        }
    }

    public static void main(String[] args) {
       /* long ns = 600_000;
        double smallns = ns / 1_000_000.;
        System.out.println(df.format(smallns)+" from "+smallns+" and "+ns);*/
        final Packet packet = new Packet(-1, Unpooled.copiedBuffer(new byte[]{(byte)105, (byte)116, (byte)101, (byte)109, (byte)32, (byte)50, (byte)50, (byte)54, (byte)54, (byte)52, (byte)32, (byte)50, (byte)48, (byte)10}));
        String command = packet.readString();
        System.out.println("was "+ Arrays.toString(packet.getBuffer().array())+" -> "+command);
    }

    /**
     * Queues the {@code msg} for this session to be encoded and sent to the
     * client.
     *
     * @param builder the packet to queue.
     */
    public void write(PacketBuilder builder) {
        if (channel == null)
            return;
        if (!channel.isOpen())
            return;
        try {
            Packet packet = builder.toPacket();
            //logger.info("sending packet " +packet.getOpcode() + " with size " + packet.getSize());
            if (!channel.isActive()) {
                return;
            }
            channel.write(packet);
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    /**
     * Flushes this channel.
     */
    public void flush() {
        if (channel == null)
            return;
        if (!channel.isOpen())
            return;
        channel.flush();
    }

    /**
     * Gets the player I/O operations will be executed for.
     *
     * @return the player I/O operations.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current state of this I/O session.
     *
     * @return the current state.
     */
    public SessionState getState() {
        return state;
    }

    /**
     * Sets the value for {@link PlayerSession#state}.
     *
     * @param state the new value to set.
     */
    public void setState(SessionState state) {
        this.state = state;
    }

    @Nullable
    public Channel getChannel() {
        return channel;
    }

    public void setPacketCounter(int packetCounter) {
        this.packetCounter = packetCounter;
    }

    public int getPacketCounter() {
        return packetCounter;
    }

    @Override
    public String toString() {
        return "PlayerSession{" +
            "player=" + player +
            ", state=" + state +
            '}';
    }
}
