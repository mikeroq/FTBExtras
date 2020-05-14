package xyz.pwncraft.ftbextras.commands;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import xyz.pwncraft.ftbextras.FTBLibCompat;
import xyz.pwncraft.ftbextras.utility.Utility;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OnlineListExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

       Collection<ForgePlayer> players = Universe.get().getOnlinePlayers();
       List<Text> online = new ArrayList<>();
       List<ForgeTeam> teams = new ArrayList<>();
       List<Text> message = new ArrayList<>();
       List<ForgePlayer> no_team = new ArrayList<>();

        players.stream().map(p -> p.team).filter(t -> !teams.contains(t)).forEach(teams::add);
        players.stream().filter(t -> !t.team.isValid()).forEach(no_team::add);
        for (ForgeTeam t : teams)
        {
            String teamId = null;
            try {
                teamId = FTBLibCompat.getInstance().getId(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (t.isValid()) {
                List<Text> team_online = new ArrayList<>();
                team_online.add(Text.builder()
                        .append(Utility.format("(&6" + teamId + "&r: "))
                        .onClick(TextActions.runCommand(("/ft info " + teamId)))
                        .onHover(TextActions.showText(Utility.format("&6Click to view team info!")))
                        .build());
                boolean first = true;
                for (ForgePlayer p : t.getMembers()) {
                    if (p.isOnline()) {
                        if (!first) {
                            team_online.add(Utility.format(", "));
                        }
                        first = false;
                        team_online.add(Text.builder()
                                .append(Text.of(p.getName()))
                                .onClick(TextActions.executeCallback((dummy -> Utility.tpPlayer(src, Utility.getUser(p.getId())))))
                                .onHover(TextActions.showText(Text.builder()
                                        .append(Text.of("Click to teleport to " + p.getName()))
                                        .build()))
                                .build());
                    }
                }
                team_online.add(Utility.format(")"));
                online.add(Text.join(team_online));
            }
        }
        for (ForgePlayer u : no_team) {
            online.add(Text.builder()
                    .append(Text.of(u.getName()))
                    .onClick(TextActions.executeCallback((dummy -> Utility.tpPlayer(src, Utility.getUser(u.getId())))))
                    .onHover(TextActions.showText(Text.builder()
                            .append(Text.of("Click to teleport to " + u.getName()))
                            .build()))
                    .build());
        }
        message.add(Text.joinWith(Utility.format(", "),online));
        PaginationList.builder()
                .title(Utility.format("&6[Online Teams/Players]"))
                .contents(message)
                .padding(Utility.format("&4&m-"))
                .sendTo(src);
        return CommandResult.success();
    }
}

