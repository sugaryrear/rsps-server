package com.ferox.game.world.entity.mob.player.commands.impl.kotlin

import com.ferox.game.GameEngine
import com.ferox.game.world.entity.mob.player.Player
import com.ferox.game.world.entity.mob.player.commands.impl.owner.CheckServerWealthCommand
import com.ferox.util.Utils
import java.io.File

/**
 * @author Shadowrs/jak tardisfan121@gmail.com
 */
object SaveWealthInfo {

    @JvmStatic
    fun saveWealth(pFeedback: Player, storage: CheckServerWealthCommand.AtomicStorage) {
        // kotlin syntax is just easier to deal with than java
        var str = ""
        var topWealthString = ""
        var topPlayers = 0
        val sortedValues = Utils.sortByComparator(storage.playersValues, false)
        sortedValues.forEach { (key: String, value: Long) ->
            if (topPlayers++ < 10) {
                topWealthString += key + "=" + Utils.formatNumber(value) + " " + "bm" + "\n"
            }
        }
        val goal = storage.toScanAmt.get()
        str += ("The players with the most wealth are: \n$topWealthString \n")
        str += ("Scanned " + (goal) + " offline players.\n")
        str += ("The total BM cash for all " + goal + " players is: " + Utils.formatNumber(storage.sumBloodMoneyWealth.get())+"\n")
        str += ("The total BM wealth for all " + goal + " players is: " + Utils.formatNumber(storage.sumBloodMoneyItemWealth.get())+"\n")
        str += ("The total item count for all " + goal + " players is: " + Utils.formatNumber(storage.itemsCount.toLong())+"\n")
        str += ("The total REFERRAL by name count for all " + goal + " players is: " + Utils.formatNumber(storage.sumRefersByName.toLong())+"\n")
        str += ("The total VOTES count for all " + goal + " players is: " + Utils.formatNumber(storage.sumVotePoints.toLong())+"\n")
        str += ("The total ELY for all " + goal + " players is: " + Utils.formatNumber(storage.sumEly.get())+"\n")
        str += ("The total ARCANE for all " + goal + " players is: " + Utils.formatNumber(storage.sumArcane.get())+"\n")
        str += ("The total AGS for all " + goal + " players is: " + Utils.formatNumber(storage.sumAgs.get())+"\n")
        str += ("The total AGS OR for all " + goal + " players is: " + Utils.formatNumber(storage.sumAgsOR.get())+"\n")
        str += ("The total ANCIENT VLS for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncientVLS.get())+"\n")
        str += ("The total ANCIENT SWH for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncientSWH.get())+"\n")
        str += ("The total FACEGUARD for all " + goal + " players is: " + Utils.formatNumber(storage.sumFacegaurd.get())+"\n")
        str += ("The total DHL for all " + goal + " players is: " + Utils.formatNumber(storage.sumDHL.get())+"\n")
        str += ("The total 5 BOND for all " + goal + " players is: " + Utils.formatNumber(storage.sum5Bond.get())+"\n")
        str += ("The total 10 BOND for all " + goal + " players is: " + Utils.formatNumber(storage.sum10Bond.get())+"\n")
        str += ("The total 20 BOND for all " + goal + " players is: " + Utils.formatNumber(storage.sum20Bond.get())+"\n")
        str += ("The total 40 BOND for all " + goal + " players is: " + Utils.formatNumber(storage.sum40Bond.get())+"\n")
        str += ("The total 50 BOND for all " + goal + " players is: " + Utils.formatNumber(storage.sum50Bond.get())+"\n")
        str += ("The total 100 BOND for all " + goal + " players is: " + Utils.formatNumber(storage.sum100Bond.get())+"\n")
        str += ("The total WYVERN SHIELD for all " + goal + " players is: " + Utils.formatNumber(storage.sumWyvernShield.get())+"\n")
        str += ("The total DRAGONFIRE WARD for all " + goal + " players is: " + Utils.formatNumber(storage.sumWardShield.get())+"\n")
        str += ("The total SANG SCYTHE for all " + goal + " players is: " + Utils.formatNumber(storage.sumSangScythe.get())+"\n")
        str += ("The total HOLY SCYTHE for all " + goal + " players is: " + Utils.formatNumber(storage.sumHolyScythe.get())+"\n")
        str += ("The total SCYTHE for all " + goal + " players is: " + Utils.formatNumber(storage.sumScythe.get())+"\n")
        str += ("The total TBOW for all " + goal + " players is: " + Utils.formatNumber(storage.sumTwistedbow.get())+"\n")
        str += ("The total TBOW (I) for all " + goal + " players is: " + Utils.formatNumber(storage.sumTwistedBowI.get())+"\n")
        str += ("The total SANG TBOW for all " + goal + " players is: " + Utils.formatNumber(storage.sumSangTwistedBow.get())+"\n")
        str += ("The total ELDER MAUL for all " + goal + " players is: " + Utils.formatNumber(storage.sumElderMaul.get())+"\n")
        str += ("The total INFERNAL CAPE for all " + goal + " players is: " + Utils.formatNumber(storage.sumInfernalCape.get())+"\n")
        str += ("The total SERP for all " + goal + " players is: " + Utils.formatNumber(storage.sumSerp.get())+"\n")
        str += ("The total TSOTD for all " + goal + " players is: " + Utils.formatNumber(storage.sumTSOTD.get())+"\n")
        str += ("The total CRAWS BOW for all " + goal + " players is: " + Utils.formatNumber(storage.sumCrawBow.get())+"\n")
        str += ("The total VIG MACE for all " + goal + " players is: " + Utils.formatNumber(storage.sumVig.get())+"\n")
        str += ("The total CRAWS BOW C for all " + goal + " players is: " + Utils.formatNumber(storage.sumCrawBowC.get())+"\n")
        str += ("The total VIG MACE C for all " + goal + " players is: " + Utils.formatNumber(storage.sumVigC.get())+"\n")
        str += ("The total FERO GLOVES for all " + goal + " players is: " + Utils.formatNumber(storage.sumFero.get())+"\n")
        str += ("The total AVERNIC DEFENDER for all " + goal + " players is: " + Utils.formatNumber(storage.sumAvernic.get())+"\n")
        str += ("The total PRIMORDIAL for all " + goal + " players is: " + Utils.formatNumber(storage.sumPrim.get())+"\n")
        str += ("The total PEGASIAN for all " + goal + " players is: " + Utils.formatNumber(storage.sumPeg.get())+"\n")
        str += ("The total ETERNAL for all " + goal + " players is: " + Utils.formatNumber(storage.sumEternal.get())+"\n")
        str += ("The total PRIMORDIAL OR for all " + goal + " players is: " + Utils.formatNumber(storage.sumPrimOR.get())+"\n")
        str += ("The total PEGASIAN OR for all " + goal + " players is: " + Utils.formatNumber(storage.sumPegOR.get())+"\n")
        str += ("The total ETERNAL OR for all " + goal + " players is: " + Utils.formatNumber(storage.sumEternalOR.get())+"\n")
        str += ("The total CORRUPTED BOOTS for all " + goal + " players is: " + Utils.formatNumber(storage.sumCorruptedBoots.get())+"\n")
        str += ("The total FEROX COINS for all " + goal + " players is: " + Utils.formatNumber(storage.sumLuxCoins.get())+"\n")
        str += ("The total ANCETSTRAL HAT for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncestralHat.get())+"\n")
        str += ("The total ANCESTRAL TOP for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncestralTop.get())+"\n")
        str += ("The total ANCESTRAL BOTTOM for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncestralBottom.get())+"\n")
        str += ("The total TWISTED ANCETSTRAL HAT for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncestralHatT.get())+"\n")
        str += ("The total TWISTED ANCESTRAL TOP for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncestralTopT.get())+"\n")
        str += ("The total TWISTED ANCESTRAL BOTTOM for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncestralBottomT.get())+"\n")
        str += ("The total DRAGON CLAWS for all " + goal + " players is: " + Utils.formatNumber(storage.sumClaws.get())+"\n")
        str += ("The total DRAGON CLAWS OR for all " + goal + " players is: " + Utils.formatNumber(storage.sumClawsOr.get())+"\n")
        str += ("The total DARK ELDER MAUL for all " + goal + " players is: " + Utils.formatNumber(storage.sumDarkElder.get())+"\n")
        str += ("The total JAWA PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumJawaPet.get())+"\n")
        str += ("The total ZRIAWK PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumZriawkPet.get())+"\n")
        str += ("The total FAWKES PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumFawkesPet.get())+"\n")
        str += ("The total RECOLORED FAWKES PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumRecoloredFawkesPet.get())+"\n")
        str += ("The total NIFFLER PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumNifflerPet.get())+"\n")
        str += ("The total WAMPA PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumWampaPet.get())+"\n")
        str += ("The total BABY ARAGOG PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumBabyAragogPet.get())+"\n")
        str += ("The total MINI NECROMANCER PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumMiniNecromancerPet.get())+"\n")
        str += ("The total CORRUPTED NECHRYARCH PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumCorruptedNechryarchPet.get())+"\n")
        str += ("The total GRIM REAPER PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumGrimReaperPet.get())+"\n")
        str += ("The total KERBEROS PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumKerberosPet.get())+"\n")
        str += ("The total SKORPIOS PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumSkorpiosPet.get())+"\n")
        str += ("The total ARACHNE PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumArachnePet.get())+"\n")
        str += ("The total ARTIO PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumArtioPet.get())+"\n")
        str += ("The total LITTLE NIGHTMARE PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumLittleNightmarePet.get())+"\n")
        str += ("The total DEMENTOR PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumDementorPet.get())+"\n")
        str += ("The total FENRIR GREYBACK JR PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumFenrirGreybackJrPet.get())+"\n")
        str += ("The total FLUFFY JR PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumFluffyJrPet.get())+"\n")
        str += ("The total ANCIENT KBD PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncientKBDPet.get())+"\n")
        str += ("The total ANCIENT CHAOS ELE PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncientChaosElePet.get())+"\n")
        str += ("The total ANCIENT BARRELCHEST PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumAncientBarrelchestPet.get())+"\n")
        str += ("The total FOUNDER IMP PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumFounderImpPet.get())+"\n")
        str += ("The total BABY LAVA DRAGON PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumBabyLavaDragonPet.get())+"\n")
        str += ("The total JALTOK JAD PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumJaltokJadPet.get())+"\n")
        str += ("The total TZREK ZUK PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumTzrekZukPet.get())+"\n")
        str += ("The total RING OF ELYSIAN for all " + goal + " players is: " + Utils.formatNumber(storage.sumRingOfElysianPet.get())+"\n")
        str += ("The total BLOOD MONEY PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumBloodMoneyPet.get())+"\n")
        str += ("The total GENIE PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumGeniePet.get())+"\n")
        str += ("The total DHAROK PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumDharokPet.get())+"\n")
        str += ("The total ZOMBIES CHAMPION PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumZombiesChampionPet.get())+"\n")
        str += ("The total ABYSSAL DEMON PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumAbyssalDemonPet.get())+"\n")
        str += ("The total DARK BEAST PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumDarkBeastPet.get())+"\n")
        str += ("The total BABY SQUIRT PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumBabySquirtPet.get())+"\n")
        str += ("The total JALNIB REK PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumJalnibRekPet.get())+"\n")
        str += ("The total CENTAUR MALE PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumCentaurMalePet.get())+"\n")
        str += ("The total CENTAUR FEMALE PET for all " + goal + " players is: " + Utils.formatNumber(storage.sumCentaurFemalePet.get())+"\n")
        str += ("The total CRYSTAL HELM for all " + goal + " players is: " + Utils.formatNumber(storage.sumCrystalHelm.get())+"\n")
        str += ("The total CRYSTAL BODY for all " + goal + " players is: " + Utils.formatNumber(storage.sumCrystalBody.get())+"\n")
        str += ("The total CRYSTAL LEGS for all " + goal + " players is: " + Utils.formatNumber(storage.sumCrystalLegs.get())+"\n")
        str += ("The total DARK ARMADYL HELMET for all " + goal + " players is: " + Utils.formatNumber(storage.sumDarkArmadylHelm.get())+"\n")
        str += ("The total DARK ARMADYL CHESTPLATE for all " + goal + " players is: " + Utils.formatNumber(storage.sumDarkArmadylChestplate.get())+"\n")
        str += ("The total DARK ARMADYL CHAINSKIRT for all " + goal + " players is: " + Utils.formatNumber(storage.sumDarkArmadylChainskirt.get())+"\n")
        str += ("The total BOW OF FAERDHINEN for all " + goal + " players is: " + Utils.formatNumber(storage.sumBowOfFaerdhinen.get())+"\n")

        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - summary.txt").createNewFile()
            File("./data/eco scan - summary.txt").writeText(str)
        }

        fullbm(storage)
        fullvotes(storage)
        fullRefByName(storage)
        fullitems(storage)
    }

    fun fullbm(storage: CheckServerWealthCommand.AtomicStorage) {
        val sortedValues = Utils.sortByComparator(storage.playersValues, false)
        var str = "full breakdown is:\n"
        sortedValues.forEach { (key: String, value: Long) ->
            str += key + "=" + Utils.formatNumber(value) + " " + "bm" + "\n"
        }
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - bm.txt").createNewFile()
            File("./data/eco scan - bm.txt").writeText(str)
        }
    }

    fun fullvotes(storage: CheckServerWealthCommand.AtomicStorage) {
        val sortedValues = storage.loaded.toMutableList().sortedByDescending {
            storage.vp(it)
        }
        var str = "full breakdown is:\n"
        sortedValues.forEach { p ->
            str += "${p.mobName} has ${Utils.formatNumber(storage.vp(p))} votes\n"
        }
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - votes.txt").createNewFile()
            File("./data/eco scan - votes.txt").writeText(str)
        }
    }

    fun fullRefByName(storage: CheckServerWealthCommand.AtomicStorage) {
        val sortedValues = storage.loaded.toMutableList().sortedByDescending {
            storage.refc(it)
        }
        var str = "full breakdown is:\n"
        sortedValues.forEach { p ->
            str += "${p.mobName} has ${Utils.formatNumber(storage.refc(p))} refs by name\n"
        }
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - ref by name.txt").createNewFile()
            File("./data/eco scan - ref by name.txt").writeText(str)
        }
    }

    fun fullitems(storage: CheckServerWealthCommand.AtomicStorage) {
        println("bm items starting")
        val sortedValues = storage.loaded.toMutableList().filter { storage.BMtotal(it) > 0 }.sortedByDescending {
            storage.BMtotal(it)
        }
        var str = "full breakdown is:\n"
        sortedValues.forEachIndexed { ix, p ->
            val items = storage.playerBmItems.get(p)?.filterNotNull()
            str += "${p.mobName} has ${Utils.formatNumber(storage.BMtotal(p))} bm on account from ${items?.size?: 0} items\n"
            items?.forEachIndexed { index, it ->
                if (it.amount > 0)
                // if (index < 20) // only record 20 items to file, otherwise it takes a long ass time to save the txt file...
                    str += "${it.name()} x ${it.amount} = ${it.amount * 1L * it.bloodMoneyPrice.value()} bm\n"
            }
            str += "\n\n"
            if (ix % 50 == 0)
                println("done ${(ix.toDouble()/sortedValues.size.toDouble())*100.0}% bm scans...")
        }
        println("bm items save done, submit to save")
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - player all bm items.txt").createNewFile()
            File("./data/eco scan - player all bm items.txt").writeText(str)
            println("bm items save complete")
        }
    }
}
