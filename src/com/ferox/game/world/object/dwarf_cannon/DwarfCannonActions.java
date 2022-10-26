package com.ferox.game.world.object.dwarf_cannon;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.ObjectIdentifiers;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.game.world.object.dwarf_cannon.DwarfCannon.*;

/**
 * @author Patrick van Elderen | April, 18, 2021, 18:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class DwarfCannonActions extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == ItemIdentifiers.CANNON_BASE) {

                var reclaim = player.<Boolean>getAttribOr(AttributeKey.LOST_CANNON, false);
                if (reclaim && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                    player.message("You can't deploy this cannon, you have one you need to reclaim.");
                } else {

                    DwarfCannon cannon = new DwarfCannon(player, CANNON_OBJECTS[0]);

                    if (!cannon.hasParts()) {
                        player.message("You don't have all the parts to build your cannon.");
                        return true;
                    }
                    if (!cannon.isValidSpot()) {
                        player.message("There's not enough room to setup your cannon here.");
                        return true;
                    }

                    if (!cannon.handleAreaRestriction()) {
                        return true;
                    }

                    if (World.getWorld().getOwnedObject(player, DwarfCannon.IDENTIFIER) != null) {
                        player.message("You already have a cannon deployed.");
                        return true;
                    }

                    Chain.bound(player).cancelWhen(() -> cannon.getOwnerOpt().isEmpty() || cannon.getOwner().dead()).runFn(1, () -> {

                        // wheres spawn obj
                        World.getWorld().registerOwnedObject(cannon);
                        player.faceObj(cannon);

                        cannon.getDecayTimer().start();

                        cannon.add();
                        player.animate(SETUP_ANIM);
                        player.inventory().remove(CANNON_PARTS[cannon.getStage().ordinal()], 1);
                        player.message("You place down the base.");
                        player.lock();
                    }).then(2, () -> {
                        for (int index = 0; index < 3; index++) {
                            // add new Tasks @ instantly (0*2=0), 2 4, 6
                            Chain.bound(player).runFn(index * 2, () -> {
                                player.faceObj(cannon);
                                player.animate(SETUP_ANIM);
                                cannon.incrementSetupStage();
                                Item cannonPart = new Item(CANNON_PARTS[cannon.getStage().ordinal()], 1);
                                String name = cannonPart.name().toLowerCase().replace("cannon", "").trim();
                                player.inventory().remove(CANNON_PARTS[cannon.getStage().ordinal()], 1);
                                player.message("You add the " + name + ".");
                            });
                        }
                    }).then(6, () -> {
                        cannon.fill();
                        player.unlock();
                    });
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(option == 1) {
            //Repairing
            if(object.getId() == ObjectIdentifiers.BROKEN_MULTICANNON) {
                if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
                    DwarfCannon cannon = ((DwarfCannon) object);
                    player.runFn(1, () -> {
                        cannon.getDecayTimer().reset();
                        cannon.getDecayTimer().start();
                        player.animate(3684);
                    }).then(2, () -> cannon.setStage(CannonStage.FIRING, true));
                } else {
                    player.message("Your not the owner of this cannon.");
                }
                return true;
            }
            if(object.getId() == ObjectIdentifiers.DWARF_MULTICANNON) {
                if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
                    ((DwarfCannon) object).fill();
                } else {
                    player.message("Your not the owner of this cannon.");
                }
                return true;
            }
            if(object.getId() == ObjectIdentifiers.CANNON_BASE || object.getId() == ObjectIdentifiers.CANNON_STAND || object.getId() == ObjectIdentifiers.CANNON_BARRELS) {
                if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
                    ((DwarfCannon) object).pickup();
                } else {
                    player.message("Your not the owner of this cannon.");
                }
                return true;
            }
        }
        if(option == 2) {
            if(object.getId() == ObjectIdentifiers.DWARF_MULTICANNON) {
                if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
                    ((DwarfCannon) object).pickup();
                } else {
                    player.message("Your not the owner of this cannon.");
                }
                return true;
            }
        }
        if(option == 3) {
            if(object.getId() == ObjectIdentifiers.DWARF_MULTICANNON) {
                if (!object.isOwnedObject()) {
                    return true;
                }
                if (object.asOwnedObject().isOwner(player)) {
                    DwarfCannon cannon = ((DwarfCannon) object);
                    if (player.inventory().hasFreeSlots(1) || player.inventory().hasCapacityFor(new Item(CANNON_BALL))) {
                        if (cannon.getAmmo() > 0) {
                            player.inventory().add(CANNON_BALL, cannon.getAmmo());
                            player.message("You unload your cannon and receive Cannonball x " + cannon.getAmmo());
                            cannon.setAmmo(0);
                            cannon.setStage(CannonStage.FURNACE, false);
                        }
                    } else {
                        player.message("You don't have enough inventory space to do that.");
                    }
                } else {
                    player.message("Your not the owner of this cannon.");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if(item.getId() == CANNON_BALL && object.getId() == BASE) {
            if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
                ((DwarfCannon) object).fill();
            } else {
                player.message("Your not the owner of this cannon.");
            }
            return true;
        }
        return false;
    }
}
