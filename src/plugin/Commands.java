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

        int[][] maze = generateMaze(Integer.parseInt(args[0]), true);

        Location start = ((Player) sender).getLocation();
        start.setX(((Player) sender).getLocation().getX());
        start.setY(((Player) sender).getLocation().getY() - 1);
        start.setZ(((Player) sender).getLocation().getZ());

        for (int row = 0; row < maze.length; row++)
            for (int column = 0; column < maze.length; column++)
            {
                start.setX(start.getX() + row);
                start.setZ(start.getZ() + column);

                if (maze[row][column] == 0 || maze[row][column] == 3) {
                    start.getBlock().setType(Material.COAL_BLOCK);
                    start.setY(start.getY() + 1);
                    start.getBlock().setType(Material.COAL_BLOCK);
                    start.setY(start.getY() + 1);
                    start.getBlock().setType(Material.COAL_BLOCK);
                    start.setY(start.getY() - 2);
                }
                else {
                    start.getBlock().setType(Material.WOOL);
                    start.setY(start.getY() + 1);
                    start.getBlock().setType(Material.AIR);
                    start.setY(start.getY() + 1);
                    start.getBlock().setType(Material.AIR);
                    start.setY(start.getY() - 2);
                }

                start.setX(((Player) sender).getLocation().getX());
                start.setZ(((Player) sender).getLocation().getZ());
            }

//        for(int i = 0; i < 4; i++){
//            for(int j = 0; j < Integer.parseInt(args[0]); i++) {
//                if(i==0) {
//                    start.setX(start.getX());
//                    start.setZ(start.getZ() + 1);
//                    start.getBlock().setType(Material.COAL_BLOCK);
//                }
//                if(i==0) {
//                    start.setX(start.getX());
//                    start.setZ(start.getZ() - 1);
//                    start.getBlock().setType(Material.COAL_BLOCK);
//                }
//                if(i==0) {
//                    start.setX(start.getX());
//                    start.setZ(start.getZ() + 1);
//                    start.getBlock().setType(Material.COAL_BLOCK);
//                }
//                if(i==0) {
//                    start.setX(0);
//                    start.setZ(j);
//                    start.getBlock().setType(Material.COAL_BLOCK);
//                }
//            }
//        }

        return true;
    }

    private static int[][] generateMaze(int size, boolean multiPath){

        // Гайд по цифрам(cell):
        // 0-стена
        // 1-обычный проход
        // 2-выход из комнаты(антибаг)
        // 3-стены комнаты

        int roomsCount = 3;                                                                                                     // Выставляем количство комнат которое хотим видеть

        Random random = new Random();
        //int maze[][] = new int[size][size];                                                                                   // Пустой квадрат
        MazeWithRoom crDung = createDungeon(size, roomsCount, true);
        int maze[][] = crDung.getMaze();                                                                                        // Получаем пустой лабиринт с комнатами
        int realRoomsCount = crDung.getRoomsCount();                                                                            // Количество комнат которое удалось сгенерировать

        maze[0][0] = 1;                                                                                                         // Задаем стартовую позицию как посещенную
        int keyCellsCount = (size % 2 != 0) ? ((int)Math.pow((size + 1), 2) / 4) : ((int)Math.pow((size), 2) / 4);              // Колличество ключевых ячеек, где будем останавливаться
        int filledKeyCells = 1 + (realRoomsCount * 4);                                                                          // Заполненные ключевые ячейки
        int nowPos = 0;                                                                                                         // Текущая позиция
        Coordinates[] position = new Coordinates[keyCellsCount];                                                                // Массив с пройденными координатами
        position[nowPos] = new Coordinates();
        position[nowPos].setX(0);
        position[nowPos].setY(0);

        while(filledKeyCells < keyCellsCount){
            if(checkCellsAround(maze, position[nowPos])){
                int rndValue = random.nextInt(4) + 1;
                switch(rndValue){
                    case 1: { // x- вверх
                        if(!checkCellGoTo(maze, position[nowPos], rndValue, 2, 1)) break;
                        maze[position[nowPos].getX() - 1][position[nowPos].getY()] = 1;
                        maze[position[nowPos].getX() - 2][position[nowPos].getY()] = 1;
                        nowPos++;
                        position[nowPos] = new Coordinates();
                        position[nowPos].setX(position[nowPos - 1].getX() - 2);
                        position[nowPos].setY(position[nowPos - 1].getY());
                        filledKeyCells++;
                        break;
                    }
                    case 2: { // x+ вниз
                        if(!checkCellGoTo(maze, position[nowPos], rndValue, 2, 1)) break;
                        maze[position[nowPos].getX() + 1][position[nowPos].getY()] = 1;
                        maze[position[nowPos].getX() + 2][position[nowPos].getY()] = 1;
                        nowPos++;
                        position[nowPos] = new Coordinates();
                        position[nowPos].setX(position[nowPos - 1].getX() + 2);
                        position[nowPos].setY(position[nowPos - 1].getY());
                        filledKeyCells++;
                        break;
                    }
                    case 3: { // y- влево
                        if(!checkCellGoTo(maze, position[nowPos], rndValue, 2, 1)) break;
                        maze[position[nowPos].getX()][position[nowPos].getY() - 1] = 1;
                        maze[position[nowPos].getX()][position[nowPos].getY() - 2] = 1;
                        nowPos++;
                        position[nowPos] = new Coordinates();
                        position[nowPos].setX(position[nowPos - 1].getX());
                        position[nowPos].setY(position[nowPos - 1].getY() - 2);
                        filledKeyCells++;
                        break;
                    }
                    case 4: { // y+ вправо
                        if(!checkCellGoTo(maze, position[nowPos], rndValue, 2, 1)) break;
                        maze[position[nowPos].getX()][position[nowPos].getY() + 1] = 1;
                        maze[position[nowPos].getX()][position[nowPos].getY() + 2] = 1;
                        nowPos++;
                        position[nowPos] = new Coordinates();
                        position[nowPos].setX(position[nowPos - 1].getX());
                        position[nowPos].setY(position[nowPos - 1].getY() + 2);
                        filledKeyCells++;
                        break;
                    }
                }
            }
            else{
                nowPos--;
            }
        }

        if(multiPath){                                                      // Ломаем стены для вариативности прохождения

            int breakingWallsCount = (int)Math.pow((size / 5), 2);          // Колличество ломаемых стен

            for(int i = 0; i <  breakingWallsCount; i++) {
                boolean exit = false;
                do {
                    int randomX = random.nextInt(size - 1);
                    int randomY = random.nextInt(size - 1);
                    int way = random.nextInt(4) - 1;
                    if((randomX % 2 == 0 && randomY % 2 != 0) || (randomX % 2 != 0 && randomY % 2 == 0)) {
                        if(checkCellGoTo(maze, new Coordinates(randomX, randomY), way, 1, 1) /*&&
                                !checkCellGoTo(maze, new Coordinates(randomX, randomY), way, 1, 3)*/) {
                            maze[randomX][randomY] = 1;
                            exit = true;
                        }
                    }
                } while(!exit);
            }
        }

        return maze;
    }

    private static MazeWithRoom createDungeon(int size, int roomsCount, boolean isDungeon) { // юзлес метод, выпилить в продакшене если плагин когда-нибудь понадобится(нет)

        int[][] dungeon = new int[size][size];
        int finalRoomsCount = 0;

        if(isDungeon) {
            for (int i = 0; i < roomsCount; i++) {
                MazeWithRoom cR = createRoomInDungeon(dungeon, 3, 3);
                finalRoomsCount += cR.getRoomsCount();
            }
        }

        return new MazeWithRoom(dungeon, finalRoomsCount);
    }

    public static class MazeWithRoom{

        private int[][] maze;
        private int roomsCount;

        public  MazeWithRoom() {

        }

        public MazeWithRoom(int[][] maze, int roomsCount) {
            this.maze = maze;
            this.roomsCount = roomsCount;
        }

        public int[][] getMaze() {
            return maze;
        }

        public void setMaze(int[][] maze) {
            this.maze = maze;
        }

        public int getRoomsCount() {
            return roomsCount;
        }

        public void setRoomsCount(int roomsCount) {
            this.roomsCount = roomsCount;
        }
    }

    private static MazeWithRoom createRoomInDungeon(int[][] dungeon, int width, int height) { // width и height задаются без учета стен

        int maxRandomXValue = (dungeon.length - 2) - width - 2; // Задаем максимальные значения рандома координат
        int maxRandomYValue = (dungeon.length - 2) - height - 2;

        if (maxRandomXValue < 0 || maxRandomYValue < 0) { // Если не подходит по параметрам возвращаем лабиринт без комнаты
            System.out.println("Maze too small");
            return new MazeWithRoom(dungeon, 0);
        }

        Random random = new Random(); // Генерируем координаты
        int randomX, randomY;

        // Проверка на возможность добавления комнаты
        boolean exit;
        int failCounter = 0; // Количество неудачных попыток создания начальной точки для комнаты, до выхода из генерации
        do {
            exit = true;
            randomX = maxRandomXValue > 0 ? random.nextInt(maxRandomXValue) + 2 : 2;
            randomY = maxRandomYValue > 0 ? random.nextInt(maxRandomYValue) + 2 : 2;
            if(randomX % 2 != 0 ) randomX++;
            if(randomY % 2 != 0 ) randomY++;

            for(int i = 0; i < width+2; i++){
                for(int j = 0; j < height+2; j++){
                    if(dungeon[randomX+i][randomY+j] != 0) {
                        failCounter++;
                        exit = false;
                    }
                }
            }

            if(failCounter > 10) {                               // Регулируем количество попыток создания начальной точки
                System.out.println("Not enough place");
                return new MazeWithRoom(dungeon, 0);
            }
        } while(!exit);

        for(int i = 0; i < width; i++) { // Генерируем комнату
            for (int j = 0; j < height; j++) {
                dungeon[randomX + i][randomY + j] = 1;

                if(i == 0) dungeon[randomX + i - 1][randomY + j] = 3;       // Делаем стены для комнаты (опционально, можно выпилить, использовать можно
                if(i == 0) dungeon[randomX + i + width][randomY + j] = 3;   // как для изменения цвета стен, так и для того, чтобы не рандомило разбитие)
                if(j == 0) dungeon[randomX + i][randomY + j - 1] = 3;
                if(j == 0) dungeon[randomX + i][randomY + j + height] = 3;
            }
        }

        dungeon[randomX - 1][randomY - 1] = 3;
        dungeon[randomX + width][randomY + height] = 3;
        dungeon[randomX + width][randomY - 1] = 3;
        dungeon[randomX - 1][randomY + height] = 3;

        int way = random.nextInt(4) + 1; // Делаем вход в комнату
        if(way == 1) { dungeon[randomX + (width/2)][randomY - 1] = 1; dungeon[randomX + (width/2)][randomY - 2] = 2; }
        if(way == 2) { dungeon[randomX + (width/2)][randomY + height] = 1; dungeon[randomX + (width/2)][randomY + height + 1] = 2; }
        if(way == 3) { dungeon[randomX - 1][randomY + (height/2)] = 1; dungeon[randomX - 2][randomY + (height/2)] = 2; }
        if(way == 4) { dungeon[randomX + width][randomY + (height/2)] = 1; dungeon[randomX + width + 1][randomY + (height/2)] = 2; }

        return new MazeWithRoom(dungeon, 1);
    }

    private static boolean checkCellsAround(int[][] maze, Coordinates position) {

        if(position.getX() - 2 >= 0) if(maze[position.getX() - 2][position.getY()] != 1) return true;            // x-
        if(position.getX() + 2 <= maze.length) if(maze[position.getX() + 2][position.getY()] != 1) return true;  // x+
        if(position.getY() - 2 >= 0) if(maze[position.getX()][position.getY() - 2] != 1) return true;            // y-
        if(position.getY() + 2 <= maze.length) if(maze[position.getX()][position.getY() + 2] != 1) return true;  // y+

        return false;
    }

    private static boolean checkCellGoTo(int[][] maze, Coordinates position, int way, int keyOrWall, int cell) {  // way - направление, keyOrWall - 2-ключевая клетка, 1-стена. cell - цифры, какая ячейка

        if(way == 1) if(position.getX() - 2 >= 0) if(maze[position.getX() - keyOrWall][position.getY()] != cell) return true;            // x-
        if(way == 2) if(position.getX() + 2 <= maze.length) if(maze[position.getX() + keyOrWall][position.getY()] != cell) return true;  // x+
        if(way == 3) if(position.getY() - 2 >= 0) if(maze[position.getX()][position.getY() - keyOrWall] != cell) return true;            // y-
        if(way == 4) if(position.getY() + 2 <= maze.length) if(maze[position.getX()][position.getY() + keyOrWall] != cell) return true;  // y+

        return false;
    }

    private static class Coordinates {

        private int x;
        private int y;

        public Coordinates() {

        }

        public Coordinates(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
