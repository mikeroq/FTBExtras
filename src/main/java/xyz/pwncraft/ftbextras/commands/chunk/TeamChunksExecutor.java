package xyz.pwncraft.ftbextras.commands.chunk;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class TeamChunksExecutor implements CommandExecutor {



    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        /* Disabled for now until it works better
        ForgeTeam team = Universe.get().getTeam(args.<String>getOne("teamid").get());
        if (!team.isValid()) throw new CommandException(Util.format("[&6FTBExtras&r] Could not find team!"));

        FTBUtilitiesTeamData data = FTBUtilitiesTeamData.get(team);
        Task.Builder taskBuilder = Task.builder().async();

        taskBuilder.execute(
                () -> {

                    int max = data.getMaxClaimChunks();
                    int claimed = ClaimedChunks.instance.getTeamChunks(team, OptionalInt.empty(), true ).size();
                    List<Text> contents = new ArrayList<>();
                    List<ClaimedChunk> oldClaims = new ArrayList<>(ClaimedChunks.instance.getTeamChunks(team, OptionalInt.empty()));

                    final int range = 10;
                    List<ClaimedChunk> newClaims = new ArrayList<>();
                    oldClaims.forEach(chunkA -> {
                        final AtomicBoolean add = new AtomicBoolean(true);
                        newClaims.forEach(ChunkB -> {
                            if (chunkA == ChunkB || chunkA.getPos().dim != ChunkB.getPos().dim) return;
                            final int diffX = chunkA.getPos().posX - ChunkB.getPos().posX;
                            final int diffY = chunkA.getPos().posZ - ChunkB.getPos().posZ;
                            if (diffX > -range && diffX < range && diffY > -range && diffY < range) add.set(false);
                        });
                        if (add.get()) newClaims.add(chunkA);
                    });

                    for (ClaimedChunk c : newClaims)
                    {

                        Text chunkInfo = Text.builder()
                                .append(Util.format("&6Dim: &r" + c.getPos().dim + " &6Coords: &r" + c.getPos().posX + ", " + c.getPos().posZ))
                                .onClick(TextActions.executeCallback(dummySrc -> Util.tpChunk(src, c.getPos())))
                                .onHover(TextActions.showText(Text.builder()
                                        .append(Text.of("Click here to teleport to chunk!"))
                                        .build()))
                                .build();
                        contents.add(chunkInfo);
                    }
                    String teamId = null;
                    try {
                        teamId = FTBLibCompat.getInstance().getId(team);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    PaginationList.builder()
                            .title(Util.format("[&6Chunks of Team: &r" + teamId + "]"))
                            .header(Util.format("&6Claimed Chunks: &r" + claimed + "/" + max))
                            .contents(contents)
                            .padding(Text.of("-"))
                            .sendTo(src);
                }
        ).submit(Sponge.getPluginManager().getPlugin("ftb-extra-utils").get().getInstance().get());
        */
        return CommandResult.success();
    }
}

