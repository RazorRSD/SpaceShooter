package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sahan.spaceshooter.SpaceShooterGameClass;
import com.sahan.spaceshooter.data.GameDataManager;
import com.sahan.spaceshooter.data.GameProgress;
import com.sahan.spaceshooter.data.PlayerUpgrades;

public class WorkshopScreen implements Screen {
    private final SpaceShooterGameClass game;
    private final Stage stage;
    private final GameDataManager gameDataManager;
    private GameProgress gameProgress;
    private Common common;
    private Table contentTable;
    private ScrollPane scrollPane;
    private Label bankLabel;
    private boolean showingUpgrades = true;

    private static final float WINDOW_WIDTH = 940;
    private static final float WINDOW_HEIGHT = 1400;

    public WorkshopScreen(SpaceShooterGameClass game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT));
//        this.stage.getRoot().setSkin(game.skin);
        this.gameDataManager = new GameDataManager();
        this.gameProgress = gameDataManager.loadGameProgress();
        this.common = new Common();

        createUI();

        Gdx.input.setInputProcessor(stage);
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // Load and set background
        Texture backgroundTexture = new Texture(Gdx.files.internal("ui/shop/window.png"));
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        mainTable.setBackground(background);

        // Title
        Label titleLabel = new Label("Workshop", new Label.LabelStyle(common.createBoldFont(48), com.badlogic.gdx.graphics.Color.WHITE));
        mainTable.add(titleLabel).expandX().center().padTop(50).row();

        // Bank display
        bankLabel = new Label("Bank: " + gameProgress.bank, new Label.LabelStyle(common.createBoldFont(24), com.badlogic.gdx.graphics.Color.YELLOW));
        mainTable.add(bankLabel).expandX().center().padTop(20).row();

        // Create content table and scroll pane
        contentTable = new Table();
        scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);

        // Position the scroll pane within the background image
        mainTable.add(scrollPane).width(WINDOW_WIDTH * 0.9f).height(WINDOW_HEIGHT * 0.6f).expand().center().padTop(20).row();

        // Navigation buttons
        Table buttonTable = new Table();
        ImageButton backButton = createIconButton("ui/components/button/back_btn.png");
        ImageButton shopButton = createIconButton("ui/components/button/ship_btn.png");

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showingUpgrades = !showingUpgrades;
                refreshContent();
            }
        });

        buttonTable.add(backButton).size(80, 80).padRight(20);
        buttonTable.add(shopButton).size(80, 80);
        mainTable.add(buttonTable).expandX().center().padBottom(50);

        stage.addActor(mainTable);

        refreshContent();
    }

    private ImageButton createIconButton(String iconPath) {
        Texture iconTexture = new Texture(Gdx.files.internal(iconPath));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(iconTexture));
        return new ImageButton(style);
    }

    private void refreshContent() {
        contentTable.clear();
        if (showingUpgrades) {
            addUpgradeOptions();
        } else {
            addShipOptions();
        }
    }

    private void addUpgradeOptions() {
        addShopItem("Weapon Upgrade", "ui/shop/weapon_icon.png", 100, this::upgradeWeapon);
        addShopItem("Shield Upgrade", "ui/shop/shield_icon.png", 150, this::upgradeShield);
        addShopItem("Engine Upgrade", "ui/shop/engine_icon.png", 200, this::upgradeEngine);
    }

    private void addShipOptions() {
        addShopItem("Scout Ship", "ui/shop/scout_ship_icon.png", 500, () -> purchaseShip("Scout"));
        addShopItem("Fighter Ship", "ui/shop/fighter_ship_icon.png", 1000, () -> purchaseShip("Fighter"));
        addShopItem("Destroyer Ship", "ui/shop/destroyer_ship_icon.png", 2000, () -> purchaseShip("Destroyer"));
    }

    private void addShopItem(String name, String iconPath, int cost, Runnable action) {
        Table itemTable = new Table();
        Texture slotTexture = new Texture(Gdx.files.internal("ui/shop/slot.png"));
        TextureRegionDrawable slotBackground = new TextureRegionDrawable(new TextureRegion(slotTexture));
        itemTable.setBackground(slotBackground);


        Image icon = new Image(new Texture(Gdx.files.internal(iconPath)));
        Label nameLabel = new Label(name, new Label.LabelStyle(common.createBoldFont(36), com.badlogic.gdx.graphics.Color.WHITE));

        Texture priceBtnTexture = new Texture(Gdx.files.internal("ui/components/button/price_btn.png"));
        TextButton.TextButtonStyle buyButtonStyle = new TextButton.TextButtonStyle();
        buyButtonStyle.up = new TextureRegionDrawable(new TextureRegion(priceBtnTexture));
        buyButtonStyle.font = common.createBoldFont(20);
        TextButton buyButton = new TextButton("", buyButtonStyle);

        // Add bank icon and cost label to the button
        Image bankIcon = new Image(new Texture(Gdx.files.internal("ui/components/statsbar/bank.png")));
        Label costLabel = new Label(String.valueOf(cost), new Label.LabelStyle(common.createBoldFont(20),
                gameProgress.bank >= cost ? com.badlogic.gdx.graphics.Color.WHITE : com.badlogic.gdx.graphics.Color.RED));

        Table buttonContentsTable = new Table();
        buttonContentsTable.add(bankIcon).size(30, 30).padRight(5).left();
        buttonContentsTable.add(costLabel).left();
        buyButton.add(buttonContentsTable).expand().left().padLeft(10);


        buyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameProgress.bank >= cost) {
                    gameProgress.bank -= cost;
                    action.run();
                    gameDataManager.saveGameProgress(gameProgress.completedMissions, gameProgress.bank, gameProgress.score,
                            gameProgress.lives, gameProgress.powerLevel, gameProgress.powerUpPoints,
                            gameProgress.upgrades);
                    refreshContent();
                    updateBankDisplay();
                } else {
                    // Show "Not enough credits" message
                }
            }
        });

        // Adjust the layout
        itemTable.add(icon).size(80, 80).padLeft(10).padRight(10);
        itemTable.add(nameLabel).expandX().left();
        itemTable.add(buyButton).size(150, 60).padRight(10);  // Adjust size as needed

        contentTable.add(itemTable).width(WINDOW_WIDTH * 0.85f).pad(10).row();
    }

    private void updateBankDisplay() {
        bankLabel.setText("Bank: " + gameProgress.bank);
    }

    private void upgradeWeapon() {
        gameProgress.upgrades.weaponLevel++;
    }

    private void upgradeShield() {
        gameProgress.upgrades.shieldLevel++;
    }

    private void upgradeEngine() {
        gameProgress.upgrades.engineLevel++;
    }

    private void purchaseShip(String shipType) {
        gameProgress.upgrades.ownedShips.add(shipType);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}