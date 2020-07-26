import java.util.ArrayList;
import java.util.Scanner;

public class LieRen extends Player{

    public int IDclass = 3;
//    public int place; // 在本轮游戏中的位置， 0到8中其中一个
//    public int dead = 0; // 死亡为1， 活着为0

    public LieRen(int i){
        place = i;
    }

    public String toString() {
        return "我是猎人";
    }

    public void actionNight(){

    }

    public void actionDay(int dieNum, boolean dieAtNight,
                          ArrayList<ArrayList<Integer>> deadList, ArrayList<Player> playerList){
        // 白天发动技能 [被投票出去之后 或者 晚上死亡]
        Scanner scan = new Scanner(System.in); // create a Scanner object
        System.out.println("你选择手动发动技能吗？");
        System.out.println("请输入 [yes] or [no] 来完成上述决定.");
        String liead1 = scan.nextLine();
        if(liead1.equals("yes")){
            System.out.println("你想发动技能带走谁?");
            if((dieNum == 1) && (dieAtNight)){
                // 昨晚只有一人死（就是自己猎人死了), 可以发动技能带走除自己之外的任何一人.
                while(true){
                    System.out.println("请输入你想带走的人是几号 [0, 9)之间的一个整数");
                    int liead2 = scan.nextInt();
                    if(!checkDeadList(deadList, liead2)){
                        // 猎人发动技能的对象不在已死的人名单中，说明可以发动
                        playerList.get(liead2).dead = 1;
                        // deadList 新增一个人死亡 TODO:
                        break;
                    }
                }

            }
        }

    }

    public boolean checkDeadList(ArrayList<ArrayList<Integer>> deadList, int targetShoot){
        return super.checkDeadList(deadList, targetShoot);
    }

    public void sayDay(ArrayList<ArrayList<Integer>> allKnown){
        // 白天发声，爆出自己身份
        Scanner scan = new Scanner(System.in);  // create a Scanner object
        ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]

        System.out.println("你想要帮助猎人在白天说话吗？");
        System.out.println("请输入[yes] or [no] 来完成上述决定.");
        String lie1 = scan.nextLine();
        if(lie1.equals("yes")){
            // 帮助猎人在白天爆出身份(可以谎报自己不是猎人(暂时只能谎报是村民)，暂时不支持报自己是狼)
            System.out.println("你想要把自己的身份报成？");
            System.out.println("请输入[0]村民 or [3]猎人 来完成上述决定.");
            int lie2 = scan.nextInt();

            temp.add(0, this.place);
            if(lie2 == 0){
                temp.add(1, 0);
            }else{
                temp.add(1, 3);
            }
            allKnown.add(0, temp);

        }else{
            // 系统暂支持只能报自己是猎人
            temp.add(0, this.place);
            temp.add(1, 3);
            allKnown.add(0, temp);
        }
    }
}
