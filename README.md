# CaveCrawler
The goal of this game is to make a smooth networking experience for players on multiple computers. After I iron out the details of the single player game, I will add the networking component. I would also like to design more levels that require precise movement to win.

To run this project, first enter the CaveCrawler directory and run `mvn package`. Then, run the project by executing `java -jar ./target/cave-crawler-1.0-jar-with-dependencies.jar` in the same directory.

The most interesting algorithm currently in the game can be found in the Board class. Examine the updateVisibility function. This is an attempt at a simple rasterization algorithm to check if a given square is visible from the player's current position. A future project is to make the wall visibility algorithm better (currently, a wall is visible if it is adjacent to a visible square). This can be done by starting from an edge of the wall instead of the center and using a variation of the updateVisibility function.

A close second is the easily reusable implementation of A* found in the graph package.

A nice feature is the ability to create new levels easily. It can be done by following the instructions in the src/main/resources/Information/mapInstructions.txt. Then, add your map to the src/main/resources/Maps folder and to the relevant map list file(s) (simplemaps.txt or complexmaps.txt in src/main/resources/Information). A simple map does not use slugs or zombies. All simple maps should also be on the complex map list (the complex map list is all maps, the simple list should filter out levels with slugs and zombies).

Each of the monsters has customizable statistics (health, attack, defence, and speed) and a unique AI. Currently, the slug uses A* to find the shortest path to you and attack. The zombie attempts to move directly toward you (because they can smell brains, but can't figure out how to path correctly) and may run into a wall. The skeleton walks in a straight line unless it sees you to the left or to the right (in a straight line like a rook), in which case it will move toward you. These can be avoided through careful steps. I plan on factoring out the AI's common code to remove repeated lines.

To try out the game, run the jar found in out/artifacts. It accepts three command line arguments: --complex allows all levels, --hard sets the visibility to be limited, and --text runs the terminal version.
