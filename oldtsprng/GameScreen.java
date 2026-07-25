package game.tsprng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GameScreen implements Screen {

    // =========================
    // RENDER
    // =========================
    SpriteBatch batch;
    BitmapFont font;
    Texture catTexture;
    Texture buttonTexture;
    Texture starTexture;
    Texture coinTexture;
    Texture meadowBackground;

    boolean playingCutscene = false;
    float cutsceneTimer = 0f;
    float starRotation = 0f;

    float catTime = 0f;
    float catScale = 1f;

    // =========================
    // DATA
    // =========================
    LinkedHashMap<String, Integer> inventory;
    String currentAura = "NONE";
    long coins = 0L;
    String statusMessage = "";
    boolean dogOwned = false;
    final long dogCost = 500000L;

    Cat cat;

    // =========================
    // RNG SYSTEM
    // =========================
    int rollCount = 0;
    int pityCounter = 0;
    int pityThreshold = 10;

    boolean autoRoll = false;
    float autoRollTimer = 0f;
    float catRollCooldown = 0f;
    float inventoryClickCooldown = 0f;
    final float catRollCooldownTime = 0.12f;
    final float inventoryClickCooldownTime = 0.12f;
    final float autoRollInterval = 0.2f;

    float catX;
    float catY;
    float catSize;
    float uiScale = 1f;
    float buttonScale = 1f;
    float inventoryX;
    float inventoryY;
    float inventoryWidth;
    float inventoryHeight;
    float inventoryScroll = 0f;
    boolean inventoryOpen = false;

    float rollButtonX;
    float rollButtonY;
    float rollButtonWidth;
    float rollButtonHeight;

    float potion5X;
    float potion5Y;
    float potion100X;
    float potion100Y;
    float potionButtonWidth;
    float potionButtonHeight;
    final long boundPotionCost = 10000L;
    final long heavenlyPotionCost = 100000L;

    float craftButtonX;
    float craftButtonY;
    float craftButtonWidth;
    float craftButtonHeight;
    float shopButtonX;
    float shopButtonY;
    float shopButtonWidth;
    float shopButtonHeight;
    float inventoryButtonX;
    float inventoryButtonY;
    float inventoryButtonWidth;
    float inventoryButtonHeight;
    float popupX;
    float popupY;
    float popupWidth;
    float popupHeight;
    boolean craftingOpen = false;
    boolean shopOpen = false;
    String selectedCraftDevice = "Lucky Glove";
    boolean luckyGloveCrafted = false;
    boolean eclipseDeviceCrafted = false;

    float activePotionLuck = 1f;
    String activePotionLabel = "";

    float starScale = 0f;
    float starR = 1f;
    float starG = 1f;
    float starB = 1f;

    float closeButtonX;
    float closeButtonY;
    float closeButtonSize;

    double gloveLuck = 1.0;
    String equippedGlove = "None";

    String lastRolledAura = "Nothing yet";

    private float scaled(float value) {
        return value * uiScale;
    }

    public GameScreen() {

        currentAura = LoadSystem.loadEquippedAura();

        batch = new SpriteBatch();
        font = new BitmapFont();
        catTexture = loadCatTexture(currentAura);
        buttonTexture = createSolidTexture();
        coinTexture = createCoinTexture();
        starTexture = new Texture("star.png");
        meadowBackground = new Texture("background/bg1.png");

        inventory = new LinkedHashMap<>(LoadSystem.loadInventory());
        if (inventory == null) inventory = new LinkedHashMap<>();
        coins = LoadSystem.loadCoins();

        equippedGlove = LoadSystem.loadGlove();
        gloveLuck = LoadSystem.loadGloveLuck();
        luckyGloveCrafted = LoadSystem.loadLuckyGloveCrafted();
        eclipseDeviceCrafted = LoadSystem.loadEclipseDeviceCrafted();
        dogOwned = LoadSystem.loadDogOwned();
        if (equippedGlove.equals("Lucky Glove")) luckyGloveCrafted = true;
        if (equippedGlove.equals("Eclipse Device")) eclipseDeviceCrafted = true;
        gloveLuck = getDeviceLuck(equippedGlove);

        cat = new Cat();

        registerAuras();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                inventoryScroll -= amountY * scaled(30f);
                clampInventoryScroll();
                return true;
            }
        });
    }

    private final GlyphLayout layout = new GlyphLayout();

    private Texture loadCatTexture(String aura) {

        FileHandle auraTexture = Gdx.files.internal("cat/cat" + aura + ".png");

        if (auraTexture.exists()) {
            return new Texture(auraTexture);
        }

        FileHandle doubleExtensionTexture = Gdx.files.internal("cat" + aura + ".png.png");

        if (doubleExtensionTexture.exists()) {
            return new Texture(doubleExtensionTexture);
        }

        return new Texture("catNONE.png");
    }


    private Texture createSolidTexture() {

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    private Texture createCoinTexture() {

        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        pixmap.setColor(0.56f, 0.34f, 0.05f, 1f);
        pixmap.fillRectangle(4, 1, 8, 1);
        pixmap.fillRectangle(2, 3, 12, 1);
        pixmap.fillRectangle(1, 5, 14, 7);
        pixmap.fillRectangle(2, 12, 12, 1);
        pixmap.fillRectangle(4, 14, 8, 1);

        pixmap.setColor(1f, 0.82f, 0.18f, 1f);
        pixmap.fillRectangle(5, 2, 7, 1);
        pixmap.fillRectangle(3, 4, 10, 2);
        pixmap.fillRectangle(2, 6, 12, 5);
        pixmap.fillRectangle(4, 11, 9, 2);

        pixmap.setColor(1f, 0.96f, 0.48f, 1f);
        pixmap.fillRectangle(5, 4, 3, 2);
        pixmap.fillRectangle(4, 6, 2, 4);

        pixmap.setColor(0.78f, 0.48f, 0.08f, 1f);
        pixmap.fillRectangle(10, 5, 2, 7);
        pixmap.fillRectangle(6, 7, 5, 2);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    private double getLuckMultiplier() {

        double luck = 1.0;

        luck *= gloveLuck;
        luck *= activePotionLuck;

        if (dogOwned) {
            luck *= 2.0;
        }

        if (isDoubleLuckReady()) {
            luck *= 2.0;
        }

        return luck;
    }

    private void craftSelectedDevice() {

        if (isDeviceCrafted(selectedCraftDevice)) {
            statusMessage = selectedCraftDevice + " already crafted";
            return;
        }

        if (!hasRecipeItems(selectedCraftDevice)) {
            statusMessage = "Missing recipe items";
            return;
        }

        consumeRecipeItems(selectedCraftDevice);

        if (selectedCraftDevice.equals("Lucky Glove")) {
            luckyGloveCrafted = true;
        } else if (selectedCraftDevice.equals("Eclipse Device")) {
            eclipseDeviceCrafted = true;
        }

        equippedGlove = selectedCraftDevice;
        gloveLuck = getDeviceLuck(equippedGlove);
        statusMessage = "Crafted " + selectedCraftDevice;
        saveGame();
    }

    private void toggleSelectedDevice() {

        if (!isDeviceCrafted(selectedCraftDevice)) {
            statusMessage = "Craft it first";
            return;
        }

        if (equippedGlove.equals(selectedCraftDevice)) {
            equippedGlove = "None";
        } else {
            equippedGlove = selectedCraftDevice;
        }

        gloveLuck = getDeviceLuck(equippedGlove);
        saveGame();
    }

    private boolean isDeviceCrafted(String device) {
        if (device.equals("Lucky Glove")) return luckyGloveCrafted;
        if (device.equals("Eclipse Device")) return eclipseDeviceCrafted;
        return false;
    }

    private double getDeviceLuck(String device) {
        if (device.equals("Lucky Glove")) return 1.1;
        if (device.equals("Eclipse Device")) return 1.5;
        return 1.0;
    }

    private String getDeviceBonusText(String device) {
        if (device.equals("Lucky Glove")) return "+10% luck";
        if (device.equals("Eclipse Device")) return "+50% luck";
        return "+0% luck";
    }

    private boolean hasRecipeItems(String device) {
        if (device.equals("Lucky Glove")) {
            return inventory.getOrDefault("TOPAZ, 1 in 150", 0) >= 5 &&
                inventory.getOrDefault("RARE, 1 in 16", 0) >= 30 &&
                inventory.getOrDefault("COMMON, 1 in 2", 0) >= 150;
        }

        if (device.equals("Eclipse Device")) {
            return inventory.getOrDefault("TOPAZ, 1 in 150", 0) >= 35 &&
                inventory.getOrDefault("RAGE_HEATED, 1 in 12,500", 0) >= 12 &&
                inventory.getOrDefault("AQUATIC, 1 in 40,000", 0) >= 3 &&
                inventory.getOrDefault("EXOTIC, 1 in 99,999", 0) >= 1;
        }

        return false;
    }

    private void consumeRecipeItems(String device) {
        if (device.equals("Lucky Glove")) {
            removeInventoryItem("TOPAZ, 1 in 150", 5);
            removeInventoryItem("RARE, 1 in 16", 30);
            removeInventoryItem("COMMON, 1 in 2", 150);
        } else if (device.equals("Eclipse Device")) {
            removeInventoryItem("TOPAZ, 1 in 150", 35);
            removeInventoryItem("RAGE_HEATED, 1 in 12,500", 12);
            removeInventoryItem("AQUATIC, 1 in 40,000", 3);
            removeInventoryItem("EXOTIC, 1 in 99,999", 1);
        }
    }

    private String getRecipeText(String device) {
        if (device.equals("Lucky Glove")) {
            return "5 Topaz  30 Rare  150 Common";
        }

        return "35 Topaz  12 Rage Heated  3 Aquatic  1 Exotic";
    }

    private String[] getRecipeRows(String device) {
        if (device.equals("Lucky Glove")) {
            return new String[] {
                "5 Topaz",
                "30 Rare",
                "150 Common"
            };
        }

        return new String[] {
            "35 Topaz",
            "12 Rage Heated",
            "3 Aquatic",
            "1 Exotic"
        };
    }

    private void saveGame() {
        SaveSystem.save(
            currentAura,
            inventory,
            equippedGlove,
            gloveLuck,
            coins,
            luckyGloveCrafted,
            eclipseDeviceCrafted,
            dogOwned
        );
    }

    private boolean isAnyPopupOpen() {
        return craftingOpen || inventoryOpen || shopOpen;
    }

    private void buyDog() {
        if (dogOwned) {
            statusMessage = "Dog already bought";
            return;
        }

        if (coins < dogCost) {
            statusMessage = "Need " + formatCoins(dogCost) + " coins";
            return;
        }

        coins -= dogCost;
        dogOwned = true;
        statusMessage = "Bought Dog";
        saveGame();
    }

    private void removeInventoryItem(String aura, int amount) {
        int remaining = inventory.getOrDefault(aura, 0) - amount;

        if (remaining > 0) {
            inventory.put(aura, remaining);
        } else {
            inventory.remove(aura);
        }
    }

    private boolean isDoubleLuckReady() {
        return pityCounter >= pityThreshold - 1;
    }

    private void usePotion(float luck, String label) {
        activePotionLuck = luck;
        activePotionLabel = label;
    }

    private boolean buyPotion(long cost, float luck, String label) {
        if (coins < cost) {
            statusMessage = "Need " + formatCoins(cost) + " coins";
            return false;
        }

        coins -= cost;
        usePotion(luck, label);
        statusMessage = "Bought " + label;
        saveGame();
        return true;
    }

    private void calculateLayout() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        catSize = Math.max(112f, Math.min(width * 0.3f, height * 0.42f));
        uiScale = Math.max(0.75f, Math.min(catSize / 128f, 1.6f));
        buttonScale = uiScale * 0.82f;

        rollButtonWidth = 150f * buttonScale;
        rollButtonHeight = 36f * buttonScale;
        rollButtonX = (width - rollButtonWidth) * 0.5f;
        rollButtonY = scaled(28f);

        potionButtonWidth = 120f * buttonScale;
        potionButtonHeight = 34f * buttonScale;
        potion100X = rollButtonX - scaled(180f);
        potion100Y = rollButtonY + scaled(86f);
        potion5X = potion100X;
        potion5Y = potion100Y + potionButtonHeight + scaled(8f);
        craftButtonWidth = 120f * buttonScale;
        craftButtonHeight = 34f * buttonScale;
        craftButtonX = width - craftButtonWidth - scaled(32f);
        craftButtonY = height * 0.5f + scaled(8f);
        shopButtonWidth = craftButtonWidth;
        shopButtonHeight = craftButtonHeight;
        shopButtonX = craftButtonX;
        shopButtonY = craftButtonY + shopButtonHeight + scaled(10f);
        inventoryButtonWidth = craftButtonWidth;
        inventoryButtonHeight = craftButtonHeight;
        inventoryButtonX = craftButtonX;
        inventoryButtonY = craftButtonY - inventoryButtonHeight - scaled(10f);

        popupWidth = width * 0.9f;
        popupHeight = height * 0.9f;
        popupX = (width - popupWidth) * 0.5f;
        popupY = (height - popupHeight) * 0.5f;

        inventoryWidth = popupWidth - scaled(24f);
        inventoryX = popupX + scaled(12f);
        inventoryY = popupY + scaled(12f);
        inventoryHeight = popupHeight - scaled(64f);

        closeButtonSize = scaled(32f);

        closeButtonX = popupX + popupWidth - closeButtonSize - scaled(10f);
        closeButtonY = popupY + popupHeight - closeButtonSize - scaled(10f);

        catX = (width - catSize) * 0.5f;

        float bottomSafeEdge = rollButtonY + rollButtonHeight + scaled(54f);
        float topSafeEdge = height - scaled(84f);
        float availableCatHeight = topSafeEdge - bottomSafeEdge;

        if (availableCatHeight > scaled(96f)) {
            catSize = Math.min(catSize, availableCatHeight / 1.2f);
        }

        catX = (width - catSize) * 0.5f;
        catY = bottomSafeEdge + (availableCatHeight - catSize) * 0.5f;

        clampInventoryScroll();
    }

    private void clampInventoryScroll() {

        float contentHeight = getSortedAuras().size() * scaled(30f);
        float maxScroll = Math.max(0f, contentHeight - inventoryHeight);

        if (inventoryScroll < 0f) inventoryScroll = 0f;
        if (inventoryScroll > maxScroll) inventoryScroll = maxScroll;
    }

    // =========================
    // AURAS
    // =========================
    private void registerAuras() {

        AuraRegistry.register(new Aura("OBLIVION, the truth seeker", 10000000000L));
        AuraRegistry.register(new Aura("EQUINOX, the force between positive and negative", 2500000000L));
        AuraRegistry.register(new Aura("LUMINOSITY, the absolute radiance", 1200000000L));
        AuraRegistry.register(new Aura("AEGIS, 1 in 825,000,000", 825000000L));
        AuraRegistry.register(new Aura("MATRIX_OVERDRIVE, 1 in 503,000,000", 503000000L));
        AuraRegistry.register(new Aura("ABYSSALHUNTER, 1 in 400,000,000", 400000000L));
        AuraRegistry.register(new Aura("SYMPHONY, 1 in 175,000,000", 175000000L));
        AuraRegistry.register(new Aura("SAILOR_FLYINGDUTCHMAN, 1 in 70,000,000", 70000000L));
        AuraRegistry.register(new Aura("ARCANE_DARK, 1 in 30,000,000", 30000000L));
        AuraRegistry.register(new Aura("SAILOR, 1 in 12,000,000", 12000000L));
        AuraRegistry.register(new Aura("GALAXY, 1 in 5,000,000", 5000000L));
        AuraRegistry.register(new Aura("GRAVITATIONAL, 1 in 2,000,000", 2000000L));
        AuraRegistry.register(new Aura("UNDEAD_DEVIL, 1 in 666,666", 666666L));
        AuraRegistry.register(new Aura("EXOTIC, 1 in 99,999", 99999L));
        AuraRegistry.register(new Aura("AQUATIC, 1 in 40,000", 40000L));
        AuraRegistry.register(new Aura("RAGE_HEATED, 1 in 12,500", 12500L));
        AuraRegistry.register(new Aura("FLUSHED, 1 in 6,900", 6900L));
        AuraRegistry.register(new Aura("MAGNETIC, 1 in 2,048", 2048L));
        AuraRegistry.register(new Aura("JACKPOT, 1 in 777", 777L));
        AuraRegistry.register(new Aura("TOPAZ, 1 in 150", 150L));
        AuraRegistry.register(new Aura("RARE, 1 in 16", 16L));
        AuraRegistry.register(new Aura("COMMON, 1 in 2", 2L));
    }


    // =========================
    // ROLL SYSTEM
    // =========================
    private void rollAura() {

        rollCount++;

        Aura aura;

        if (activePotionLabel.equals("heavenly potion")) {
            aura = RollSystem.rollHeavenly();
        }
        else if (activePotionLabel.equals("potion of bound")) {
            aura = RollSystem.rollBound();
        }
        else {
            aura = RollSystem.roll(getLuckMultiplier());
        }

        if (aura.name.startsWith("GRAVITATIONAL")) {
            startAuraCutscene(0.7f, 0f, 1f); // dark purple
        }
        else if (aura.name.startsWith("GALAXY")) {
            startAuraCutscene(0.4f, 0f, 1f); // violet
        }
        else if (aura.name.startsWith("SAILOR")) {
            startAuraCutscene(0f, 1f, 1f); // cyan
        }
        else if (aura.name.startsWith("ARCANE_DARK")) {
            startAuraCutscene(0.8f, 0.1f, 0.4f); // dark pink
        }
        else if (aura.name.startsWith("SYMPHONY")) {
            startAuraCutscene(1f, 1f, 1f); // white
        }
        else if (aura.name.startsWith("ABYSSALHUNTER")) {
            startAuraCutscene(0f, 0.1f, 0.5f); // dark blue
        }
        else if (aura.name.startsWith("MATRIX_OVERDRIVE")) {
            startAuraCutscene(1f, 0f, 0f); // red
        }
        else if (aura.name.startsWith("AEGIS")) {
            startAuraCutscene(1f, 1f, 0f); // yellow
        }
        else if (aura.name.startsWith("LUMINOSITY")) {
            startAuraCutscene(0.75f, 0.75f, 0.75f); // gray
        }
        else if (aura.name.startsWith("EQUINOX")) {
            startAuraCutscene(1f, 0.5f, 0f); // orange
        }
        else if (aura.name.startsWith("OBLIVION")) {
            startAuraCutscene(0.4f, 0.2f, 0f); // brown
        }
        else if (aura.rarity >= 2000000L) {
            startAuraCutscene(1f, 1f, 1f); // fallback
        }

        // increase pity counter
        pityCounter++;

        // reset after pity roll
        if (isDoubleLuckReady()) {
            pityCounter = 0;
        }

        activePotionLuck = 1f;
        activePotionLabel = "";

        inventory.put(
            aura.name,
            inventory.getOrDefault(aura.name, 0) + 1
        );
        coins += getCoinReward(aura);
        statusMessage = "+" + formatCoins(getCoinReward(aura)) + " coins";

        lastRolledAura = aura.name;

        saveGame();
    }

    private long getCoinReward(Aura aura) {

        String name = aura.name;

        if (name.startsWith("COMMON")) return 1;
        if (name.startsWith("RARE")) return 2;
        if (name.startsWith("TOPAZ")) return 5;
        if (name.startsWith("JACKPOT")) return 8;
        if (name.startsWith("MAGNETIC")) return 15;
        if (name.startsWith("FLUSHED")) return 23;
        if (name.startsWith("RAGE_HEATED")) return 55;
        if (name.startsWith("AQUATIC")) return 125;
        if (name.startsWith("EXOTIC")) return 280;
        if (name.startsWith("UNDEAD_DEVIL")) return 666;
        if (name.startsWith("GRAVITATIONAL")) return 1350;
        if (name.startsWith("GALAXY")) return 3500;
        if (name.startsWith("SAILOR_FLYINGDUTCHMAN")) return 21000;
        if (name.startsWith("SAILOR")) return 6700;
        if (name.startsWith("ARCANE_DARK")) return 14000;
        if (name.startsWith("SYMPHONY")) return 48000;
        if (name.startsWith("ABYSSALHUNTER")) return 115000;
        if (name.startsWith("MATRIX_OVERDRIVE")) return 170000;
        if (name.startsWith("AEGIS")) return 270000;
        if (name.startsWith("LUMINOSITY")) return 670000;
        if (name.startsWith("EQUINOX")) return 1250000;
        if (name.startsWith("OBLIVION")) return 3000000;

        return 1;
    }

    private String formatCoins(long amount) {
        if (amount >= 1000000000000L) {
            return formatCompact(amount, 1000000000000L, "t");
        }
        if (amount >= 1000000000L) {
            return formatCompact(amount, 1000000000L, "b");
        }
        if (amount >= 1000000L) {
            return formatCompact(amount, 1000000L, "m");
        }
        if (amount >= 1000L) {
            return formatCompact(amount, 1000L, "k");
        }

        return Long.toString(amount);
    }

    private String formatCompact(long amount, long unit, String suffix) {
        long whole = amount / unit;
        long tenths = (amount % unit) * 10L / unit;

        if (tenths == 0L || whole >= 100L) {
            return whole + suffix;
        }

        return whole + "." + tenths + suffix;
    }

    // =========================
    // SORTED INVENTORY
    // =========================
    private List<String> getSortedAuras() {

        List<String> list = new ArrayList<>(inventory.keySet());

        list.sort((a, b) -> {
            long r1 = getAuraRarity(a);
            long r2 = getAuraRarity(b);
            return Long.compare(r2, r1); // highest rarity first
        });

        return list;
    }

    private long getAuraRarity(String aura) {

        Aura registered = AuraRegistry.get(aura);

        if (registered.rarity != 2L || aura.equals("COMMON, 1 in 2")) {
            return registered.rarity;
        }

        int rarityStart = aura.indexOf("1 in ");

        if (rarityStart == -1) {
            return registered.rarity;
        }

        StringBuilder digits = new StringBuilder();

        for (int i = rarityStart + 5; i < aura.length(); i++) {
            char c = aura.charAt(i);

            if (Character.isDigit(c)) {
                digits.append(c);
            }
        }

        if (digits.length() == 0) {
            return registered.rarity;
        }

        return Long.parseLong(digits.toString());
    }

    private void startAuraCutscene(float r, float g, float b) {
        playingCutscene = true;
        cutsceneTimer = 0f;
        starRotation = 0f;
        starScale = 0f;

        starR = r;
        starG = g;
        starB = b;
    }

    private void drawCraftingPopup() {
        float panelX = popupX;
        float panelY = popupY;
        float panelW = popupWidth;
        float panelH = popupHeight;
        float itemW = scaled(190f);
        float itemH = scaled(38f);
        float luckyX = panelX + scaled(24f);
        float eclipseX = luckyX;
        float luckyY = panelY + panelH - scaled(92f);
        float eclipseY = luckyY - itemH - scaled(12f);
        float actionW = scaled(150f);
        float actionH = scaled(38f);
        float actionY = panelY + scaled(30f);
        float equipX = panelX + panelW - actionW * 2f - scaled(36f);
        float craftX = panelX + panelW - actionW - scaled(24f);

        batch.setColor(0.9f, 0.86f, 0.76f, 1f);
        batch.draw(buttonTexture, panelX, panelY, panelW, panelH);
        batch.setColor(selectedCraftDevice.equals("Lucky Glove") ? 0.48f : 0.62f, 0.65f, 0.5f, 1f);
        batch.draw(buttonTexture, luckyX, luckyY, itemW, itemH);
        batch.setColor(selectedCraftDevice.equals("Eclipse Device") ? 0.48f : 0.62f, 0.65f, 0.5f, 1f);
        batch.draw(buttonTexture, eclipseX, eclipseY, itemW, itemH);
        batch.setColor(0.48f, 0.65f, 0.5f, 1f);
        batch.draw(buttonTexture, equipX, actionY, actionW, actionH);
        batch.setColor(0.68f, 0.52f, 0.7f, 1f);
        batch.draw(buttonTexture, craftX, actionY, actionW, actionH);
        batch.setColor(1f, 1f, 1f, 1f);

        font.setColor(0, 0, 0, 1);
        font.draw(batch, "Crafting", panelX + scaled(24f), panelY + panelH - scaled(26f));
        font.draw(batch, "Lucky Glove", luckyX + scaled(18f), luckyY + scaled(25f));
        font.draw(batch, "Eclipse Device", eclipseX + scaled(18f), eclipseY + scaled(25f));

        float detailX = panelX + scaled(260f);
        float detailY = panelY + panelH - scaled(78f);
        font.draw(batch, selectedCraftDevice, detailX, detailY);
        font.draw(batch, getDeviceBonusText(selectedCraftDevice), detailX, detailY - scaled(30f));

        String[] recipeRows = getRecipeRows(selectedCraftDevice);
        for (int i = 0; i < recipeRows.length; i++) {
            font.draw(batch, recipeRows[i], detailX, detailY - scaled(70f + i * 30f));
        }

        float statusY = detailY - scaled(70f + recipeRows.length * 30f + 14f);
        font.draw(batch, isDeviceCrafted(selectedCraftDevice) ? "Crafted" : "Not crafted", detailX, statusY);
        font.draw(batch, equippedGlove.equals(selectedCraftDevice) ? "Unequip" : "Equip", equipX + scaled(38f), actionY + scaled(25f));
        font.draw(batch, isDeviceCrafted(selectedCraftDevice) ? "Crafted" : "Craft", craftX + scaled(46f), actionY + scaled(25f));

        InventoryClickSystem.register("SELECT_LUCKY_GLOVE", luckyX, luckyY, itemW, itemH);
        InventoryClickSystem.register("SELECT_ECLIPSE_DEVICE", eclipseX, eclipseY, itemW, itemH);
        InventoryClickSystem.register("EQUIP_SELECTED_DEVICE", equipX, actionY, actionW, actionH);
        InventoryClickSystem.register("CRAFT_SELECTED_DEVICE", craftX, actionY, actionW, actionH);

        batch.setColor(0.85f, 0.35f, 0.35f, 1f);
        batch.draw(
            buttonTexture,
            closeButtonX,
            closeButtonY,
            closeButtonSize,
            closeButtonSize
        );

        batch.setColor(1f, 1f, 1f, 1f);
        font.draw(
            batch,
            "X",
            closeButtonX + scaled(10f),
            closeButtonY + scaled(24f)
        );

        InventoryClickSystem.register(
            "CLOSE_MENU",
            closeButtonX,
            closeButtonY,
            closeButtonSize,
            closeButtonSize
        );

    }

    private void drawShopPopup() {
        float panelX = popupX;
        float panelY = popupY;
        float panelW = popupWidth;
        float panelH = popupHeight;
        float buyW = scaled(160f);
        float buyH = scaled(38f);
        float buyX = panelX + panelW - buyW - scaled(24f);
        float buyY = panelY + scaled(30f);
        float dogSize = scaled(96f);

        batch.setColor(0.9f, 0.86f, 0.76f, 1f);
        batch.draw(buttonTexture, panelX, panelY, panelW, panelH);
        batch.setColor(0.48f, 0.65f, 0.5f, 1f);
        batch.draw(buttonTexture, buyX, buyY, buyW, buyH);
        batch.setColor(1f, 1f, 1f, 1f);

        font.setColor(0, 0, 0, 1);
        font.draw(batch, "Shop", panelX + scaled(24f), panelY + panelH - scaled(26f));
        font.draw(batch, "Dog", panelX + scaled(24f), panelY + panelH - scaled(78f));
        font.draw(batch, "Cost: " + formatCoins(dogCost), panelX + scaled(24f), panelY + panelH - scaled(108f));
        font.draw(batch, dogOwned ? "Owned" : "", panelX + scaled(24f), panelY + panelH - scaled(138f));
        batch.draw(catTexture, panelX + scaled(260f), panelY + panelH - scaled(170f), dogSize, dogSize);
        font.draw(batch, dogOwned ? "Owned" : "Buy", buyX + scaled(58f), buyY + scaled(25f));

        InventoryClickSystem.register("BUY_DOG", buyX, buyY, buyW, buyH);

        batch.setColor(0.85f, 0.35f, 0.35f, 1f);
        batch.draw(
            buttonTexture,
            closeButtonX,
            closeButtonY,
            closeButtonSize,
            closeButtonSize
        );

        batch.setColor(1f, 1f, 1f, 1f);
        font.draw(
            batch,
            "X",
            closeButtonX + scaled(10f),
            closeButtonY + scaled(24f)
        );

        InventoryClickSystem.register(
            "CLOSE_MENU",
            closeButtonX,
            closeButtonY,
            closeButtonSize,
            closeButtonSize
        );
    }

    // =========================
    // RENDER
    // =========================
    @Override
    public void render(float delta) {

        if (playingCutscene) {

            cutsceneTimer += delta;

            starRotation += 60f * delta;

            if (starScale < 5f) {
                starScale += delta * 0.15f;
            }

            if (cutsceneTimer > 4f) {
                playingCutscene = false;
            }

            float fade = Math.min(cutsceneTimer / 2f, 1f);

            Gdx.gl.glClearColor(
                starR * fade * 0.25f,
                starG * fade * 0.25f,
                starB * fade * 0.25f,
                1f
            );
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();

            batch.setColor(starR, starG, starB, 1f);

            float size = 100f * starScale;

            float starWidth = starTexture.getWidth();
            float starHeight = starTexture.getHeight();

            float scale = starScale;

            float drawW = starWidth * scale;
            float drawH = starHeight * scale;


            batch.draw(
                starTexture,
                Gdx.graphics.getWidth() / 2f - drawW / 2f,
                Gdx.graphics.getHeight() / 2f - drawH / 2f,
                drawW / 2f,
                drawH / 2f,
                drawW,
                drawH,
                1f,
                1f,
                starRotation,
                0,
                0,
                starTexture.getWidth(),
                starTexture.getHeight(),
                false,
                false
            );

            batch.end();

            return;
        }


        InventoryClickSystem.clear();
        calculateLayout();
        font.getData().setScale(uiScale);

        catTime += delta;
        catScale += (1f - catScale) * 8f * delta;
        if (catRollCooldown > 0f) {
            catRollCooldown -= delta;
        }
        if (inventoryClickCooldown > 0f) {
            inventoryClickCooldown -= delta;
        }

        // AUTO ROLL
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            autoRoll = !autoRoll;
        }

        if (autoRoll) {
            autoRollTimer += delta;

            if (autoRollTimer >= autoRollInterval) {
                rollAura();
                autoRollTimer = 0f;
            }
        }

        // CAT CLICK
        if (Gdx.input.justTouched()) {

            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            float visualCatSize = catSize * catScale;
            float visualCatX = catX - (visualCatSize - catSize) * 0.5f;
            float visualCatY = catY - (visualCatSize - catSize) * 0.5f;


            if (!isAnyPopupOpen() &&
                catRollCooldown <= 0f &&
                x >= visualCatX && x <= visualCatX + visualCatSize &&
                y >= visualCatY && y <= visualCatY + visualCatSize) {

                rollAura();
                catScale = 1.2f;
                catRollCooldown = catRollCooldownTime;
            }

            if (!isAnyPopupOpen() &&
                x >= rollButtonX && x <= rollButtonX + rollButtonWidth &&
                y >= rollButtonY && y <= rollButtonY + rollButtonHeight) {

                autoRoll = !autoRoll;
            }

            if (!isAnyPopupOpen() &&
                x >= potion5X && x <= potion5X + potionButtonWidth &&
                y >= potion5Y && y <= potion5Y + potionButtonHeight) {

                buyPotion(boundPotionCost, 20000f, "potion of bound");
            }

            if (!isAnyPopupOpen() &&
                x >= potion100X && x <= potion100X + potionButtonWidth &&
                y >= potion100Y && y <= potion100Y + potionButtonHeight) {

                buyPotion(heavenlyPotionCost, 350000f, "heavenly potion");
            }

            if (x >= craftButtonX && x <= craftButtonX + craftButtonWidth &&
                y >= craftButtonY && y <= craftButtonY + craftButtonHeight) {

                craftingOpen = !craftingOpen;
                if (craftingOpen) {
                    inventoryOpen = false;
                    shopOpen = false;
                }
            }

            if (x >= shopButtonX && x <= shopButtonX + shopButtonWidth &&
                y >= shopButtonY && y <= shopButtonY + shopButtonHeight) {

                shopOpen = !shopOpen;
                if (shopOpen) {
                    craftingOpen = false;
                    inventoryOpen = false;
                }
            }

            if (x >= inventoryButtonX && x <= inventoryButtonX + inventoryButtonWidth &&
                y >= inventoryButtonY && y <= inventoryButtonY + inventoryButtonHeight) {

                inventoryOpen = !inventoryOpen;
                if (inventoryOpen) {
                    craftingOpen = false;
                    shopOpen = false;
                }
            }
        }

        // CLEAR
        Gdx.gl.glClearColor(0.78f, 0.69f, 0.56f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // HUD
        font.setColor(0, 0, 0, 1);
        font.draw(batch, "Equipped Aura: " + currentAura, scaled(24f), Gdx.graphics.getHeight() - scaled(24f));
        font.draw(batch, "Device: " + equippedGlove + " (" + gloveLuck + "x)", scaled(24f), Gdx.graphics.getHeight() - scaled(48f));

        float coinPanelX = potion100X;
        float coinPanelY = rollButtonY + scaled(4f);
        float coinPanelW = scaled(120f);
        float coinPanelH = scaled(30f);
        batch.setColor(0.34f, 0.28f, 0.18f, 1f);
        batch.draw(buttonTexture, coinPanelX, coinPanelY, coinPanelW, coinPanelH);
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(coinTexture, coinPanelX + scaled(8f), coinPanelY + scaled(7f), scaled(16f), scaled(16f));
        font.draw(batch, formatCoins(coins), coinPanelX + scaled(32f), coinPanelY + scaled(21f));

        font.draw(batch, isDoubleLuckReady() ? "2x luck ready" : "2x luck in " + (pityThreshold - pityCounter) + " rolls", rollButtonX, rollButtonY + rollButtonHeight + scaled(16f));

        batch.setColor(0.18f, 0.18f, 0.22f, 1f);
        batch.draw(buttonTexture, rollButtonX, rollButtonY, rollButtonWidth, rollButtonHeight);
        batch.setColor(0.68f, 0.88f, 0.68f, 1f);
        batch.draw(buttonTexture, potion5X, potion5Y, potionButtonWidth, potionButtonHeight);
        batch.setColor(0.9f, 0.68f, 0.72f, 1f);
        batch.draw(buttonTexture, potion100X, potion100Y, potionButtonWidth, potionButtonHeight);
        batch.setColor(0.74f, 0.76f, 0.92f, 1f);
        batch.draw(buttonTexture, craftButtonX, craftButtonY, craftButtonWidth, craftButtonHeight);
        batch.setColor(0.6f, 0.82f, 0.9f, 1f);
        batch.draw(buttonTexture, shopButtonX, shopButtonY, shopButtonWidth, shopButtonHeight);
        batch.setColor(0.9f, 0.82f, 0.5f, 1f);
        batch.draw(buttonTexture, inventoryButtonX, inventoryButtonY, inventoryButtonWidth, inventoryButtonHeight);
        batch.setColor(1f, 1f, 1f, 1f);

        font.draw(batch, autoRoll ? "Auto Roll: ON" : "Auto Roll: OFF", rollButtonX + scaled(12f), rollButtonY + scaled(23f));
        font.draw(batch, activePotionLabel.isEmpty() ? "No potion active" : activePotionLabel + " ready", potion100X, potion5Y + potionButtonHeight + scaled(24f));
        font.draw(batch, "Shop", shopButtonX + scaled(38f), shopButtonY + scaled(22f));
        font.draw(batch, "Crafting", craftButtonX + scaled(26f), craftButtonY + scaled(22f));
        font.draw(batch, "Inventory", inventoryButtonX + scaled(22f), inventoryButtonY + scaled(22f));
        font.draw(batch, "bound " + formatCoins(boundPotionCost), potion5X + scaled(14f), potion5Y + scaled(22f));
        font.draw(batch, "heavenly " + formatCoins(heavenlyPotionCost), potion100X + scaled(8f), potion100Y + scaled(22f));

        String rollText = "You got: " + lastRolledAura;

        layout.setText(font, rollText);

        font.draw(
            batch,
            rollText,
            (Gdx.graphics.getWidth() - layout.width) / 2f,
            catY + catSize + scaled(40f)
        );

        font.draw(batch,
            "Luck: " + String.format("%.1f", getLuckMultiplier()) + "x",
            scaled(24f),
            Gdx.graphics.getHeight() - scaled(72f)
        );

        // CAT
        float rot = (float) Math.sin(catTime * 2f) * 8f;

        batch.draw(
            catTexture,
            catX,
            catY,
            catSize / 2f,
            catSize / 2f,
            catSize,
            catSize,
            catScale,
            catScale,
            rot,
            0,
            0,
            catTexture.getWidth(),
            catTexture.getHeight(),
            false,
            false
        );

        if (inventoryOpen) {
            batch.setColor(0.9f, 0.86f, 0.76f, 1f);
            batch.draw(buttonTexture, popupX, popupY, popupWidth, popupHeight);
            batch.setColor(1f, 1f, 1f, 1f);
            font.setColor(0, 0, 0, 1);
            font.draw(batch, "Inventory", popupX + scaled(12f), popupY + popupHeight - scaled(16f));

            List<String> sorted = getSortedAuras();

            float rowHeight = scaled(30f);
            float y = inventoryY + inventoryHeight - scaled(10f) + inventoryScroll;
            for (String aura : sorted) {

                int count = inventory.getOrDefault(aura, 0);

                if (y < inventoryY + scaled(18f)) {
                    y -= rowHeight;
                    continue;
                }

                if (y > inventoryY + inventoryHeight - scaled(4f)) {
                    y -= rowHeight;
                    continue;
                }

                font.setColor(0, 0, 0, 1);

                font.draw(batch,
                    aura + " x" + count,
                    inventoryX,
                    y
                );

                InventoryClickSystem.register(aura, inventoryX, y - scaled(18f), inventoryWidth, scaled(22f));

                y -= rowHeight;
            }

            batch.setColor(0.85f, 0.35f, 0.35f, 1f);
            batch.draw(
                buttonTexture,
                closeButtonX,
                closeButtonY,
                closeButtonSize,
                closeButtonSize
            );

            batch.setColor(1f, 1f, 1f, 1f);
            font.draw(
                batch,
                "X",
                closeButtonX + scaled(10f),
                closeButtonY + scaled(24f)
            );

            InventoryClickSystem.register(
                "CLOSE_MENU",
                closeButtonX,
                closeButtonY,
                closeButtonSize,
                closeButtonSize
            );
        }

        if (craftingOpen) {
            drawCraftingPopup();
        }

        if (shopOpen) {
            drawShopPopup();
        }

        if (Gdx.input.justTouched()) {

            float mx = Gdx.input.getX();
            float my = Gdx.graphics.getHeight() - Gdx.input.getY();

            String clicked =
                InventoryClickSystem.checkClick(mx, my);

            if (clicked != null) {

                switch (clicked) {

                    case "SELECT_LUCKY_GLOVE":
                        selectedCraftDevice = "Lucky Glove";
                        break;

                    case "SELECT_ECLIPSE_DEVICE":
                        selectedCraftDevice = "Eclipse Device";
                        break;

                    case "EQUIP_SELECTED_DEVICE":
                        toggleSelectedDevice();
                        break;

                    case "CRAFT_SELECTED_DEVICE":
                        craftSelectedDevice();
                        break;

                    case "BUY_DOG":
                        buyDog();
                        break;

                    case "OPEN_CRAFTING":
                        craftingOpen = !craftingOpen;
                        if (craftingOpen) {
                            shopOpen = false;
                            inventoryOpen = false;
                        }
                        break;

                    case "OPEN_SHOP":
                        shopOpen = !shopOpen;
                        if (shopOpen) {
                            craftingOpen = false;
                            inventoryOpen = false;
                        }
                        break;

                    case "OPEN_INVENTORY":
                        inventoryOpen = !inventoryOpen;
                        if (inventoryOpen) {
                            craftingOpen = false;
                            shopOpen = false;
                        }
                        break;

                    case "CLOSE_MENU":
                        inventoryOpen = false;
                        craftingOpen = false;
                        shopOpen = false;
                        break;


                    default:

                        if (inventoryOpen) {

                            if (clicked.equals(currentAura)) {
                                currentAura = "NONE";
                                cat.setAura(new Aura("NONE", 0));
                            } else {
                                currentAura = clicked;
                                cat.setAura(new Aura(clicked, 0));
                            }

                            catTexture.dispose();
                            catTexture = loadCatTexture(currentAura);

                            saveGame();
                        }

                        break;
                }
            }
        }

        InventoryClickSystem.register(
            "OPEN_CRAFTING",
            craftButtonX,
            craftButtonY,
            craftButtonWidth,
            craftButtonHeight
        );

        InventoryClickSystem.register(
            "OPEN_SHOP",
            shopButtonX,
            shopButtonY,
            shopButtonWidth,
            shopButtonHeight
        );

        InventoryClickSystem.register(
            "OPEN_INVENTORY",
            inventoryButtonX,
            inventoryButtonY,
            inventoryButtonWidth,
            inventoryButtonHeight
        );



        batch.end();
    }

        @Override public void show () {
        }
        @Override public void resize ( int width, int height){
        }
        @Override public void pause () {
        }
        @Override public void resume () {
        }
        @Override public void hide () {
        }

        @Override
        public void dispose () {
            Gdx.input.setInputProcessor(null);
            batch.dispose();
            font.dispose();
            catTexture.dispose();
            buttonTexture.dispose();
            starTexture.dispose();
            coinTexture.dispose();
            meadowBackground.dispose();
        }
    }
