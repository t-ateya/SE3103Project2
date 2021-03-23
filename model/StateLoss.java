package model;

import java.awt.*;

public class StateLoss extends StateRender{
    @Override
    public void render(Graphics2D g2, int score) {
        g2.setFont(new Font("Arial", Font.PLAIN, 32));
        g2.drawString("Game Over. You Loss! Score: " + String.valueOf(score), 10, 100);
    }
}
