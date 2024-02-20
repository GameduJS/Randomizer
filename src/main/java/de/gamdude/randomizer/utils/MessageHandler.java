package de.gamdude.randomizer.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageHandler {

    @NotNull
    private static final ResourceBundle DEFAULT_RECOURSE_BUNDLE = ResourceBundle.getBundle("messages");
    private static final Map<Locale, ResourceBundle> LOCALE_RESOURCE_BUNDLE_MAP = new HashMap<>();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static void sendMessage(Player player, String key, String... placeHolders) {
        player.sendMessage(getMessage(player, key, placeHolders));
    }

    public static Component getMessage(Player player, String key, String... placeHolders) {
        return MINI_MESSAGE.deserialize(getString(player, key, placeHolders));
    }

    public static String getString(Player player, String key, String... placeHolders) {
        Locale playerLocale = player.locale();

        if(!LOCALE_RESOURCE_BUNDLE_MAP.containsKey(playerLocale)) // player updated language without rejoining
            registerLanguage(player);
        ResourceBundle resourceBundle = LOCALE_RESOURCE_BUNDLE_MAP.getOrDefault(playerLocale, DEFAULT_RECOURSE_BUNDLE);


        String messageString = resourceBundle.getString(key);
        for(String placeHolder : placeHolders)
            messageString = messageString.replaceFirst("%d", placeHolder);
        return messageString;
    }

    public static void registerLanguage(Player player) {
        Locale locale = player.locale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DEFAULT_RECOURSE_BUNDLE.getBaseBundleName(), locale);
        LOCALE_RESOURCE_BUNDLE_MAP.put(locale, Objects.requireNonNullElse(resourceBundle, DEFAULT_RECOURSE_BUNDLE));
    }

}
