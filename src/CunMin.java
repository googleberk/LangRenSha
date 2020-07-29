import java.util.ArrayList;
import java.util.Scanner;

public class CunMin extends Player{

    public int IDclass = 0;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
//    public int dead = 0; // 死亡为1， 活着为0

    public CunMin(int i){
        place = i;
    }

    public String toString() {
        return "我是村民";
    }

    public void actionNight(){

    }

    public void actionDay(){

    }

    public void sayDay(ArrayList<ArrayList<Integer>> allKnown){
        Scanner scan = new Scanner(System.in);
        System.out.println("你想要手动操控村民发言吗？");
        System.out.println("请输入 [yes] or [no] 来完成以上决定.");
        String cmsd1 = scan.nextLine();
        if(cmsd1.equals("yes")){
            // 手动操控村民发言, 既 村名可以说自己的身份为神[不能悍跳预言家]，暂时不支持说自己的身份为狼
            System.out.println("你想要把自己的身份说成 [0]村民 或者 [6]神？");
            int cmsd2 = scan.nextInt();
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(this.place, cmsd2);
            allKnown.add(temp);
        }else{
            // 系统自动爆出自己的正确真实身份 [系统不会撒谎]
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(this.place, 0);
        }
    }


}
