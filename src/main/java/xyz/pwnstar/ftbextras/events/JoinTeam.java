package xyz.pwnstar.ftbextras.events;

import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import xyz.pwnstar.ftbextras.FTBLibCompat;
import xyz.pwnstar.ftbextras.utility.Utility;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;

import java.lang.reflect.InvocationTargetException;

public class JoinTeam {

    @SubscribeEvent
    public void onTeamJoin(ForgeTeamPlayerJoinedEvent event) throws InvocationTargetException, IllegalAccessException {

        ForgeTeam team = event.getTeam();
        String teamID = FTBLibCompat.getInstance().getId(team);
        Sponge.getServer().getOnlinePlayers().stream()
            .filter(p -> p.hasPermission("ftbextras.online"))
            .forEach(p -> p.sendMessage(Utility.format("Player " + event.getPlayer().getName() + " joined team " + teamID)));
    }

}
