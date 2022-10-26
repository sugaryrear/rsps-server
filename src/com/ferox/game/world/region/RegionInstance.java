package com.ferox.game.world.region;


import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;

import java.util.ArrayList;

/**
 * Handles a custom region instance for a player
 * @author Gabriel
 */
public class RegionInstance {

    public enum RegionInstanceType {
        BARROWS,
        GRAVEYARD,
        FIGHT_CAVE,
        WARRIORS_GUILD,
        NOMAD,
        RECIPE_FOR_DISASTER,
        CONSTRUCTION_HOUSE,
        SCORPIA,
        KRAKEN,
        CONSTRUCTION_DUNGEON,
        ZULRAH,
        TOURNAMENT,
        VLADIMIR, CRAZY_SCIENTIST_THIRD_AREA;
    }

    private Player owner;
    private RegionInstanceType type;
    private ArrayList<Npc> npcsList;
    private ArrayList<Player> playersList;

    public RegionInstance(Player p, RegionInstanceType type) {
        this.owner = p;
        this.type = type;
        this.npcsList = new ArrayList<Npc>();
        if (type == RegionInstanceType.CONSTRUCTION_HOUSE) {
            this.playersList = new ArrayList<Player>();
        }
    }

    public void destruct() {
        //        for (NPC n : npcsList) {
        //            if (n != null && n.getHitpoints() > 0 && World.getWorld().getNpcs().get(n.getIndex()) != null && n.getSpawnedfor () != null && n.getSpawnedfor ().getIndex() == owner.getIndex() && !n.isDying()) {
        //                if (n.getId() >= 4278 && n.getId() <= 4284) {
        //                    owner.getMinigameAttributes().getWarriorsGuildAttributes().setSpawnedArmour(false);
        //                }
        //                if (n.getId() >= 2024 && n.getId() <= 2034)
        //                    Barrows.killBarrowsNpc(owner, n, false);
        //                World.deregister(n);
        //            }
        //        }
        npcsList.clear();
        owner.setRegionInstance(null);
    }

    public void add(Mob c) {
        if (type == RegionInstanceType.CONSTRUCTION_HOUSE) {
            if (c.isPlayer()) {
                playersList.add((Player)c);
            } else if (c.isNpc()) {
                npcsList.add((Npc)c);
            }
        }
        if (c.getRegionInstance() == null || c.getRegionInstance() != this) {
            c.setRegionInstance(this);
        }
    }

    public void remove(Mob c) {
        if (type == RegionInstanceType.CONSTRUCTION_HOUSE) {
            if (c.isPlayer()) {
                playersList.remove((Player)c);
                if (owner == ((Player)c)) {
                    destruct();
                }
            } else if (c.isNpc()) {
                npcsList.remove((Npc)c);
            }
        }
        if (c.getRegionInstance() != null && c.getRegionInstance() == this) {
            c.setRegionInstance(null);
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public RegionInstanceType getType() {
        return type;
    }

    public ArrayList<Npc> getNpcsList() {
        return npcsList;
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    @Override
    public boolean equals(Object other) {
        return (RegionInstanceType)other == type;
    }
}

