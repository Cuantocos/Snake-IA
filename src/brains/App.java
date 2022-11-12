package brains;

import java.util.LinkedList;
import java.util.List;

import edu.unlam.snake.engine.Point;
import game.GameDifficulty;
import game.GameMode;
import game.GameStudent;

public class App {
	public static void main(String[] args) {
		GameMode gameMode = GameMode.NORMAL;
		MyBrain myBrain = new MyBrain();
		GameStudent.start(gameMode, 2, 2, GameDifficulty.HARD, obstacleMap(gameMode), myBrain);
		// Pueden probar multiples copias de su Brain o varias copias distintas enviado
		// un array en vez del objeto
	}

	// Prueben generar distinta combinación de mapas de obstaculos
	static List<Point> obstacleMap(GameMode gameMode) {
		int SIZE = gameMode == GameMode.NORMAL ? 20 : 40;
		List<Point> obstacles = new LinkedList<Point>();
		for (int i = 1; i < SIZE; i += 2) {
			obstacles.add(new Point(i, i));
			obstacles.add(new Point(i, SIZE - i));
		}
		return obstacles;
	}
}

/**
 * Comandos:
 * 1 -> Aumenta movimientos / seg
 * 2 -> Disminuye movimientos / seg
 * 3 -> 20 movimientos / seg
 * 4 -> 4 movimientos / seg
 * 5 -> 1 movimiento / seg
 * P -> Pausa
 * R -> Siguiente ronda
 * + -> Aumentar zoom
 * - -> Disminuir zoom
 * 
 * Nota:
 * El juego empieza pausado (P)
 * Para salir de pausa deberán tocar (3), (4) o (5)
 */
