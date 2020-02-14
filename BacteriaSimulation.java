import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Musa Hamwala II
 */
public class BacteriaSimulation
{
	static int genNum = 0;
	static final int ALIVE = 1;
	static final int DEAD = 0;
    
    public static void main(String[] args)
    {
    	/**
    	 	Taking in console input for the simulation to begin
         */
    	System.out.println("-- Bacteria Simulation --\n");
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a series of comma separated integers with a space in between. finalised with end\r\n" + 
        		"e.g (1,2 2,2 3,2 3,9 end):\n\nInput:");
        
        String input = in.nextLine();
        
        // Checks for the "end" within the string input
        while(!input.endsWith("end")) {
        	System.out.println("Please enter a series of comma separated integers with a space in between. finalised with end\r\n" + 
            		"e.g (1,2 2,2 3,2 3,9 end):\n\nInput:");
        	input = in.nextLine();
        }
        
        // removing end from the string input
        String validatedInput = input.replace("end", "");
        
        	/**
           Splitting the input by a space
        	 */
        	String inputBacteriaCells[] = validatedInput.split(" ");
        	int size_x = 0;
        	int size_y = 0;
        	
        	int count  = 0;
        	
        	/**
         	Separate X and Y from the input
         	Initialising petri dish (2d array)
        	 */
        	for (String tmp : inputBacteriaCells)
        	{
        		String tmp_array[] = inputBacteriaCells[count].split(",");
        		int x = Integer.parseInt(tmp_array[0]);
        		int y = Integer.parseInt(tmp_array[1]);
        		System.out.println("cords x: "+x+" y: "+y);
        		
        		/**
             	Getting size of X and Y for Grid generation (adding 3 to complete the grid)
        		 */
        		if (size_x < x)
        		{
        			size_x = x;
        		}
        		if(size_y < y)
        		{
        			size_y = y;
        		}
        		
        		count++;
        	}
        	
        	// Initialising petri dish (2d array)
        	int solution[][] = new int[size_x+1][size_y+1];
        	int x = 0;
        	int y = 0;
        	count = 0;
        	
        	for (String tmp : inputBacteriaCells)
        	{
        		String tmp_array[] = inputBacteriaCells[count].split(",");
        		x = Integer.parseInt(tmp_array[0]);
        		y = Integer.parseInt(tmp_array[1]);
        		
        		// Live bacteria cells are populated with 1
        		solution[x][y] = 1;
        		count++;
        	}
        	
        	createGrid(solution, 0);
        	System.out.println("\nOutput:");
        	iterator(solution, size_x, size_y);
    	}
        
    
    /**
      Printing the grid
    */
    public static void createGrid(int[][] grid, int gen)
    {

    	System.out.println("\n--- Generation: "+ gen);
    	 for (int[] row : grid)
         {
         	System.out.println(Arrays.toString(row));
         }
    }
 
    /**
		Getting the number of neighbors each cell has
    */
    public static void iterator(final int[][] grid, int rows, int cols)
    {
    	int rowCount = grid.length;
        int colCount = grid[0].length;
    	int[][] nextGenerationGrid = new int[rowCount][colCount];

        for (int y = 0; y < rowCount; y++) {

            for (int x = 0; x < colCount; x++) {

                if (lessThanTwoNeighbours(grid, y, x)) {
                	// Under populated
                    nextGenerationGrid[y][x] = DEAD;
                } else if (TwoOrThreeNeighbours(grid, y, x)) {
                	// Stable
                    nextGenerationGrid[y][x] = ALIVE;
                } else if (MoreThanThreeNeighbours(grid, y, x)) {
                	// Over Populated
                    nextGenerationGrid[y][x] = DEAD;
                } else if (DeadAndThreeNeighbours(grid, y, x)) {
                	// Reproduction
                    nextGenerationGrid[y][x] = ALIVE;
                } else {
                    nextGenerationGrid[y][x] = grid[y][x];
                }
                
                //  Printing the output for the next generation
                if(nextGenerationGrid[y][x] == ALIVE) {
                	System.out.println(x + "," + y);
                }
                
            }
        }
        System.out.println("end");
       
        /**
           Generation loop
         */
        while(genNum < 5)
        {
        	genNum ++;
        	createGrid(nextGenerationGrid, genNum);
        	System.out.println("\nOutput:");
            iterator(nextGenerationGrid, rows, cols);
        }
    }
    
    private static boolean DeadAndThreeNeighbours(int[][] grid, int row, int column) {
        int livingNeighbours = countlivingNeighbours(grid, row, column);
        return deadCell(grid, row, column) && livingNeighbours == 3;
    }
    
    private static boolean MoreThanThreeNeighbours(int[][] grid, int row, int column) {
        int livingNeighbours = countlivingNeighbours(grid, row, column);
        return cellAlive(grid, row, column) && livingNeighbours > 3;
    }

    private static boolean TwoOrThreeNeighbours(int[][] grid, int row, int column) {
        int livingNeighbours = countlivingNeighbours(grid, row, column);
        return cellAlive(grid, row, column) && (livingNeighbours == 2 || livingNeighbours == 3);
    }

    private static boolean lessThanTwoNeighbours(int[][] grid, int row, int column) {
        int neighboursCount = countlivingNeighbours(grid, row, column);
        return cellAlive(grid, row, column) && neighboursCount < 2;
    }
    
    public static int countlivingNeighbours(int[][] grid, int row, int col) {
    	// Used to iterate around each cell
        int[][] cellSurroundings = {
                {row - 1, col - 1},
                {row - 1, col},
                {row - 1, col + 1},
                {row, col + 1},
                {row + 1, col + 1},
                {row + 1, col},
                {row + 1, col - 1},
                {row, col - 1},
        };

        int neighboursAlive = 0;

        for (int i = 0; i < cellSurroundings.length; i++) {

            int rowCheck = cellSurroundings[i][0];
            int colcheck = cellSurroundings[i][1];
            if (isInGrid(grid, rowCheck, colcheck) && cellAlive(grid, rowCheck, colcheck)) {
            	neighboursAlive ++ ;
            }
        }

        return neighboursAlive;
    }
    
    // Simple check for Live cell
    public static boolean cellAlive(int[][] grid, int row, int column) {
        return grid[row][column] == ALIVE;
    }

    // Simple check for Dead cell
    public static boolean deadCell(int[][] grid, int row, int column) {
        return grid[row][column] == DEAD;
    }
    
    // Prevent out of bounds
    private static boolean isInGrid(int[][] grid, int row, int col) {
    	int rowCount = grid.length;
        int colCount = grid[0].length;
        return row >= 0 && col >= 0 && row < rowCount && col < colCount;
    }
    
}
