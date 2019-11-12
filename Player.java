import java.util.ArrayList;

public class Player {
    private int point;
    private int depth; //�ǂ̂��炢�[�������Ă��邩
    private ArrayList<Integer> treasure = new ArrayList<Integer>(); //��ɓ��ꂽ����
    private int state; //�O�i��0,��ނ�1,���҂�2,���v��3

    public Player(){
        point = 0;
        depth = 0;
        state = 0;
        treasure.clear();
    }

    public void startRound(){ //round�J�n����
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