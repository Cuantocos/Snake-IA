package brains;

import java.util.ArrayList;
import java.util.List;

import edu.unlam.snake.brain.Brain;
import edu.unlam.snake.engine.Direction;
import edu.unlam.snake.engine.Point;

public class MyBrain extends Brain {

	// Pueden agregarse todos los atributos necesarios
	//List<Direction> enemiesPrevius = new ArrayList<Direction>();		
	public MyBrain() {
		super("!!Vamos banana¡¡");		
	}

	/**
	 * @pre: recibe un objeto de la clase 'Point' y uno de la clase 'Direction'
	 * 
	 * @post: devuelve la dirección en la que se moverá la 'Snake' en el próximo turno. 
	 *  
	 * @param head el punto de origen antes de desplazar la 'Snake'.
	 * @param previous la última dirección en la que se desplazó la 'Snake'.
	 */
	public Direction getDirection(Point head, Direction previous) {
		
		List<Point> fruits = info.getFruits();
		List<Point> snake = info.getSnake();
		List<List<Point>> enemies = info.getEnemies();
		List<Point> obstacles = info.getObstacles();
		
		List<Direction> direcciones = new ArrayList<Direction>();

		direcciones = this.direccionAUnPoint(head, this.obtenerFrutaCercana(head), previous);
		for (Direction direccion : direcciones) {
			if (this.directionPosible(head, direccion))
				return direccion;
		}

		return previous;
	}

	// Obtiene la fruta mas cercana.
	
	/**
	 * @pre: recibe un objeto de la clase 'Point' y uno de la clase 'Direction'.
	 * 
	 * @post: devuelve un objeto de la clase point, en donde se encuentra la fruta más cercana
	 * respecto de la cabeza.
	 *  
	 * @param el punto donde se encuentra ubicada la cabeza de la 'Snake'. 
	 */
	private Point obtenerFrutaCercana(Point head) {
		List<Point> fruits = info.getFruits();
		Point frutaMin = fruits.get(0);

		// Calculamos distancias de frutas.
		for (Point fruta : fruits) {
			if (obtenerDistancia(head, fruta) < obtenerDistancia(head, frutaMin)) {
				frutaMin = fruta;
			}
		}
		return frutaMin;
	}

	/**
	 * @pre: recibe dos objetos de la clase 'Point'.
	 * 
	 * @post: devuelve la distancia entre los dos puntos dados.
	 *  
	 * @param point1 .
	 * @param point2 .
	 */
	private int obtenerDistancia(Point point1, Point point2) {
		return Math.abs(point1.getX() - point2.getX()) + Math.abs(point1.getY() - point2.getY());
	}
	
	/**
	 * @pre: recibe un objeto de la clase 'Point' y uno de la clase 'Direction'
	 * 
	 * @post: Devuelve una lista de las direcciones preferidas para ir hacia un punto. 
	 *  
	 * @param head el punto de origen de la cabeza antes de desplazar la 'Snake'. 
	 * @param destino el punto destino al que se desea desplazar la 'Snake'.
	 * @param previous la direccion del ultimo movimiento realizado por la 'Snake'.
	 */
	private List<Direction> direccionAUnPoint(Point head, Point destino, Direction previous) {
		List<Direction> path = new ArrayList<Direction>();

		int distanciaX = head.getX() - destino.getX();
		int distanciaY = head.getY() - destino.getY();

		if (Math.abs(distanciaX) > Math.abs(distanciaY)) {
			if (distanciaX > 0 && Direction.LEFT.compatibleWith(previous)) {
				path.add(Direction.LEFT);
			} else if (Direction.RIGHT.compatibleWith(previous)) {
				path.add(Direction.RIGHT);
			}
			if (distanciaY < 0 && Direction.UP.compatibleWith(previous)) {
				path.add(Direction.UP);
			} else if (Direction.DOWN.compatibleWith(previous)) {
				path.add(Direction.DOWN);
			}

		} else {
			if (distanciaY > 0 && Direction.DOWN.compatibleWith(previous)) {
				path.add(Direction.DOWN);
			} else if (Direction.UP.compatibleWith(previous)) {
				path.add(Direction.UP);
			}
			if (distanciaX > 0 && Direction.LEFT.compatibleWith(previous)) {
				path.add(Direction.LEFT);
			} else if (Direction.RIGHT.compatibleWith(previous)) {
				path.add(Direction.RIGHT);
			}
		}
		path.add(this.directionMissing(path, previous));
		if (path.size() < 3) {
			path.add(this.directionMissing(path, previous));
		}

		return path;
	}
	
	/**
	 * @pre: recibe un objeto de la clase 'Point'.
	 * @post: checkea si el punto proporcionado, se encuentra contenido en la
	 * lista de cosas a esquivar, devolviendo false en caso de estar ocupado 
	 * o true en caso contrario.
	 *  
	 * @param point el punto donde se verificará si existen obstaculos o no.
	 */
	private boolean pointPosible(Point point) {
		// Juntamos la lista de enemigos y obstaculos en una sola.	
		List<Point> dodge = new ArrayList<Point>();	
		for (int i = 0; i < info.getEnemies().size(); i++) {
			dodge.addAll(info.getEnemies().get(i));
			dodge.addAll(getEnemyMove(info.getEnemies().get(i).get(0)));
		}
		dodge.addAll(info.getSnake());


		for (Point point2 : dodge) {
			if (point2.equals(point))
				return false;
		}
		for (Point point2 : info.getObstacles()) {
			if (point2.equals(point))
				return false;
		}
		return true;
	}
	
	/**
	 * @pre: recibe un objeto de la clase 'Point'.
	 * @post: devuelve una lista de los posibles movimientos enemigos para evitar la colición futura
	 *  
	 * @param enemyHead punto donde se encuentra la 'Snake' enemiga antes de moverse.
	 */
	private List<Point> getEnemyMove(Point enemyHead){
		List<Point> iEnemyMoves = new ArrayList<Point>();
		iEnemyMoves.add(new Point(enemyHead.getX()-1, enemyHead.getY()));
		iEnemyMoves.add(new Point(enemyHead.getX()+1, enemyHead.getY()));
		iEnemyMoves.add(new Point(enemyHead.getX(), enemyHead.getY()-1));
		iEnemyMoves.add(new Point(enemyHead.getX(), enemyHead.getY()+1));
		return iEnemyMoves;
	}

	/**
	 * @pre: recibe un objeto de la clase 'Point' y uno de la clase 'Direction'
	 * 
	 * @post: devuelve un boolean que dice si la cabeza se puede mover en esa direccion.
	 *  
	 * @param head el punto de origen antes de desplazar la 'Snake'.
	 * @param previus la dirección en la que se verificará si existe un obstaculo
	 * si se moviera la 'Snake'.
	 */
	private boolean directionPosible(Point head, Direction previus) {
		Point futurePoint;
		boolean flag = false;

		if (previus == Direction.LEFT) {
			futurePoint = new Point(head.getX() - 1, head.getY());
		} else if (previus == Direction.RIGHT) {
			futurePoint = new Point(head.getX() + 1, head.getY());
		} else if (previus == Direction.UP) {
			futurePoint = new Point(head.getX(), head.getY() + 1);
		} else {
			futurePoint = new Point(head.getX(), head.getY() - 1);
		}

		List<Point> enemiesFuturesMoves = getEnemyMove(futurePoint);
		
		for (Point point : enemiesFuturesMoves) {
			if (this.pointPosible(point)) {
				flag = true;
			}
		}
		if (flag == false) {
			return false;
		}
		
		
		return this.pointPosible(futurePoint);
	}
	
	/**
	 * @pre: recibe una lista de 'Directions', y un objeto de la clase 'Direction'.
	 * 
	 * @post: devuelve una de las posibles 'Directions' que falten en la lista otorgada.
	 *  
	 * @param path .
	 * @param previous .
	 */
	private Direction directionMissing(List<Direction> path, Direction previous) {
		List<Direction> auxList = new ArrayList<Direction>();
		// Iniciamos la lista
		for (int i = 0; i < Direction.values().length; i++) {
			if (previous.compatibleWith(Direction.values()[i])) {
				auxList.add(Direction.values()[i]);
			}
		}
		for (int i = 0; i < 3; i++) {
			if (!(path.contains(auxList.get(i))))
				return auxList.get(i);
		}
		return null;
	}
}
