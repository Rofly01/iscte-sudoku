import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

class Sudoku {
	private String filename;
	private SudokuBoard sudokuBoard;
	public ColorImage boardImage;
	
	
	public Sudoku(String file, double difficulty) {
		String[] game = read_game(file);
		boardImage = new ColorImage(470, 470);

		filename = "";
		for (int i = 0; file.charAt(i) != '.'; i++)
			filename += file.charAt(i);
		
		if (file.indexOf("sudgame") != -1){
			sudokuBoard = new SudokuBoard(game[0], game[1]);
			sudokuBoard.loadSteps(filename);
		}
		else
			sudokuBoard = new SudokuBoard(game[0]);
		sudokuBoard.makeGame(difficulty);
		save();
	}
	
	
	public void Play(int x, int y, int v) {
		sudokuBoard.putNumber(x - 1, y - 1, v);
		save();
	}
	
	
	public void Random() {
		sudokuBoard.randomPlay();
		save();
	}
	
	
	public void Undo() {
		sudokuBoard.undo();
		save();
	}
	
	
	public void Reset() {
		sudokuBoard.reset();
		save();
	}
	
	
	private void save() {
		sudokuBoard.applyOperations(boardImage);
		write_game(filename + ".sudgame", sudokuBoard.getInitialBoard(), sudokuBoard.getGameBoard());
		sudokuBoard.saveSteps(filename);
	}
	
	
	/* ______________3.0_________________ */
	private String[] read_game(String file) {
		try {
			String game[] = new String[2];
			Scanner scanner = new Scanner(new File(file));
			
			game[0] = "";
			game[1] = "";
			for(int i = 0; scanner.hasNextLine(); i++) {
				if (i < 9) {
					game[0] += scanner.nextLine();
					game [0]+= "\n";
				}
				else if (i > 9) {
					game[1] += scanner.nextLine();
					game [1]+= "\n";
				}
				else
					scanner.nextLine();
			}
			scanner.close();
			return game;
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("O ficheiro " + file + " não foi encontrado!");
		}
	}
	
	
	private void write_game(String file, String matriz1, String matriz2) {
		try {
			PrintWriter writer = new PrintWriter(new File(file));
			Scanner scanner1 = new Scanner(matriz1);
			Scanner scanner2 = new Scanner(matriz2);
			
			for (int i = 0; scanner1.hasNextLine() || scanner2.hasNextLine(); i++) {
				if (i < 9)
					writer.println(scanner1.nextLine().replace("   ", " "));
				else if (i > 9)
					writer.println(scanner2.nextLine().replace("   ", " "));
				else
					writer.println("");
			}
			writer.close();
			scanner1.close();
			scanner2.close();
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("O ficheiro *.subgame não foi escrito!");
		}
	}
}