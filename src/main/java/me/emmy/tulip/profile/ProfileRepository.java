package me.emmy.tulip.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.database.profile.IProfile;
import me.emmy.tulip.database.profile.handler.MongoProfileHandler;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 18:27
 */
@Getter
public class ProfileRepository {
    private final Map<UUID, Profile> profiles = new HashMap<>();
    public MongoCollection<Document> collection;
    public final IProfile profile;

    public ProfileRepository() {
        this.profile = new MongoProfileHandler();
    }

    public void initializeEveryProfile() {
        this.collection = Tulip.getInstance().getMongoService().getMongoDatabase().getCollection("profiles");
        this.collection.find().forEach(this::loadProfile);
    }

    /**
     * Load a profile from a document
     *
     * @param document the document to load the profile from
     */
    private void loadProfile(Document document) {
        UUID uuid = UUID.fromString(document.getString("uuid"));
        Profile profile = new Profile(uuid);
        profile.loadProfile();

        profiles.put(profile.getUuid(), profile);
    }

    /**
     * Get a profile by its UUID without adding it to the map if it doesn't exist
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfileWithNoAdding(UUID uuid) {
        return profiles.get(uuid);
    }

    /**
     * Get a profile by its UUID and add it to the map if it doesn't exist
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfile(UUID uuid) {
        if (!profiles.containsKey(uuid)) {
            Profile profile = new Profile(uuid);
            profile.loadProfile();
            addProfile(uuid, profile);
            return profile;
        }

        return profiles.get(uuid);
    }

    /**
     * Add or update a profile by its UUID.
     *
     * @param uuid    the UUID of the profile
     * @param profile the profile to add or update
     */
    public void addProfile(UUID uuid, Profile profile) {
        profiles.put(uuid, profile);
    }

    /**
     * Remove a profile by its UUID.
     *
     * @param uuid the UUID of the profile to remove
     */
    public void removeProfile(UUID uuid) {
        profiles.remove(uuid);
    }
}