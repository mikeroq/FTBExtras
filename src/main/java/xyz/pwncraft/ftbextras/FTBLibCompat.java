package xyz.pwncraft.ftbextras;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.OptionalInt;

public class FTBLibCompat {
    private static class FTBLibCompatLoader {
        private static FTBLibCompat INSTANCE;

        static {
            try {
                INSTANCE = new FTBLibCompat();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
    public static FTBLibCompat getInstance() {
        return FTBLibCompatLoader.INSTANCE;
    }


    private static final String[] getIdMethodAliases = new String[] { "getId", "getID" };
    private Method getIdMethod = null;
    private Method unclaimChunkMethod;
    private Method unclaimAllChunksMethod;
    private boolean useTwoArgs;
    private boolean useThreeArgs;

    // Keep this private, access with CompatLayer.getInstance() instead!
    private FTBLibCompat() throws NoSuchMethodException {
        for(String getIdMethodAlias : getIdMethodAliases) {
            try {
                getIdMethod = ForgeTeam.class.getMethod(getIdMethodAlias);
                break;
            } catch (NoSuchMethodException e) {
                System.out.println(String.format("%s not found on object ForgeTeam. Trying next alias.", getIdMethodAlias));
            }
        }
        if (getIdMethod == null) throw new NoSuchMethodException("Blame TechFrog. No teamID for you!");
        try {
            unclaimChunkMethod = ClaimedChunks.class.getMethod("unclaimChunk", ChunkDimPos.class);
        } catch (NoSuchMethodException e) {
            unclaimChunkMethod = ClaimedChunks.class.getMethod("unclaimChunk", ForgePlayer.class, ChunkDimPos.class);
            useTwoArgs = true;
        }
        try {
            unclaimAllChunksMethod = ClaimedChunks.class.getMethod("unclaimAllChunks", ForgeTeam.class, OptionalInt.class );
        } catch (NoSuchMethodException e) {
            unclaimAllChunksMethod = ClaimedChunks.class.getMethod("unclaimAllChunks", ForgePlayer.class, ForgeTeam.class, OptionalInt.class );
            useThreeArgs = true;
        }
    }

    public String getId(ForgeTeam team) throws IllegalAccessException, InvocationTargetException {
        return (String) getIdMethod.invoke(team);
    }
    public void unclaimChunk(ForgePlayer player, ChunkDimPos pos) throws IllegalAccessException, InvocationTargetException {
        if (useTwoArgs) unclaimChunkMethod.invoke(ClaimedChunks.instance, player, pos);
        else unclaimChunkMethod.invoke(ClaimedChunks.instance, pos);
    }
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void unclaimAllChunks(ForgePlayer player, ForgeTeam team, OptionalInt dim) throws IllegalAccessException, InvocationTargetException {
        if (useThreeArgs) unclaimAllChunksMethod.invoke(ClaimedChunks.instance, player, team, dim);
        else unclaimAllChunksMethod.invoke(ClaimedChunks.instance, team, dim);
    }
}
