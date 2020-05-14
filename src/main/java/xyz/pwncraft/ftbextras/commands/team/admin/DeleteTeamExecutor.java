package xyz.pwncraft.ftbextras.commands.team.admin;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import xyz.pwncraft.ftbextras.FTBLibCompat;
import xyz.pwncraft.ftbextras.utility.Utility;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.lang.reflect.InvocationTargetException;

public class DeleteTeamExecutor implements CommandExecutor {


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!args.<String>getOne("id").isPresent()) throw new CommandException(Text.of("No team specified"));

        ForgeTeam team = Universe.get().getTeam(args.<String>getOne("id").get());
        String teamId = null;
        try {
            teamId = FTBLibCompat.getInstance().getId(team);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        ForgePlayer o = team.owner;

        for (ForgePlayer player : team.getMembers())
        {
            if (player != o)
            {
                team.removeMember(player);
            }
        }

        if (o != null)
        {
            team.removeMember(o);
        }

        team.delete();
        ForgeTeam checkTeam = Universe.get().getTeam(args.<String>getOne("id").get());
        if (!checkTeam.isValid())
        {
            src.sendMessage(Utility.format("[&6FTBExtras&r] Deleted teamid " + teamId + " successfully!"));
        }
        else {
            src.sendMessage(Utility.format("[&6FTBExtras&r] Error occurred while deleting teamid " + teamId + "!"));
        }

        return CommandResult.success();
    }
}

