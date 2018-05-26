package io.prodity.commons.spigot.account;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

public class PlayerReference {
    private final UUID playerId;
    private final String playerName;
    private final Instant lastSeen;

    public PlayerReference(UUID playerId, String playerName, @Nullable Instant lastSeen) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.lastSeen = lastSeen;
    }

    public PlayerReference(UUID playerId, String playerName) {
        this(playerId, playerName, Instant.now());
    }

    public UUID getId() {
        return this.playerId;
    }

    public String getName() {
        return this.playerName;
    }

    /**
     * Gets the last time the player logged into a server sharing the database.
     * If the player has never joined the server then the returned value is
     * null.
     *
     * @return the last time the player joined
     */
    @Nullable
    public Instant getLastJoin() {
        return this.lastSeen;
    }

    @Nullable
    public Player dereference() {
        return Bukkit.getPlayer(this.playerId);
    }

    public static PlayerReference of(Player player) {
        return new PlayerReference(player.getUniqueId(), player.getName(), Instant.ofEpochMilli(player.getLastPlayed()));
    }
}
