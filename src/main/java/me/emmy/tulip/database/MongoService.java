package me.emmy.tulip.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 15:27
 */
@Getter
public class MongoService {
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;

    /**
     * Start the MongoDB connection
     */
    public void startMongo() {
        try {
            FileConfiguration config = ConfigService.getInstance().getSettingsConfig();

            String databaseName = config.getString("mongo.database");
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Connecting to the MongoDB database..."));

            ConnectionString connectionString = new ConnectionString(Objects.requireNonNull(config.getString("mongo.uri")));
            MongoClientSettings.Builder settings = MongoClientSettings.builder();
            settings.applyConnectionString(connectionString);
            settings.applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS));
            settings.retryWrites(true);

            this.mongoClient = MongoClients.create(settings.build());
            this.mongoDatabase = mongoClient.getDatabase(databaseName);

            Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully connected to the MongoDB database."));
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cFailed to connect to the MongoDB database."));
            ServerUtil.disablePlugin();
        }
    }
}