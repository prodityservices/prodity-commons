package example;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.UUID;

// An implementation of this interface is automatically generated
// The table is created automatically by src/main/resources/db/migrations
public interface RenownDao {

    @SqlQuery("SELECT `player_renown` FROM `renown` WHERE `player_id`= :player")
    int getRenown(UUID player);

    @SqlUpdate(
            "INSERT INTO `renown`(`player_id`,`player_renown`) VALUES(:player, :value) " +
            "ON DUPLICATE KEY UPDATE `player_renown`=`player_renown` + :value")
    void incrementRenown(UUID player, int value);
}
