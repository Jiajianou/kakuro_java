/**
 * @author JiajianOu, BritonDeets
 * 
 * A Simple Kakuro board game (Cross Sum) that provides color coded interaction with the user.
 * @collaborator Cell, TextFileHandler
 * @param File. A file which contains the path to a usable txt file.
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class GameBoard extends JFrame {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	EventHandler eh = new EventHandler();
	private JMenuBar menuBar;
	private Cell previousCell;
	private Cell currentSelectedCell;
	private Cell currentCell; // the cursor is above this cell.
	private String buttonPressed;
	private int boardSize;
	private Cell[][] cellMatrix;
	private File file;

	public GameBoard(File gameFile) {
		/**
		 * Constructor
		 * @param: txt file with usable string layouts. 
		 * 
		 */
		
		int theSize = new TextFileHandler(gameFile).getBoardSize();		//create container and foundation for the Jcomponents.
		
		setSize(theSize*65, theSize*65);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Kakuro");

		Container c = getContentPane();

		buildMenu();

		c.add(createGamePanel(gameFile));

		setVisible(true);

	}

	public JPanel createGamePanel(File gameFile) {
		
		/**
		 * Constructing cells for the game board.
		 * @param: txt File.
		 * 
		 * passes the file to TextFileHandler and it breaks down the string contend into individual cell info.
		 */
		
		
		JPanel p = new JPanel();

		TextFileHandler tfh = new TextFileHandler(gameFile);						

		String[][] board = tfh.getGameLayout();

		boardSize = board[0].length;

		cellMatrix = new Cell[boardSize][boardSize];

		p.setLayout(new GridLayout(board[0].length, board.length));			//uses 2 dimensional array list to store cells;

		Font customFont = new Font("Arial", Font.BOLD, 20);			

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				
				cellMatrix[i][j] = new Cell(board[i][j]);									//creating each cells

				Cell n = cellMatrix[i][j];
				
				if(n.getCellType().equals("w") && n.getUserNumber() > 0){						//when loading the white cell, if there is a number on it from the previous save
																							//displays it
					
					n.displayText(n.getUserNumber()+"");
					
					
					
				}

				n.setRowNumber(i);
				n.setColumnNumber(j);										//adding listeners to each cell

				n.addMouseListener(eh);
				n.addActionListener(eh);
				n.addKeyListener(eh);

				n.setBorder(new LineBorder(Color.BLACK));
				n.setHorizontalAlignment(JTextField.CENTER);

				n.setFont(customFont);

				p.add(n);
			}

		}

		return p;
	}

	// Cell checking_________________________________________________

	@SuppressWarnings("unchecked")
	public void checkRow(Cell c) {
		
		/**
		 * Checks entire row for the sum of the current section.
		 * @param: int row number.
		 */
		
		
		
		// System.out.println("Check row hit");
				int correctRowSum = 0;
				int currentRowSum = 0;

				int rowNum = c.getRowNumber();
				int colNum = c.getColumnNumber();

				ArrayList<Cell> cellList = new ArrayList<Cell>();
				// gets segment of 1 black cell and white cells to consider
				for (int i = colNum; i >= 0; i--) {
					if (cellMatrix[rowNum][i].getCellType().equals("w")) {
						cellList.add(cellMatrix[rowNum][i]);

					}
					if (cellMatrix[rowNum][i].getCellType().equals("b")) {

						cellList.add(0, cellMatrix[rowNum][i]);
						break;
					}

				}
				for (int i = colNum + 1; i < cellMatrix[rowNum].length; i++) {
					if (cellMatrix[rowNum][i].getCellType().equals("w")) {
						cellList.add(cellMatrix[rowNum][i]);

					}
					if (cellMatrix[rowNum][i].getCellType().equals("b")) {
						break;
					}
					if (cellMatrix[rowNum][i].getCellType().equals("e")) {
						break;
					}

				}

				correctRowSum = cellList.get(0).getUpperNumber();

				for (Cell cl : cellList) {

					if (c.getCellType().equals("w")) {
						currentRowSum += cl.getUserNumber();
					}
				}

				if (currentRowSum == correctRowSum && currentRowSum > 0) {
					cellMatrix[cellList.get(0).getRowNumber()][cellList.get(0).getColumnNumber()].setUpperGreen();
					repaint();
				}
				if (currentRowSum != correctRowSum && currentRowSum > 0) {
					cellMatrix[cellList.get(0).getRowNumber()][cellList.get(0).getColumnNumber()].setUpperRed();
					repaint();
				}

				if (hasDuplicates(cellList) && currentRowSum > 0) {
					cellMatrix[cellList.get(0).getRowNumber()][cellList.get(0).getColumnNumber()].setUpperWhite();
					repaint();
				}

		
		
	}

	public void checkColumn(Cell c) {
		
		/**
		 * Checks the entire Column for sum comparison.
		 * 
		 * @param: int column number.
		 */
		
		
		int correctColSum = 0;
		int currentColSum = 0;

		int rowNum = c.getRowNumber();
		int colNum = c.getColumnNumber();

		ArrayList<Cell> cellList = new ArrayList<Cell>();
		// gets segment of 1 black cell and white cells to consider
		for (int i = rowNum; i >= 0; i--) {
			if (cellMatrix[i][colNum].getCellType().equals("w")) {
				cellList.add(cellMatrix[i][colNum]);

			}
			if (cellMatrix[i][colNum].getCellType().equals("b")) {

				cellList.add(0, cellMatrix[i][colNum]);

				break;
			}

		}

		for (int i = rowNum + 1; i < cellMatrix[rowNum].length; i++) {
			if (cellMatrix[i][colNum].getCellType().equals("w")) {
				cellList.add(cellMatrix[i][colNum]);

			}
			if (cellMatrix[i][colNum].getCellType().equals("b")) {
				break;
			}
			if (cellMatrix[i][colNum].getCellType().equals("e")) {
				break;
			}

		}

		correctColSum = cellList.get(0).getLowerNumber();

		for (Cell cl : cellList) {

			if (c.getCellType().equals("w")) {
				currentColSum += cl.getUserNumber();
			}
		}

		if (currentColSum == correctColSum) {
			cellMatrix[cellList.get(0).getRowNumber()][cellList.get(0).getColumnNumber()].setLowerGreen();
			repaint();
		}
		if (currentColSum != correctColSum && currentColSum > 0) {
			cellMatrix[cellList.get(0).getRowNumber()][cellList.get(0).getColumnNumber()].setLowerRed();
			repaint();
		}

		if (hasDuplicates(cellList)) {
			cellMatrix[cellList.get(0).getRowNumber()][cellList.get(0).getColumnNumber()].setLowerWhite();
			repaint();
		}


	}

	

	public int doMath(Stack<Integer> s) {
		
		/**
		 * Adds all int values in the stack and return the sum.
		 * 
		 * @param Stack<Integer>
		 * @return int. 
		 */
	
	int sum = 0;
	
	while (!s.empty()) {
		
		sum = sum + s.pop();
	}
	
	
	
	return sum;
}

	
	public boolean hasDuplicates(ArrayList<Cell> cellList) {
		for (Cell c : cellList) {
			for (Cell c1 : cellList) {
				if (c.getUserNumber() == c1.getUserNumber() && c.getColumnNumber() != c1.getColumnNumber()
						&& c.getRowNumber() != c1.getColumnNumber()) {
					return true;
				}

				if (c.getUserNumber() == c1.getUserNumber() && c.getRowNumber() != c1.getRowNumber()) {
					return true;
				}

			}
		}

		return false;
	
	}
	public boolean stackHasDuplicates(Stack<Integer> s) {
		
		/**
		 * Checks the stack for duplicates.
		 * 
		 * @param: Stack<Integer>
		 * @return: boolean
		 */

		HashSet<Integer> set = new HashSet<Integer>();			//use hash set one to one logic

		while (!s.empty()) {

			int num = s.pop();
			System.out.println(num);
			if (num != 0) {

				boolean inserted = set.add(num);
				if (inserted == false)
					return true;

			}

		}

		return false;
	}

	public void buildMenu() {
		
		/**
		 * Builds navigating menu.
		 */
		
		menuBar = new JMenuBar();

		menuBar.setLayout(new FlowLayout());

		// creates File menu
		String[] menuItems = { "Open", "Save", "Exit" };
		menuBar.add(buildMenuCategory("File", menuItems));

		String[] optionItems = { "Toggle Suggestions", "Reset" };
		menuBar.add(buildMenuCategory("Options", optionItems));

		menuBar.setLayout(new FlowLayout());

		int buttonWidth = 40; // change button size here
		int buttonHeight = 20;
		
		JButton blank = new JButton("");
		blank.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
		blank.addActionListener(eh);
		menuBar.add(blank);

		for (int i = 1; i < 10; i++) {
			JButton b = new JButton(String.valueOf(i));
			b.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			b.addActionListener(eh);
			menuBar.add(b);
		}

		setJMenuBar(menuBar);

	}

	public JMenu buildMenuCategory(String name, String[] menuItems) {
		/**
		 * Menu sub category.
		 */
		
		JMenu menu = new JMenu(name);
		for (int i = 0; i < menuItems.length; i++) {
			JMenuItem menuItem = new JMenuItem(menuItems[i]);
			menuItem.addActionListener(eh);
			menu.add(menuItem);
		}
		return menu;
	}

	private class EventHandler implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
 		
		/**
		 * EnventHandler.
		 */

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			/**
			 * When a cell is clicked. Updates the current selected cell's location, and changes the color for location identification, and check rows, as well as columns.
			 */


			Cell c = (Cell) arg0.getComponent();

			currentSelectedCell = c;

			currentSelectedCell.setBackground(new Color(122,215,229));

			checkRow(c);
			checkColumn(c);

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
			/**
			 * When the cursor is entered the cell area, changes cell background color for visual aiding.
			 */

			Cell c = (Cell) arg0.getComponent();

			currentCell = c;

			c.setBackground(new Color(206,227,232));

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			/**
			 * When the cursor leaves, the white cell's background color returns to white.
			 */

			currentCell.setBackground(Color.WHITE);

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

			Cell c = (Cell) arg0.getComponent();
			previousCell = c;

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			/**
			 * Handles menu bar JButtons and file saving etc.
			 */

			if (arg0.getActionCommand().equals("Exit")) {
				System.exit(0);
			}

			if (arg0.getActionCommand() == "Open") {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to open");
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File fileToRead = fileChooser.getSelectedFile();
					new GameBoard(fileToRead);
					
					
					
				
					
				}
			}
			
			if (arg0.getActionCommand().equals("Reset")) {

				//GameBoard gb = new GameBoard(file);
				
				for (int v = 0;v < boardSize;v++) {
					for(int w = 0; w < boardSize; w++) {
						Cell thisCell = cellMatrix[v][w];
						
						if(thisCell.getCellType().equals("w")) {
							thisCell.displayText("");
							thisCell.setNumber("0");
							checkRow(thisCell);
							checkColumn(thisCell);
						}


						
					}
				}

			}
			
			if (arg0.getActionCommand().equals("Save")) {
				

				
				JFileChooser fileOpener = new JFileChooser();
				
				fileOpener.setCurrentDirectory(new File("/User/"));
							
				int result = fileOpener.showSaveDialog(new JFrame());
				
				if (result == JFileChooser.APPROVE_OPTION) {
				
			
				 File selectedFile = fileOpener.getSelectedFile();
				
				 
				 try {
					FileWriter lineWriter = new FileWriter(selectedFile.getAbsolutePath());			//stitching cells together to make a string line
					
						for (int u = 0; u < boardSize; u++) {
							
							String thisLine = "";
							
							for(int v = 0; v < boardSize; v++) {
								
								thisLine += cellMatrix[u][v].toString() + ",";
								
							}
							
							String l = thisLine.substring(0, thisLine.length()-1) + "\n";
							
							lineWriter.write(l);
						
							
							
					}
						
						lineWriter.close();
						
						
				} catch (IOException e) {
					// TODO Auto-generated catch block	
					
					e.printStackTrace();
				}
								
				}

			} 
			
					

			// buttons

			buttonPressed = arg0.getActionCommand();
			
			if(buttonPressed.length()<2) {
			currentSelectedCell.displayText(buttonPressed);
			currentSelectedCell.setNumber(buttonPressed);
			}
			// button pressed, check row and column

			checkRow(currentSelectedCell);
			checkColumn(currentSelectedCell);

		}
		
		
		

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
			Cell cell = (Cell) e.getComponent();
			char c = e.getKeyChar();
			String cs = String.valueOf(c);
			currentSelectedCell.displayText(cs);
			currentSelectedCell.setNumber(cs);

			checkRow(cell);
			checkColumn(cell);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public static void main(String[] args) {
		String file = "Resources/10x10Easy.txt";
		File gameFile = new File(file);
		GameBoard gb = new GameBoard(gameFile);
		gb.setFile(gameFile);
	}

	private void setFile(File gameFile) {
		this.file = gameFile;

	}
}