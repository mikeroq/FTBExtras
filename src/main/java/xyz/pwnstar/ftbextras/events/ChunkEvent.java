package xyz.pwnstar.ftbextras.events;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftbutilities.data.ClaimedChunk;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import xyz.pwnstar.ftbextras.FTBLibCompat;
import xyz.pwnstar.ftbextras.utility.Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.common.text.SpongeTexts;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ChunkEvent {

    @SubscribeEvent
    public void onChunkChanged(EnteringChunk event){
        if (!(event.getEntity() instanceof EntityPlayerMP) || !Universe.loaded())
        {
            return;
        }


        Player player = (Player) event.getEntity();
        ChunkDimPos pos = new ChunkDimPos((Entity) player);
        ClaimedChunk chunk = ClaimedChunks.instance.getChunk(pos);
        ForgeTeam team = chunk == null ? null : chunk.getTeam();
        short teamID = team == null ? 0 : team.getUID();

        DataContainer container = player.toContainer();
        DataQuery queryLastChunk = DataQuery.of("UnsafeData", "ForgeData", "ftbu_lchunk");
        Short zero = 0;
        Optional<Short> lastChunk = Optional.of(container.getShort(queryLastChunk).orElse(zero));
        //System.out.println(Utility.format("Before: " + lastChunk + " TeamID: " + teamID));
        //System.out.println("before last chunk");
        if (lastChunk.get() != teamID) {
            if (teamID == 0) {
                container.remove(queryLastChunk);
            } else {
                container.set(queryLastChunk, teamID);
            }
            //System.out.println(Utility.format("Inside: " + lastChunk + " TeamID: " + teamID));
            //System.out.println("before chunk null");
            if (chunk == null) return;

            String teamIDString = "";
            try {
                teamIDString = "&6Team ID: &r" + FTBLibCompat.getInstance().getId(team) + "\n";
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            String teamTitle = SpongeTexts.toPlain(team.getTitle()) + "\n";
            String teamDesc = "";
            String teamOwner = "";
            if (!team.getDesc().isEmpty()) teamDesc = team.getDesc() + "\n";
            if (team.type.isPlayer) {
                teamOwner = "&6Owner: &r" + team.owner.getName() + "\n";
            } else if (team.type.isServer) {
                teamOwner = "&6Server Team&r\n";
            }
            //System.out.println("before message");
            player.sendMessage(ChatTypes.ACTION_BAR, Utility.format(teamTitle + teamDesc + teamOwner + teamIDString));
        }

    }

}
