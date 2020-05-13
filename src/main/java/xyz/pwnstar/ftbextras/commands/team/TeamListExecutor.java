package xyz.pwnstar.ftbextras.commands.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import xyz.pwnstar.ftbextras.FTBLibCompat;
import xyz.pwnstar.ftbextras.utility.Utility;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.common.text.SpongeTexts;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TeamListExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        List<Text> contents = new ArrayList<>();
        for (ForgeTeam t : Universe.get().getTeams()) {
            String title = SpongeTexts.toPlain(t.getTitle());
            String teamOwner;
            String teamId = null;

            if (t.owner == null) teamOwner = "SERVER";
            else teamOwner = t.owner.getName();

            String teamDesc = t.getDesc();
            if (t.getDesc().isEmpty()) teamDesc = "";

            try {
                teamId = FTBLibCompat.getInstance().getId(t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            Text team = Text.builder()
                    .append(Utility.format("&6" + title))
                    .onClick(TextActions.runCommand("/ft info " + teamId))
                    .onHover(TextActions.showText(Utility.format(
                            "&6** Team Information **\n" +
                                    "Team ID:&f " + teamId + "\n" +
                                    "&6UUID:&f " + t.getUID() + "\n" +
                                    "&6Desc: &f" + teamDesc + "\n" +
                                    "&6Team Owner: &f" + teamOwner + "\n" +
                                    "&rClick to view full team info")))
                    .build();
            contents.add(team);
        }

        PaginationList.builder()
                .title(Utility.format("&6[ &eTeam List &6]"))
                .contents(contents)
                .padding(Utility.format("&4&m-"))
                .sendTo(src);
        return CommandResult.success();
    }
}

