package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static int[] dijkstra(int[][] graph, int source) {
        
        int count = graph.length;
        boolean[] visitedVertex = new boolean[count];
        int[] distance = new int[count];
        
        for (int i = 0; i < count; i++) {
            visitedVertex[i] = false;
            distance[i] = Integer.MAX_VALUE;
        }
    
        // Distance of self loop is zero
        distance[source] = 0;
        for (int i = 0; i < count; i++) {
    
          // Update the distance between neighbouring vertex and source vertex
          int u = findMinDistance(distance, visitedVertex);
          visitedVertex[u] = true;
    
          // Update all the neighbouring vertex distances
          for (int v = 0; v < count; v++) {
            if (!visitedVertex[v] && graph[u][v] != 0 && (distance[u] + graph[u][v] < distance[v])) {
              distance[v] = distance[u] + graph[u][v];
            }
          }
        }
        return(distance);
      }
    
      // Finding the minimum distance
      private static int findMinDistance(int[] distance, boolean[] visitedVertex) {
        int minDistance = Integer.MAX_VALUE;
        int minDistanceVertex = -1;
        for (int i = 0; i < distance.length; i++) {
          if (!visitedVertex[i] && distance[i] < minDistance) {
            minDistance = distance[i];
            minDistanceVertex = i;
          }
        }
        return minDistanceVertex;
      }
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

    	// WRITE YOUR CODE HERE
        String locateTitanInputFile = args[0];
        String locateTitanOutputFile = args[1];

        StdIn.setFile(locateTitanInputFile);
        StdOut.setFile(locateTitanOutputFile);

        int numGen = StdIn.readInt();
        double[] genStab = new double[numGen];

        for (int i = 0; i < numGen; i++) {

            int index = StdIn.readInt();
            genStab[index] = StdIn.readDouble();
        }

        int[][] pathEnergy = new int[numGen][numGen];

        for (int i = 0; i < numGen; i++) {
            for (int j = 0; j < numGen; j++) {

                pathEnergy[i][j] = StdIn.readInt();
            }
        }

        for (int i = 0; i < numGen; i++) {
            for (int j = 0; j < numGen; j++) {

                if (pathEnergy[i][j] > 0) {

                    pathEnergy[i][j] = (int) (pathEnergy[i][j] / (genStab[i]*genStab[j]));
                }
            }
        }

        int[] distance = dijkstra(pathEnergy, 0);

        StdOut.print(distance[distance.length - 1]);
    }
}
