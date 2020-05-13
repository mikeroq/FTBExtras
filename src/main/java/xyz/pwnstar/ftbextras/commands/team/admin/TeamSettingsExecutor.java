package xyz.pwnstar.ftbextras.commands.team.admin;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class TeamSettingsExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!args.<String>getOne("id").isPresent()) throw new CommandException(Text.of("No team specified"));
        if (!(src instanceof Player)) throw new CommandException(Text.of("You are not a player..."));
        ForgeTeam team = Universe.get().getTeam(args.<String>getOne("id").get());
        EntityPlayerMP player = (EntityPlayerMP) src;

        if (!team.isValid()) throw new CommandException(Text.of("Team does not exist!"));
        FTBLibAPI.editServerConfig(player, team.getSettings(), team);

        return CommandResult.success();
    }
}

