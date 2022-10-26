package com.ferox.game.world.entity.combat.method.impl.npcs.inferno;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.position.Tile;

public class AncestralGlyph extends Npc {


    public AncestralGlyph(int id, Tile tile) {
        super(id, tile);
        respawns(false);


        putAttrib(AttributeKey.GLYPH_MOVE_LEFT, false);
        putAttrib(AttributeKey.GLYPH_MOVE_RIGHT, false);
    }
int yaxis = 5357;
    boolean canmoveleft = getAttribOr(AttributeKey.GLYPH_MOVE_LEFT, true);
    boolean canmoveright = getAttribOr(AttributeKey.GLYPH_MOVE_RIGHT, false);
    @Override
    public void sequence() {
//        boolean canmoveleft = getAttribOr(AttributeKey.GLYPH_MOVE_LEFT, true);
//        boolean canmoveright = getAttribOr(AttributeKey.GLYPH_MOVE_RIGHT, false);

        //System.out.println(canmoveleft+" "+canmoveright);
        super.sequence();


        if (dead()) {
            return;
        }

        if (getX() != 2257 && canmoveleft) { // From forward, start left

            getRouteFinder().routeAbsolute(2257, yaxis);
        } else  if(getX() == 2257) {
            canmoveleft = false;
            canmoveright = true;

            }

      else if(getX() != 2283 && canmoveright) {
            getRouteFinder().routeAbsolute(2283, yaxis);

        } else if(getX() == 2283){
            canmoveleft = true;
            canmoveright = false;

      }


    }

    @Override
    public void die() {

        super.die();
    }


}

