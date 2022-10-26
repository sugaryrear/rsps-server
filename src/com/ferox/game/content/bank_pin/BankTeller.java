package com.ferox.game.content.bank_pin;

import com.ferox.game.content.bank_pin.dialogue.BankTellerDialogue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.MapObjects;
import com.ferox.game.world.position.Tile;

import java.util.function.Predicate;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class BankTeller {

    public static Runnable bankerDialogue(Player player, Npc npc) {
      //  player.message("here11");
       // player.message(npc.def().name);
        if (npc.def().name != null && (npc.def().name.contains("Banker")) && isNearBank(npc)) {
       // player.message("here");
            return () -> player.getDialogueManager().start(new BankTellerDialogue(), npc);
        }
        return null;
    }

    public static boolean isNearBank(Npc npc) {//banker_3888 stuff
        Predicate<GameObject> isBankBooth = obj -> "Bank booth".equals(obj.definition().name) || "exchange".contains(obj.definition().name) || obj.getId() == 10060
            || obj.getId() == 30389  || obj.getId() == 10061;

        Tile north = npc.tile().transform(0, 1);
        if (MapObjects.get(isBankBooth, north).isPresent()) {
            return true;
        }
        Tile west = npc.tile().transform(-1, 0);
        if (MapObjects.get(isBankBooth, west).isPresent()) {
            return true;
        }
        Tile east = npc.tile().transform(1, 0);
        if (MapObjects.get(isBankBooth, east).isPresent()) {
            return true;
        }
        Tile south = npc.tile().transform(0, -1);
        if (MapObjects.get(isBankBooth, south).isPresent()) {
            return true;
        }
        return false;
    }
    public static Tile correctTile(Npc npc) {//banker_3888 stuff
         Tile tile = new Tile(0,0);
        Predicate<GameObject> isBankBooth = obj -> "Bank booth".equals(obj.definition().name) || "exchange".contains(obj.definition().name) || obj.getId() == 10060
            || obj.getId() == 30389  || obj.getId() == 10061;

        Tile north = npc.tile().transform(0, 1);
        if (MapObjects.get(isBankBooth, north).isPresent()) {
            tile = new Tile(0,1);
        }
        Tile west = npc.tile().transform(-1, 0);
        if (MapObjects.get(isBankBooth, west).isPresent()) {
            tile = new Tile(-1,0);
        }
        Tile east = npc.tile().transform(1, 0);
        if (MapObjects.get(isBankBooth, east).isPresent()) {
            tile = new Tile(1,0);
        }
        Tile south = npc.tile().transform(0, -1);
        if (MapObjects.get(isBankBooth, south).isPresent()) {
            tile = new Tile(0,-1);
        }
        return tile;
    }
}
