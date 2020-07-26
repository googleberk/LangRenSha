import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LangRen extends Player{

    public int IDclass = 1;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
//    public int dead = 0; // 死亡为1， 活着为0

    public LangRen(int i){
        place = i;
    }

    public String toString() {
        return "我是狼人";
    }

    public int actionNight(ArrayList<Integer> langRenPlace, ArrayList<Player> playerList, int night, int selfIND){
        // 狼人的刀人逻辑: 第一天随机刀人(可以自刀), 第二天往后根据场上死掉的村名和神的相对数量(狼人视角,好人可以撒谎)
        //                                     来决定刀谁(这时候不能自刀了)，

        // decide to 刀一个人
        Random rand = new Random();
        int die;

        while(true){


            if(night == 1){
                if(selfIND == 1){
                    // 选择自己刀人
                    Scanner scan2 = new Scanner(System.in);  // create a Scanner object
                    System.out.println("你想要刀几号？");
                    System.out.println("请输入[0, 9) 之间的一个整数来完成上述决定.");
                    int die2 = scan2.nextInt();
                    playerList.get(die2).dead = 1;
                    System.out.println("你已经成功刀了" + die2 + "号.");
                    die = die2;
                    break;
                }else{
                    // 选择系统随机刀人
                    int die1 = rand.nextInt(9);
                    playerList.get(die1).dead = 1;
                    System.out.println(die1 + "号已经在第" + night + "晚上被刀死了.");
                    die = die1;
                    break;
                }
            }

        }
        return die;

    }

    public void actionDay(){

    }




}
