import java.util.ArrayList;

public class Player {
    private int point;
    private int depth; //どのくらい深く潜っているか
    private ArrayList<Integer> treasure = new ArrayList<Integer>(); //手に入れた財宝
    private int state; //前進で0,後退で1,生還で2,沈没で3

    public Player(){
        point = 0;
        depth = 0;
        state = 0;
        treasure.clear();
    }

    public void startRound(){ //round開始処理
        depth = 0;
        state = 0;
        treasure.clear();
    }

    public void addTreasure(int PointChipLevel){
        treasure.add(PointChipLevel);
    }

    public void addPoint(int point){
        this.point += point;
    }

    public int getTreasureNum(){
        if(treasure.size() == 0)
            return 0;
        else
            return treasure.size();
    }

    public int getTreasureLevel(int treasureNum){
        return treasure.get(treasureNum);
    }

    public int getState(){
        return state;
    }

    public int getDepth(){
        return depth;
    }

    public int getPoint(){
        return point;
    }
}