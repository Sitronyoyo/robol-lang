public class Grid {
    NumberExp width, height;

    public Grid(NumberExp width, NumberExp height) {
        this.width = width;
        this.height = height;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width.num && y >= 0 && y < height.num;
    }
}
