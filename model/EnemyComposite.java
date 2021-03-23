package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import view.GameBoard;

public class EnemyComposite extends GameElement {

	public static final int NROWS = 2;
	public static final int NCOLS = 10;
	public static final int ENEMY_SIZE = 20;
	public static final int UNIT_MOVE = 5;

	private ArrayList<ArrayList<GameElement>> rows;
	private ArrayList<GameElement>bombs;
	private boolean movingToRight = true;
	private Random random = new Random();
	private int score = 0;
	private StateRender stateRender = null;
	private Bonus bonus = new Bonus();

	public EnemyComposite() {
		rows = new ArrayList<>();
		bombs = new ArrayList<>();

		for (int r = 0; r < NROWS; r++) {
			var oneRow = new ArrayList<GameElement>();
			rows.add(oneRow);
			for (int c = 0; c < NCOLS; c++) {
				oneRow.add(new Enemy(c * ENEMY_SIZE * 2, r * ENEMY_SIZE * 2, ENEMY_SIZE, Color.yellow, true));
			}
		}
	}

	@Override
	public void render(Graphics2D g2) {

		if(random.nextInt(100) > 30) {
			showBonus(rows.get(1).get(0).y + ENEMY_SIZE * 2);
		}
		if(bonus.isVisible()) bonus.render(g2);

		if(stateRender == null) {
				//render enemy array
				for (var r : rows) {
					for (var e : r) {
						e.render(g2);
					}
				}
				//render Bombs
				for (var b: bombs){
					b.render(g2);
				}
				g2.drawString("Score: " + score, 0, 30);
		}
		else stateRender.render(g2, score);
	}

	@Override
	public void animate() {
		int dy = 0;
		int dx = UNIT_MOVE;
		if (movingToRight) {
			if (rightEnd() >= GameBoard.WIDTH) {
				dx = -dx;
				movingToRight = false;
				dy = ENEMY_SIZE;
			}
		} else {
			dx = -dx;
			if (leftEnd() <= 0) {
				dx = -dx;
				movingToRight = true;
				dy = ENEMY_SIZE;
			}
		}

		// update x loc
		for (var row : rows) {
			for (var e : row) {
				e.x += dx;
				e.y += dy;
				if(e.y >= GameBoard.HEIGHT - ENEMY_SIZE) {
					stateRender = new StateLoss();
				}
			}
		}

		//animate bombs
		for (var b: bombs){
			b.animate();
		}
	}

	private int rightEnd() {
		int xEnd = -100;

		for (var row : rows) {
			if (row.size() == 0)
				continue;
			int x = row.get(row.size() - 1).x + ENEMY_SIZE;
			if (x > xEnd)
				xEnd = x;
		}
		return xEnd;
	}

	private int leftEnd() {
		int xEnd = 9000;
		for (var row : rows) {
			if (row.size() == 0)
				continue;
			int x = row.get(0).x;
			if (x < xEnd)
				xEnd = x;

		}

		return xEnd;
	}

	public void dropBombs(){
		for (var row:rows){
			for (var e: row){
				if (random.nextFloat() <0.1F){
					bombs.add(new Bomb(e.x, e.y));
				}
			}
		}
	}

	public void removeBombsOutOfBound(){
		var remove = new ArrayList<GameElement>();
		for (var b: bombs){
			if (b.y >= GameBoard.HEIGHT){
				remove.add(b);
			}
		}
		bombs.removeAll(remove);
	}

	public void processCollision(Shooter shooter){
		var removeBullets = new ArrayList<GameElement>();
		//bullets vs enemies
		for (var row: rows){
			var removeEnemies = new ArrayList<GameElement>();
			for (var enemy: row){
				for (var bullet: shooter.getWeapons()){
					if (enemy.collideWith(bullet)){
						removeBullets.add(bullet);
						removeEnemies.add(enemy);
						score += 10;
					}
				}

				if(bonus.isVisible() && enemy.collideWith(bonus)) {
					enemy.y += ENEMY_SIZE;
					bonus.setVisible(false);
					bonus.setUsed(true);
				}

			}
			row.removeAll(removeEnemies);
		}
		shooter.getWeapons().removeAll(removeBullets);

		int n = 0;
		for (var row: rows){
			n += row.size();
		}
		if(n == 0) {
			stateRender = new StateWin();
		}

		//bullets vs bombs, shooters vs bombs
		var removeBombs = new ArrayList<GameElement>();
		removeBullets.clear();

		var removeComp = new ArrayList<GameElement>();

		for (var b: bombs){

			for(var component: shooter.getComponents()) {
				if(b.collideWith(component)) {
					removeBombs.add(b);
					removeComp.add(component);
				}
			}

			for (var bullet:shooter.getWeapons()){
				if (b.collideWith(bullet)){
					removeBombs.add(b);
					removeBullets.add(bullet);
				}
			}
		}

		shooter.getComponents().removeAll(removeComp);
		if(shooter.getComponents().size() == 0) {
			stateRender = new StateLoss();
		}

		shooter.getWeapons().removeAll(removeBullets);
		bombs.removeAll(removeBombs);
	}

	private void showBonus(int y) {
		if(bonus.isVisible() || bonus.isUsed()) return;
		bonus.color = Color.CYAN;
		bonus.x = random.nextInt(GameBoard.WIDTH);
		bonus.y = y;///random.nextInt(GameBoard.HEIGHT);
		bonus.width = 11;
		bonus.height = 11;
		bonus.setVisible(true);
		bonus.setUsed(false);
	}
}
