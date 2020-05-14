package xyz.pwncraft.ftbextras.commands;

import xyz.pwncraft.ftbextras.utility.Utility;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class HelpCommandExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        List<Text> contents = new ArrayList<>();

        contents.add(Text.of(TextColors.GOLD, "/ft", TextColors.GRAY, " - ", TextColors.YELLOW, "Returns team info of chunk you are in."));
        contents.add(Text.of(TextColors.GOLD, "/ft help", TextColors.GRAY, " - ", TextColors.YELLOW, "You are here. OwO."));
        contents.add(Text.of(TextColors.GOLD, "/ft list", TextColors.GRAY, " - ", TextColors.YELLOW, "List all teams."));
        contents.add(Text.of(TextColors.GOLD, "/ft find <player>", TextColors.GRAY, " - ", TextColors.YELLOW, "Find team of a player."));
        contents.add(Text.of(TextColors.GOLD, "/ft info <teamid>", TextColors.GRAY, " - ", TextColors.YELLOW, "Get team info with ID or stand in chunk."));
        contents.add(Text.of(TextColors.GOLD, "/ft chunks <teamid>", TextColors.GRAY, " - ", TextColors.YELLOW, "List all claimed chunks of a team."));
        contents.add(Text.of(TextColors.GOLD, "/ft settings <teamid>", TextColors.GRAY, " - ", TextColors.YELLOW, "Edit settings of a team."));
        contents.add(Text.of(TextColors.GOLD, "/ft add <player> <teamid>", TextColors.GRAY, " - ", TextColors.YELLOW, "Add player to a team."));
        contents.add(Text.of(TextColors.GOLD, "/ft remove <player>", TextColors.GRAY, " - ", TextColors.YELLOW, "Remove player from their team."));
        contents.add(Text.of(TextColors.GOLD, "/ft promote <player>", TextColors.GRAY, " - ", TextColors.YELLOW, "Promote team member to owner."));
        contents.add(Text.of(TextColors.GOLD, "/ft delete <teamid>", TextColors.GRAY, " - ", TextColors.YELLOW, "Delete a team"));
        contents.add(Text.of(TextColors.GOLD, "/ft claim [radius] [teamid]", TextColors.GRAY, " - ", TextColors.YELLOW, "Claim chunks of a team in a radius."));

        PaginationList.builder()
                .title(Utility.format("&6[FTBExtras Help]&r"))
                .contents(contents)
                .padding(Utility.format("&4&m-"))
                .sendTo(src);
        return CommandResult.success();
    }
}
