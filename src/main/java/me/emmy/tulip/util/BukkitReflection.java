package me.emmy.tulip.util;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author I dont know
 * @project FFA
 * @date 5/26/2024
 */
public class BukkitReflection {
    private static final String CRAFT_BUKKIT_PACKAGE;
    private static final String NET_MINECRAFT_SERVER_PACKAGE;

    private static final Class CRAFT_SERVER_CLASS;
    private static final Method CRAFT_SERVER_GET_HANDLE_METHOD;

    private static final Class PLAYER_LIST_CLASS;
    private static final Field PLAYER_LIST_MAX_PLAYERS_FIELD;

    private static final Class CRAFT_PLAYER_CLASS;
    private static final Method CRAFT_PLAYER_GET_HANDLE_METHOD;

    private static final Class ENTITY_PLAYER_CLASS;
    private static final Field ENTITY_PLAYER_PING_FIELD;

    private static final Class CRAFT_ITEM_STACK_CLASS;
    private static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD;
    private static final Class ENTITY_ITEM_STACK_CLASS;
    private static final Method ENTITY_ITEM_STACK_GET_NAME;

    private static final Class SPIGOT_CONFIG_CLASS;
    private static final Field SPIGOT_CONFIG_BUNGEE_FIELD;

    static {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

            CRAFT_BUKKIT_PACKAGE = "org.bukkit.craftbukkit." + version + ".";
            NET_MINECRAFT_SERVER_PACKAGE = "net.minecraft.server." + version + ".";

            CRAFT_SERVER_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "CraftServer");
            CRAFT_SERVER_GET_HANDLE_METHOD = CRAFT_SERVER_CLASS.getDeclaredMethod("getHandle");
            CRAFT_SERVER_GET_HANDLE_METHOD.setAccessible(true);

            PLAYER_LIST_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "PlayerList");
            PLAYER_LIST_MAX_PLAYERS_FIELD = PLAYER_LIST_CLASS.getDeclaredField("maxPlayers");
            PLAYER_LIST_MAX_PLAYERS_FIELD.setAccessible(true);

            CRAFT_PLAYER_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "entity.CraftPlayer");
            CRAFT_PLAYER_GET_HANDLE_METHOD = CRAFT_PLAYER_CLASS.getDeclaredMethod("getHandle");
            CRAFT_PLAYER_GET_HANDLE_METHOD.setAccessible(true);

            ENTITY_PLAYER_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "EntityPlayer");
            ENTITY_PLAYER_PING_FIELD = ENTITY_PLAYER_CLASS.getDeclaredField("ping");
            ENTITY_PLAYER_PING_FIELD.setAccessible(true);

            CRAFT_ITEM_STACK_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "inventory.CraftItemStack");
            CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD =
                    CRAFT_ITEM_STACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class);
            CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.setAccessible(true);

            ENTITY_ITEM_STACK_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "ItemStack");
            ENTITY_ITEM_STACK_GET_NAME = ENTITY_ITEM_STACK_CLASS.getDeclaredMethod("getName");

            SPIGOT_CONFIG_CLASS = Class.forName("org.spigotmc.SpigotConfig");
            SPIGOT_CONFIG_BUNGEE_FIELD = SPIGOT_CONFIG_CLASS.getDeclaredField("bungee");
            SPIGOT_CONFIG_BUNGEE_FIELD.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException("Failed to initialize Bukkit/NMS Reflection");
        }
    }

    /**
     * Gets a field from a class.
     *
     * @param holderClass the class to get the field from
     * @param fieldName   the name of the field to get
     * @return the field
     */
    public static @NonNull Field needField(final @NonNull Class<?> holderClass, final @NonNull String fieldName) {
        final Field field;
        try {
            field = holderClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw new IllegalStateException(String.format("Unable to find field '%s' in class '%s'", fieldName, holderClass.getCanonicalName()), e);
        }
    }

    /**
     * Invokes a method handle with the given parameters.
     *
     * @param methodHandle the method handle to invoke
     * @param params       the parameters to invoke the method handle with
     * @return the result of the method handle invocation
     */
    private static Object invokeOrThrow(final @NonNull MethodHandle methodHandle, final Object @NonNull ... params) {
        try {
            if (params.length == 0) {
                return methodHandle.invoke();
            }
            return methodHandle.invokeWithArguments(params);
        } catch (final Throwable throwable) {
            throw new IllegalStateException(String.format("Unable to invoke method with args '%s'", Arrays.toString(params)), throwable);
        }
    }

    /**
     * Gets the ping of a player.
     *
     * @param player the player to get the ping of
     * @return the ping of the player
     */
    public static int getPing(Player player) {
        try {
            int ping = ENTITY_PLAYER_PING_FIELD.getInt(CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player));

            return Math.max(ping, 0);
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Sets the max players of the server.
     *
     * @param server the server to set the max players of
     * @param slots  the amount of slots to set
     */
    public static void setMaxPlayers(Server server, int slots) {
        try {
            PLAYER_LIST_MAX_PLAYERS_FIELD.set(CRAFT_SERVER_GET_HANDLE_METHOD.invoke(server), slots);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the server is a BungeeCord server.
     *
     * @return true if the server is a BungeeCord server, otherwise false
     */
    public static boolean isBungeeServer() {
        try {
            return (boolean) SPIGOT_CONFIG_BUNGEE_FIELD.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}