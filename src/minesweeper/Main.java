package minesweeper;

public class Main implements Runnable{
	private GUI gui = new GUI();
	public static void main(String[] args) {
		new Thread(new Main()).start();
		
	}
	@Override
	public void run() {
		while(true) {
			gui.repaint();
			if(gui.resetter == false) {
				gui.checkVictoryStatus();
				System.out.println("victory:"+ gui.victory + " Deafeat:" + gui.defeat);
			}			
		}
		
	}

}
