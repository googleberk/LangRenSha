import java.util.ArrayList;
import java.util.Random;
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

    public int actionDay(ArrayList<ArrayList<Integer>> deadList, ArrayList<Player> playerList, int day){
        // 白天发动技能 [被投票出去之后 或者 晚上死亡(并且晚上不是被女巫毒死的)]
        Scanner scan = new Scanner(System.in); // create a Scanner object
        int daizouFinal = 9; // 初始值为9, 后面有两个地方重新赋值

        System.out.println("你选择手动发动技能吗？");
        System.out.println("请输入 [yes] or [no] 来完成上述决定.");
        String liead1 = scan.nextLine();

        if(liead1.equals("yes")){
            System.out.println("你想发动技能带走谁?");

            // 昨晚猎人死了/或者白天被投票出去, 可以发动技能带走除(自己和已经死掉的人之外)[这些人应该在猎人发动技能之前就被存在deadList中了]的任何一人.
            // TODO: 检查这些人应该在猎人发动技能之前就被存在deadList中了
            while(true){
                System.out.println("请输入你想带走的人是几号 [0, 9)之间的一个整数");
                // 这个时候尽量人为的做出判断, 如果再缩小可以带走的人范围，对于AI来说比较麻烦
                int liead2 = scan.nextInt();

                if(!checkDeadList(deadList, liead2)){
                    // 猎人发动技能的对象不在已死的人名单中，说明可以发动技能带走这个人
                    playerList.get(liead2).dead = 1;
                    // deadList 新增一个人死亡 TODO:
                    deadList.get(day).add(liead2);   // 死亡名单按照天数存
                    System.out.println("[" + this.place + "]号猎人已经成功带走[" + liead2 + "]号.");
                    daizouFinal = liead2;
                    break;
                }
            }

        }else{
            // 交由系统随机带走一个人
            Random rand = new Random();
            while(true){
                int liead3 = rand.nextInt(9);
                if(!checkDeadList(deadList, liead3)){
                    // 猎人发动技能的对象不在已死的人名单中，说明可以发动技能带走这个人
                    playerList.get(liead3).dead = 1;
                    // deadList 新增一个人
                    deadList.get(day).add(liead3);
                    daizouFinal = liead3;
                    break;
                }
            }
        }
        return daizouFinal;
    }

    public boolean checkDeadList(ArrayList<ArrayList<Integer>> deadList, int targetShoot){
        return super.checkDeadList(deadList, targetShoot);
    }

    public void sayDay(ArrayList<ArrayList<Integer>> allKnown){
        // 白天发声，报出自己身份
        // 目前仅有以下两种情况可以发言:
        // (1) 第一夜死亡(不管是不是被女巫毒死的，都可以发言)，先发言，再发动技能
        // (2) 除第一夜死亡之外，第二天及以后的白天被投票出局，可以先发言，再发动技能
        // (3) 除第一夜死亡之外，后面从第二天开始都不可以发言，只可以发动技能
        Scanner scan = new Scanner(System.in);  // create a Scanner object
        ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]

        System.out.println("你想要帮助猎人在白天发言吗？");
        System.out.println("请输入[yes] or [no] 来完成上述决定.");
        String lie1 = scan.nextLine();
        if(lie1.equals("yes")){
            // 帮助猎人在白天爆出身份(可以谎报自己不是猎人(暂时只能谎报是村民) 或者说自己是神，暂时不支持报自己是狼)
            System.out.println("你想要把自己的身份报成？");
            System.out.println("请输入[0]村民 or [3]猎人 or [6]神牌 来完成上述决定.");
            int lie2 = scan.nextInt();

            // 记录自己的身份到allKnown
            temp.add(0, this.place);
            if(lie2 == 0){
                System.out.println("我[" + this.place + "]号的身份是村民.");
                temp.add(1, 0);
            }else if(lie2 == 6){
                System.out.println("我[" + this.place + "]号的身份是神.");
                temp.add(1, 6);
            }else{
                System.out.println("我[" + this.place + "]号的身份是猎人.");
                temp.add(1, 3);
            }
            System.out.println("我[" + this.place + "]号的身份播报完毕.");
            allKnown.add(temp);
        }else{
            // 系统暂支持只能报自己是猎人 (系统不能撒谎)
            System.out.println("我[" + this.place + "]号的身份是猎人.");
            System.out.println("我[" + this.place + "]号的身份播报完毕.");
            temp.add(0, this.place);
            temp.add(1, 3);
            allKnown.add(temp);
        }
    }
}
