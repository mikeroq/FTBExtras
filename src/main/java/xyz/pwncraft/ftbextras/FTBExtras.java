package xyz.pwncraft.ftbextras;

import com.google.inject.Inject;
import xyz.pwncraft.ftbextras.commands.*;
import xyz.pwncraft.ftbextras.commands.chunk.*;
import xyz.pwncraft.ftbextras.commands.team.*;
import xyz.pwncraft.ftbextras.commands.team.admin.*;
import xyz.pwncraft.ftbextras.commands.chunk.admin.*;
import xyz.pwncraft.ftbextras.events.ChunkEvent;
import xyz.pwncraft.ftbextras.events.JoinTeam;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;


@Plugin(
        id = "ftbextras",
        name = "FTBExtras",
        description = "Team/chunk commands for older FTB utilities modpacks.",
        authors = {
                "shinyafro",
                "pwnstar"
        },
        dependencies = {
                @Dependency(id = "ftblib")
        },
        version = "1.0.0"
)
public class FTBExtras {

    @Inject
    private Logger logger;


    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        logger.info("Starting plugin. Registering events...");
        MinecraftForge.EVENT_BUS.register(new ChunkEvent());
        MinecraftForge.EVENT_BUS.register(new JoinTeam());
        logger.info("Events registered. Registering commands...");

        CommandSpec help = CommandSpec.builder()
                .executor(new HelpCommandExecutor())
                .build();
        CommandSpec getTeam = CommandSpec.builder()
                .permission("ftbextras.team.find")
                .arguments(GenericArguments.user(Text.of("player")))
                .executor(new TeamFromPlayerExecutor())
                .build();

        CommandSpec remove = CommandSpec.builder()
                .permission("ftbextras.admin.remove")
                .arguments(GenericArguments.user(Text.of("player")))
                .executor(new RemovePlayerExecutor())
                .build();

        CommandSpec add = CommandSpec.builder()
                .permission("ftbextras.admin.add")
                .arguments(GenericArguments.user(Text.of("player")),GenericArguments.string(Text.of("teamid")))
                .executor(new AddPlayerExecutor())
                .build();

        CommandSpec transfer = CommandSpec.builder()
                .permission("ftbextras.admin.promote")
                .arguments(GenericArguments.user(Text.of("player")))
                .executor(new TransferOwnerExecutor())
                .build();

        CommandSpec teamList = CommandSpec.builder()
                .permission("ftbextras.team.list")
                .executor(new TeamListExecutor())
                .build();

        CommandSpec onlineList = CommandSpec.builder()
                .permission("ftbextras.online")
                .executor(new OnlineListExecutor())
                .build();

        CommandSpec claimChunk = CommandSpec.builder()
                .permission("ftbextras.chunk.claim")
                .executor(new ClaimChunkExecutor())
                .arguments(
                        GenericArguments.flags().flag("f").buildWith(GenericArguments.none()),
                        GenericArguments.optionalWeak(GenericArguments.onlyOne(GenericArguments.integer(Text.of("radius")))),
                        GenericArguments.optionalWeak(GenericArguments.onlyOne(GenericArguments.string(Text.of("teamid"))))
                )
                .build();

        CommandSpec unclaimChunk = CommandSpec.builder()
                .permission("ftbextras.chunk.unclaim")
                .executor(new UnclaimChunksExecutor())
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.onlyOne(GenericArguments.string(Text.of("teamid")))),
                        GenericArguments.optionalWeak(GenericArguments.onlyOne(GenericArguments.integer(Text.of("dim"))))
                )
                .build();

        CommandSpec getTeamInfo = CommandSpec.builder()
                .permission("ftbextras.team.info")
                .arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("teamid"))))
                .executor(new TeamInfoExecutor())
                .build();

        CommandSpec teamSettings = CommandSpec.builder()
                .permission("ftbextras.admin.settings")
                .arguments(GenericArguments.string(Text.of("id")))
                .executor(new TeamSettingsExecutor())
                .build();
        /*
        CommandSpec teamChunks = CommandSpec.builder()
                .permission("ftbextras.chunk.list")
                .arguments(GenericArguments.string(Text.of("teamid")))
                .executor(new TeamChunksExecutor())
                .build();
        */
        CommandSpec teamDelete = CommandSpec.builder()
                .permission("ftbextras.admin.delete")
                .arguments(GenericArguments.string(Text.of("id")))
                .executor(new DeleteTeamExecutor())
                .build();

        CommandSpec mainCommand = CommandSpec.builder()
                .permission("ftbextras.main")
                .child(help,"help")
                .child(getTeam, "find")
                .child(teamList, "list")
                .child(claimChunk, "claim")
                .child(unclaimChunk, "unclaim")
                .child(getTeamInfo, "info")
                .child(teamSettings, "settings")
                .child(teamDelete,"delete")
                //.child(teamChunks,"chunks")
                .child(remove,"remove")
                .child(add,"add")
                .child(transfer,"promote")
                .child(onlineList,"online")
                .executor(new MainCommandExecutor())
                .build();

        Sponge.getCommandManager().register(this, mainCommand, "ft");
        logger.info("Commands registered! Plugin loaded.");

    }
}
