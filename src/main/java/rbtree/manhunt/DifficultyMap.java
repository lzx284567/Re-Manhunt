package rbtree.manhunt;

import java.util.HashMap;
import java.util.Map;

public class DifficultyMap {

    public static Map<String, Integer> getDifficultyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("STONE AGE", 1);
        map.put("GETTING AN UPGRADE", 1);
        map.put("ACQUIRE HARDWARE", 5);
        map.put("ISN'T IT IRON PICK", 6);
        map.put("HOT STUFF", 9);
        map.put("SUIT UP", 6);//沉船，所以降低难度
        map.put("DIAMONDS!", 7);//沉船，所以降低难度
        map.put("ICE BUCKET CHALLENGE", 8);//废弃地狱门遗迹，降低难度
        map.put("NOT TODAY, THANK YOU", 7);
        map.put("ENCHANTER", 11);//沉船/废弃地狱门遗迹，降低难度
        map.put("COVER ME WITH DIAMONDS", 8);//沉船，所以降低难度
        map.put("WE NEED TO GO DEEPER", 11);
        map.put("ZOMBIE DOCTOR", 25);
        map.put("EYE SPY", 75);
        map.put("THE END?", 100);

        map.put("NETHER", 11);//与WE NEED TO GO DEEPER一样
        map.put("OH SHINY", 13);
        map.put("SUBSPACE BUBBLE", 32);
        map.put("THOSE WERE THE DAYS", 15);
        map.put("A TERRIBLE FORTRESS", 16);
        map.put("HIDDEN IN THE DEPTHS", 35);
        map.put("WHO IS CUTTING ONIONS?", 15);
        map.put("RETURN TO SENDER", 15);
        map.put("THIS BOAT HAS LEGS", 18);
        map.put("WAR PIGS", 17);
        map.put("SPOOKY SCARY SKELETON", 40);
        map.put("INTO FIRE", 20);
        map.put("COVER ME IN DEBRIS", 105);
        map.put("NOT QUITE \"NINE\" LIVES", 16);
        map.put("UNEASY ALLIANCE", 65);//待定
        map.put("HOT TOURIST DESTINATIONS", 21);
        map.put("FEELS LIKE HOME", 25);
        map.put("WITHERING HEIGHTS", 70);//待定
        map.put("LOCAL BREWERY", 22);
        map.put("BRING HOME THE BEACON", 80);
        map.put("A FURIOUS COCKTAIL", 120);
        map.put("BEACONATOR", 250);//待定
        map.put("HOW DID WE GET HERE?", 1200);//待定

        map.put("THE END", 100);//与THE END?一样
        map.put("FREE THE END", 125);
        map.put("YOU NEED A MINT", 101);
        map.put("THE NEXT GENERATION", 126);
        map.put("REMOTE GETAWAY", 127);
        map.put("THE END... AGAIN...", 150);
        map.put("THE CITY AT THE END OF THE GAME", 135);
        map.put("SKY'S THE LIMIT", 150);
        map.put("GREAT VIEW FROM UP HERE", 140);

        map.put("ADVENTURE", 1);
        map.put("SNEAK 100", 24);
        map.put("CRAFTERS CRAFTING CRAFTERS", 10);
        map.put("CAVES & CLIFFS", 16);
        map.put("HEART TRANSPLANTER", 20);
        map.put("STICKY SITUATION", 23);
        map.put("MONSTER HUNTER", 1);
        map.put("SURGE PROTECTOR", 17);
        map.put("MINECRAFT: TRIAL(S) EDITION", 30);
        map.put("OL' BETSY", 10);
        map.put("THE POWER OF BOOKS", 13);
        map.put("ISN'T IT SCUTE?", 12);
        map.put("RESPECTING THE REMNANTS", 16);
        map.put("SWEET DREAMS", 5);
        map.put("IS IT A BIRD?", 14);
        map.put("WHAT A DEAL!", 7);
        map.put("CRAFTING A NEW LOOK", 15);
        map.put("VOLUNTARY EXILE", 20);
        map.put("COUNTRY LODE, TAKE ME HOME", 12);
        map.put("MONSTERS HUNTED", 400);
        map.put("IT SPREADS", 22);
        map.put("TAKE AIM", 9);
        map.put("A THROWAWAY JOKE", 220);
        map.put("POSTMORTAL", 140);
        map.put("BLOWBACK", 37);
        map.put("LIGHTEN UP", 35);
        map.put("OVER-OVERKILL", 150);
        map.put("UNDER LOCK AND KEY", 14);
        map.put("WHO NEEDS ROCKETS?", 15);
        map.put("ARBALISTIC", 45);
        map.put("TWO BIRDS, ONE ARROW", 40);//待定
        map.put("WHO'S THE PILLAGER NOW?", 25);
        map.put("CAREFUL RESTORATION", 32);
        map.put("ADVENTURING TIME", 600);
        map.put("SOUND OF MUSIC", 25);
        map.put("LIGHT AS A RABBIT", 9);
        map.put("IS IT A BALLOON?", 14);
        map.put("HIRED HELP", 18);
        map.put("STAR TRADER", 21);
        map.put("SMITHING WITH STYLE", 350);
        map.put("HERO OF THE VILLAGE", 180);//待定
        map.put("BULLSEYE", 28);
        map.put("SNIPER DUEL", 15);
        map.put("VERY VERY FRIGHTENING", 230);
        map.put("IS IT A PLANE?", 61);
        map.put("REVAULTING", 25);

        map.put("HUSBANDRY", 2);
        map.put("YOU'VE GOT A FRIEND IN ME", 20);
        map.put("THE PARROTS AND THE BATS", 3);
        map.put("FISHY BUSINESS", 8);
        map.put("GLOW AND BEHOLD!", 7);
        map.put("SMELLS INTERESTING", 45);
        map.put("STAY HYDRATED!", 0);
        map.put("A SEEDY PLACE", 4);
        map.put("WHATEVER FLOATS YOUR GOAT!", 13);
        map.put("BEE OUR GUEST", 14);
        map.put("TOTAL BEELOCATION", 19);
        map.put("BUKKIT BUKKIT", 26);
        map.put("BEST FRIENDS FOREVER", 8);
        map.put("BIRTHDAY SONG", 26);
        map.put("TWO BY TWO", 300);
        map.put("TACTICAL FISHING", 9);
        map.put("LITTLE SNIFFS", 60);
        map.put("A BALANCED DIET", 200);
        map.put("SERIOUS DEDICATION", 55);
        map.put("WAX ON", 28);
        map.put("WHEN THE SQUAD HOPS INTO TOWN", 33);//待定
        map.put("GOOD AS NEW", 16);
        map.put("SHEAR BRILLIANCE", 14);
        map.put("A COMPLETE CATALOGUE", 75);
        map.put("THE WHOLE PACK", 120);
        map.put("THE CUTEST PREDATOR", 12);
        map.put("PLANTING THE PAST", 66);
        map.put("WAX OFF", 14);
        map.put("WITH OUR POWERS COMBINED!", 52);
        map.put("THE HEALING POWER OF FRIENDSHIP!", 16);
        return map;
    }
}
