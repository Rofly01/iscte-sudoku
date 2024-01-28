class SudokuAux {

	/* ______________1.0_________________ */
	static boolean checkRepetition(int[] v) {
		for (int i = 0; i < v.length; i++) {
			if (v[i] == 0)
				continue;
			for (int j = 0; j < v.length; j++) {
				if (i == j || v[j] == 0)
					continue;
				if (v[i] == v[j])
						return (false);
			}
		}
		return (true);
	}
	
	
	static boolean checkColumn(int[][] b, int x) {
		int[] vp = new int[9];

		for (int j = 0; j < b.length; j++) {
			if (b[j][x] < 0 || b[j][x] > 9)
				return false;
			vp[j] = b[j][x];
		}		
		if (!checkRepetition(vp))
			return false; 
		return true;
	}
	
	
	static boolean checkRow(int[][] b, int y) {
		int[] vp = new int[9];

		for (int j = 0; j < b.length; j++) {
			if (b[y][j] < 0 || b[y][j] > 9)
				return false;
			vp[j] = b[y][j];
		}		
		if (!checkRepetition(vp))
			return false; 
		return true;
	}
	
	
	static boolean checkSegment(int[][] b, int x, int y) {
		int k = 0;
		int[] segment = new int[9];
		
		for (int i = y; i < y + 3; i++) {
			for (int j = x; j < x + 3; j++) {
				segment[k] = b[i][j];
				k++;
			}
		}
		if (!checkRepetition(segment))
				return (false);
		return (true);
	}
	
	static boolean checkBoard(int[][] b) {
		if (b.length != 9)
			return (false);
		for (int i = 0; i < b.length; i++) {
			if (b[i].length != 9)
				return (false);
			if(!checkColumn(b, i) || !checkRow(b, i))
				return false;
		}
		for (int i = 0; i <= 6; i += 3)
			for (int j = 0; j <= 6; j += 3)
			if (!checkSegment(b, i, j))
					return false;
		return true;
	}
	
	/* ______________2.0_________________ */
	static int[][] boardSpace(int[][] b, double p) {
		int max = (int)(p * 10);
		int bs[][] = new int[9][9];
		
		for (int y = 0; y < b.length; y++) {
			for (int x = 0; x < b[y].length; x++) {
				if ((int) (Math.random() * 10) + 1 <= max)
					bs[y][x] = 0;
				else
					bs[y][x] = b[y][x];
			}
		}
		return bs;
	}
	
	/* ______________3.0_________________ */
	static String matrizToString (int[][] b) {
		String sx = "";
		
		for (int y = 0; y < b.length; y++) {
			for (int x = 0; x < b[y].length; x++) {
				sx += b[y][x];
				if (x < b[y].length - 1)
					sx += "   ";
			}
			if ( y < b.length - 1)
				sx += "\n";
		}
		return sx;
	}
	
	
	static int[][] stringToMatriz(String matriz) {		
		int c = 0;
		int[][] b = new int[9][9];
		
		for (int y = 0; y < b.length; y++) {
			for (int x = 0; x < b[y].length; x++) {
				if (matriz.charAt(c) == ' ' || matriz.charAt(c) == '\n')
					c++;
				b[y][x] = matriz.charAt(c) - '0' ;
				c++;
			}
		}
		return (b);
	}
	
	
	/* ______________4.0_________________ */
	static ColorImage makeImg(int[][] b) {
		int p = 10;
		int[][] s = new int[1][9];
    	ColorImage img = new ColorImage(470, 470);

		for (int i = 0; i < 9; i++, p += 50) {
			s[0] = b[i];
	    	img.drawText(20, p, matrizToString(s), 37, Params.PRIMARY_COLOR);
		}
		makeSegments(img, Params.PRIMARY_COLOR);
		return (img);
	}
	
	
	private static void makeColumn(ColorImage img, Color c, int x, int y) {
		for (int i = y; i < y + 150 && i < img.getHeight() - 10; i++) {
			img.setColor(x, i, c);
			img.setColor(x - 2, i - 2, c);
		}

	}
	
	
	private static void makeRow(ColorImage img, Color c, int x, int y) {
		for (int i = x; i < x + 150 && i < img.getWidth() - 10; i++) {
			img.setColor(i, y, c);
			img.setColor(i - 2, y - 2, c);
		}

	}
	
	
	static void makeSegments(ColorImage img, Color c) {	
		for (int i = 10; i < img.getHeight(); i += 150)
			for (int j = 10; j < img.getWidth(); j += 150) {
					makeColumn(img, c, i, j);
					makeRow(img, c, i, j);
			}
	}
	
	
	/* ______________5.0_________________ */
	static void changePosition(ColorImage img, int x, int y, int v) {
		for (int i = 15 + (50 * y) ; i < 4 + (50 * (y + 1)); i++) {
			for (int j = 15 + (50 * x); j < 4 + (50 * (x + 1)); j++) {
				img.setColor(j, i, Params.PLAY_COLOR);
			}
		}
		img.drawText(20 + (50 * x) + x, 10 + (50 * y), Integer.toString(v) , 37, new Color(255, 255, 255));
	}
	
	
	/* ______________6.0_________________ */
	static void atentionRow(ColorImage img, int y) {
		for (int i = 10; i < img.getWidth(); i += 150) {
			makeRow(img, Params.ATENTION_RL_COLOR, i, 10 + (50 * (y - 1)));
			makeRow(img, Params.ATENTION_RL_COLOR, i, 10 + (50 * y)) ;
		}
	}
	
	
	/* ______________7.0_________________ */
	static void atentionColumn(ColorImage img, int y) {
		for (int i = 10; i < img.getWidth(); i += 150) {
			makeColumn(img, Params.ATENTION_RL_COLOR, 10 + (50 * (y - 1)), i) ;
			makeColumn(img, Params.ATENTION_RL_COLOR, 10 + (50 * y), i) ;
		}
	}
	
	
	/* ______________8.0_________________ */
	static void atentionSegments(ColorImage img, int sx, int sy) {
		for (int i = 10 + (150 * (sx - 1)) ; i <= 10 + (150 * sx) ; i += 150)
			makeColumn(img, Params.ATENTION_SEG_COLOR, i, 10 + (150 * (sy - 1)));
		for (int j = 10 + (150 * (sy - 1)) ; j <= 10 + (150 * sy) ; j += 150)
			makeRow(img, Params.ATENTION_SEG_COLOR, 10 + (150 * (sx - 1)), j);
	}
	
	
	/* ______________0.0_________________ */
	static void shuffle(int[] v) {
		int tmp;
		
		for (int i = 0; i < v.length; i++) {
			int j = (int)( Math.random() * v.length);
			tmp = v[i];
			v[i] = v[j];
			v[j] = tmp;
		}
	}
	
	
	static void shuffleStorage(Storage[] v) {
		Storage tmp;
		
		for (int i = 0; i < v.length; i++) {
			int j = (int) (Math.random() * v.length);
			tmp = v[i];
			v[i] = v[j];
			v[j] = tmp;
		}
	}
	
	
	static int indexOfSeg(int i) {
		if (i < 3)
			return (1);
		else if (i >= 3 && i < 6)
			return (2);
		else
			return (3);
	}
	
	
	static int[][] copyMatrix(int[][] b) {
		int[][] copy = new int[b.length][b[0].length];
		
		for (int i = 0; i < b.length; i++)
			for (int j = 0; j < b[i].length; j++)
				copy[i][j] = b[i][j];
		return copy;
	}
}