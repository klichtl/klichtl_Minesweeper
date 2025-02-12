package htl.steyr.klichtl_minesweeper;

public class User {

    private String username;

    private int time;

    private String difficulty;

    public User(String username, int time, String difficulty) {
        setUsername(username);
        setTime(time);
        setDifficulty(difficulty);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}