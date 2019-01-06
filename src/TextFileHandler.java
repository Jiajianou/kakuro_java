/**
 * @author JiajianOu, BritonDeets
 * 
 * A supporting class for Cell() and GameBoard().
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextFileHandler {
	/**
	 * Handles cell formating in txt files.
	 */
	
	private String gameLayout [][];
	private int gridSize;
	
	public TextFileHandler(File gameFile) {
		
		/**
		 * Constructor.
		 * @param: File. Cell format txt file.
		 */

		
		
		try {
			
			
			Scanner reader = new Scanner(gameFile);								//reads into the file and breaks down to appropriate parts
			
			String [] sampleLine = reader.next().split(",");
			
			gridSize = sampleLine.length;
			
			gameLayout = new String [gridSize] [gridSize];
			
			for(int i = 0; i < sampleLine.length; i ++) {
				gameLayout[0][i] = sampleLine[i];
			}
			
			
			int i = 1;

			while(reader.hasNext()) {
				
				String line = reader.next();
				
				String [] lineSegments = line.split(",");
				
				for (int j = 0; j < gridSize; j++) {
					
					gameLayout[i][j] = lineSegments[j];
						
				}
				
				i++;
			}
			
			reader.close();
					
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public String toString() {
		
		String outPutString = "";
		
		for (int i = 0 ; i < gridSize; i++) {
			for (int j = 0 ; j < gridSize ; j++) {
				outPutString += gameLayout[i][j] + "  ";
			}
			outPutString += "\n";
		}
		return outPutString;	
	}
	
	public int getGridSize() {
		/**
		 * @return int. The size of the board or number of cells on each row and column.
		 */
		
		return gridSize;
	}
	
	public String [][] getGameLayout(){
		/**
		 * @return 2 dimensional string array.
		 */
		
		return gameLayout;
	}
	
	public int getBoardSize() {
		/**
		 * @return int. Board size.
		 */
		return gridSize;
	}
	

	
}
