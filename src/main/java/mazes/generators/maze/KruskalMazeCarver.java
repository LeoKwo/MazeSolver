package mazes.generators.maze;

import datastructures.concrete.Graph;
// import datastructures.interfaces.IEdge;
// import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
// import misc.exceptions.NotYetImplementedException;
//
// import java.awt.*;
import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
@SuppressWarnings("unchecked")
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        // throw new NotYetImplementedException();
        Random rand = new Random();
        ISet<Room> rooms = maze.getRooms();
        ISet<Wall> walls = maze.getWalls();
        for (Wall wall : walls) {
            wall.setDistance(rand.nextDouble());
        }
        // Graph newMaze = new Graph(rooms, walls);
        // ISet<Wall> mst = newMaze.findMinimumSpanningTree();
        return new Graph(rooms, walls).findMinimumSpanningTree();

        // Graph Graph(ISet<V> vertices, ISet<E> edges)
    }

    // @Override
    // public ISet<Wall> returnWallsToRemove(Maze maze) {
    //     Random rand = new Random();
    //
    //     ISet<Wall> toRemove = new ChainedHashSet<>();
    //     for (Wall wall : maze.getWalls()) {
    //         if (rand.nextDouble() >= this.probabilityOfKeepingEdge) {
    //             toRemove.add(wall);
    //         }
    //     }
    //     return toRemove;
    // }
}
