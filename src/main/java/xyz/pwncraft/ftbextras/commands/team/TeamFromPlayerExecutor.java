package xyz.pwncraft.ftbextras.commands.team;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import xyz.pwncraft.ftbextras.utility.TeamInfo;
import xyz.pwncraft.ftbextras.utility.Utility;

import java.util.Optional;

public class TeamFromPlayerExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<User> user = args.getOne("player");
        Optional<ForgePlayer> player = user
                .map(User::getUniqueId)
                .map(Universe.get()::getPlayer);

        if (!player.isPresent()) throw new CommandException(Utility.format("&f[&6FTBExtras&r] Player not found!"));
        if (!player.get().hasTeam()) throw new CommandException(Utility.format("&f[&6FTBExtras&r] Player " + player.get().getName() + " has no team!"));
        src.sendMessage(Utility.format("[&6FTBExtras&r] Team info of player: " + player.get().getName()));

        TeamInfo.getTeamInfo(src, player.get().team);

        return CommandResult.success();


    }
}
