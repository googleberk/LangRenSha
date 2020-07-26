import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Game {

    int size = 9;

    public int yuYanJiaPlace;
    public int lieRenPlace;
    public int nvWuPlace;
    public ArrayList<Integer> cunMinPlace;
    public ArrayList<Integer> langRenPlace;

    public ArrayList<Player> playerList;
    public ArrayList<Integer> chaYanList;
    public ArrayList<Integer> IDlist;

    public int night = 1; // 记录夜晚
    public int day = 1; // 记录白天

    public ArrayList<ArrayList<Integer>> deadList;
    public ArrayList<ArrayList<Integer>> allKnown;  // 存着白天根据发言得出的公共信息

    public void initGame(){

        IDlist = new ArrayList<>(size);
        IDlist.add(0, 0);
        IDlist.add(1, 0);
        IDlist.add(2, 0);
        IDlist.add(3, 1);
        IDlist.add(4, 1);
        IDlist.add(5, 1);
        IDlist.add(6, 2);
        IDlist.add(7, 3);
        IDlist.add(8, 4);

        // shuffle the above list
        Collections.shuffle(IDlist);
        System.out.println(IDlist.toString());

        // 找出本轮中刚开始时候所有人的位置
        for(int i = 0; i<9; i++){
            if(IDlist.get(i) == 0){
                cunMinPlace.add(i);
            } else if (IDlist.get(i) == 1){
                langRenPlace.add(i);
            } else if (IDlist.get(i) == 2){
                nvWuPlace = i;
            } else if(IDlist.get(i) == 3){
                lieRenPlace = i;
            } else if(IDlist.get(i) == 4){
                yuYanJiaPlace = i;
            } else{
                System.out.println("Something Wrong!");
            }
        }


        // store player in a list
        playerList = new ArrayList<>(size);
        for(int i = 0; i < 9; i++){
            playerList.add(new Player());
        }

        //instantiate each player
        RandomAssignment ra = new RandomAssignment();
        for(int i = 0; i < 9; i ++){
            ra.assign(i, playerList.get(i), IDlist.get(i));
        }

        // test
        for(int i = 0; i < 9; i++){
            System.out.println(playerList.get(i).toStringA(IDlist.get(i)));
        }


        // create 预言家查验 list
        chaYanList = new ArrayList<>(size); // 0 为未查验， 1为已经查验
        for(int i = 0; i<9; i++) {
            chaYanList.add(i, 0);
        }
        // 身份定下来之后，自己就不用查验自己
        chaYanList.add(yuYanJiaPlace, 1);

    }




    public void startGame(){
        int duSi;

        while(true){
            if(night == 1){
                // 第一天全部闭眼
                int langDaoDeRen;

                // 先是预言家开始查验
                System.out.println("预言家第" + night + "晚开始验人了.");
                ((YuYanJia) playerList.get(yuYanJiaPlace)).actionNight(chaYanList, IDlist);
                // 然后是狼人刀人
                int selfIND = 0; // 没有选择哪个狼人-->0, 选择了哪一个狼人-->1

                System.out.println("狼人第" + night + "晚开始刀人了.");

                Scanner scan1 = new Scanner(System.in);  // create a Scanner object
                System.out.println("你想要选择某一个狼人来执行刀人的指令吗？");
                System.out.println("请输入[yes] or [no] 来完成上述决定");
                String daoRen1 = scan1.nextLine();
                if(daoRen1.equals("yes")){
                    System.out.println("你想要选择哪一个狼人来执行刀人的指令吗? ");
                    System.out.println("请从这三个整数中选择一个: " + langRenPlace.get(0) + " " + langRenPlace.get(1) + " "
                            + langRenPlace.get(2));
                    System.out.println("请输入以上三个整数中任何一个来作为你想扮演的狼人: ");
                    int daoRen2 = scan1.nextInt();
                    System.out.println("你已成功选择" + daoRen2 + "号狼人作为视角.");
                    selfIND = 1;
                    langDaoDeRen = ((LangRen) playerList.get(daoRen2)).actionNight(langRenPlace, playerList, night, selfIND);
                }else{
                    // 系统将自动随机一个狼人来执行刀人指令
                    Random rand = new Random();
                    int randLang = rand.nextInt(3);
                    langDaoDeRen = ((LangRen) playerList.get(langRenPlace.get(randLang))).actionNight(langRenPlace,
                            playerList, night, selfIND);
                    System.out.println("系统已经成功选择" + langRenPlace.get(randLang) + "号狼人作为视角.");
                }

                // 第一天的时候[最后]是女巫看到刀人的结果，决定救不救这个人, 第一天可以毒人(包括毒自己)
                System.out.println("女巫第" + night + "晚开始行动了.");
                duSi = ((NvWu) playerList.get(nvWuPlace)).actionNight(langDaoDeRen, playerList);
                // 另外【猎人和村民晚上是没有动作的】
            }
            night += 1; // 到第二天白天 这时候[day = 2, night = 2]
            day += 1;

            // 真预言家第二天必定会跳，狼人永远不会悍跳预言家
            if(day == 2){
                // check who died yesterday
                checkDied();
                System.out.println("昨晚一共死了" + deadList.size() + "人.");
                for(int i = 1; i < deadList.size(); i++){
                    System.out.println(deadList.get(1).get(i) + "号在第" + (night-1) + "晚死亡.");
                }
                // 死掉的人发言
                System.out.println("下面死掉的人开始(放技能)/发声");

            }


        }
    }

    public void checkDied(){
        deadList = new ArrayList<>(10); // 从1开始存，9天default
        int j = 0;
        ArrayList<Integer> temp = new ArrayList<>(2); // 每天(夜里)最多两个人死亡

        for(int i = 0; i < 9; i++){
            if(playerList.get(i).dead == 1){
                temp.add(j, i);
                j += 1;
            }
        }
        deadList.add(1, temp);

    }

    public void deadActionDay(int duSi, ArrayList<ArrayList<Integer>> allKnown,
                              ArrayList<ArrayList<Integer>> deadList, ArrayList<Player> playerList){
        int deadLen = deadList.get(1).size();
        Player dead1;
        Player dead2;
        int dieNum;
        boolean dieAtNight;

        if(deadLen == 1){
            dead1 = playerList.get(deadList.get(1).get(0));
            dieNum = 1;
            if((dead1.ID == 3) && (duSi == dead1.place)){
                // 如果死掉的是猎人 并且晚上被女巫毒死, 则第二天不能发动技能，但是可以说话(可以谎报自己的身份)，
                ((LieRen) playerList.get(dead1.place)).sayDay(allKnown);
            }else if ((dead1.ID == 3) && (duSi != dead1.place)){
                // 如果晚上死掉的是猎人，并且不是被女巫毒死的，则第二天可以发动技能，并且说话
                dieAtNight = true;
                ((LieRen) playerList.get(dead1.place)).actionDay(dieNum, dieAtNight, deadList, playerList);
                ((LieRen) playerList.get(dead1.place)).sayDay(allKnown);
            }


        }else{
            dieNum = 2;
            dead1 = playerList.get(deadList.get(1).get(0));
            dead2 = playerList.get(deadList.get(1).get(1));
        }




    }




}
