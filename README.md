# MazeGenerator

Maze Generator project under the Advanced Programming course

## Technical Description

### GUI

The GUI is implemented with `Swing JPanels`. You can selected the width, height and levels of the maze.

The user can choose two types of algorithms for generating the maze:
- Kruskal's algorithm breaks random walls throughout the maze and looks "uniform".
- Prim's algorithm breaks walls connecting cells starting from one entrance and connecting more nodes adjacent with previous ones. This makes the animation look like it's being generated starting from one side of the maze to the other.

When the "Animated" option is selected (which it is by default), the initial maze (with all walls up) is displayed and then each updated node (in order given by the chosen algorithm) is repainted (only his region, not the whole screen). A `javax.swing.Timer` is used to synchronize the frames.

If the "3D" option is selected and the user has chosen multiple levels, the "UP" and "DOWN" buttons can be used to navigate the maze levels, during the animation and after.

The "Generate" button creates the maze by calling on the `MazeFactory` instance, passing it the selected options.

The "Save" button creates a `JFileChooser` popup where the user can choose where to save their maze. The maze is saved as a PNG.

### Maze and generation

The maze is represented as a graph and the path generating is implemented using Random Spanning Tree algorithms. Kruskal's and Prim's are being used.

On 3D mazes, the up/down staircases (from one level to another) are seen less often than possible lateral movement (on same level). When shuffling the edges of the graph, they are given a weight; staircases have ~5% the weight of lateral movement. That is because if they had the same weight, the maze would fill up with a lot of staircases (which clutters the visual). Upward and downward staircases can be found in the same cell.

## Source of insipration

[Astrolog.org's labirinth types](http://www.astrolog.org/labyrnth/algrithm.htm)


## Authors

- Petru Ciocirlan
