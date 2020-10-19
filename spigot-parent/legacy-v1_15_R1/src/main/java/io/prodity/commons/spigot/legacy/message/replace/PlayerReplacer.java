package io.prodity.commons.spigot.legacy.message.replace;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.utils.StringUtils;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlayerReplacer extends Replacer {

    public static PlayerReplacer create() {
        return new PlayerReplacer();
    }

    public static PlayerReplacer create(Replacer replacer) {
        return new PlayerReplacer(replacer);
    }

    public static PlayerReplacer createWithPapi() {
        return new PlayerReplacer().papi(true);
    }

    public static PlayerReplacer createWithPapi(Replacer replacer) {
        return new PlayerReplacer(replacer).papi(true);
    }

    @Getter
    private final Map<String, Function<Player, String>> playerMap;

    protected PlayerReplacer() {
        super();
        this.playerMap = Maps.newHashMap();
    }

    protected PlayerReplacer(Replacer replacer) {
        super(replacer);
        if (replacer instanceof PlayerReplacer) {
            final PlayerReplacer playerReplacer = (PlayerReplacer) replacer;
            this.playerMap = Maps.newHashMap(playerReplacer.playerMap);
        } else {
            this.playerMap = Maps.newHashMap();
        }
        this.papi(replacer.isPapi());
    }

    @Override
    public PlayerReplacer papi(boolean papi) {
        super.papi(true);
        return this;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && this.playerMap.isEmpty();
    }

    @Override
    public PlayerReplacer addAll(Replacer replacer) {
        if (replacer instanceof PlayerReplacer) {
            this.playerMap.putAll(((PlayerReplacer) replacer).playerMap);
        }
        super.addAll(replacer);
        return this;
    }

    @Override
    public PlayerReplacer add(String target, Supplier<Object> replacement) {
        super.add(target, replacement);
        return this;
    }

    @Override
    public PlayerReplacer add(String target, Object replacement) {
        super.add(target, replacement);
        return this;
    }

    @Override
    public PlayerReplacer add(String target, String replacement) {
        super.add(target, replacement);
        return this;
    }

    @Override
    public PlayerReplacer clear() {
        super.clear();
        return this;
    }

    @Override
    public PlayerReplacer remove(String... targets) {
        for (String target : targets) {
            this.playerMap.remove(target);
        }
        super.remove(targets);
        return this;
    }

    @Override
    public PlayerReplacer remove(Iterable<String> targets) {
        for (String target : targets) {
            this.playerMap.remove(target);
        }
        super.remove(targets);
        return this;
    }

    public PlayerReplacer add(String target, Function<Player, String> replacement) {
        this.playerMap.put(target, replacement);
        return this;
    }

    @Override
    public List<String> replaceList(CommandSender sender, List<String> messages) {
        if (sender instanceof Player) {
            return this.replaceList((Player) sender, messages);
        }
        return super.replaceList(sender, messages);
    }

    public List<String> replaceList(Player player, List<String> messages) {
        if (messages == null || messages.isEmpty() || this.isEmpty()) {
            return Lists.newArrayList();
        }
        final List<String> list = Lists.newArrayList();
        for (String message : messages) {
            list.add(this.replace(player, message));
        }
        return list;
    }

    @Override
    public String replace(CommandSender sender, String message) {
        if (sender instanceof Player) {
            return this.replace((Player) sender, message);
        }
        return super.replace(sender, message);
    }

    public String replace(Player player, String message) {
        if (message == null || message.isEmpty() || this.isEmpty()) {
            return message;
        }
        message = this.replace(message);
        for (Entry<String, Function<Player, String>> entry : this.playerMap.entrySet()) {
            final Supplier<String> supplier = () -> entry.getValue().apply(player);
            message = StringUtils.fastReplace(message, entry.getKey(), supplier);
        }
        return message;
    }

    @Override
    public PlayerReplacer clone() {
        return PlayerReplacer.create(this);
    }

}
