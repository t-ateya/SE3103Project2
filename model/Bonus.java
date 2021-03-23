package model;

import java.awt.*;

public class Bonus extends GameElement {

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private boolean isUsed = false, visible = false;

    @Override
    public void render(Graphics2D g2) {
        //todo
        g2.setColor(color);
        g2.fillOval(x, y, width, height);
    }

    @Override
    public void animate() {
        //todo
    }
}
