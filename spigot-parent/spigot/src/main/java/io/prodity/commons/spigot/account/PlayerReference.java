package io.prodity.commons.spigot.account;

import com.google.common.base.MoreObjects;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public class PlayerReference {

    public static PlayerReference of(Player player) {
        return new PlayerReference(player.getUniqueId(), player.getName(), Instant.ofEpochMilli(player.getLastPlayed()));
    }

    private final UUID id;
    private final String name;
    private final Instant lastSeen;

    @JdbiConstructor
    public PlayerReference(UUID playerId, String playerName, @Nullable Instant playerLastSeen) {
        this.id = playerId;
        this.name = playerName;
        this.lastSeen = playerLastSeen;
    }

    public PlayerReference(UUID playerId, String playerName) {
        this(playerId, playerName, Instant.now());
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Gets the last time the player logged into a server sharing the database.
     * If the player has never joined the server then the returned value is
     * null.
     *
     * @return the last time the player joined
     */
    @Nullable
    public Instant getLastSeen() {
        return this.lastSeen;
    }

    @Nullable
    public Player dereference() {
        return Bukkit.getPlayer(this.id);
    }

    public OfflinePlayer offlineDereference() {
        return Bukkit.getOfflinePlayer(this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.lastSeen);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlayerReference)) {
            return false;
        }
        final PlayerReference that = (PlayerReference) object;
        return Objects.equals(this.id, that.id) &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.lastSeen, that.lastSeen);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", this.id)
            .add("name", this.id)
            .add("lastSeen", this.lastSeen)
            .toString();
    }

}
