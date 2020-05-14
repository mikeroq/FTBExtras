package xyz.pwncraft.ftbextras.commands.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftbutilities.data.ClaimedChunk;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import xyz.pwncraft.ftbextras.utility.TeamInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import xyz.pwncraft.ftbextras.utility.Utility;

public class TeamInfoExecutor implements CommandExecutor {



    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.<String>getOne("teamid").isPresent())
        {
            ForgeTeam team = Universe.get().getTeam(args.<String>getOne("teamid").get());
            if (!team.isValid()) throw new CommandException(Utility.format("[&6FTBExtras&r] Could not find team!"));
            TeamInfo.getTeamInfo(src, team);
        }
        else {
            if (!(src instanceof Player)) throw new CommandException(Text.of("Only players can run this command without a teamid!"));
            EntityPlayerMP player = (EntityPlayerMP) src;
            ChunkDimPos pos = new ChunkDimPos(player);
            ClaimedChunk chunk = ClaimedChunks.instance.getChunk(pos);

            if (chunk == null) throw new CommandException(Utility.format("&f[&6FTBExtras&r] This chunk is not claimed!"));

            src.sendMessage(Utility.format("[&6FTBExtras&r] Chunk: DIM" + chunk.getPos().dim + ": " + chunk.getPos().posX + ", " + chunk.getPos().posZ + " Belongs to:"));
            TeamInfo.getTeamInfo(src, chunk.getTeam());
        }
        return CommandResult.success();
    }
}

