package plugin;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

public class Commands implements CommandExecutor {

    private final Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        int[][] maze = {
                {1, 1, 1, 1, 1},
                {0, 0, 1, 0, 1},
                {1, 1, 1, 0, 1},
                {0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1},
        };

        Location start = ((Player) sender).getLocation();
        start.setX(((Player) sender).getLocation().getX());
        start.setY(((Player) sender).getLocation().getY() - 1);
        start.setZ(((Player) sender).getLocation().getZ());

        //Location forcycle = start;

        for (int row = 0; row < 5; row++)
            for (int column = 0; column < 5; column++)
            {
                start.setX(start.getX() + row);
                start.setZ(start.getZ() + column);

                if (maze[row][column] == 0) start.getBlock().setType(Material.COAL_BLOCK);
                else start.getBlock().setType(Material.WOOL);

                start.setX(((Player) sender).getLocation().getX());
                start.setZ(((Player) sender).getLocation().getZ());
            }

        return true;
    }

    private int[][] generateMaze(int size) {

        int[][] maze = new int[size][size];
        int cellsCount = 0;

        // Генерация пустого лабиринта с клетками
        for(int row = 0; row < size; row++)
            for(int column = 0; column < size; column++)
            {
                if(row % 2 == 0) {
                    if (column % 2 == 0) {
                        maze[row][column] = 0;
                        cellsCount++;
                    }
                    else maze[row][column] = 0;
                }
                else
                    maze[row][column] = 0;
            }

        breakTheWall(maze, cellsCount, size);

        return maze;
    }

    private int[][] breakTheWall(int[/*row*/][/*column*/] emptyMaze, int cellsCount, int size) {

        Random random = new Random();
        int nowPos = 0, fullCellsCount = 1;

        mazePosition[] position = null;
        position[0] = new mazePosition(0,0);

        while(nowPos < cellsCount){
            if(hasFreeCell(emptyMaze, position[nowPos])){
                switch(random.nextInt(4)){
                    case 1: { // x-
                        emptyMaze[position[nowPos].x - 1][position[nowPos].y] = 1;
                        emptyMaze[position[nowPos].x - 2][position[nowPos].y] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x - 2, position[nowPos - 1].y);
                    }
                    case 2: { // x+
                        emptyMaze[position[nowPos].x + 1][position[nowPos].y] = 1;
                        emptyMaze[position[nowPos].x + 2][position[nowPos].y] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x + 2, position[nowPos - 1].y);
                    }
                    case 3: { // y-
                        emptyMaze[position[nowPos].x][position[nowPos].y - 1] = 1;
                        emptyMaze[position[nowPos].x][position[nowPos].y - 2] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x, position[nowPos - 1].y - 2);
                    }
                    case 4: { // y+
                        emptyMaze[position[nowPos].x][position[nowPos].y + 1] = 1;
                        emptyMaze[position[nowPos].x][position[nowPos].y + 2] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x, position[nowPos - 1].y + 2);
                    }
                }
            }
            else{
                nowPos--;
            }
        }

        return emptyMaze;
    }

    public boolean hasFreeCell(int[][] maze, mazePosition position) {

        if(position.x - 2 >= 0) if(maze[position.x - 2][position.y] == 0) return true;
        if(position.x + 2 <= maze.length) if(maze[position.x + 2][position.y] == 0) return true;
        if(position.y - 2 >= 0) if(maze[position.x][position.y - 2] == 0) return true;
        if(position.y + 2 <= maze.length) if(maze[position.x][position.y + 2] == 0) return true;

        return false;
    }

    private class mazePosition{

        public int x;
        public int y;

        public mazePosition (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
