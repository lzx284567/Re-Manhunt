package rbtree.manhunt;

import org.bukkit.Material;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetAllItems {
    public static List<String> getAllItemNames() {
        Set<String> itemNames = new HashSet<>();
        for (Material material : Material.values()) {
            if (material.name().startsWith("LEGACY_")) continue;
            if (!material.isItem()) continue;
            itemNames.add(material.name());
        }
        return new ArrayList<>(itemNames);
    }
}