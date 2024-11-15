package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

public class Common {
    public TextButton createLargeButton(String text, Skin skin) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.up = skin.newDrawable("white", Color.BLUE);
        style.down = skin.newDrawable("white", Color.CYAN);
        style.over = skin.newDrawable("white", Color.ROYAL);

        BitmapFont boldFont = createBoldFont(24);
        style.font = boldFont;

        TextButton button = new TextButton(text, style);
        button.setSize(300, 100);

        button.getLabel().setFontScale(2.0f);
        button.getLabel().setStyle(new Label.LabelStyle(boldFont, Color.WHITE));
        button.getLabel().setAlignment(Align.center);

        button.getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return button;
    }

    public TextButton createPlasticButton(String text, Skin skin) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        NinePatch upPatch = new NinePatch(new Texture("ui/components/button/button_up.png"), 12, 12, 12, 12);
        NinePatch downPatch = new NinePatch(new Texture("ui/components/button/button_down.png"), 12, 12, 12, 12);
        NinePatch overPatch = new NinePatch(new Texture("ui/components/button/button_over.png"), 12, 12, 12, 12);

        style.up = new NinePatchDrawable(upPatch);
        style.down = new NinePatchDrawable(downPatch);
        style.over = new NinePatchDrawable(overPatch);

        style.font = createBoldFont(24);
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.LIGHT_GRAY;

        TextButton button = new TextButton(text, style);
        button.setSize(300, 100);

        button.getLabel().setFontScale(2.0f);
        button.getLabel().setAlignment(Align.center);

        return button;
    }

    public BitmapFont createBoldFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth = 2;
        BitmapFont boldFont = generator.generateFont(parameter);
        generator.dispose();
        return boldFont;
    }
}
