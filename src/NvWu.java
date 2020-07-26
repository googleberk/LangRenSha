import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NvWu extends Player{

    public int IDclass = 2;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
//    public int dead = 0; // 死亡为1， 活着为0

    public NvWu(int i){
        place = i;
    }

    public String toString() {
        return "我是女巫";
    }

    /** 返回谁被毒死了 */
    public int actionNight(int langDaoDeRen, ArrayList<Player> playerList){
        // 女巫第一天(随机)[或者根据用户的输入]考虑救不救这个被刀的人
        int duSi = 9; // 如果duSi = 9, 说明没有毒人。
        Scanner myObj = new Scanner(System.in);  // create a Scanner object

        System.out.println("你想要救" + langDaoDeRen + "号吗?");
        System.out.println("请输入[yes] or [no] 来完成上述决定");
        String jiu = myObj.nextLine();
        if(jiu.equals("yes")){
            //救下这个已经在第一天被狼刀过的人
            playerList.get(langDaoDeRen).dead = 0;
            System.out.println("你已经成功救下" + langDaoDeRen + "号.");
        }else{
            // 女巫没有选择救这个人
            // 女巫第一天可以选择考虑毒人
            System.out.println("你想要使用毒药吗?");
            System.out.println("请输入[yes] or [no] 来完成上述决定");
            String du = myObj.nextLine();
            if(du.equals("yes")){
                // 选择毒人
                System.out.println("你想手动毒人吗?");
                System.out.println("请输入[yes] or [no] 来完成上述决定");
                String du2 = myObj.nextLine();
                if (du2.equals("yes")){
                    // 选择手动毒人
                    System.out.println("你想要毒几号?");
                    System.out.println("请输入[0, 9)除" + langDaoDeRen + "号之外的一个整数来完成上述决定");
                    int du3 = myObj.nextInt();
                    // 就毒[du3]号这个人
                    playerList.get(du3).dead = 1;
                    System.out.println("你[女巫]今晚成功毒了" + du3 + "号.");
                    duSi = du3;
                }else{
                    // 系统将自动选择一个【除上面[没有救的人]之外】毒
                    Random rand = new Random();
                    while(true){
                        int du4 = rand.nextInt(9);
                        if(du4 != langDaoDeRen){
                            // 成功找出那个要毒的人(也可以毒自己)
                            playerList.get(du4).dead = 1;
                            System.out.println("你[女巫]今晚成功毒了" + du4 + "号.");
                            duSi = du4;
                            break;
                        }
                    }
                }
            }else{
                // 不选择毒人
                System.out.println("你[女巫]今晚没有选择救人也没有选择毒人.");
            }
        }
        return duSi;
    }

    public void actionDay(){



    }


}
