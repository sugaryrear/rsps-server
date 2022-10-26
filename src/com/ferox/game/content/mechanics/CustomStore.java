package com.ferox.game.content.mechanics;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.game.world.entity.AttributeKey.MAC_ADDRESS;

/**
 * @author Patrick van Elderen | May, 24, 2021, 12:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class CustomStore extends PacketInteraction {

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
//        if(button == 28061) {
//            player.getPacketSender().sendConfig(1125, 1);
//            player.getPacketSender().sendConfig(1126, 0);
//            player.getPacketSender().sendConfig(1127, 0);
//            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {//pk store first option
//                World.getWorld().shop(4).open(player);
//            }  else if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 2) {//donator store first option
//                World.getWorld().shop(43).open(player);
//            } else if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 3) {//general store first option
//                World.getWorld().shop(1).open(player);
//            }
//
//            return true;
//        }
//        if(button == 28062) {
//            player.getPacketSender().sendConfig(1125, 0);
//            player.getPacketSender().sendConfig(1126, 1);
//            player.getPacketSender().sendConfig(1127, 0);
//            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
//                World.getWorld().shop(5).open(player);
//            } else if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 2) {
//                World.getWorld().shop(44).open(player);
//            } else if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 3) {
//                World.getWorld().shop(9).open(player);
//            }
//            return true;
//        }
//        if(button == 28063) {
//            player.getPacketSender().sendConfig(1125, 0);
//            player.getPacketSender().sendConfig(1126, 0);
//            player.getPacketSender().sendConfig(1127, 1);
//            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
//                World.getWorld().shop(18).open(player);
//            } else if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 2) {
//                World.getWorld().shop(45).open(player);
//            } else if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 3) {
//                World.getWorld().shop(38).open(player);
//            }
//            return true;
//        }
        if(button == 28061) {
            player.getPacketSender().sendConfig(1125, 1);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(4).open(player);
            } else {
                World.getWorld().shop(43).open(player);
            }
            return true;
        }
        if(button == 28062) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 1);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(5).open(player);
            } else {
                World.getWorld().shop(44).open(player);
            }
            return true;
        }
        if(button == 28063) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 1);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(18).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28068) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 1);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(118).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28069) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 1);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(119).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28070) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 1);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(120).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28071) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 1);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(121).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28072) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 1);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(122).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28073) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 1);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(123).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28074) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 1);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(124).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28075) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 1);
            player.getPacketSender().sendConfig(1136, 0);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(125).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        if(button == 28077) {
            player.getPacketSender().sendConfig(1125, 0);
            player.getPacketSender().sendConfig(1126, 0);
            player.getPacketSender().sendConfig(1127, 0);
            player.getPacketSender().sendConfig(1128, 0);
            player.getPacketSender().sendConfig(1129, 0);
            player.getPacketSender().sendConfig(1130, 0);
            player.getPacketSender().sendConfig(1131, 0);
            player.getPacketSender().sendConfig(1132, 0);
            player.getPacketSender().sendConfig(1133, 0);
            player.getPacketSender().sendConfig(1134, 0);
            player.getPacketSender().sendConfig(1135, 0);
            player.getPacketSender().sendConfig(1136, 1);

            if(player.<Integer>getAttribOr(AttributeKey.CUSTOM_SHOP_ACTION,0) == 1) {
                World.getWorld().shop(126).open(player);
            } else {
                World.getWorld().shop(45).open(player);
            }
            return true;
        }
        return false;
    }

}
