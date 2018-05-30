package io.prodity.commons.spigot.account;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NameFetcher implements Callable<Map<UUID, NameFetcher.Lookup<NameFetcher.NameHistory>>> {

    public static class Lookup<T> {

        private final T value;
        private final Exception exception;

        public Lookup(@Nullable T value, @Nullable Exception exception) {
            this.value = value;
            this.exception = exception;
        }

        @Nullable
        public T getValue() {
            return this.value;
        }

        @Nullable
        public Exception getException() {
            return this.exception;
        }
    }

    public static class NameHistory {

        private final List<NameHistoryEntry> history;

        public NameHistory(List<NameHistoryEntry> history) {
            this.history = history;
        }

        public String getOriginalName() {
            return this.history.get(0).getName();
        }

        public String getCurrentName() {
            return this.history.get(this.history.size() - 1).getName();
        }

        public List<NameHistoryEntry> getHistory() {
            return this.history;
        }

        @Override
        public String toString() {
            return "NameHistory{" +
                "history=" + this.history +
                '}';
        }
    }

    public static class NameHistoryEntry {

        private final String name;
        private final Instant changedAt;

        public NameHistoryEntry(String name, @Nullable Instant changedAt) {
            this.name = name;
            this.changedAt = changedAt;
        }

        public String getName() {
            return this.name;
        }

        /**
         * Gets the date that the user changed to this name at.  If this NameHistoryEntry represents the user's first
         * name, this value will be null.
         *
         * @return the date the user changed to this name or null
         */
        @Nullable
        public Instant getChangedAt() {
            return this.changedAt;
        }

        @Override
        public String toString() {
            return "NameHistoryEntry{" +
                "name='" + this.name + '\'' +
                ", changedAt=" + this.changedAt +
                '}';
        }
    }

    private static final String PROFILE_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static final ThreadLocal<JSONParser> PARSER = ThreadLocal.withInitial(JSONParser::new);

    public static Optional<NameHistory> getHistory(UUID id) throws IOException, ParseException {
        List<JSONObject> raw = NameFetcher.requestHistory(id);
        if (raw == null) {
            return Optional.empty();
        }
        List<NameHistoryEntry> history = new ArrayList<>();
        for (JSONObject object : raw) {
            String name = (String) object.get("name");
            Instant changedToAt = object.containsKey("changedToAt") ? Instant.ofEpochMilli((long) object.get("changedToAt")) : null;
            history.add(new NameHistoryEntry(name, changedToAt));
        }
        history.sort(Comparator.comparing(NameHistoryEntry::getChangedAt, Comparator.nullsFirst(Comparator.naturalOrder())));
        return Optional.of(new NameHistory(Collections.unmodifiableList(history)));
    }

    @Nullable
    private static List<JSONObject> requestHistory(UUID id) throws IOException, ParseException {
        URLConnection connection = new URL(String.format(NameFetcher.PROFILE_URL, id.toString().replace("-", ""))).openConnection();
        try (InputStream stream = connection.getInputStream()) {
            String content = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
            if (content.isEmpty()) {
                return null;
            } else {
                return (List<JSONObject>) NameFetcher.PARSER.get().parse(content);
            }
        }
    }

    private final List<UUID> uuids;

    public NameFetcher(List<UUID> uuids) {
        this.uuids = ImmutableList.copyOf(uuids);
    }

    @Override
    public Map<UUID, Lookup<NameHistory>> call() {
        Map<UUID, Lookup<NameHistory>> uuidNameHistory = new HashMap<>();
        for (UUID uuid : this.uuids) {
            try {
                uuidNameHistory.put(uuid, new Lookup<>(NameFetcher.getHistory(uuid).orElse(null), null));
            } catch (Exception e) {
                uuidNameHistory.put(uuid, new Lookup<>(null, e));
            }
        }
        return uuidNameHistory;
    }
}