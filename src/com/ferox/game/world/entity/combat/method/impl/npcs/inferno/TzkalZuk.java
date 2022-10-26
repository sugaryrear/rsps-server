package com.ferox.game.world.entity.combat.method.impl.npcs.inferno;

import com.ferox.game.content.minigames.impl.fight_caves.FightCavesMinigame;
import com.ferox.game.content.minigames.impl.inferno.InfernoMinigame;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Utils;

public class TzkalZuk extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = target.getAsPlayer();
       //if( player.getMinigame() instanceof FightCavesMinigame){
         InfernoMinigame minigame = (InfernoMinigame) player.getMinigame();
//target.message("your x: "+target.getX()+"    glyph x:  "+minigame.glyph.getX());
//if(minigame.glyph.getX() >= target.getX() && minigame.glyph.getX()+2 <= target.getX()){
         if(target.getX() >= minigame.glyph.getX()  && target.getX() < minigame.glyph.getX() + 3){
           //  target.message("behind glyph");
             return;
         }
        //}
        mob.animate(mob.attackAnimation());
        new Projectile(mob, target, 156, 32, 65, 30, 30, 0).sendProjectile();
       // target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 3;
        target.hit(mob,Utils.random(80),3);

      //  target.hit(mob, Utils.random(80));

    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getAsNpc().getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 50;
    }

    public void handleAfterHit(Hit hit) {
        //End gfx when target was hit or splash
        hit.getTarget().graphic(hit.getDamage() > 0 ? 157 : 85);
    }
}
