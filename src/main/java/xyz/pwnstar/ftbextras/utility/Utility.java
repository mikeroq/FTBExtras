package xyz.pwnstar.ftbextras.utility;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftbutilities.data.ClaimResult;
import com.feed_the_beast.ftbutilities.data.ClaimedChunk;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class Utility {

    public static String getTimeStringFromSeconds(long time) {
        time = Math.abs(time);
        long sec = time % 60;
        long min = (time / 60) % 60;
        long hour = (time / 3600) % 24;
        long day = time / 86400;


        if (time == 0) {
            return "Moments ago";
        }

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append(" ");
            if (day > 1) {
                sb.append("days");
            } else {
                sb.append("day");
            }
        }

        if (hour > 0) {
            sb.append(", ");
            sb.append(hour).append(" ");
            if (hour > 1) {
                sb.append("hours");
            } else {
                sb.append("hour");
            }
        }

        if (min > 0) {
            sb.append(", ");
            sb.append(min).append(" ");
            if (min > 1) {
                sb.append("minutes");
            } else {
                sb.append("minute");
            }
        }

        if (sec > 0) {
            sb.append(", ");
            sb.append(sec).append(" ");
            if (sec > 1) {
                sb.append("seconds");
            } else {
                sb.append("second");
            }
        }

        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "unknown";
        }
    }
    public static String getLastPlayedTime(UUID uuid)
    {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!userStorage.isPresent()) return null;
        Optional<User> user = userStorage.get().get(uuid);
        return user.map(value -> getTimeStringFromSeconds(Instant.now().getEpochSecond() - value.get(Keys.LAST_DATE_PLAYED).get().getEpochSecond())).orElse(null);
    }

    public static Text format(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string);
    }

    public static void tpPlayer(CommandSource src, Optional<User> user) {
        World world = Sponge.getServer().loadWorld(user.flatMap(User::getWorldUniqueId).get()).get();

        double x = user.get().getPosition().getX();
        double y = user.get().getPosition().getY();
        double z = user.get().getPosition().getZ();

        Location<World> loc = new Location<>(world, x, y, z);

        Player srcPlayer = (Player) src;
        EntityPlayerMP p = (EntityPlayerMP) src;

        srcPlayer.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
        srcPlayer.setLocation(loc);
        if (user.get().isOnline()) src.sendMessage(Utility.format("[&6FTBExtras&r] Teleported to " + user.get().getName() + "."));
        else src.sendMessage(Utility.format("[&6FTBExtras&r] Teleported to the last location of " + user.get().getName() + "."));

    }

    public static void tpChunk(CommandSource src, ChunkDimPos pos) {
        World world = Sponge.getServer().loadWorld(Sponge.getServer().getAllWorldProperties().stream().filter(x -> x.getAdditionalProperties().getInt(DataQuery.of("SpongeData", "dimensionId")).get() == pos.dim).findFirst().get()).get();
        double x = pos.posX;
        double y = 125;
        double z = pos.posZ;

        x = x * 16 + 8;
        z = z * 16 + 8;
        Location<World> loc = new Location<>(world, x, y, z);
        Player srcPlayer = (Player) src;

        srcPlayer.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
        srcPlayer.setLocation(loc);
        src.sendMessage(Utility.format("[&6FTBExtras&r] Teleported to chunk " + pos.posX + ", " + pos.posZ + " in " + world.getName() + "."));
    }

    public static Optional<User> getUser(UUID uuid) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.get().get(uuid);
    }

    public static ClaimResult claimChunk(ForgePlayer player, ChunkDimPos pos)
    {
        if (!player.hasTeam())
        {
            return ClaimResult.NO_TEAM;
        }

        FTBUtilitiesTeamData data = FTBUtilitiesTeamData.get(player.team);

        ClaimedChunk chunk = ClaimedChunks.instance.getChunk(pos);

        if (chunk != null)
        {
            return ClaimResult.ALREADY_CLAIMED;
        }

        chunk = new ClaimedChunk(pos, data);
        ClaimedChunks.instance.addChunk(chunk);
        return ClaimResult.SUCCESS;
    }


}
