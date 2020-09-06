package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public boolean resetter = false;
	boolean flagger = false;
	Date startDate = new Date();
	Date endDate;
	int spacing = 5;
	//coordinates of mouse pointer
	int mx = -100, my = -100;
	int neighs;
	int smileyX = 605;
	int smileyY = 5;
	int smileyCenterX = smileyX + 35;
	int smileyCenterY = smileyY + 35;
	
	int flaggerX = 445;
	int flaggerY = 5;
	
	int flaggerCenterX = flaggerX + 35;
	int flaggerCenterY = flaggerY + 35;
	
	String vicMeString = "Nothing Yet!!";
	int vicMesX = 740;
	int vicMesY = -50;
	int timeX = 1120;
	int timeY = 5;
	
	boolean happiness = true;
	boolean victory = false;
	boolean defeat = false;
	Random rand = new Random();
	int sec = 0;
	
	int[][] mines = new int[16][9];
	int[][] neighbours = new int[16][9];
	boolean[][] revealed = new boolean[16][9];
	boolean[][] flagged = new boolean[16][9];
	
	
	
	
	public GUI() {
		this.setTitle("Minesweeper");
		this.setSize(1286, 829);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		// initialize mines and revealed matrix
		for(int i=0;i<16;i++) {
			for(int j=0;j<9;j++) {
				if(rand.nextInt(100) < 20) {
					mines[i][j] = 1;
				} else {
					mines[i][j] = 0;
				}
				revealed[i][j] = false;
			}
		}
		//initialize neighbours
		for(int i=0;i<16;i++) {
			for(int j=0;j<9;j++) {
				neighs = 0;
				for(int m=0;m<16;m++) {
					for(int n=0;n<9;n++) {
						if(m==i && n==j) continue;
						if(isN(i, j, m, n)==true) {
							neighs++;
						}
					}
				}
				neighbours[i][j]=neighs;
			}
		}
		
		
		Board board = new Board();
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
	}
	
	public class Board extends JPanel {

		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
			g.setColor(Color.gray.darker());
			g.fillRect(0, 0, 1280, 800);		
			
			for(int i=0;i<16;i++) {
				for(int j=0;j<9;j++) {
					
					g.setColor(Color.gray);
					
					if(mines[i][j]==1)
						g.setColor(Color.yellow);
					if(revealed[i][j]==true) {
						g.setColor(Color.white);
						if(mines[i][j]==1) {
							g.setColor(Color.red);
						}
					}
					if(mx >= spacing+i*80 && mx < i*80+80-spacing && my >= spacing +j*80+80+26 && my < j*80+80 + 80-spacing + 26 ) {
						g.setColor(Color.gray.brighter());
					}
					g.fillRect(spacing+i*80, spacing + j*80+80, 80-2*spacing, 80-2*spacing);
					if(revealed[i][j]==true) {
						g.setColor(Color.black);
						if(mines[i][j]==0 && neighbours[i][j]!=0) {
							switch (neighbours[i][j]) {
								case 1:
									g.setColor(Color.blue);
									break ;
								case 2:
									g.setColor(Color.green);
									break ;
								case 3:
									g.setColor(Color.red);
									break ;
								case 4:
									g.setColor(new Color(0,0,128));
									break ;
								case 5:
									g.setColor(new Color(178,34,34));
									break ;
								case 6:
									g.setColor(new Color(72,209,204));
									break ;
								case 8:
									g.setColor(Color.DARK_GRAY);
									break ;
							}
							g.setFont(new Font("Tahoma", Font.BOLD, 40));
							g.drawString(Integer.toString(neighbours[i][j]), i*80+27, j*80+80+55);
						} else if(mines[i][j]==1){
							g.fillRect(i*80+10+20, j*80+80+20, 20, 40);
							g.fillRect(i*80+20, j*80+80+10+20, 40, 20);
							g.fillRect(i*80+5+20, j*80+80+5+20, 30, 30);
							g.fillRect(i*80+38, j*80+80+15, 4, 50);
							g.fillRect(i*80+15, j*80+80+38, 50, 4);
						}
					}
					
					//flags painting
					if(flagged[i][j] == true && revealed[i][j] == false) {
						
						g.setColor(Color.black);
						g.fillRect(i*80+32+5, j*80+80+15+5, 5, 40);
						g.fillRect(i*80+20+5, j*80+80+50+5, 30, 10);
						g.setColor(Color.red);
						g.fillRect(i*80+16+5, j*80+80+15+5, 20, 15);
						g.setColor(Color.black);
						g.fillRect(i*80+16+5, j*80+80+15+5, 20, 15);
						g.fillRect(i*80+17+5, j*80+80+16+5, 18, 13);
						g.fillRect(i*80+18+5, j*80+80+17+5, 16, 11);
					}
				}
			}
			
			// paint smiley
			g.setColor(Color.yellow);
			g.fillOval(smileyX, smileyY, 70, 70);
			g.setColor(Color.black);
			g.fillOval(smileyX+15, smileyY+20, 10, 10);
			g.fillOval(smileyX+45, smileyY+20, 10, 10);
			if(happiness == true) {
				g.fillRect(smileyX+20, smileyY+50, 30, 5);
				g.fillRect(smileyX+17, smileyY+45, 5, 5);
				g.fillRect(smileyX+48, smileyY+45, 5, 5);
			} else {
				g.fillRect(smileyX+20, smileyY+45, 30, 5);
				g.fillRect(smileyX+17, smileyY+50, 5, 5);
				g.fillRect(smileyX+48, smileyY+50, 5, 5);
			}
			
			//flagger painting
			
			g.setColor(Color.black);
			g.fillRect(flaggerX+32, flaggerY+15, 5, 40);
			g.fillRect(flaggerX+20, flaggerY+50, 30, 10);
			g.setColor(Color.red);
			g.fillRect(flaggerX+16, flaggerY+15, 20, 15);
			g.setColor(Color.black);
			g.fillRect(flaggerX+16, flaggerY+15, 20, 15);
			g.fillRect(flaggerX+17, flaggerY+16, 18, 13);
			g.fillRect(flaggerX+18, flaggerY+17, 16, 11);
			
			if(flagger == true) {
				g.setColor(Color.red);
			}
			
			g.drawOval(flaggerX, flaggerY, 70, 70);
			g.drawOval(flaggerX+1, flaggerY+1, 68, 68);
			g.drawOval(flaggerX+2, flaggerY+2, 66, 66);
			
			
			// paint time counter
			g.setColor(Color.black);
			g.fillRect(timeX, timeY, 140, 70);
			if(defeat == false && victory == false) {
				sec = (int)((new Date().getTime() - startDate.getTime()) / 1000);
			}
			if(sec > 999) {
				sec = 999;
			}
			g.setColor(Color.white);
			g.setFont(new Font("Tahoma", Font.PLAIN, 80));
			if(sec < 10) {
				g.drawString("00"+Integer.toString(sec), timeX, timeY+65);
			} else if(sec<100) {
				g.drawString("0"+Integer.toString(sec), timeX, timeY+65);
			} else {
				g.drawString(Integer.toString(sec), timeX, timeY+65);
			}
			
			
			//victory message
			if(victory==true) {
				g.setColor(Color.green);
				vicMeString = "YOU WIN";
	
			} else if(defeat == true) {
				g.setColor(Color.red);
				vicMeString = "YOU LOSE";
			}
			
			if(victory == true || defeat == true) {
				vicMesY =  (-50 + (int)(new Date().getTime() - endDate.getTime())/10);
				if(vicMesY>70) {
					vicMesY = 70;
				}
				g.setFont(new Font("Tahoma", Font.PLAIN, 70));
				g.drawString(vicMeString, vicMesX, vicMesY);
			}
		}
	}
	
	public class Move implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();			
		}
		
	}

	public class Click implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {

			if(inBoxX() != -1 && inBoxY() != -1) {
				if(flagger == true && revealed[inBoxX()][inBoxY()] == false) {
					if(flagged[inBoxX()][inBoxY()] == false) {
						flagged[inBoxX()][inBoxY()] = true;
					} else {
						flagged[inBoxX()][inBoxY()] = false;
					}
				} else {
					revealed[inBoxX()][inBoxY()] = true;
				}				
				//System.out.println("mouse is in"+ inBoxX()+" "+inBoxY()+" , mine neigh: "+neighbours[inBoxX()][inBoxY()]);
			}
			
			
			if(inSimely() == true) {
				System.out.println("inside smiley");
				reset();
			}
			
			if(inFlagger() == true) {
				if(flagger==false ) {
					flagger = true;
				} else {
					flagger = false;
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public int totalMines() {
		int total = 0;
		for(int i=0; i<16;i++) {
			for(int j=0;j<9;j++) {
				if(mines[i][j]==1)
					total++;
			}
		}
		return total;
		
	}
	
	public int totalBoxesRevealed() {
		int total = 0;
		for(int i=0; i<16;i++) {
			for(int j=0;j<9;j++) {
				if(revealed[i][j]==true)
					total++;
			}
		}
		return total;
	}
	
	public void checkVictoryStatus() {
		if(defeat == false) {
			for(int i=0;i<16;i++) {
				for(int j=0;j<9;j++) {
					if(revealed[i][j]== true && mines[i][j]==1) {
						defeat = true;
						happiness = false;
						endDate = new Date();
					}
				}
			}
		}
		if(totalBoxesRevealed() >= 144 - totalMines() && victory == false) {
			victory = true;
			endDate = new Date();
		}
		
	}
	
	public void reset() {
		flagger = false;
		resetter = true;
		startDate = new Date();
		happiness = true;
		victory = false;
		defeat = false;
		vicMesY = -50;
		vicMeString = "Nothing Yet!!";
		// initialize mines and revealed matrix
		for(int i=0;i<16;i++) {
			for(int j=0;j<9;j++) {
				if(rand.nextInt(100) < 20) {
					mines[i][j] = 1;
				} else {
					mines[i][j] = 0;
				}
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		//initialize neighbours
		for(int i=0;i<16;i++) {
			for(int j=0;j<9;j++) {
				neighs = 0;
				for(int m=0;m<16;m++) {
					for(int n=0;n<9;n++) {
						if(m==i && n==j) continue;
						if(isN(i, j, m, n)==true) {
							neighs++;
						}
					}
				}
				neighbours[i][j]=neighs;
			}
		}
		resetter = false;
	}

	public boolean inSimely() {
		int dif = (int) Math.sqrt(Math.abs(mx-smileyCenterX)*Math.abs(mx-smileyCenterX) + Math.abs(my-smileyCenterY)*Math.abs(my-smileyCenterY) );
		if(dif<35) return true;
		return false;
	}
	
	public boolean inFlagger() {
		int dif = (int) Math.sqrt(Math.abs(mx-flaggerCenterX)*Math.abs(mx-flaggerCenterX) + Math.abs(my-flaggerCenterY)*Math.abs(my-flaggerCenterY) );
		if(dif<35) return true;
		return false;
	}
	
	public int inBoxX() {
		for(int i=0;i<16;i++) {
			for(int j=0;j<9;j++) {
				if(mx >= spacing+i*80 && mx < i*80+80-spacing && my >= spacing +j*80+80+26 && my < j*80+80 + 80-spacing + 26 ) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int inBoxY() {
		for(int i=0;i<16;i++) {
			for(int j=0;j<9;j++) {
				if(mx >= spacing+i*80 && mx < i*80+80-spacing && my >= spacing +j*80+80+26 && my < j*80+80 + 80-spacing + 26 ) {
					return j;
				}
			}
		}
		return -1;
	}

	public boolean isN(int mX, int mY, int cX, int cY) {
		if(mX - cX < 2 && mX-cX > -2 && mY -cY < 2 && mY - cY > -2 && mines[cX][cY] == 1) {
			return true;			
		}
		return false;				
	}

	
}
