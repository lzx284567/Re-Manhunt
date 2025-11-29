package rbtree.manhunt;

import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import static rbtree.manhunt.GameEnd.gameEnd;

public class OnPlayerGetAdvancement implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGetAdvancement(PlayerAdvancementDoneEvent event){
        Player eventPlayer = event.getPlayer();
        if(Manhunt.gamePhase != 1) return;
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        AdvancementDisplay display = event.getAdvancement().getDisplay();
        if(display != null){
            if(!Manhunt.displayAdvancement) event.message(null);
            Component component = display.title();
            String displayName = serializer.serialize(component);
            if(Manhunt.endMethod == 2 && IdentityUtils.isRunner(eventPlayer) && displayName.equalsIgnoreCase(Manhunt.goalAdvancement)){
                gameEnd("runner",event.getPlayer().getName());
            }
        }
    }
}
