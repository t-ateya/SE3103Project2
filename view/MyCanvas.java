package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import model.GameElement;

import javax.swing.JPanel;

public class MyCanvas extends JPanel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private GameBoard gameBoard;
	private ArrayList<GameElement>gameElements = new ArrayList<>();
	

	public MyCanvas(GameBoard gameBoard, int width, int height){
		this.setGameBoard(gameBoard);
		setBackground(Color.black);
		setPreferredSize(new Dimension(width, height));
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		for (var e: gameElements){
			e.render(g2);
		}
	}

	public ArrayList<GameElement> getGameElements() {
		return gameElements;
	}

	
}
