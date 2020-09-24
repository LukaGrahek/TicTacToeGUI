import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import javax.swing.JOptionPane;

/**
 * TicTacToe Game
 * @author Luka Grahek
 * May 6- May 9 , 2020
 * 
 * The scores from each session will be saved when the program is closed. This is 
 * done by setting the setDefaultCloseOperation() to not close, and instead run a  
 * method that adds the current scores to a txt file then closes the program.
 * 
 * There is also a JOptionPane to make sure the user wants to close the program.
 * 
 */

public class TicTacToeGUI extends JFrame implements MouseListener  {

	private JFrame JFrame;
	private JPanel back;                 //JPanel that is behind the TicTacToe board                       
	private JPanel gamePanel;            //Game Panel that sits on top of the back JPanel
	private JLabel[] spot;               //9 spots on the board. Used JLabel to allow for better animation
	private JLabel playBtn;              //Press this JLabel to play, and to play again. Used JLabel to allow for better animation
	private JLabel turnDisplay;          //Displays who's turn it is, X or O's turn
	private JLabel title;                //Title of the game at the top 
	private JLabel prevScores;           //Button to display previous scores
	private JTextArea intro;             //JTextArea for the instructions on the menu/starting page
	private JTextArea scoreDisp;         //Displays previous scores
	private JLayeredPane layeredPane;    //layeredPane allows JPanels to be put on top of each other in the preferred order
	private static String turn = "X";    //Who's turn is it. X or O, X starts first game, loser starts the play again games, and switches on ties.
	private String winner;               //if there is a winner, the winner will be here, either X or O.
	private Font fTitle = new Font("Stencil", Font.PLAIN, 33);                                //Font for the title
	private Font scoreDFont = new Font("Stencil", Font.PLAIN, 30);                            //Font for category of previous scores display
	private Font turnFont = new Font("Arial", Font.PLAIN, 30);                                //Font for the turnDisplay JLabel
	private Font introFont = new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 30);     //Font for the instructions at the starting page/menu
	private Font playAgainFont = new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 20);  //Play again button font
	private Font playFont = new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 60);       //Play button font at the starting page/menu
	private static int xWins;            //total amount of wins that player X has
	private static int oWins;            //total amount of wins that player O has
	private static int ties;             //total amount of ties
	private static int games = 1;        //total games played
	private boolean started = false;     //whether or not the starting page/menu start button has been pressed
	private boolean full;                //whether or not there is a tie
	private boolean dispScores = false;  //whether or not the previous scores on on display

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TicTacToeGUI frame = new TicTacToeGUI();
				//in case there is any error when starting
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				//when the user clicks the red X in the top right of the window this will run,
				//adding the current scores to the prevScores text file, then closing the program.
				frame.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent windowEvent) {

						//Changes the background color of the JOptionPane when closing
						UIManager UI=new UIManager();
						UI.put("OptionPane.background",new ColorUIResource(128,90,70));
						UI.put("Panel.background",new ColorUIResource(120,150,0));

						//if the user wants to close the program, scores will be saved to txt
						//file and program will close otherwise the program won't close.
						if (JOptionPane.showConfirmDialog(frame, 
								"Are you sure you want to CLOSE Luka's TiktacToe", "You don't have to quit...", 
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
							try {
								FileWriter fw = new FileWriter("Scores.txt",true);
								PrintWriter pw = new PrintWriter(fw);

								FileReader fr = new FileReader("Scores.txt");
								BufferedReader br = new BufferedReader(fr);
								String line = br.readLine();//checker
								int p = 0;                  //counter
								while(line !=null) {
									line = br.readLine();
								}
								//if there has been at least 1 game played,  it will save the score
								if(games-1>0) {
									pw.printf("\n"+(games-1)+"               "+xWins+"               "+oWins+"              "+ties);
									pw.close();
								}

							} catch (IOException e) {
								System.out.println("txt file problem");
								e.printStackTrace();
							}
							System.exit(0);
						}
					}
				});

			}
		});
	}

	/**
	 * game 
	 */
	public TicTacToeGUI() {



		//set the properties of the JFrame
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //When the user wants to close the program, their scores will saved first, then the program will close.
		setBounds(400, 100, 589, 800);
		setVisible(true);
		setResizable(false);
		setTitle("Luka's TikTakToe");

		//set the properties of the layeredPane
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 583, 733);
		getContentPane().add(layeredPane);
		layeredPane.setLayout(null);

		//set the properties of the back JPanel
		back = new JPanel();
		back.setBounds(0, 0, 583, 771);
		back.setBackground(new Color(188, 143, 143));
		back.setBorder(new LineBorder(new Color(128, 128, 0), 10));
		back.setLayout(null);
		layeredPane.add(back,1);

		//set the properties of the game board JPanel
		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(240, 128, 128));
		gamePanel.setBounds(50, 75, 483, 463);
		gamePanel.setLayout(new GridLayout(3, 3,5,5));
		gamePanel.setVisible(false);
		layeredPane.add(gamePanel,0);

		////set the properties of title
		title = new JLabel("Luka's Game of TicToeTac");
		title.setBounds(79, 21, 429, 33);
		title.setFont(fTitle);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		back.add(title);

		//set the properties of the turn display JLabel
		turnDisplay = new JLabel("");
		turnDisplay.setBounds(240,540,422,70);
		turnDisplay.setVisible(false);
		turnDisplay.setOpaque(false);
		turnDisplay.setFont(turnFont);
		back.add(turnDisplay);

		//set the properties of the intro/instructions JTextArea
		intro = new JTextArea("Welcome to Luka's TicToeTac!"
				+ "\n There will be two Players:"
				+ "\n player: X , and player: O"
				+ "\n Press the PLAY button to begin.");
		intro.setBounds(100,200,422,200);
		intro.setOpaque(false);
		intro.setEditable(false);
		intro.setFont(introFont);
		back.add(intro);

		scoreDisp = new JTextArea();
		scoreDisp.setBounds(79,24,429,600);
		scoreDisp.setVisible(false);
		scoreDisp.setEditable(false);
		scoreDisp.setOpaque(false);
		scoreDisp.setFont(introFont);
		back.add(scoreDisp);

		prevScores = new JLabel("Previous Scores");
		prevScores.setBounds(90,675,422,60);
		prevScores.setBorder(new LineBorder(new Color(100, 120, 0), 10));
		prevScores.setHorizontalAlignment(SwingConstants.CENTER);
		prevScores.setFont(introFont);
		prevScores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		prevScores.addMouseListener(new MouseAdapter() {
			//when the "Previous Scores" button has been pressed it will do a pressed down animation
			@Override
			public void mousePressed(MouseEvent arg0) {
				prevScores.setBounds(100,680,402,50);
				prevScores.setBorder(new LineBorder(new Color(100, 100, 0), 10));
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						try {
							//return JLabel to its original size/color
							prevScores.setBounds(90,675,422,50);
							prevScores.setBorder(new LineBorder(new Color(100, 120, 0), 10));

							//wait half a second
							Thread.sleep(500);	
							
							//if the scores are not currently displayed, they will be
							if(!dispScores) {
								turnDisplay.setVisible(false);
								gamePanel.setVisible(false);
								viewScores();
								title.setText("Games  X-Wins  O-Wins  Ties");
								title.setFont(scoreDFont);
								intro.setVisible(false);
								scoreDisp.setVisible(true);
								dispScores = true;
								prevScores.setText("Back");
								playBtn.setVisible(false);
							}
							//if the scores are currently displayed, the program will go back to the menu/game
							else {

								title.setText("Luka's Game of TicToeTac");
								dispScores = false;
								scoreDisp.setVisible(false);
								intro.setVisible(true);
								prevScores.setText("Previous Scores");
								if(started) {
									turnDisplay.setVisible(true);
									gamePanel.setVisible(true);
								}
								if(!started) {
									playBtn.setVisible(true);
								}
							}
						} catch (InterruptedException q) {
							q.printStackTrace();
						}
					}
				}).start();
			}

		});
		back.add(prevScores);

		//set the properties of the Play and Play Again JLabels
		playBtn = new JLabel("Play");

		//MouseListener for when the JLabel is pressed and released
		playBtn.addMouseListener(new MouseAdapter() {

			//when pressed, the JLabel will turn into a smaller form of itself, so the the user knows they pressed the button
			@Override
			public void mousePressed(MouseEvent arg0) {
				//for the play button
				if(!started) {
					playBtn.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 40));
					playBtn.setBounds(170, 415, 240, 120);
					playBtn.setBorder(new LineBorder(new Color(100, 120, 0), 10));
				}
				//for the play again button
				else {

					playBtn.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 15));
					playBtn.setBounds(199, 550, 180, 60);
					playBtn.setBorder(new LineBorder(new Color(128, 170, 0), 10));
				}
			}
			//when the JLabel is released, it will go back to its original size, wait half a second, then start the game from the beginning.
			@Override
			public void mouseReleased(MouseEvent arg0) {
				//for the play button
				if(!started) {
					new Thread(new Runnable() {
						public void run() {
							try {
								//return JLabel to its original size/color
								playBtn.setFont(playFont);
								playBtn.setBounds(140, 400, 300, 150);
								playBtn.setBorder(new LineBorder(new Color(128, 170, 0), 10));

								//wait half a second
								Thread.sleep(500);

								//start the first game
								gamePanel.setVisible(true);
								playBtn.setBounds(190, 545, 200, 70);
								playBtn.setText("PLAY AGAIN");
								playBtn.setFont(playAgainFont);
								intro.setBounds(100,620,422,50);
								updateScore();
								turnDisplay.setVisible(true);
								turnDisplay.setText(turn+"'s turn");
								playBtn.setVisible(false);
								started = true;
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}).start();
				}
				//for the play again button
				else {
					new Thread(new Runnable() {
						public void run() {
							try {
								//return JLabel to its original size/color
								playBtn.setBounds(190, 545, 200, 70);
								playBtn.setFont(playAgainFont);

								//wait half a second
								Thread.sleep(500);	

								//reset the game board
								turnDisplay.setVisible(true);
								turnDisplay.setText(turn+"'s turn");
								playBtn.setVisible(false);

								//reset all 9 spots on the board
								for(int i = 0;i<9;i++) {
									spot[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
									spot[i].setBorder(new LineBorder(new Color(69, 4, 20), 10));
									spot[i].setText("");
								}
								winner = null;
								full = false;
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}).start();
				}
			}
		});
		//continue setting the properties of the play/play again JLabel
		playBtn.setHorizontalAlignment(SwingConstants.CENTER);
		playBtn.setFont(playFont);
		playBtn.setBounds(140, 400, 300, 150);
		playBtn.setBorder(new LineBorder(new Color(128, 170, 0), 10));
		playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		back.add(playBtn);

		//initialize/set the properties for all 9 spots on the board
		spot = new JLabel[9];
		for(int i = 0;i<9;i++) {
			spot[i] = new JLabel("");
			spot[i].setBorder(new LineBorder(new Color(69, 4, 20), 10));
			spot[i].setHorizontalAlignment(SwingConstants.CENTER);
			spot[i].setFont(new Font("Arial", Font.BOLD, 1));
			spot[i].addMouseListener(this);
			spot[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			gamePanel.add(spot[i]);
		}
	}

	//not used, but must be here because the JFrame implements MouseListener.
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	//changes the spot border to white when you mouse over the spot.
	@Override
	public void mouseEntered(MouseEvent e) {
		//if the game is over the border won't switch color.
		if(winner == null&&!full) {
			//checks which spot is being hovered over by the mouse
			for (int i = 0; i < 9; i++) {
				if (e.getSource() ==spot[i]) { 
					//if the border is black, the border will not change color.(black border means the spot is being pressed on)
					if(spot[i].getBackground().getRGB()!=(new Color(0,0,0)).getRGB())
						spot[i].setBorder(new LineBorder(new Color(200, 200, 255), 10));
				}
			}
		}
	}

	//when the mouse exits the spot, it will go back to its original color
	@Override
	public void mouseExited(MouseEvent e) {
		//if the game is over the border won't switch color.
		if(winner == null&&!full) {
			//checks which spot the mouse left
			for (int i = 0; i < 9; i++) {
				if (e.getSource() ==spot[i]) { 
					//if the border is black, the border will not change color.(black border means the spot is being pressed on)
					if(spot[i].getBackground().getRGB()!=(new Color(0,0,0)).getRGB()) {
						spot[i].setBorder(new LineBorder(new Color(69, 4, 20), 10));
					}
				}
			}
		}
	}

	//when the mouse presses on a spot that has not been taken, the border will change to black
	@Override
	public void mousePressed(MouseEvent e) {
		//if the game is over the spot won't change
		if(winner == null&&!full) {
			//checks which spot the mouse pressed
			for (int i = 0; i < 9; i++) {
				if (e.getSource() ==spot[i]) {
					//only if the spot has not been taken it will change it's border color to black
					if(spot[i].getText()=="") {
						spot[i].setBorder(new LineBorder(new Color(0, 0,0), 10));
						spot[i].setBackground(new Color(0,0,0));
					}
				}
			}
		}
	}

	//when the mouse has been released, it will check what should go in that spot
	@Override
	public void mouseReleased(MouseEvent e) {
		//if the game is over the spot won't change
		if(winner == null&&!full) {
			new Thread(new Runnable() {
				public void run() {
					//checks which spot the mouse released
					for (int i = 0; i < 9; i++) {
						if (e.getSource() ==spot[i]) {
							//if the spot is empty
							if(spot[i].getText()=="") {
								spot[i].setBackground(new Color(238,238,238));
								spot[i].setBorder(new LineBorder(new Color(69, 4, 20), 10));
								spot[i].setText(getTurn());
								spot[i].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

								//next players turn
								setTurn();
								turnDisplay.setText(turn+"'s turn");

								//The animation of the X/O appearing on the board
								for(int s = 2;s<80;s++) {
									spot[i].setFont(new Font("Arial", Font.BOLD, s));
									try {
										Thread.sleep(1);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}

								//checks if this was the winning move
								if(isWinner()) {
									playBtn.setVisible(true);
									turnDisplay.setVisible(false);
									updateScore();
								}

								//checks if this spot being taken tied the game, there can't be a winner and a tied game 
								if(winner==null) {
									if(isFull()) {
										//freeze all spots, so that they cannot be altered without pressing the play again JLabel
										for (int l = 0; l < 9; l++) {
											spot[l].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
											spot[l].setBorder(new LineBorder(new Color(69, 4, 20), 10));
										}
										turnDisplay.setVisible(false);
										playBtn.setVisible(true);
										updateScore();
									}
								}
							}
						}
					}
				}
			}).start();
		}
	}

	/**
	 * what turn it is.
	 * @return who's turn it is, X or O.
	 */
	public static String getTurn() {
		return turn;
	}

	/**
	 * set the next players turn
	 */
	public static void setTurn() {
		//if the previous turn was X, this turn is O.
		if(turn == "X") {
			turn = "O";
		}
		//if the previous turn was O, this turn is X.
		else {
			turn = "X";
		}
	}

	/**
	 * checks all possible winning spots, to check is there has been a winner.
	 * @return whether or not there is a winner.
	 */
	public boolean isWinner() {
		String p; // p, short for player, is used to check if either player X or O has won.
		//checks both for player X and O
		for(int a = 0;a<2;a++) {
			p= "O";
			if(a==0) {
				p = "X";
			}
			//return true if there is a winner in any of these spot combinations
			if(checkWinner(0,1,2,p)||
					checkWinner(3,4,5,p)||
					checkWinner(6,7,8,p)||
					checkWinner(0,3,6,p)||
					checkWinner(1,4,7,p)||
					checkWinner(2,5,8,p)||
					checkWinner(0,4,8,p)||
					checkWinner(2,4,6,p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks the combinations of three spots to see if in those three spots are all X/O
	 * @param a spot 1
	 * @param b spot 2
	 * @param c spot 3
	 * @param p to check for either "X" or "O"
	 * @return if this spot combination is a winner. true means winning spots
	 */
	public boolean checkWinner(int a, int b, int c, String p) {
		//if all these spots contain the same character(X or O)
		if(spot[a].getText() ==p&&spot[b].getText()==p&&spot[c].getText()==p) {
			//freeze all the spots
			for (int i = 0; i < 9; i++) {
				spot[i].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				spot[i].setBorder(new LineBorder(new Color(69, 4, 20), 10));
			}
			//change the winning spots to green
			spot[a].setBorder(new LineBorder(new Color(0, 255, 0), 10));
			spot[b].setBorder(new LineBorder(new Color(0, 255, 0), 10));
			spot[c].setBorder(new LineBorder(new Color(0, 255, 0), 10));
			//set the winner
			winner = p;
			//update the score if X won 
			if(p.equals("X")) {
				xWins++;
				games++;

			}
			//update the score if O won 
			if(p.equals("O")) {
				oWins++;
				games++;

			}
			return true;
		}
		return false;
	}

	/**
	 * Checks if there has been a tie
	 * @return true for a tie, false for not a tie
	 */
	public boolean isFull() {
		//checks if any spots are empty
		for (int i = 0; i < 9; i++) {
			//if a spot is empty, that means there is not a tie
			if(spot[i].getText()==""){
				return false;
			}
		}
		//if no spots are empty the board will freeze
		for (int i = 0; i < 9; i++) {
			spot[i].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			spot[i].setBorder(new LineBorder(new Color(69, 4, 20), 10));
		}
		//if there is not already a winner, the total tie count will be increased.
		if(winner==null) {
			ties++;
			games++;

			full = true;
		}
		return true;
	}

	/**
	 * whenever there is a win or a tie, the score can be updated by calling this method.
	 *
	 */
	public void updateScore() {
		intro.setText("X wins: "+xWins+"      O wins: "+ oWins+ "      ties: "+ties);
	}

	/**
	 * Will update the scores table with this sessions scores
	 * @return 1  to close to program
	 */
	public void viewScores() {
		try {
			scoreDisp.setText("");
			FileWriter fw = new FileWriter("Scores.txt",true);
			PrintWriter pw = new PrintWriter(fw);

			FileReader fr = new FileReader("Scores.txt");
			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			while(line !=null) {
				scoreDisp.append("\n"+line);
				line = br.readLine();
			}
		}
		catch(IOException r) {
			System.out.print("error with printing scores");
		}

	}
}