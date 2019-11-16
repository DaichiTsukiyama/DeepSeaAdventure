import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

public class Player {
    final static int SUBMARINE = 0;
    final static int LEVEL1_CHIP = 2;
    final static int LEVEL2_CHIP = 3;
    final static int LEVEL3_CHIP = 5;
    final static int LEVEL4_CHIP = 7;
    final static int ALIVE  = 2;
    final static int DEAD   = 3;
    final static int DIVE   = 1;
    final static int RETURN = 0;

    private int point;
    private int depth; //�ǂ̂��炢�[�������Ă��邩
    private ArrayList<Integer> treasure = new ArrayList<Integer>(); //��ɓ��ꂽ����
    private int state; //�O�i��0,��ނ�1,���҂�2,���v��3

    public Player(){
        point = 0;
        depth = 0;
        state = DIVE;
        treasure.clear();
    }

    public void startRound(){ //round�J�n����
        depth = SUBMARINE;
        state = DIVE;
        treasure.clear();
    }

    public void diveSeaOrReturnSubmarine(int dice, int[] playerDepth, int PLnum, int deepestPosition){
        int upDown = 1;
        if(state == RETURN){
            upDown = -1;
        }
        while(dice > 0 && depth <= deepestPosition){
            dice--;
            depth += upDown;
            for(int i = 0; i < PLnum; i++){
                if(depth == playerDepth[i]){
                    dice++;
                }
            }
            if(depth == SUBMARINE){
                break;
            }
        }
        if(depth > deepestPosition){
            int setPosition = deepestPosition;
            Arrays.sort(playerDepth);
            for(int i = PLnum-1; i >= 0; i--){
                if(playerDepth[i] == setPosition){
                    setPosition--;
                }
            }
            depth = setPosition;
        }
    }

    public void addTreasure(int pointChipLevel){
        treasure.add(pointChipLevel);
        Collections.sort(treasure);
    }

    public void addTreasureList(ArrayList<Integer> treasure){
        this.treasure = treasure;
    }

    public void addPoint(int point){
        this.point += point;
    }

    public void addState(int state){
        this.state = state;
    }

    public void addDepth(int depth){
        this.depth = depth;
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

    public ArrayList<Integer> getTreasurList(){
        return treasure;
    }
}