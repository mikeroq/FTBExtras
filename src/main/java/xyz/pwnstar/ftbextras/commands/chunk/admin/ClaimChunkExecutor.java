package xyz.pwnstar.ftbextras.commands.chunk.admin;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftbutilities.data.ClaimResult;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import xyz.pwnstar.ftbextras.FTBLibCompat;
import xyz.pwnstar.ftbextras.utility.Utility;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import net.minecraft.entity.player.EntityPlayerMP;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.OptionalInt;
import java.util.UUID;


public class ClaimChunkExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) throw new CommandException(Text.of("You don't look like a player to me...."));

        EntityPlayerMP srcPlayer = (EntityPlayerMP) src;

        int radius = args.<Integer>getOne("radius").orElse(0);
        if (radius > 30) throw new CommandException(Utility.format("[&6FTBExtras&r] Radius too large! Max allowed is 30."));
        ForgePlayer p;
        if (args.getOne("teamid").isPresent())
        {
            p = new ForgePlayer(Universe.get(), UUID.nameUUIDFromBytes("FakePlayerClaimAs".getBytes(StandardCharsets.UTF_8)), "FakePlayerClaimAs");
            p.team = Universe.get().getTeam(String.valueOf(args.getOne("teamid").get()));
        } else {
            p = Universe.get().getPlayer(srcPlayer);
        }

        ChunkDimPos pos = new ChunkDimPos(srcPlayer);
        FTBUtilitiesTeamData data = FTBUtilitiesTeamData.get(p.team);

        String teamId = null;
        try {
            teamId = FTBLibCompat.getInstance().getId(p.team);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        int success = 0;
        int error = 0;

        int area2 = ((radius * 2 + 1) * (radius * 2 + 1));
        int chunks_avail = data.getMaxClaimChunks() - ClaimedChunks.instance.getTeamChunks(p.team, OptionalInt.empty()).size();
        if (area2 > chunks_avail && p.team.type.isPlayer)
        {
            src.sendMessage(Utility.format("[&6FTBExtras&r] Cannot claim! Team " + teamId + " only has " + chunks_avail + " available. Needed " + area2 + "!"));
        }
        else {
            src.sendMessage(Utility.format("[&6FTBExtras&r] Team " + teamId + " attempting to claim " + area2 + " chunk(s)."));

            for (int x = -radius; x <= radius; x++) {

                for (int z = -radius; z <= radius; z++) {

                    ChunkDimPos pos1 = new ChunkDimPos(pos.posX + x, pos.posZ + z, pos.dim);
                    if (args.hasAny("f")) {
                        try {
                            FTBLibCompat.getInstance().unclaimChunk(p, pos1);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }

                    ClaimResult result = Utility.claimChunk(p, pos1);

                    if (result.equals(ClaimResult.SUCCESS)) {
                        success++;
                    } else {
                        error++;
                    }
                }
            }
            String error_text = "";
            if (error > 0) error_text = " Failed to claim " + error + " chunk(s)!";
            src.sendMessage(Utility.format("[&6FTBExtras&r] Team " + teamId + " claimed " + success + " chunk(s)." + error_text));
        }
        return CommandResult.success();
    }
}
