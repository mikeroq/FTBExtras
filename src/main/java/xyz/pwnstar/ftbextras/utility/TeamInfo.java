package xyz.pwnstar.ftbextras.utility;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.common.text.SpongeTexts;
import xyz.pwnstar.ftbextras.FTBLibCompat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class TeamInfo {
    public static void getTeamInfo(CommandSource src, ForgeTeam team) {
        if (!team.isValid()) return;

        FTBUtilitiesTeamData data = FTBUtilitiesTeamData.get(team);
        int max = data.getMaxClaimChunks();
        int claimed = ClaimedChunks.instance.getTeamChunks(team, OptionalInt.empty(), true ).size();

        List<Text> contents = new ArrayList<>();
        contents.add(Utility.format("&6Title: &r" + SpongeTexts.toPlain(team.getTitle())));
        if (!team.getDesc().isEmpty()) contents.add(Utility.format("&6Description: &r" + team.getDesc()));
        contents.add(Utility.format("&6UID: &r" + team.getUID() + " / " + String.format("%04x", team.getUID())));
        contents.add(Utility.format("&6Claimed Chunks: &r" + claimed + "/" + max));

        List<Text> members = new ArrayList<>();
        if (team.type.isServer) {
            contents.add(Utility.format("&6Owner: &rSERVER"));
        }
        else {
            String lastPlayed = Utility.getLastPlayedTime(team.owner.getId());
            Text team_owner;
            if (team.owner.isOnline()) lastPlayed = "Online now!";
            Text owner;
            Text online;
            if (team.owner.isOnline()) {
                team_owner = Utility.format("&6Owner: &a" + team.owner.getName() + "&r");
                online = Utility.format("&6Online Now!&r");
            } else {
                team_owner = Utility.format("&6Owner: &r" + team.owner.getName());
                online = Utility.format("&6Last Online: &r" + lastPlayed);
            }
            Optional<User> user = Utility.getUser(team.owner.getId());
            if (src.hasPermission("ftbextras.teleport")) {
                owner = Text.builder()
                        .append(team_owner)
                        .onClick(TextActions.executeCallback(dummy -> Utility.tpPlayer(src, user)))
                        .onHover(TextActions.showText(Text.builder().append(online).append(Utility.format("\nClick to teleport!")).build()))
                        .build();
            }
            else {
                owner = Text.builder()
                        .append(team_owner)
                        .onHover(TextActions.showText(online))
                        .build();
            }

            contents.add(owner);
        }


        boolean first = true;
        if (team.getMembers().size() > 1)
        {
            members.add(Utility.format("&6Members: &r"));
            for (ForgePlayer p : team.getMembers()) {


                if (team.owner != p) {
                    if (!first) {
                        members.add(Text.of(", "));
                    }
                    first = false;
                    String lastPlayed;
                    Text player_name;
                    Optional<User> user = Utility.getUser(p.getId());

                    if (p.isOnline()) {
                        lastPlayed = "Online now!";
                        player_name = Utility.format("&a" + p.getName() + "&r");
                    }
                    else {
                        lastPlayed = "&6Last Online: &r" + Utility.getLastPlayedTime(p.getId());
                        player_name = Utility.format(p.getName());
                    }
                    Text hoverText;
                    if (team.isModerator(p)) {
                        hoverText = Utility.format("&6Moderator&r\n" + lastPlayed + "\nClick to teleport!");
                    }
                    else {
                        hoverText = Utility.format(lastPlayed + "\nClick to teleport!");
                    }
                    Optional<User> pUser = Utility.getUser(p.getId());
                    Text player;
                    player = Text.builder()
                            .append(Text.of(player_name))
                            .onClick(TextActions.executeCallback(dummy -> Utility.tpPlayer(src, pUser)))
                            .onHover(TextActions.showText(hoverText))
                            .build();
                    members.add(player);
                }

            }
            Text list = Text.join(members);
            contents.add(list);
        }
        String teamId = null;
        try {
            teamId = FTBLibCompat.getInstance().getId(team);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        PaginationList.builder()
                .title(Utility.format("&6[Team Information]"))
                .header(Utility.format("&6Team ID: &r" + teamId))
                .contents(contents)
                .padding(Utility.format("&4&m-"))
                .sendTo(src);
    }
}
