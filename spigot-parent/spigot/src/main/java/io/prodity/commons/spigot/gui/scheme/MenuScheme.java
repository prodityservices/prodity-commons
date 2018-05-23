package io.prodity.commons.spigot.gui.scheme;

import com.google.common.collect.ImmutableList;
import io.prodity.commons.spigot.gui.Gui;
import io.prodity.commons.spigot.gui.scheme.mapping.SchemeMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuScheme implements Cloneable {

    private static final boolean[] EMPTY_MASK = new boolean[]{false, false, false, false, false, false, false, false, false};
    private static final int[] EMPTY_SCHEME = new int[0];

    private final SchemeMapping mapping;
    private final List<boolean[]> maskRows;
    private final List<int[]> schemeRows;

    public MenuScheme(SchemeMapping mapping) {
        this.mapping = mapping == null ? SchemeMapping.empty() : mapping;
        this.maskRows = new ArrayList<>();
        this.schemeRows = new ArrayList<>();
    }

    public MenuScheme() {
        this((SchemeMapping) null);
    }

    private MenuScheme(MenuScheme other) {
        this.mapping = other.mapping.clone();
        this.maskRows = new ArrayList<>();
        for (boolean[] arr : other.maskRows) {
            this.maskRows.add(Arrays.copyOf(arr, arr.length));
        }
        this.schemeRows = new ArrayList<>();
        for (int[] arr : other.schemeRows) {
            this.schemeRows.add(Arrays.copyOf(arr, arr.length));
        }
    }

    public MenuScheme mask(String string) throws IllegalArgumentException {
        final char[] chars = string.replace(" ", "").toCharArray();
        if (chars.length != 9) {
            throw new IllegalArgumentException("invalid mask: " + string);
        }
        boolean[] ret = new boolean[9];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '1' || c == 't') {
                ret[i] = true;
            } else if (c == '0' || c == 'f' || c == 'x') {
                ret[i] = false;
            } else {
                throw new IllegalArgumentException("invalid mask character: " + c);
            }
        }
        this.maskRows.add(ret);
        return this;
    }

    public MenuScheme mask(String... strings) {
        for (String s : strings) {
            this.mask(s);
        }
        return this;
    }

    public MenuScheme maskEmpty(int lines) {
        for (int i = 0; i < lines; i++) {
            this.maskRows.add(EMPTY_MASK);
            this.schemeRows.add(EMPTY_SCHEME);
        }
        return this;
    }

    public MenuScheme scheme(int... schemeIds) {
        for (int schemeId : schemeIds) {
            if (!this.mapping.hasMappingFor(schemeId)) {
                throw new IllegalArgumentException("mapping does not contain value for id: " + schemeId);
            }
        }
        this.schemeRows.add(schemeIds);
        return this;
    }

    public void apply(Gui<?> gui) {
        // the index of the item slot in the inventory
        int invIndex = 0;

        // iterate all of the loaded masks
        for (int i = 0; i < this.maskRows.size(); i++) {
            final boolean[] mask = this.maskRows.get(i);
            final int[] scheme = this.schemeRows.get(i);

            int schemeIndex = 0;

            // iterate the values in the mask (0 --> 8)
            for (boolean b : mask) {

                // increment the index in the gui. we're handling a new item.
                int index = invIndex++;

                // if this index is masked.
                if (b) {

                    // this is the value from the scheme map for this slot.
                    int schemeMappingId = scheme[schemeIndex++];

                    // lookup the value for this location, and apply it to the gui
                    this.mapping.get(schemeMappingId).ifPresent(item -> gui.setItem(item, index));
                }
            }
        }
    }

    public List<Integer> getMaskedIndexes() {
        final List<Integer> indexes = new ArrayList<>();

        int index = 0;

        for (boolean[] mask : this.maskRows) {
            for (boolean shouldMask : mask) {
                if (shouldMask) {
                    indexes.add(index);
                }
                index++;
            }
        }

        return indexes;
    }

    public ImmutableList<Integer> getMaskedIndexesImmutable() {
        return ImmutableList.copyOf(this.getMaskedIndexes());
    }

    public MenuPopulator populator(Gui<?> gui) {
        return new MenuPopulator(gui, this);
    }

    @Override
    public MenuScheme clone() {
        return new MenuScheme(this);
    }

}