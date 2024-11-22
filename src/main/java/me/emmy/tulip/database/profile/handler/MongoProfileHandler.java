package me.emmy.tulip.database.profile.handler;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.database.profile.IProfile;
import me.emmy.tulip.database.serializer.ItemStackSerializer;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 18:25
 */
public class MongoProfileHandler implements IProfile {

    /**
     * Load a profile
     *
     * @param profile the profile to load
     */
    @SuppressWarnings("unchecked")
    public void loadProfile(Profile profile) {
        Document document = Tulip.getInstance().getProfileRepository().getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.assignDefaultKitLayout(profile);
            this.saveProfile(profile);
            return;
        }

        profile.setName(document.getString("name"));
        profile.setOnline(document.getBoolean("online"));
        profile.getCoins().setCoins(document.getInteger("coins"));
        profile.setOwnedProducts((List<String>) document.get("ownedProducts"));

        Document settingsDocument = (Document) document.get("settings");
        profile.getSettings().setShowScoreboard(settingsDocument.getBoolean("showScoreboard"));
        profile.getSettings().setShowTablist(settingsDocument.getBoolean("showTablist"));

        this.statsDocumentToProfile(profile, document);
        this.layoutDocumentToProfile(profile, document);
    }

    /**
     * Save a profile
     *
     * @param profile the profile to save
     */
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getName());
        document.put("online", profile.isOnline());
        document.put("coins", profile.getCoins().getCoins());
        document.put("ownedProducts", profile.getOwnedProducts());

        Document settingsDocument = new Document();
        settingsDocument.put("showScoreboard", profile.getSettings().isShowScoreboard());
        settingsDocument.put("showTablist", profile.getSettings().isShowTablist());
        document.put("settings", settingsDocument);

        Document statsDocument = new Document();

        FFARepository ffaRepository = Tulip.getInstance().getFfaRepository();
        Map<String, Integer> killsMap = new HashMap<>();
        Map<String, Integer> deathsMap = new HashMap<>();
        Map<String, Integer> highestKillstreakMap = this.statsToDocument(profile, ffaRepository, killsMap, deathsMap);

        statsDocument.put("kills", killsMap);
        statsDocument.put("deaths", deathsMap);
        statsDocument.put("highestKillstreak", highestKillstreakMap);

        document.put("stats", statsDocument);

        Document kitLayoutsDoc = this.kitsToDocument(profile);
        document.put("kitLayouts", kitLayoutsDoc);

        Tulip.getInstance().getProfileRepository().getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Stats document to profile
     *
     * @param profile the profile to layout the stats document to
     * @param document the document to layout to the profile
     */
    private void statsDocumentToProfile(Profile profile, Document document) {
        Document statsDocument = (Document) document.get("stats");
        if (statsDocument != null) {
            Map<Kit, Integer> kitKills = new HashMap<>();
            Map<Kit, Integer> kitDeaths = new HashMap<>();
            Map<Kit, Integer> highestKillstreak = new HashMap<>();

            Document killsDoc = (Document) statsDocument.get("kills");
            Document deathsDoc = (Document) statsDocument.get("deaths");
            Document highestKillstreakDoc = (Document) statsDocument.get("highestKillstreak");

            if (killsDoc != null) {
                for (Map.Entry<String, Object> entry : killsDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getKitRepository().getKit(entry.getKey());
                    if (kit != null) {
                        kitKills.put(kit, (Integer) entry.getValue());
                    }
                }
            }

            if (deathsDoc != null) {
                for (Map.Entry<String, Object> entry : deathsDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getKitRepository().getKit(entry.getKey());
                    if (kit != null) {
                        kitDeaths.put(kit, (Integer) entry.getValue());
                    }
                }
            }

            if (highestKillstreakDoc != null) {
                for (Map.Entry<String, Object> entry : highestKillstreakDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getKitRepository().getKit(entry.getKey());
                    if (kit != null) {
                        highestKillstreak.put(kit, (Integer) entry.getValue());
                    }
                }
            }

            profile.getStats().setKitKills(kitKills);
            profile.getStats().setKitDeaths(kitDeaths);
            profile.getStats().setHighestKillstreak(highestKillstreak);
        }
    }

    /**
     * Layout a document to a profile
     *
     * @param profile the profile to layout the document to
     * @param document the document to layout to the profile
     */
    private void layoutDocumentToProfile(Profile profile, Document document) {
        Document kitLayoutsDoc = (Document) document.get("kitLayouts");
        if (kitLayoutsDoc != null) {
            for (Kit kit : Tulip.getInstance().getKitRepository().getKits()) {
                String kitName = kit.getName();
                Document layoutDoc = (Document) kitLayoutsDoc.get(kitName);
                if (layoutDoc != null) {
                    ItemStack[] items = ItemStackSerializer.deserializeItemStackArray(layoutDoc.getString("items"));
                    profile.getKitLayout().setLayout(kitName, items);
                }
            }
        } else {
            assignDefaultKitLayout(profile);
        }
    }

    /**
     * Assign the default kit layout
     *
     * @param profile the profile to assign the default kit layout to
     */
    private void assignDefaultKitLayout(Profile profile) {
        for (Kit kit : Tulip.getInstance().getKitRepository().getKits()) {
            profile.getKitLayout().setLayout(kit.getName(), kit.getItems());
        }
    }

    /**
     * Convert stats to a document
     *
     * @param profile the profile to convert the stats to a document for
     * @param ffaRepository the FFA repository
     * @param killsMap the kills map
     * @param deathsMap the deaths map
     * @return the document
     */
    private @NotNull Map<String, Integer> statsToDocument(Profile profile, FFARepository ffaRepository, Map<String, Integer> killsMap, Map<String, Integer> deathsMap) {
        Map<String, Integer> highestKillstreakMap = new HashMap<>();

        for (AbstractFFAMatch match : ffaRepository.getMatches()) {
            Kit kit = match.getKit();
            String kitName = kit.getName();

            Integer kills = profile.getStats().getKitKills().getOrDefault(kit, 0);
            killsMap.put(kitName, kills);

            Integer deaths = profile.getStats().getKitDeaths().getOrDefault(kit, 0);
            deathsMap.put(kitName, deaths);

            Integer highestKillstreak = profile.getStats().getHighestKillstreak().getOrDefault(kit, 0);
            highestKillstreakMap.put(kitName, highestKillstreak);
        }
        return highestKillstreakMap;
    }

    /**
     * Convert kits to a document
     *
     * @param profile the profile to convert the kits to a document for
     * @return the document
     */
    private @NotNull Document kitsToDocument(Profile profile) {
        Document kitLayoutsDoc = new Document();
        for (Kit kit : Tulip.getInstance().getKitRepository().getKits()) {
            String kitName = kit.getName();

            Document layoutDoc = new Document();
            layoutDoc.put("items", ItemStackSerializer.serializeItemStackArray(profile.getKitLayout().getLayout(kitName)));

            kitLayoutsDoc.put(kitName, layoutDoc);
        }
        return kitLayoutsDoc;
    }
}