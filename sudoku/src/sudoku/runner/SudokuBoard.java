package sudoku.runner;

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import sudoku.framework.*;


class SudokuBoard {
	
	private int iSteps;
	private Storage[] steps;
	private ColorImage auxiliaryImage;
	private int[][] finishedBoard, initialBoard, gameBoard;

	
	/* ______________1.0_________________ */
	SudokuBoard(String matriz1, String matriz2) {
		iSteps = 0;
		steps = new Storage[81];
		initialBoard = SudokuAux.stringToMatriz(matriz1);
		gameBoard = SudokuAux.stringToMatriz(matriz2);
	}
	
	
	SudokuBoard(String matriz) {
		iSteps = 0;
		steps = new Storage[81];
		finishedBoard = SudokuAux.stringToMatriz(matriz);
	}
	
	
	String getInitialBoard() {
		return SudokuAux.matrizToString(initialBoard);
	}
	
	
	String getGameBoard() {
		return SudokuAux.matrizToString(gameBoard);
	}
	
	
	void makeGame(double p) {
		if (initialBoard == null) {
			if (!SudokuAux.checkBoard(finishedBoard) || !isFinished(finishedBoard))
				p = 1;
			initialBoard = SudokuAux.boardSpace(finishedBoard, p);
			gameBoard = SudokuAux.copyMatrix(initialBoard);
		}
		auxiliaryImage = SudokuAux.makeImg(gameBoard);
	}
	
	
	void applyOperations(ColorImage finalImage) {
		Storage[][] invalidPlace = {storeInvalidRows(), storeInvalidColumns(), storeInvalidSegments()};
		
		finalImage.Copy(auxiliaryImage);
		auxiliaryImage = SudokuAux.makeImg(gameBoard);
		for (int i = 0; i < invalidPlace.length ; i++) {
			for (int j = 0; j < invalidPlace[i].length; j++)
				if (invalidPlace[i][j] != null)
					switch(i) {
					  case 0:
						  SudokuAux.atentionRow(finalImage, invalidPlace[i][j].getY());
					    break;
					  case 1:
						  SudokuAux.atentionColumn(finalImage, invalidPlace[i][j].getX());
					    break;
					  default:
						  SudokuAux.atentionSegments(finalImage, invalidPlace[i][j].getX(), invalidPlace[i][j].getY());
					}
		}
		if (isFinished(gameBoard))
			SudokuAux.makeSegments(finalImage, Params.FINISHED_COLOR);
	}
	
	
	/* ______________3.0_________________ */
	void putNumber(int x, int y, int v) {
		if ((x < 0 || x > 8) || (y < 0 || y > 8) || (v < 1 || v > 9))
			throw new IllegalArgumentException("Coordenada/Valor invalida!");
		if (gameBoard[y][x] == 0) {
			steps[iSteps++] = new Storage(x, y, gameBoard[y][x]);
			gameBoard[y][x] = v;
			SudokuAux.changePosition(auxiliaryImage, x, y, v);
		}	
	}
	
	
	/* ______________4.0_________________ */
	void randomPlay(){
		boolean done;
		Storage[] zeros = storeZeros();
		int[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9};

		SudokuAux.shuffle(v);
		for (int i = 0; i < zeros.length; i++) {
			for (int j = 0; j < v.length; j++) {
				done = true;
				putNumber(zeros[i].getX(), zeros[i].getY(), v[j]);
				Storage[] invS = storeInvalidSegments();
				for (int k = 0; k < invS.length && invS[k] != null; k++) {
					if (invS[k].getX() == SudokuAux.indexOfSeg(zeros[i].getX()) 
							&& invS[k].getY() == SudokuAux.indexOfSeg(zeros[i].getY())) {
						undo();
						done = false;
					}
				}
				if (done)
					return;
			}
		}
	}
	
	
	/* ______________5.0_________________ */
	void reset() {
		iSteps = 0;
		gameBoard = SudokuAux.copyMatrix(initialBoard);
		auxiliaryImage = SudokuAux.makeImg(gameBoard);
	}
	
	
	/* ______________9.0_________________ */
	void undo() {
		if (iSteps - 1 >= 0) {
			Storage lasp = steps[iSteps - 1];
			gameBoard[lasp.getY()][lasp.getX()] = lasp.getV();
			auxiliaryImage = SudokuAux.makeImg(gameBoard);
			iSteps--;
		}
	}

	
	void saveSteps(String file) {
		try {
			PrintWriter writer = new PrintWriter(new File(file + ".sudsteps"));
			
			for (int i = 0; i < iSteps; i++)
				writer.println(steps[i].getX() + " " + steps[i].getY() + " " + steps[i].getV());
			writer.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Não foi possível guardar as jogadas!");
		}
	}
	
	
	void loadSteps(String file) {
		try {
			Scanner scan = new Scanner(new File(file + ".sudsteps"));
			
			for (int i = 0; scan.hasNextLine(); i++) {
				String line = scan.nextLine();
				int x = line.charAt(0) - '0', y = line.charAt(2) - '0', v = line.charAt(4) - '0';
				steps[i] = new Storage (x, y, v);
				iSteps = i + 1;
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Não foi possível carregar as jogadas!");
		}
	}

	
	/* ______________8.0_________________ */
	private boolean isFinished(int[][] b) {
		for (int y = 0; y < b.length; y++) {
			int cx = 0, cy = cx;
			for (int x = 0; x < b[y].length; x++) {
				cx += b[y][x];
				cy += b[x][y];
			}
			if (cx != 45 || cy != 45)
				return false;
		}
		return true;
	}
	
	
	/* ______________2.0_________________ */
	private int numberAtPosition(int x, int y) {
		if ((x >= 0 && x <= 8) && (y >= 0 && y <= 8))
			return gameBoard[y][x];
		else
			throw new IllegalArgumentException("Coordenada invalida!");
	}
	
	
	/* ______________4.0_________________ */
	private int numberOfZeros() {
		int i = 0;
		
		for (int y = 0; y < gameBoard.length; y++) {
			for (int x = 0; x < gameBoard[y].length; x++) {
				if (numberAtPosition(x, y) == 0)
					i++;
			}
		}
		return i;
	}
	
	
	private Storage[] storeZeros() {
		int i = 0;
		Storage[] zeros = new Storage[numberOfZeros()];
		
		for (int y = 0; y < gameBoard.length; y++) {
			for (int x = 0; x < gameBoard[y].length; x++) {
				if (numberAtPosition(x, y) == 0) {					
					zeros[i] = new Storage(x, y);
					i++;
				}
			}
		}
		SudokuAux.shuffleStorage(zeros);
		return (zeros);
	}
	
	
	/* ______________6.0_________________ */
	private Storage[] storeInvalidSegments() {
		int i = 0;
		Storage[] invS = new Storage[9];
		
		for (int y = 0; y <= 6; y += 3)
			for (int x = 0; x <= 6; x += 3){
				if (!SudokuAux.checkSegment(gameBoard, x, y)) {
					invS[i] = new Storage ((x / 3) + 1, (y / 3) + 1);
					i++;
				}
			}
		return (invS);
	}
	
	
	/* ______________7.0_________________ */
	private Storage[] storeInvalidRows(){
		int i = 0;
		Storage[] invR = new Storage[9];
		
		for (int y = 0; y < gameBoard.length; y++) {
			if (!SudokuAux.checkRow(gameBoard, y)) {
				invR[i] = new Storage (0, y + 1);
				i++;
			}
		}
		return invR;
	}
	
	
	private Storage[] storeInvalidColumns() {
		int i = 0;
		Storage[] invC = new Storage[9];
		
		for (int x = 0; x < gameBoard.length; x++) {
			if (!SudokuAux.checkColumn(gameBoard, x)) {
				invC[i] = new Storage(x + 1, 0);
				i++;
			}
		}
		return invC;
	}
}