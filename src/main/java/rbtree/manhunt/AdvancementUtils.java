package rbtree.manhunt;

import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdvancementUtils {
    public static List<String> getAllAdvancements() {
        List<String> displayNames = new ArrayList<>();
        Iterator<Advancement> advancementIterator = Bukkit.getServer().advancementIterator();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            AdvancementDisplay display = advancement.getDisplay();
            if (display != null) {
                Component titleComponent = display.title();
                String displayName = serializer.serialize(titleComponent);
                displayNames.add(displayName.replace(" ","_"));
            }
        }
        return displayNames;
    }

    public static Advancement getAdvancement(String advancementName){
        Iterator<Advancement> advancementIterator = Bukkit.getServer().advancementIterator();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            AdvancementDisplay display = advancement.getDisplay();
            if (display != null) {
                Component titleComponent = display.title();
                String displayName = serializer.serialize(titleComponent);
                if(displayName.equals(advancementName)){
                    return advancement;
                }
            }
        }
        return null;
    }
}