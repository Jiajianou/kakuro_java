/**
 * @author JiajianOu, BritonDeets
 * 
 * A supporting class for the GameBoard() class.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JTextField;

public class Cell extends JTextField {
	
	/**
	 * Maintaining the responsibilities of a cell.
	 */

	private static final long serialVersionUID = 1L;
	private static int width = 100, height = 100;
	private String cellType;
	
	private String upperNum;
	private String lowerNum;
	
	private String upperOriginal;
	private String lowerOriginal;
	
	private int upperSum = 0;
	private int lowerSum = 0;
	
	private int numberEntered = 0;
	
	private int rowNumber;			//grid tracking, for this row, while isWhiteCell is true, check sum
	private int columnNumber;
	
	private String buttonLabel;
	
	private Color upperColor = Color.WHITE;
	private Color lowerColor = Color.WHITE;
	
	
	
	public Cell(String label) {
		
		/**
		 * @param a string segment from TextFileHandler
		 * 
		 * @function breaks down the string segments then assigns the value to appropriate variables.
		 * 
		 * @example B0012   The first character is the cell type. 00 is the top number and 12 is the lower number.
		 */
		
		
		String c = label.charAt(0) + "";								//breaks a string segment down to 3 parts , seg 1, cell type
		
		String upper = label.substring(1,3) + "";				//seg 2 upper number, however, if this is a white cell, this would be the current number
		String lower = label.substring(3,5) + "";				//seg 3 lower number.
		
		
		if(!upper.equals("00") && c.equals("w")) {
			numberEntered = Integer.parseInt(upper.charAt(1)+"");
		}
		
		
		if(c.equals("b")) {
			upperOriginal = upper;
			lowerOriginal = lower;
			
		}
		
		
		if (upper.equals("00")) {
			upperNum = "0";

		} else {
			
			
			if(upper.startsWith("0")) {
				
				upperNum = upper.charAt(1) + "";
			
				
			} else upperNum = upper;
					
		}
		
		
		
		
		if (lower.equals("00")) {
			lowerNum = "0";
		} else {
			
			if (lower.startsWith("0")) {
				
				lowerNum = lower.charAt(1) + "";
				
			} else lowerNum = lower;
		}
		

					
		
		
		
		//setText(c);
		setCellType(c);
		
		setPreferredSize(new Dimension(width, height));
		
		
		
	  
	}
	
	
	public String toString() {
		/**
		 * Overridden toString(). Returns unique string format that the Cell class understands.
		 * 
		 * @return String
		 */
		
		String body;
		String tail;
		
		if(cellType.equals("w")) {
			
			if(numberEntered == 0) {
				
				body = "00";
			} else body = "0"+numberEntered;
			
			tail = "00";
				
		} else if (cellType.equals("e")) {
			body = "00";
			tail = "00";
		
		}else {
			
			body = upperOriginal;
			tail = lowerOriginal;
		}
		
		
		return cellType + body + tail;
	}
	

	public void setNumber(String s) {
		/**
		 * Sets the white cell's current number. Void type. For internal sum calculation.
		 */
		
		if (s == "") {
			
			numberEntered = 0;
		} else {
		
		numberEntered = Integer.parseInt(s);
		}
	}
	
	public void paint(Graphics g) {
		/**
		 * A overridden paint method that creates a distinctive graphics between different cells.
		 */
		
		
		super.paint(g);
		if(this.getCellType().equals("b")) {
			
			
			if(upperNum.equals("0")) {						//if a black cell's top corner sum is 0, hides it by painting it the same as the background color.
				
				g.setColor(Color.WHITE);
				
				g.drawLine(0, 0, width, height);
				
				g.setColor(Color.BLACK);
				
				g.drawString(upperNum, 34, 25); 		//top number
				
				g.setColor(lowerColor);
				g.drawString(lowerNum, 10, 50);		//bottom number
				
				this.setBackground(Color.BLACK);
				this.setOpaque(true);
								
			} else if (lowerNum.equals("0")) {									//same logic
				
				
				g.setColor(Color.WHITE);
				
				g.drawLine(0, 0, width, height);
				
				g.setColor(upperColor);
				
				g.drawString(upperNum, 34, 25); 		//top number
				
				g.setColor(Color.BLACK);
				g.drawString(lowerNum, 10, 50);		//bottom number
				
				this.setBackground(Color.BLACK);
				this.setOpaque(true);
			}
				
			else {
			
			
			g.setColor(Color.WHITE);
			g.drawLine(0, 0, width, height);
			
			g.setColor(upperColor);
			
			g.drawString(upperNum, 34, 25); 		//top number
			
			g.setColor(lowerColor);
			g.drawString(lowerNum, 10, 50);		//bottom number
			
			this.setBackground(Color.BLACK);
			this.setOpaque(true);
			
			}
			

				
		}
		

		
		
		if(this.getCellType().equals("e")) {
			this.setBackground(Color.BLACK);
			this.setOpaque(true);
			
			
		}
		

		//this.setBackground(Color.GREEN);
		
		this.setEditable(false);
		
		this.setOpaque(true);
		
	}
	
	public int getUserNumber() {
		
		/**
		 * Get the current value stored in the white cell. 0 by default.
		 * 
		 * @return int
		 */
		
		
		return numberEntered;
	}
	
	public void numberEntered(int n) {
		/**
		 * Set the white cell number to a int value.
		 */
		
		numberEntered = n;
	}

	public int getUpperNumber() {
		
		/**
		 * @return a black cell's upper sum. int type
		 * 
		 */
		
		return Integer.parseInt(upperNum);
	}
	
	public int getLowerNumber() {
		
		/**
		 * @return a black cell's lowerSum. int type
		 */
		
		return Integer.parseInt(lowerNum);
	}
	
	public String getCellType() {
		
		/**
		 * @return String. Cell type.
		 */
		return cellType;
	}
	
	public void setCellType(String t) {
		/**
		 * Sets cell type to w, b or e.
		 */
		this.cellType = t;
	}
	
	public boolean isWhiteCell() {
		
		/**
		 * @return boolean. Whether or not a cell is white.
		 */
		
		if(this.cellType.equals("w")) {
			return true;
		} return false;
	}
	
	public void displayText(String numberEntered) {
		
		/**
		 * Displays string on white cell.
		 */
		
		buttonLabel = numberEntered;
		
		if(cellType.equals("w")) this.setText(buttonLabel);
	}
	
	public void setRowNumber(int row) {
		
		/**
		 * Sets row number.
		 */
		
		rowNumber = row;
		
	}
	
	public void setColumnNumber(int col) {
		
		/**
		 * Sets column number.
		 */
		
		columnNumber = col;
	}
	
	public int getRowNumber() {
		
		/**
		 * @return int. Row number.
		 */
		
		return rowNumber;
	}
	
	public int getColumnNumber() {
		
		/**
		 * @return int. Column number.
		 */
		
		return columnNumber;
	}
	
	public String getButtonLabel() {
		/**
		 * @return String. Value on white cell.
		 */
		return buttonLabel;
	}
	
	public void setUpperRed() {
		/**
		 * Sets the black cell's upper sum number to red.
		 */
		upperColor = Color.RED;
	}
	
	public void setUpperGreen() {
		/**
		 * Sets the black cell's upper sum number to green.
		 */
		
		upperColor = Color.GREEN;
	}
	
	public void setUpperWhite() {
		/**
		 * Sets the black cell's upper number to white.
		 */
		upperColor = Color.WHITE;
	}
	
	public void setLowerRed() {
		/**
		 * Sets the black cell's lower sum to red.
		 */
		
		lowerColor = Color.RED;
	}
	
	public void setLowerGreen() {
		/**
		 * Sets the black cell's lower sum to green.
		 */
		lowerColor = Color.GREEN;
	}
	
	public void setLowerWhite() {
		/**
		 * Sets the black cell's lower sum to white.
		 */
		
		lowerColor = Color.WHITE;
	}
	
	public void setButtonLabel(String buttonLabel) {
		/**
		 * Sets cell's label.
		 */
		
		this.buttonLabel = buttonLabel;
	}
	
}