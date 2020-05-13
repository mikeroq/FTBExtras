package xyz.pwnstar.ftbextras.commands.chunk.admin;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import xyz.pwnstar.ftbextras.utility.Utility;
import xyz.pwnstar.ftbextras.FTBLibCompat;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;


public class UnclaimChunksExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        ForgePlayer p = new ForgePlayer(Universe.get(), UUID.nameUUIDFromBytes("FakePlayerClaimAs".getBytes(StandardCharsets.UTF_8)), "FakePlayerClaimAs");
        p.team = Universe.get().getTeam(String.valueOf(args.getOne("teamid").get()));
        if (!p.team.isValid()) throw new CommandException(Utility.format("[&6FTBExtras&r] Invalid team!"));

        Optional<Integer> dim = args.getOne("dim");
        OptionalInt dim1 = dim.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        try {
            FTBLibCompat.getInstance().unclaimAllChunks(p, p.team, dim1);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        String teamId = null;
        try {
            teamId = FTBLibCompat.getInstance().getId(p.team);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        src.sendMessage(Utility.format("[&6FTBExtras&r] Unclaimed all chunks for team " + teamId ));

        return CommandResult.success();
    }
}
