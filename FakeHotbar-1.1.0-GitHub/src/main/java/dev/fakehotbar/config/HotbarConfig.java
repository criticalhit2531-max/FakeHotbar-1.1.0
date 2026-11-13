package dev.fakehotbar.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class HotbarConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("fakehotbar.json");

    // displayOrder[position] = real inventory slot number (1..9).
    private static int[] displayOrder = {4, 5, 3, 1, 6, 7, 2, 8, 9};

    private HotbarConfig() {}

    public static int[] getDisplayOrder() {
        return Arrays.copyOf(displayOrder, displayOrder.length);
    }

    public static int positionOfActualSlot(int actualSlotNumber) {
        for (int position = 0; position < displayOrder.length; position++) {
            if (displayOrder[position] == actualSlotNumber) {
                return position;
            }
        }
        return actualSlotNumber - 1;
    }

    public static void setDisplayOrder(int[] order) {
        if (!isValid(order)) {
            return;
        }
        displayOrder = Arrays.copyOf(order, order.length);
        save();
    }

    public static void reset() {
        displayOrder = new int[]{4, 5, 3, 1, 6, 7, 2, 8, 9};
        save();
    }

    public static boolean isValid(int[] order) {
        if (order == null || order.length != 9) return false;

        boolean[] seen = new boolean[10];
        for (int value : order) {
            if (value < 1 || value > 9 || seen[value]) return false;
            seen[value] = true;
        }
        return true;
    }

    public static void load() {
        if (!Files.exists(FILE)) return;

        try {
            ConfigData data = GSON.fromJson(Files.readString(FILE), ConfigData.class);
            if (data != null && isValid(data.displayOrder)) {
                displayOrder = Arrays.copyOf(data.displayOrder, 9);
            }
        } catch (Exception exception) {
            System.err.println("[Fake Hotbar] Invalid config; using default order.");
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(new ConfigData(displayOrder)));
        } catch (IOException exception) {
            System.err.println("[Fake Hotbar] Could not save config: " + exception.getMessage());
        }
    }

    private record ConfigData(int[] displayOrder) {}
}
