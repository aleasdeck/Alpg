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

        int[][] maze = generateMaze(20);

        Location start = ((Player) sender).getLocation();
        start.setX(((Player) sender).getLocation().getX());
        start.setY(((Player) sender).getLocation().getY() - 1);
        start.setZ(((Player) sender).getLocation().getZ());

        for (int row = 0; row < maze.length; row++)
            for (int column = 0; column < maze.length; column++)
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

        // Определяем количество ячеек
        if(size % 2 != 0) cellsCount = (size + 1) / 4;
        else cellsCount = size / 4;

        // Строим лабиринт и рушим стены
        breakTheWall(maze, cellsCount, size);

        return maze;
    }

    private int[][] breakTheWall(int[/*row*/][/*column*/] emptyMaze, int cellsCount, int size) {

        Random random = new Random();
        int nowPos = 0, fullCellsCount = 1;
        emptyMaze[0][0] = 1;

        mazePosition[] position = new mazePosition[cellsCount];
        position[0] = new mazePosition();

        while(fullCellsCount < cellsCount){
            System.out.println(">------<" + fullCellsCount);
            if(hasFreeCell(emptyMaze, position[nowPos])){
                switch(random.nextInt(5)){
                    case 1: { // x-
                        System.out.println(">>> 1 Вниз");
                        if(position[nowPos].x - 1 < 0) break;
                        emptyMaze[position[nowPos].x - 1][position[nowPos].y] = 1;
                        emptyMaze[position[nowPos].x - 2][position[nowPos].y] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x - 2, position[nowPos - 1].y);
                        break;
                    }
                    case 2: { // x+
                        System.out.println(">>> 2 Вверх");
                        if(position[nowPos].x + 1 > size) break;
                        emptyMaze[position[nowPos].x + 1][position[nowPos].y] = 1;
                        emptyMaze[position[nowPos].x + 2][position[nowPos].y] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x + 2, position[nowPos - 1].y);
                        break;
                    }
                    case 3: { // y-
                        System.out.println(">>> 3 Налево");
                        if(position[nowPos].y - 1 < 0) break;
                        emptyMaze[position[nowPos].x][position[nowPos].y - 1] = 1;
                        emptyMaze[position[nowPos].x][position[nowPos].y - 2] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x, position[nowPos - 1].y - 2);
                        break;
                    }
                    case 4: { // y+
                        System.out.println(">>> 4 Направо");
                        if(position[nowPos].y + 1 > size) break;
                        emptyMaze[position[nowPos].x][position[nowPos].y + 1] = 1;
                        emptyMaze[position[nowPos].x][position[nowPos].y + 2] = 1;
                        nowPos++;
                        fullCellsCount++;
                        position[nowPos] = new mazePosition(position[nowPos - 1].x, position[nowPos - 1].y + 2);
                        break;
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

        public mazePosition () {

        }

        public int x;
        public int y;

        public mazePosition (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
