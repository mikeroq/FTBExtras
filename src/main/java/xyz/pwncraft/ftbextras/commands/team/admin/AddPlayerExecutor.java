package xyz.pwncraft.ftbextras.commands.team.admin;

import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
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
import org.spongepowered.api.entity.living.player.User;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;


@SuppressWarnings("NullableProblems")
public class AddPlayerExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<User> user = args.getOne("player");
        Optional<ForgePlayer> p = user
                .map(User::getUniqueId)
                .map(Universe.get()::getPlayer);

        if (!args.<String>getOne("teamid").isPresent()) throw new CommandException(Utility.format("[&6FTBExtras&r] Team missing!"));
        ForgeTeam team = Universe.get().getTeam(args.<String>getOne("teamid").get());
        if (!team.isValid()) throw new CommandException (Utility.format("[&6FTBExtras&r] Team not valid!"));

        if (p.isPresent()) {
            String teamId = null;
            try {
                teamId = FTBLibCompat.getInstance().getId(p.get().team);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (p.get().hasTeam()) throw new CommandException (Utility.format("[&6FTBExtras&r] " + p.get().getName() + " is already a member of team " + teamId));
            team.setStatus(p.get(), EnumTeamStatus.INVITED);
            if (team.addMember(p.get(),true)) {
                team.addMember(p.get(),false);
                src.sendMessage(Utility.format("[&6FTBExtras&r] Added " + p.get().getName() + " to team " + team.getID()));
            } else {
                src.sendMessage(Utility.format("[&6FTBExtras&r] Failed to add " + p.get().getName() + " to team " + team.getID()));
            }
        } else throw new CommandException (Utility.format("[&6FTBExtras&r] Could not find player!"));
        return CommandResult.success();
    }
}
