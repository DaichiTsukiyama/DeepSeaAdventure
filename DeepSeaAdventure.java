import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class DeepSeaAdventure{
   final static String CLR_COMMAND = "\033\143";
   final static int SUBMARINE = 0;
   final static int LEVEL1_CHIP = 2;
   final static int LEVEL2_CHIP = 3;
   final static int LEVEL3_CHIP = 5;
   final static int LEVEL4_CHIP = 7;
   final static int BRANK_CHIP = 1;
   final static int NONE = -1;
   final static int ALIVE  = 2;
   final static int DEAD   = 3;
   final static int DIVE   = 1;
   final static int RETURN = 0;

   public static int[] field = {SUBMARINE,              //0:������,-1:�ՊO,�e���x���̃`�b�v��8�Â�
                                LEVEL1_CHIP,LEVEL1_CHIP,LEVEL1_CHIP,LEVEL1_CHIP,LEVEL1_CHIP,LEVEL1_CHIP,LEVEL1_CHIP,LEVEL1_CHIP,
                                LEVEL2_CHIP,LEVEL2_CHIP,LEVEL2_CHIP,LEVEL2_CHIP,LEVEL2_CHIP,LEVEL2_CHIP,LEVEL2_CHIP,LEVEL2_CHIP,
                                LEVEL3_CHIP,LEVEL3_CHIP,LEVEL3_CHIP,LEVEL3_CHIP,LEVEL3_CHIP,LEVEL3_CHIP,LEVEL3_CHIP,LEVEL3_CHIP,
                                LEVEL4_CHIP,LEVEL4_CHIP,LEVEL4_CHIP,LEVEL4_CHIP,LEVEL4_CHIP,LEVEL4_CHIP,LEVEL4_CHIP,LEVEL4_CHIP,
                                NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE}; 
   public static int[][] pointChip = {{0,0,1,1,2,2,3,3},
                                      {4,4,5,5,6,6,7,7},
                                      {8,8,9,9,10,10,11,11},
                                      {12,12,13,13,14,14,15,15}};
   public static Player[] PL = new Player[6];
   public static int[] playerDepth = {0,0,0,0,0,0}; //PL�̌��ݒn
   public static int oxyRest = 25;
   public static int deepestPosition = 32; //�`�b�v������Ō��(field[deepestPosition+1]�͕K��NONE)

   public static void main(String args[]){
      int numberOfPL;
      int lastTurnPlayer = 0;
      int diceA;
      int diceB;
      int diceTotal;

      System.out.print(CLR_COMMAND);
      System.out.println("Start");
      numberOfPL = decideNumberOfPlayer();
      preparePL(numberOfPL);
      shufflePoint();
      System.out.print(CLR_COMMAND);

      for(int round = 1; round <= 3; round++){
         setField(numberOfPL, round);
         setPL(numberOfPL);
         System.out.println(round + " Round");
         for(int turnPlayer = lastTurnPlayer, returned = 0; oxyRest > 0 && returned != numberOfPL; turnPlayer++,turnPlayer %= numberOfPL){
            System.out.println("PL" + (turnPlayer+1) + "�̃^�[��  ");
            switch(PL[turnPlayer].getState()){
               case DIVE:
               case RETURN:
                  oxyDecrease(turnPlayer);
                  drawField(numberOfPL);
                  if(PL[turnPlayer].getDepth() != SUBMARINE){
                     displayTreasure(turnPlayer);
                     declareDiveOrReturn(turnPlayer);
                  }
                  diceA = diceRoll();
                  diceB = diceRoll();
                  diceTotal = diceA + diceB;
                  System.out.println("�T�C�R��: " + diceA  + " + " + diceB + " = " + diceTotal);
                  if(diceTotal > PL[turnPlayer].getTreasureNum()){
                     diceTotal -= PL[turnPlayer].getTreasureNum();
                     for(int i = 0; i < numberOfPL; i++){
                        playerDepth[i] = PL[i].getDepth();
                     }
                     PL[turnPlayer].diveSeaOrReturnSubmarine(diceTotal, playerDepth, numberOfPL, deepestPosition);
                     drawField(numberOfPL);
                     switch(field[PL[turnPlayer].getDepth()]){
                        case SUBMARINE:
                           PL[turnPlayer].addState(ALIVE);
                           returned++;
                           System.out.println("PL" + (turnPlayer+1) + "�͖��������͂ɋA�҂��܂���");
                           break;
   
                        case NONE:
                           System.out.println("�ՊO�ɏo�Ă��܂��B�����I�ɐ����͂ɋA�҂��܂�");
                           PL[turnPlayer].addDepth(SUBMARINE);
                           PL[turnPlayer].addState(ALIVE);
                           break;
   
                        case BRANK_CHIP:
                           System.out.println("�u�����N�`�b�v�̏�ł�");
                           leaveTreasure(turnPlayer);
                           break;
   
                        default:
                           getTreasure(turnPlayer, PL[turnPlayer].getDepth());
                           displayTreasure(turnPlayer);
                     }
                  }
                  else{
                     System.out.println("PL" + (turnPlayer+1) + "�͕󂪏d���ē����܂���ł���");
                  }
                  break;

               case ALIVE:
                  System.out.println("PL" + (turnPlayer+1) + "�͋A�ҍς݂ł��B");
                  break;
               
               default:
                  System.out.println("error");
            }
            lastTurnPlayer = turnPlayer;
            System.out.println("");
         }
         decideDeadOrAlive(numberOfPL);
         endProcess(numberOfPL);
      }
      resultAnnounce(numberOfPL);
      System.out.println("Finish");
   }

   public static int decideNumberOfPlayer(){
      int numberOfPL = 0;
      int inputNumberOfPL;
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      String str;
      System.out.print("�v���C�l�������߂Ă��������i2~6�l�j");
      while(numberOfPL == 0){
         str = null;
         System.out.println("-> ");
         try{
            str = br.readLine();
            try{
               inputNumberOfPL = Integer.parseInt(str);
            }
            catch(NumberFormatException e){
               inputNumberOfPL = -1;
            }
            switch(inputNumberOfPL){
               case 2:
               case 3:
               case 4:
               case 5:
               case 6:
                  numberOfPL = Integer.parseInt(str);
                  break;
               default:
                  System.out.println("���̓G���[�ł��B������x���͂��Ă�������");
            }
         }
         catch(IOException e){
            e.printStackTrace();
            break;
         }
      }
      return numberOfPL;
   }

   public static void preparePL(int numberOfPL){
      Player player1 = new Player(); 
      Player player2 = new Player(); 
      Player player3 = new Player(); 
      Player player4 = new Player(); 
      Player player5 = new Player(); 
      Player player6 = new Player(); 
      PL[0] = player1;
      PL[1] = player2;
      PL[2] = player3;
      PL[3] = player4;
      PL[4] = player5;
      PL[5] = player6;
   }

   public static void shufflePoint(){ //�|�C���g�̔z����V���b�t��
      ArrayList<Integer> list = new ArrayList<Integer>();
      for(int level = 0; level < pointChip.length; level++){
         list.clear();
         for(int i = 0; i < pointChip[level].length; i++){
            list.add(pointChip[level][i]);
         }
         Collections.shuffle(list);
         for(int i = 0; i < 8; i++){
            pointChip[level][i] = list.get(i);
         }
      }
   }

   public static void setField(int numberOfPL, int round){ //�Ֆʂ���ׂ�
      if(round != 1){ //round1�͏�����Ԃŕ��ׂȂ����K�v���Ȃ�
         for(int position = 1; position <= deepestPosition; position++){ //�u�����N�`�b�v�𐮗����Ֆʂ��X�V����
            while(field[position] == BRANK_CHIP){
               for(int i = position; i <= deepestPosition; i++){
                  field[i] = field[i+1];
               }
               deepestPosition--;
            }
         }
         System.out.println(deepestPosition);
         oxyRest = 25; //�_�f��������Ԃɂ���
      }
   }

   public static void setPL(int numberOfPL){
      for(int num = 0; num < numberOfPL; num++){
         PL[num].startRound();
      }
   }

   public static void drawField(int numberOfPL){
      StringBuilder str = new StringBuilder("");
      System.out.print("SUBMARINE�y ");
      for(int PLnum = 0; PLnum < numberOfPL; PLnum++){
         if(PL[PLnum].getDepth() == SUBMARINE){
            System.out.print((PLnum+1) + " ");
         }
      }
      System.out.println("�z");

      System.out.print("�y�z");
      for(int position = 1; position <= deepestPosition; position++){
         str.append("+");
      }
      for(int PLnum = 0; PLnum < numberOfPL; PLnum++){
         if(PL[PLnum].getDepth() != 0){
            str.deleteCharAt(PL[PLnum].getDepth()-1);
            str.insert(PL[PLnum].getDepth()-1, PLnum+1);
         }
      }
      System.out.println(str.toString());

      System.out.print("�y�z");
      for(int position = 1; position <= deepestPosition; position++){
         switch(field[position]){
            case LEVEL1_CHIP: System.out.print("1"); break;
            case LEVEL2_CHIP: System.out.print("2"); break;
            case LEVEL3_CHIP: System.out.print("3"); break;
            case LEVEL4_CHIP: System.out.print("4"); break;
            case BRANK_CHIP:  System.out.print("0"); break;
            default:
               int level = field[position];
               String overlapChip = "";
               while(level % LEVEL1_CHIP == 0){
                  level /= LEVEL1_CHIP;
                  overlapChip = overlapChip + "1�~";
               }
               while(level % LEVEL2_CHIP == 0){
                  level /= LEVEL2_CHIP;
                  overlapChip = overlapChip + "2�~";
               }
               while(level % LEVEL3_CHIP == 0){
                  level /= LEVEL3_CHIP;
                  overlapChip = overlapChip + "3�~";
               }
               while(level % LEVEL4_CHIP == 0){
                  level /= LEVEL4_CHIP;
                  overlapChip = overlapChip + "4�~";
               }
               System.out.print("(" + overlapChip.substring(0, overlapChip.length()-1) + ")");
         }
      }
      System.out.println("");
   }

   public static void oxyDecrease(int turnPlayer){ //�_�f�����炷����
      oxyRest -= PL[turnPlayer].getTreasureNum();
      if(oxyRest > 0){
         System.out.println("�c��_�f:" + oxyRest);
      }
      else{
         System.out.println("�c��_�f:0");
      }
   }

   public static void declareDiveOrReturn(int turnPlayer){
      int inputState;
      if(PL[turnPlayer].getState() == DIVE){
         if(PL[turnPlayer].getDepth() == deepestPosition){
            PL[turnPlayer].addState(RETURN);
         }
         else{
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String str;
            System.out.println("�i�ނ��߂邩��錾���Ă��������i�i�ށF1, �߂�F0�j");
            while(true){
               str = null;
               System.out.print("-> ");
               try{
                  str = br.readLine();
                  try{
                     inputState = Integer.parseInt(str);
                  }
                  catch(NumberFormatException e){
                     inputState = -1;
                  }
                  if(inputState == RETURN){
                     PL[turnPlayer].addState(RETURN);
                     break;
                  }
                  else if(inputState == DIVE){
                     break;
                  }
                  else{
                     System.out.println("���̓G���[�ł��B������x���͂��Ă�������");
                  }
               }
               catch(IOException e){
                  e.printStackTrace();
                  break;
               }

            }
         }
      }
   }

   public static int diceRoll(){
      Random rnd = new Random();
      return rnd.nextInt(3) + 1;
   }

   public static void leaveTreasure(int turnPlayer){
      int selectNum;
      if(PL[turnPlayer].getTreasureNum() > 0){
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader br = new BufferedReader(isr);
         String str;
         displayTreasure(turnPlayer);
         System.out.println("�������Ă�����u���Ă����܂���?�i�͂��F1, �������F0�j");
         while(true){
            str = null;
            System.out.print("-> ");
            try{
               str = br.readLine();
               try{
                  selectNum = Integer.parseInt(str);
               }
               catch(NumberFormatException e){
                  selectNum = -1;
               }
               if(selectNum == 1){
                  displayTreasure(turnPlayer);
                  selectLeaveTreasure(turnPlayer);
                  break;
               }
               else if(selectNum == 0){
                  break;
               }
               else{
                  System.out.println("���̓G���[�ł��B������x���͂��Ă�������");
               }
            }
            catch(IOException e){
               e.printStackTrace();
               break;
            }
         }
      }
   }

   public static void displayTreasure(int PLnum){
      ArrayList<Integer> treasure = PL[PLnum].getTreasurList();
      String str;

      if(PL[PLnum].getTreasureNum() > 0){
         System.out.println("���ݏ������Ă����͈ȉ��̒ʂ�ł��B");
         for(int treasureNum = 0, level; treasureNum < treasure.size(); treasureNum++){
            level = treasure.get(treasureNum);
            if(level == LEVEL1_CHIP){
               System.out.print("Level1,");
            }
            else if(level == LEVEL2_CHIP){
               System.out.print("Level2,");
            }
            else if(level == LEVEL3_CHIP){
               System.out.print("Level3,");
            }
            else if(level == LEVEL4_CHIP){
               System.out.print("Level4,");
            }
            else{
               str = "";
               while(level % LEVEL1_CHIP == 0){
                  level /= LEVEL1_CHIP;
                  str = str + "1�~";
               }
               while(level % LEVEL2_CHIP == 0){
                  level /= LEVEL2_CHIP;
                  str = str + "2�~";
               }
               while(level % LEVEL3_CHIP == 0){
                  level /= LEVEL3_CHIP;
                  str = str + "3�~";
               }
               while(level % LEVEL4_CHIP == 0){
                  level /= LEVEL4_CHIP;
                  str = str + "4�~";
               }
               System.out.print("Level(" + str.substring(0, str.length()-1) + "),");
            }
         }
         System.out.println("");
      }
   }

   public static void selectLeaveTreasure(int PLnum){
      ArrayList<Integer> treasure = PL[PLnum].getTreasurList();
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      String str;
      int selectNum;

      System.out.println("�u���Ă���������߂Ă��������B");
      for(int treasureNum = 0, level; treasureNum < treasure.size(); treasureNum++){
         level = treasure.get(treasureNum);
         if(level == LEVEL1_CHIP){
            System.out.println("Level1 " + treasureNum + ", ");
         }
         else if(level == LEVEL2_CHIP){
            System.out.println("Level2 " + treasureNum + ", ");
         }
         else if(level == LEVEL3_CHIP){
            System.out.println("Level3 " + treasureNum + ", ");
         }
         else if(level == LEVEL4_CHIP){
            System.out.println("Level4 " + treasureNum + ", ");
         }
         else{
            str = "";
            while(level % LEVEL1_CHIP == 0){
               level /= LEVEL1_CHIP;
               str = str + "1�~";
            }
            while(level % LEVEL2_CHIP == 0){
               level /= LEVEL2_CHIP;
               str = str + "2�~";
            }
            while(level % LEVEL3_CHIP == 0){
               level /= LEVEL3_CHIP;
               str = str + "3�~";
            }
            while(level % LEVEL4_CHIP == 0){
               level /= LEVEL4_CHIP;
               str = str + "4�~";
            }
            System.out.println("Level(" + str.substring(0, str.length()-1) + ") " + treasureNum + ", ");
         }
      }
      System.out.println("��߂� " + treasure.size() + ",");

      while(true){
         str = null;
         System.out.print("-> ");
         try{
            str = br.readLine();
            try{
               selectNum = Integer.parseInt(str);
            }
            catch(NumberFormatException e){
               selectNum = -1;
            }
            if(selectNum == treasure.size()){
               break;
            }
            else if(0 <= selectNum && selectNum < treasure.size()){
               field[PL[PLnum].getDepth()] = treasure.get(selectNum);
               treasure.remove(selectNum);
               PL[PLnum].addTreasureList(treasure);
               displayTreasure(PLnum);
               break;
            }
            else{
               System.out.println("���̓G���[�ł��B������x���͂��Ă�������");
            }
         }
         catch(IOException e){
            e.printStackTrace();
            break;
         }
      }
   }

   public static void getTreasure(int turnPlayer, int depth){
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      String str;
      int selectNum;

      if(PL[turnPlayer].getTreasureNum() != 6){
         switch(field[depth]){
            case LEVEL1_CHIP: System.out.println("LEVEL1�̕�������܂���"); break;
            case LEVEL2_CHIP: System.out.println("LEVEL2�̕�������܂���"); break;
            case LEVEL3_CHIP: System.out.println("LEVEL3�̕�������܂���"); break;
            case LEVEL4_CHIP: System.out.println("LEVEL4�̕�������܂���"); break;
            default:
               int level = field[depth];
               str = "";
               while(level % LEVEL1_CHIP == 0){
                  level /= LEVEL1_CHIP;
                  str = str + "1�~";
               }
               while(level % LEVEL2_CHIP == 0){
                  level /= LEVEL2_CHIP;
                  str = str + "2�~";
               }
               while(level % LEVEL3_CHIP == 0){
                  level /= LEVEL3_CHIP;
                  str = str + "3�~";
               }
               while(level % LEVEL4_CHIP == 0){
                  level /= LEVEL4_CHIP;
                  str = str + "4�~";
               }
               System.out.println("Level(" + str.substring(0, str.length()-1) + ")�̕�������܂���");
         }
         System.out.println("�l�����܂���?�iyes 1, no 0�j");
         while(true){
            str = null;
            System.out.print("-> ");
            try{
               str = br.readLine();
               try{
                  selectNum = Integer.parseInt(str);
               }
               catch(NumberFormatException e){
                  selectNum = -1;
               }
               if(selectNum == 1){
                  PL[turnPlayer].addTreasure(field[depth]);
                  field[depth] = BRANK_CHIP;
                  break;
               }
               else if(selectNum == 0){
                  break;
               }
               else{
                  System.out.println("���̓G���[�ł��B������x���͂��Ă�������");
               }
            }
            catch(IOException e){
               e.printStackTrace();
               break;
            }
         }
      }
   }

   public static void decideDeadOrAlive(int numberOfPL){
      for(int PLnum = 0; PLnum < numberOfPL; PLnum++){
         if(PL[PLnum].getDepth() != SUBMARINE){
            PL[PLnum].addState(DEAD);
         }
      }
   }

   public static void endProcess(int numberOfPL){ //ALIVE:�|�C���g�����Z����, DEAD:��𒾂߂�
      ArrayList<Integer> sinkTreasureList = new ArrayList<Integer>();
      for(int PLnum = 0; PLnum < numberOfPL; PLnum++){
         switch(PL[PLnum].getState()){
            case ALIVE:
               String str = "(";
               int roundTotal = 0;
               ArrayList<Integer> levelList = new ArrayList<Integer>();
               for(int treasureNum = 0, level = 0; treasureNum < PL[PLnum].getTreasureNum(); treasureNum++){
                  level = PL[PLnum].getTreasureLevel(treasureNum);
                  while(level != 1){
                     if(level % LEVEL1_CHIP == 0){
                         level /= LEVEL1_CHIP;
                         levelList.add(LEVEL1_CHIP);
                     }
                     if(level % LEVEL2_CHIP == 0){
                         level /= LEVEL2_CHIP;
                         levelList.add(LEVEL2_CHIP);
                     }
                     if(level % LEVEL3_CHIP == 0){
                         level /= LEVEL3_CHIP;
                         levelList.add(LEVEL3_CHIP);
                     }
                     if(level % LEVEL4_CHIP == 0){
                         level /= LEVEL4_CHIP;
                         levelList.add(LEVEL4_CHIP);
                     }
                  }
               }
               for(int i = 0, pointChipListLevel = 0; i < levelList.size(); i++){
                  switch(levelList.get(i)){
                     case LEVEL1_CHIP: pointChipListLevel  = 0; break;
                     case LEVEL2_CHIP: pointChipListLevel  = 1; break;
                     case LEVEL3_CHIP: pointChipListLevel  = 2; break;
                     case LEVEL4_CHIP: pointChipListLevel  = 3; break;
                     default: System.out.println("pointChipListLevel error");
                  }
                  for(int j = 0; ;j++){
                     if(pointChip[pointChipListLevel][j] != NONE){
                        PL[PLnum].addPoint(pointChip[pointChipListLevel][j]);
                        roundTotal += pointChip[pointChipListLevel][j];
                        str = str + pointChip[pointChipListLevel][j] + ",";
                        pointChip[pointChipListLevel][j] = NONE;
                        break;
                     }
                  }
               }
               str = str + ")";
               System.out.print("PL"+ (PLnum+1) +"�͐��҂��܂����B���̃��E���h�̊l��P��" + roundTotal);
               System.out.println(str + ", ���݂̃g�[�^��P��" + PL[PLnum].getPoint() + "�ł�");
               break;

            case DEAD: //���ޕ���W�߂�
               System.out.println("PL"+ (PLnum+1) +"�͐��҂ł��܂���ł����B�l��������͐[�C�ɒ��݂܂�");
               for(int treasureNum = 0; treasureNum < PL[PLnum].getTreasureNum(); treasureNum++){
                  sinkTreasureList.add(PL[PLnum].getTreasureLevel(treasureNum));
               }
               break;

            default:
               System.out.println("endProcess error");
         }
      }
      if(sinkTreasureList.size() != 0){
         while(sinkTreasureList.size() % 3 != 0){ //sinkTreasureList.size()��3�Ŋ���؂��悤��������
            sinkTreasureList.add(1);
         }
         for(int i = sinkTreasureList.size()-1; i >= 0; i = i - 3){
            deepestPosition++;
            field[deepestPosition] = sinkTreasureList.get(i) * sinkTreasureList.get(i-1) * sinkTreasureList.get(i-2);
         }
      }
      System.out.println("");
   }

   public static void resultAnnounce(int numberOfPL){
      for(int PLnum = 0; PLnum < numberOfPL; PLnum++){
         System.out.print("PL" + (PLnum+1) + "�̓��_:" + PL[PLnum].getPoint() + ", ");
      }
      System.out.println("");
   }
}