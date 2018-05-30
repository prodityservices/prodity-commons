package io.prodity.commons.spigot.legacy.message.replace;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.placeholder.PlaceholderHelper;
import io.prodity.commons.spigot.legacy.utils.StringUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Replacer implements Cloneable {

    public static Supplier<String> supplyObject(Object object) {
        return () -> String.valueOf(object);
    }

    public static Replacer create() {
        return new Replacer();
    }

    public static Replacer create(Replacer replacer) {
        return new Replacer(replacer);
    }

    public static Replacer createWithPapi() {
        return new Replacer().papi(true);
    }

    public static Replacer createWithPapi(Replacer replacer) {
        return new Replacer(replacer).papi(true);
    }

    private final Map<String, Supplier<String>> map;
    private boolean papi = false;

    protected Replacer() {
        this.map = Maps.newHashMap();
    }

    protected Replacer(Replacer replacer) {
        this.map = Maps.newHashMap(replacer.map);
        this.papi = replacer.papi;
    }

    public boolean isPapi() {
        return this.papi;
    }

    public Replacer papi(boolean papi) {
        this.papi = papi;
        return this;
    }

    public Replacer addAll(Replacer replacer) {
        this.map.putAll(replacer.map);
        return this;
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public Replacer add(String target, Supplier<String> replacement) {
        this.map.put(target, replacement);
        return this;
    }

    public Replacer add(String target, String replacement) {
        this.map.put(target, () -> replacement);
        return this;
    }

    public boolean contains(String target) {
        return this.map.containsKey(target);
    }

    public Replacer clear() {
        this.map.clear();
        return this;
    }

    public Replacer remove(String... targets) {
        for (String target : targets) {
            this.map.remove(target);
        }
        return this;
    }

    public Replacer remove(Iterable<String> targets) {
        for (String target : targets) {
            this.map.remove(target);
        }
        return this;
    }

    public Map<String, Supplier<String>> getReplacements() {
        return Maps.newHashMap(this.map);
    }

    public List<String> replaceList(List<String> messages) {
        final List<String> list = Lists.newArrayList();
        if (messages == null || messages.isEmpty() || this.isEmpty()) {
            return list;
        }
        for (String message : messages) {
            list.add(this.replace(message));
        }
        return list;
    }

    public List<String> replaceList(CommandSender commandSender, List<String> messages) {
        final List<String> replacedList = this.replaceList(messages);
        if (this.papi && commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            return PlaceholderHelper.setPlaceholders(player, replacedList);
        }
        return replacedList;
    }

    public String replace(CommandSender commandSender, String message) {
        final String replacedMessage = this.replace(message);
        if (this.papi && commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            return PlaceholderHelper.setPlaceholders(player, replacedMessage);
        }
        return replacedMessage;
    }

    public String replace(String message) {
        if (message == null || message.isEmpty() || this.isEmpty()) {
            return message;
        }
        for (Entry<String, Supplier<String>> entry : this.map.entrySet()) {
            message = StringUtils.fastReplace(message, entry.getKey(), entry.getValue());
        }
        return message;
    }

    public PlayerReplacer forPlayer() {
        return PlayerReplacer.create(this);
    }

    public PlayerReplacer forPlayerWithPapi() {
        return PlayerReplacer.createWithPapi(this);
    }

    @Override
    public Replacer clone() {
        return Replacer.create(this);
    }

}