package com.ferox.game.world.entity.combat.magic;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.masks.animations.Animation;
import com.ferox.game.world.entity.masks.graphics.Graphic;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.MagicSpellbook;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.Tile;
import com.ferox.util.timers.TimerKey;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ferox.game.content.items.combine.ElderWand.*;
import static com.ferox.util.CustomItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD_C;
import static com.ferox.util.ItemIdentifiers.*;

public enum CombatSpells {

    WIND_STRIKE(new CombatSpell() {
        @Override
        public String name() {
            return "Wind strike";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 91, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(92, 100));
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }

        @Override
        public int baseMaxHit() {
            return 2;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(90, 100));
        }

        @Override
        public int baseExperience() {
            return 5;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE),
                Item.of(MIND_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return 1152;
        }
    }),
    CONFUSE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Confuse";
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {

        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(716));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 13;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(WATER_RUNE, 3),
                Item.of(EARTH_RUNE, 2),
                Item.of(BODY_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 3;
        }

        @Override
        public int spellId() {
            return 1153;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WATER_STRIKE(new CombatSpell() {
        @Override
        public String name() {
            return "Water strike";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 94, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(95, 100));
        }

        @Override
        public int baseMaxHit() {
            return 4;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(93, 100));
        }

        @Override
        public int baseExperience() {
            return 7;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(WATER_RUNE),
                Item.of(AIR_RUNE),
                Item.of(MIND_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 5;
        }

        @Override
        public int spellId() {
            return 1154;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    EARTH_STRIKE(new CombatSpell() {
        @Override
        public String name() {
            return "Earth strike";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 97, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(98, 100));
        }

        @Override
        public int baseMaxHit() {
            return 6;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(96, 100));
        }

        @Override
        public int baseExperience() {
            return 9;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE),
                Item.of(MIND_RUNE),
                Item.of(EARTH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 9;
        }

        @Override
        public int spellId() {
            return 1156;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WEAKEN(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Weaken";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(716));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {

        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 21;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(WATER_RUNE, 3),
                Item.of(EARTH_RUNE, 2),
                Item.of(BODY_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 11;
        }

        @Override
        public int spellId() {
            return 1157;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    FIRE_STRIKE(new CombatSpell() {
        @Override
        public String name() {
            return "Fire strike";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 100, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(101, 100));
        }

        @Override
        public int baseMaxHit() {
            return 8;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(99, 100));
        }

        @Override
        public int baseExperience() {
            return 11;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(
                    Item.of(AIR_RUNE),
                    Item.of(MIND_RUNE));
            }
            return List.of(
                Item.of(AIR_RUNE),
                Item.of(MIND_RUNE),
                Item.of(FIRE_RUNE, 3)
            );
        }

        @Override
        public int levelRequired() {
            return 13;
        }

        @Override
        public int spellId() {
            return 1158;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WIND_BOLT(new CombatSpell() {
        @Override
        public String name() {
            return "Wind bolt";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 118, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(119, 100));
        }

        @Override
        public int baseMaxHit() {
            return 9;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(117, 100));
        }

        @Override
        public int baseExperience() {
            return 13;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(CHAOS_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 17;
        }

        @Override
        public int spellId() {
            return 1160;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    CURSE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Curse";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 29;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(WATER_RUNE, 2),
                Item.of(EARTH_RUNE, 3),
                Item.of(BODY_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 19;
        }

        @Override
        public int spellId() {
            return 1161;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    BIND(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Bind";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 178, 0, 20, 43, 31, 0));
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(8, cast);
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(181, 100));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(177, 100));
        }

        @Override
        public int baseExperience() {
            return 30;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_BIND_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_BIND_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 3),
                Item.of(EARTH_RUNE, 3),
                Item.of(NATURE_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 20;
        }

        @Override
        public int spellId() {
            return 1572;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WATER_BOLT(new CombatSpell() {
        @Override
        public String name() {
            return "Water bolt";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 121, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(122, 100));
        }

        @Override
        public int baseMaxHit() {
            return 10;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(120, 100));
        }

        @Override
        public int baseExperience() {
            return 16;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(CHAOS_RUNE),
                Item.of(WATER_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 23;
        }

        @Override
        public int spellId() {
            return 1163;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    EARTH_BOLT(new CombatSpell() {
        @Override
        public String name() {
            return "Earth bolt";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 124, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(125, 100));
        }

        @Override
        public int baseMaxHit() {
            return 11;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(123, 100));
        }

        @Override
        public int baseExperience() {
            return 19;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(CHAOS_RUNE),
                Item.of(EARTH_RUNE, 3)
            );
        }

        @Override
        public int levelRequired() {
            return 29;
        }

        @Override
        public int spellId() {
            return 1166;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    FIRE_BOLT(new CombatSpell() {
        @Override
        public String name() {
            return "Fire bolt";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 127, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(128, 100));
        }

        @Override
        public int baseMaxHit() {
            return 12;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(126, 100));
        }

        @Override
        public int baseExperience() {
            return 22;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(
                    Item.of(AIR_RUNE, 3),
                    Item.of(CHAOS_RUNE));
            }
            return List.of(
                Item.of(AIR_RUNE, 3),
                Item.of(CHAOS_RUNE),
                Item.of(FIRE_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 35;
        }

        @Override
        public int spellId() {
            return 1169;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    CRUMBLE_UNDEAD(new CombatSpell() {
        @Override
        public String name() {
            return "Crumble Undead";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(724));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 146, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(147));
        }

        @Override
        public int baseMaxHit() {
            return 15;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(145, 100));
        }

        @Override
        public int baseExperience() {
            return 24;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(CHAOS_RUNE),
                Item.of(EARTH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 39;
        }

        @Override
        public int spellId() {
            return 1171;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WIND_BLAST(new CombatSpell() {
        @Override
        public String name() {
            return "Wind blast";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 133, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(134, 100));
        }

        @Override
        public int baseMaxHit() {
            return 13;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(132, 100));
        }

        @Override
        public int baseExperience() {
            return 25;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 3),
                Item.of(DEATH_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 41;
        }

        @Override
        public int spellId() {
            return 1172;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WATER_BLAST(new CombatSpell() {
        @Override
        public String name() {
            return "Water blast";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 136, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(137, 100));
        }

        @Override
        public int baseMaxHit() {
            return 14;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(135, 100));
        }

        @Override
        public int baseExperience() {
            return 28;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(WATER_RUNE, 3),
                Item.of(AIR_RUNE, 3),
                Item.of(DEATH_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 47;
        }

        @Override
        public int spellId() {
            return 1175;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    IBAN_BLAST(new CombatSpell() {
        @Override
        public String name() {
            return "Iban blast";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(708));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 88, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(89));
        }

        @Override
        public int baseMaxHit() {
            return 25;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(87, 100));
        }

        @Override
        public int baseExperience() {
            return 30;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of(
                Item.of(IBANS_STAFF),
                Item.of(IBANS_STAFF_U)
            );
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(DEATH_RUNE));
            }
            return List.of(
                Item.of(DEATH_RUNE),
                Item.of(FIRE_RUNE, 5)
            );
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 1539;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    SNARE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Snare";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 178, 0, 20, 43, 31, 0));
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return 2;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(16, cast);
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(180, 100));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(177, 100));
        }

        @Override
        public int baseExperience() {
            return 60;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_SNARE_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_SNARE_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 3),
                Item.of(EARTH_RUNE, 4),
                Item.of(NATURE_RUNE, 3)
            );
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 1582;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    MAGIC_DART(new CombatSpell() {
        @Override
        public String name() {
            return "Magic Dart";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1576));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 328, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(329));
        }

        @Override
        public int baseMaxHit() {
            return 19;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(327, 100));
        }

        @Override
        public int baseExperience() {
            return 30;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of(
                Item.of(SLAYERS_STAFF),
                Item.of(STAFF_OF_THE_DEAD),
                Item.of(TOXIC_STAFF_OF_THE_DEAD),
                Item.of(TOXIC_STAFF_OF_THE_DEAD_C)
            );
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(MIND_RUNE, 4),
                Item.of(DEATH_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 12037;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    EARTH_BLAST(new CombatSpell() {
        @Override
        public String name() {
            return "Earth blast";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 139, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(140, 100));
        }

        @Override
        public int baseMaxHit() {
            return 15;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(138, 100));
        }

        @Override
        public int baseExperience() {
            return 31;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 3),
                Item.of(DEATH_RUNE),
                Item.of(EARTH_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 53;
        }

        @Override
        public int spellId() {
            return 1177;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    FIRE_BLAST(new CombatSpell() {
        @Override
        public String name() {
            return "Fire blast";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1162));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 130, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(131, 100));
        }

        @Override
        public int baseMaxHit() {
            return 16;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(129, 100));
        }

        @Override
        public int baseExperience() {
            return 34;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(AIR_RUNE, 4), Item.of(DEATH_RUNE));
            }
            return List.of(
                Item.of(AIR_RUNE, 4),
                Item.of(DEATH_RUNE),
                Item.of(FIRE_RUNE, 5)
            );
        }

        @Override
        public int levelRequired() {
            return 59;
        }

        @Override
        public int spellId() {
            return 1181;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    SARADOMIN_STRIKE(new CombatSpell() {
        @Override
        public String name() {
            return "Saradomin Strike";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(811));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(76));
        }

        @Override
        public int baseMaxHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 35;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of(Item.of(SARADOMIN_STAFF), Item.of(STAFF_OF_LIGHT));
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(AIR_RUNE, 4), Item.of(BLOOD_RUNE, 2));
            }
            return List.of(
                Item.of(AIR_RUNE, 4),
                Item.of(BLOOD_RUNE, 2),
                Item.of(FIRE_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 60;
        }

        @Override
        public int spellId() {
            return 1190;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    CLAWS_OF_GUTHIX(new CombatSpell() {
        @Override
        public String name() {
            return "Claws of Guthix";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(811));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(77));
        }

        @Override
        public int baseMaxHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 35;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of(Item.of(GUTHIX_STAFF), Item.of(STAFF_OF_BALANCE));
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(AIR_RUNE, 4), Item.of(BLOOD_RUNE, 2));
            }
            return List.of(
                Item.of(AIR_RUNE, 4),
                Item.of(BLOOD_RUNE, 2),
                Item.of(FIRE_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 60;
        }

        @Override
        public int spellId() {
            return 1191;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    FLAMES_OF_ZAMORAK(new CombatSpell() {
        @Override
        public String name() {
            return "Flames of Zamorak";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(811));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(78));
        }

        @Override
        public int baseMaxHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 35;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of(
                Item.of(ZAMORAK_STAFF),
                Item.of(STAFF_OF_THE_DEAD),
                Item.of(TOXIC_STAFF_OF_THE_DEAD),
                Item.of(TOXIC_STAFF_OF_THE_DEAD_C)
            );
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(AIR_RUNE, 4), Item.of(BLOOD_RUNE, 2));
            }
            return List.of(
                Item.of(AIR_RUNE, 4),
                Item.of(BLOOD_RUNE, 2),
                Item.of(FIRE_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 60;
        }

        @Override
        public int spellId() {
            return 1192;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WIND_WAVE(new CombatSpell() {
        @Override
        public String name() {
            return "Wind wave";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 159, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(160, 80));
        }

        @Override
        public int baseMaxHit() {
            return 17;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(158, 100));
        }

        @Override
        public int baseExperience() {
            return 36;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 5),
                Item.of(BLOOD_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 62;
        }

        @Override
        public int spellId() {
            return 1183;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WATER_WAVE(new CombatSpell() {
        @Override
        public String name() {
            return "Water wave";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 162, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(163, 80,10));
        }

        @Override
        public int baseMaxHit() {
            return 18;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(161, 100));
        }

        @Override
        public int baseExperience() {
            return 37;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 5),
                Item.of(BLOOD_RUNE),
                Item.of(WATER_RUNE, 7)
            );
        }

        @Override
        public int levelRequired() {
            return 65;
        }

        @Override
        public int spellId() {
            return 1185;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    VULNERABILITY(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Vulnerability";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(729));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            // Dealth elsewhere
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 76;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(EARTH_RUNE, 5),
                Item.of(WATER_RUNE, 5),
                Item.of(SOUL_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 66;
        }

        @Override
        public int spellId() {
            return 1542;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    EARTH_WAVE(new CombatSpell() {
        @Override
        public String name() {
            return "Earth wave";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 165, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(166, 80));
        }

        @Override
        public int baseMaxHit() {
            return 19;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(164, 100));
        }

        @Override
        public int baseExperience() {
            return 40;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 5),
                Item.of(BLOOD_RUNE),
                Item.of(EARTH_RUNE, 7)
            );
        }

        @Override
        public int levelRequired() {
            return 70;
        }

        @Override
        public int spellId() {
            return 1188;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    ENFEEBLE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Enfeeble";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(729));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 83;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(EARTH_RUNE, 8),
                Item.of(WATER_RUNE, 8),
                Item.of(SOUL_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 73;
        }

        @Override
        public int spellId() {
            return 1543;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    FIRE_WAVE(new CombatSpell() {
        @Override
        public String name() {
            return "Fire wave";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 156, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(157, 80));
        }

        @Override
        public int baseMaxHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(155,100));
        }

        @Override
        public int baseExperience() {
            return 42;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(AIR_RUNE, 5), Item.of(BLOOD_RUNE));
            }
            return List.of(
                Item.of(AIR_RUNE, 5),
                Item.of(BLOOD_RUNE),
                Item.of(FIRE_RUNE, 7)
            );
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 1189;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    ENTANGLE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Entangle";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 178, 0, 20, 43, 31, 0));
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return 5;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(25, cast); // 15 second freeze timer
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(179, 80));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(177, 80));
        }

        @Override
        public int baseExperience() {
            return 91;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_ENTANGLE_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_ENTANGLE_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 5),
                Item.of(EARTH_RUNE, 5),
                Item.of(NATURE_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 79;
        }

        @Override
        public int spellId() {
            return 1592;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    STUN(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Stun";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(729));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (castOn.isPlayer()) {
                Player player = (Player) castOn;

                if (player.skills().level(Skills.ATTACK) < player.skills().xpLevel(Skills.ATTACK)) {
                    if (cast.isPlayer()) {
                        ((Player) cast).getPacketSender().sendMessage(
                            "The spell has no effect because the player is already weakened.");
                    }
                    return;
                }

                int decrease = (int) (0.10 * (player.skills().level(Skills.ATTACK)));
                player.skills().setLevel(Skills.ATTACK, player.skills().level(Skills.ATTACK) - decrease);
                player.skills().update(Skills.ATTACK);
                player.message("You feel slightly weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 90;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(EARTH_RUNE, 12),
                Item.of(WATER_RUNE, 12),
                Item.of(AIR_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 80;
        }

        @Override
        public int spellId() {
            return 1562;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    TELEBLOCK(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Teleblock";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1819));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
           return Optional.empty();
        }

        @Override
        public boolean canCast(Player player, Mob target, boolean delete) {
            if (target.getTimers().has(TimerKey.TELEBLOCK) || target.getTimers().has(TimerKey.SPECIAL_TELEBLOCK) || target.getTimers().has(TimerKey.TELEBLOCK_IMMUNITY)) {
                player.message("That player is already being affected by this spell.");
                player.getCombat().reset();
                player.getCombat().setCastSpell(null);
                return false;
            }
            return super.canCast(player, target, delete);
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            //Dealt elsewhere
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public int baseMaxHit() {
            return -1;
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 65;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_TELEPORT_SPELL_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_TELEPORT_SPELL_SACK, 1));
            }
            return List.of(
                Item.of(LAW_RUNE),
                Item.of(CHAOS_RUNE),
                Item.of(DEATH_RUNE)
            );
        }

        @Override
        public int levelRequired() {
            return 85;
        }

        @Override
        public int spellId() {
            return 12445;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    AIR_SURGE(new CombatSpell() {
        @Override
        public String name() {
            return "Air surge";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(7855));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 1456, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1457, 80));
        }

        @Override
        public int baseMaxHit() {
            return 21;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(1455, 100));
        }

        @Override
        public int baseExperience() {
            return 44;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 7),
                Item.of(WRATH_RUNE, 1)
            );
        }

        @Override
        public int levelRequired() {
            return 81;
        }

        @Override
        public int spellId() {
            return 22708;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    WATER_SURGE(new CombatSpell() {
        @Override
        public String name() {
            return "Water surge";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(7855));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 1459, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1460, 80));
        }

        @Override
        public int baseMaxHit() {
            return 22;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(1458, 100));
        }

        @Override
        public int baseExperience() {
            return 46;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(WATER_RUNE, 10),
                Item.of(AIR_RUNE, 7),
                Item.of(WRATH_RUNE, 1)
            );
        }

        @Override
        public int levelRequired() {
            return 85;
        }

        @Override
        public int spellId() {
            return 22658;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    EARTH_SURGE(new CombatSpell() {
        @Override
        public String name() {
            return "Earth surge";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(7855));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 1462, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1463, 80));
        }

        @Override
        public int baseMaxHit() {
            return 23;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(1461, 100));
        }

        @Override
        public int baseExperience() {
            return 48;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(EARTH_RUNE, 10),
                Item.of(AIR_RUNE, 7),
                Item.of(WRATH_RUNE, 1)
            );
        }

        @Override
        public int levelRequired() {
            return 90;
        }

        @Override
        public int spellId() {
            return 22628;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    FIRE_SURGE(new CombatSpell() {
        @Override
        public String name() {
            return "Fire surge";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(7855));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 1465, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1466, 80));
        }

        @Override
        public int baseMaxHit() {
            return 24;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(1464, 100));
        }

        @Override
        public int baseExperience() {
            return 50;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(FIRE_RUNE, 10), Item.of(AIR_RUNE, 7));
            }
            return List.of(
                Item.of(FIRE_RUNE, 10),
                Item.of(AIR_RUNE, 7),
                Item.of(WRATH_RUNE, 1)
            );
        }

        @Override
        public int levelRequired() {
            return 95;
        }

        @Override
        public int spellId() {
            return 22608;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    SMOKE_RUSH(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Smoke rush";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (World.getWorld().rollDie(100, 25)) {
                castOn.poison(2);
            }
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 384, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(385));
        }

        @Override
        public int baseMaxHit() {
            return 13;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 30;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(Item.of(AIR_RUNE), Item.of(CHAOS_RUNE, 2), Item.of(DEATH_RUNE, 2));
            }
            return List.of(
                Item.of(AIR_RUNE),
                Item.of(FIRE_RUNE),
                Item.of(CHAOS_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 12939;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SHADOW_RUSH(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Shadow rush";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (castOn.isPlayer()) {
                Player player = (Player) castOn;

                if (player.skills().level(Skills.ATTACK) < player.skills().xpLevel(Skills.ATTACK)) {
                    return;
                }

                int decrease = (int) (0.1 * (player.skills().level(Skills.ATTACK)));
                player.skills().setLevel(Skills.ATTACK, player.skills().level(Skills.ATTACK) - decrease);
                player.skills().update(Skills.ATTACK);
            }
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 378, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(379));
        }

        @Override
        public int baseMaxHit() {
            return 14;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 31;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE),
                Item.of(SOUL_RUNE),
                Item.of(CHAOS_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 52;
        }

        @Override
        public int spellId() {
            return 12987;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    BLOOD_RUSH(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Blood rush";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (hit.isAccurate()) {
                cast.heal(hit.getDamage() / 4); // Heal for 25% with blood barr
            }
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 372, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(373));
        }

        @Override
        public int baseMaxHit() {
            return 15;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 33;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(BLOOD_RUNE),
                Item.of(CHAOS_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 56;
        }

        @Override
        public int spellId() {
            return 12901;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    ICE_RUSH(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Ice rush";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(8, cast);
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 360, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(361));
        }

        @Override
        public int baseMaxHit() {
            return 18;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 34;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_ANCIENT_ICE_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_ANCIENT_ICE_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 2),
                Item.of(CHAOS_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 58;
        }

        @Override
        public int spellId() {
            return 12861;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SMOKE_BURST(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Smoke burst";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (World.getWorld().rollDie(100, 25)) {
                castOn.poison(2);
            }
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(389));
        }

        @Override
        public int baseMaxHit() {
            return 13;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 36;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(
                    Item.of(AIR_RUNE, 2),
                    Item.of(CHAOS_RUNE, 4),
                    Item.of(DEATH_RUNE, 2)
                );
            }
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(FIRE_RUNE, 2),
                Item.of(CHAOS_RUNE, 4),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 62;
        }

        @Override
        public int spellId() {
            return 12963;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SHADOW_BURST(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Shadow burst";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (castOn.isPlayer()) {
                Player player = (Player) castOn;

                if (player.skills().level(Skills.ATTACK) < player.skills().xpLevel(Skills.ATTACK)) {
                    return;
                }

                int decrease = (int) (0.1 * (player.skills().level(Skills.ATTACK)));
                player.skills().setLevel(Skills.ATTACK, player.skills().level(Skills.ATTACK) - decrease);
                player.skills().update(Skills.ATTACK);
            }
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(382));
        }

        @Override
        public int baseMaxHit() {
            return 18;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 37;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE),
                Item.of(SOUL_RUNE, 2),
                Item.of(CHAOS_RUNE, 4),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 64;
        }

        @Override
        public int spellId() {
            return 13011;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    BLOOD_BURST(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Blood burst";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (hit.isAccurate()) {
                cast.heal(hit.getDamage() / 4); // Heal for 25% with blood barr
            }
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(376));
        }

        @Override
        public int baseMaxHit() {
            return 21;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 39;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(BLOOD_RUNE, 2),
                Item.of(CHAOS_RUNE, 4),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 68;
        }

        @Override
        public int spellId() {
            return 12919;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    ICE_BURST(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Ice burst";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(16, cast);
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(363));
        }

        @Override
        public int baseMaxHit() {
            return 22;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 40;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_ANCIENT_ICE_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_ANCIENT_ICE_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 4),
                Item.of(CHAOS_RUNE, 4),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 70;
        }

        @Override
        public int spellId() {
            return 12881;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SMOKE_BLITZ(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Smoke blitz";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (World.getWorld().rollDie(100, 25)) {
                castOn.poison(4);
            }
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 386, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(387));
        }

        @Override
        public int baseMaxHit() {
            return 23;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 42;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(
                    Item.of(AIR_RUNE, 2),
                    Item.of(BLOOD_RUNE, 2),
                    Item.of(DEATH_RUNE, 2)
                );
            }
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(FIRE_RUNE, 2),
                Item.of(BLOOD_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 74;
        }

        @Override
        public int spellId() {
            return 12951;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SHADOW_BLITZ(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Shadow blitz";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (castOn.isPlayer()) {
                Player player = (Player) castOn;

                if (player.skills().level(Skills.ATTACK) < player.skills().xpLevel(Skills.ATTACK)) {
                    return;
                }

                int decrease = (int) (0.15 * (player.skills().level(Skills.ATTACK)));
                player.skills().setLevel(Skills.ATTACK, player.skills().level(Skills.ATTACK) - decrease);
                player.skills().update(Skills.ATTACK);
            }
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 380, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(381));
        }

        @Override
        public int baseMaxHit() {
            return 24;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 43;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 2),
                Item.of(SOUL_RUNE, 2),
                Item.of(BLOOD_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 76;
        }

        @Override
        public int spellId() {
            return 12999;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    BLOOD_BLITZ(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Blood blitz";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (hit.isAccurate()) {
                cast.heal(hit.getDamage() / 4); // Heal for 25% with blood barr
            }
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, 374, 0, 20, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(375));
        }

        @Override
        public int baseMaxHit() {
            return 25;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 45;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(BLOOD_RUNE, 4),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 80;
        }

        @Override
        public int spellId() {
            return 12911;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    ICE_BLITZ(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Ice blitz";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(25, cast); // 15 second freeze timer
        }

        @Override
        public int spellRadius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(367));
        }

        @Override
        public int baseMaxHit() {
            return 26;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(366, 100));
        }

        @Override
        public int baseExperience() {
            return 46;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_ANCIENT_ICE_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_ANCIENT_ICE_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 3),
                Item.of(BLOOD_RUNE, 2),
                Item.of(DEATH_RUNE, 2)
            );
        }

        @Override
        public int levelRequired() {
            return 82;
        }

        @Override
        public int spellId() {
            return 12871;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SMOKE_BARRAGE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Smoke barrage";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (World.getWorld().rollDie(100, 25)) {
                castOn.poison(4);
            }
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(391));
        }

        @Override
        public int baseMaxHit() {
            return 27;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 48;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            if (hasTomeOfFire) {
                return List.of(
                    Item.of(AIR_RUNE, 4),
                    Item.of(BLOOD_RUNE, 2),
                    Item.of(DEATH_RUNE, 4)
                );
            }
            return List.of(
                Item.of(AIR_RUNE, 4),
                Item.of(FIRE_RUNE, 4),
                Item.of(BLOOD_RUNE, 2),
                Item.of(DEATH_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 86;
        }

        @Override
        public int spellId() {
            return 12975;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    SHADOW_BARRAGE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Shadow barrage";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (castOn.isPlayer()) {
                Player player = (Player) castOn;

                if (player.skills().level(Skills.ATTACK) < player.skills().xpLevel(Skills.ATTACK)) {
                    return;
                }

                int decrease = (int) (0.15 * (player.skills().level(Skills.ATTACK)));
                player.skills().setLevel(Skills.ATTACK, player.skills().level(Skills.ATTACK) - decrease);
                player.skills().update(Skills.ATTACK);
            }
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(383));
        }

        @Override
        public int baseMaxHit() {
            return 28;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 49;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(AIR_RUNE, 4),
                Item.of(SOUL_RUNE, 3),
                Item.of(BLOOD_RUNE, 2),
                Item.of(DEATH_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 88;
        }

        @Override
        public int spellId() {
            return 13023;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    BLOOD_BARRAGE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Blood barrage";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            if (hit.isAccurate()) {
                cast.heal(hit.getDamage() / 4); // Heal for 25% with blood barr
            }
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(377));
        }

        @Override
        public int baseMaxHit() {
            return 29;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 51;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of(
                Item.of(DEATH_RUNE, 4),
                Item.of(SOUL_RUNE),
                Item.of(BLOOD_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 92;
        }

        @Override
        public int spellId() {
            return 12929;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    ICE_BARRAGE(new CombatEffectSpell() {
        @Override
        public String name() {
            return "Ice barrage";
        }

        @Override
        public void spellEffect(Mob cast, Mob castOn, Hit hit) {
            castOn.freeze(33, cast);
        }

        @Override
        public int spellRadius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(369));
        }

        @Override
        public int baseMaxHit() {
            return 30;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 52;
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            boolean spellSack = player.inventory().contains(BLIGHTED_ANCIENT_ICE_SACK);
            if (spellSack) {
                return List.of(Item.of(BLIGHTED_ANCIENT_ICE_SACK, 1));
            }
            return List.of(
                Item.of(WATER_RUNE, 6),
                Item.of(BLOOD_RUNE, 2),
                Item.of(DEATH_RUNE, 4)
            );
        }

        @Override
        public int levelRequired() {
            return 94;
        }

        @Override
        public int spellId() {
            return 12891;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.ANCIENT;
        }
    }),
    TRIDENT_OF_THE_SEAS(new CombatSpell() {
        @Override
        public String name() {
            return "Trident of the seas";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            Tile targPos = castOn.tile();
            int dist = cast.tile().distance(targPos);
            return Optional.of(new Projectile(cast, castOn,1252, (9 * dist), 45, 10, 0, 0, 10, 64));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1253, 90));
        }

        @Override
        public int baseMaxHit() {
            return 28;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(1251, 80));
        }

        @Override
        public int baseExperience() {
            return 50;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 1;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    TRIDENT_OF_THE_SWAMP(new CombatSpell() {
        @Override
        public String name() {
            return "Trident of the swamp";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            var tile = cast.tile();
            var tileDist = tile.distance(castOn.tile());
            return Optional.of(new Projectile(cast, castOn, 1040, (9 * tileDist), 45, 10, 0, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1042));
        }

        @Override
        public int baseMaxHit() {
            return 33;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(665, 80));
        }

        @Override
        public int baseExperience() {
            return 50;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 2;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    SANGUINESTI_STAFF(new CombatSpell() {
        @Override
        public String name() {
            return "Sanguinesti spell";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1167));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            var tile = cast.tile();
            var tileDist = tile.distance(castOn.tile());
            return Optional.of(new Projectile(cast, castOn, 1539, (9 * tileDist), 45, 10, 0, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(1541, 90));
        }

        @Override
        public int baseMaxHit() {
            return 34;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(1540, 80));
        }

        @Override
        public int baseExperience() {
            return 2;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 3;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    VOLATILE_NIGHTMARE_STAFF(new CombatSpell() {
        @Override
        public String name() {
            return "Volatile spell";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.empty();
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return 0;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 2;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 4;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    ELDRITCH_NIGHTMARE_STAFF(new CombatSpell() {
        @Override
        public String name() {
            return "Eldritch spell";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.empty();
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return 50;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return 2;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 5;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    CRUCIATUS_CURSE(new CombatSpell() {
        @Override
        public String name() {
            return "Cruciatus Curse";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(401));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, CRUCIATUS_CURSE_PROJECTILE, 0, 30, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return CRUCIATUS_CURSE_SPELL_BASE_MAX_HIT;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return BASE_EXP;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return CRUCIATUS_CURSE_SPELL;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    PETRIFICUS_TOTALUS(new CombatSpell() {
        @Override
        public String name() {
            return "Petrificus Totalus";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(401));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, PETRIFICUS_TOTALUS_PROJECTILE, 0, 30, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return PETRIFICUS_TOTALUS_SPELL_BASE_MAX_HIT;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return BASE_EXP;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return PETRIFICUS_TOTALUS_SPELL;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    AVADA_KEDAVRA(new CombatSpell() {
        @Override
        public String name() {
            return "Avada Kedavra";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(401));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, AVADA_KEDAVRA_PROJECTILE, 0, 30, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return AVADA_KEDAVRA_BASE_MAX_HIT;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return BASE_EXP;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return AVADA_KEDAVRA_SPELL;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    EXPELLIARMUS(new CombatSpell() {
        @Override
        public String name() {
            return "Expelliarmus";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(401));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, EXPELLIARMUS_PROJECTILE, 0, 30, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return EXPELLIARMUS_SPELL_BASE_MAX_HIT;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return BASE_EXP;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return EXPELLIARMUS_SPELL;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    }),
    SECTUMSEMPRA(new CombatSpell() {
        @Override
        public String name() {
            return "Sectumsempra";
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(401));
        }

        @Override
        public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
            return Optional.of(new Projectile(cast, castOn, SECTUMSEMPRA_PROJECTILE, 0, 30, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseMaxHit() {
            return SECTUMSEMPRA_SPELL_BASE_MAX_HIT;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public int baseExperience() {
            return BASE_EXP;
        }

        @Override
        public List<Item> equipmentRequired(Player player) {
            return List.of();
        }

        @Override
        public List<Item> itemsRequired(Player player) {
            return List.of();
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return SECTUMSEMPRA_SPELL;
        }

        @Override
        public MagicSpellbook spellbook() {
            return MagicSpellbook.NORMAL;
        }
    });

    /**
     * The spell attached to this element.
     */
    private final CombatSpell spell;

    /**
     * Creates a new {@link CombatSpells}.
     *
     * @param spell
     *            the spell attached to this element.
     */
    CombatSpells(CombatSpell spell) {
        this.spell = spell;
    }

    /**
     * Gets the spell attached to this element.
     *
     * @return the spell.
     */
    public final CombatSpell getSpell() {
        return spell;
    }

    /**
     * Gets the spell with a {@link CombatSpell#spellId()} of {@code id}.
     *
     * @param id
     *            the identification of the combat spell.
     * @return the combat spell with that identification.
     */
    public static Optional<CombatSpells> getCombatSpells(int id) {
        return Arrays.stream(CombatSpells.values()).filter(s -> s != null && s.getSpell().spellId() == id).findFirst();
    }
    public static Optional<CombatSpells> getCombatSpellsByName(String name) {
        return Arrays.stream(CombatSpells.values()).filter(s -> s != null && s.getSpell().name().equals(name)).findFirst();
    }

    public static CombatSpell getCombatSpellByName(String name) {
        Optional<CombatSpells> spell = getCombatSpellsByName(name);
        return spell.map(CombatSpells::getSpell).orElse(null);
    }
    public static CombatSpell getCombatSpell(int spellId) {
        Optional<CombatSpells> spell = getCombatSpells(spellId);
        return spell.map(CombatSpells::getSpell).orElse(null);
    }
}
