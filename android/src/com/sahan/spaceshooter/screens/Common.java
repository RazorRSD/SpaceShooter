package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class Common {
    public TextButton createLargeButton(String text, Skin skin) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.up = skin.newDrawable("white", Color.BLUE);
        style.down = skin.newDrawable("white", Color.CYAN);
        style.over = skin.newDrawable("white", Color.ROYAL);

        BitmapFont boldFont = createBoldFont(24); // Use 24 for button text size
        style.font = boldFont;

        TextButton button = new TextButton(text, style);
        button.setSize(300, 100);

        button.getLabel().setFontScale(2.0f);
        button.getLabel().setStyle(new Label.LabelStyle(boldFont, Color.WHITE));
        button.getLabel().setAlignment(Align.center);

        button.getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return button;
    }

    public BitmapFont createBoldFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth = 2; // This makes the font bold
        BitmapFont boldFont = generator.generateFont(parameter);
        generator.dispose();
        return boldFont;
    }
}
