package sudoku.runner;

class Storage {
	private int[] v = new int[3];
	
	Storage (int x, int y) {
		v[0] = x;
		v[1] = y;
	}
	
	Storage (int x, int y, int k) {
		v[0] = x;
		v[1] = y;
		v[2] = k;
	}
	
	int getX() {
		return v[0];
	}
	
	int getY() {
		return v[1];
	}
	
	int getV() {
		return v[2];
	}
}