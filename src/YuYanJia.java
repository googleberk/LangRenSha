import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class YuYanJia extends Player {

    public int IDclass = 4;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
    public ArrayList<ArrayList<Integer>> knownList = new ArrayList<>(); // 已经查验过的人的信息(上帝视角[好人也能查出具体身份])
    public ArrayList<ArrayList<Integer>> knownInfo = new ArrayList<>(); // 已经查验过的人的信息(正常视角[好人只能查出好人])

    public int haiWeiBaoStart = 1; // default 第一天晚上的验人信息还没有公布，所以第二天白天 haiWeiBaoStart 的天数为1.

    public YuYanJia(int i){
        place = i;
    }

    public String toString() {
        return "我是预言家";
    }

    public void actionNight(ArrayList<Integer> chaYanList, ArrayList<Integer> IDList){
        Random rand = new Random();
        Scanner scan3 = new Scanner(System.in);  // create a Scanner object
        int yuFinal;

        System.out.println("你想要手动控制预言家吗？");
        System.out.println("请输入[yes] or [no] 来完成上述决定.");
        String yu1 = scan3.nextLine();
        if (yu1.equals("yes")){
            // 手动选择查验人
            System.out.println("你想要查验几号玩家的身份?");
            while(true){
                System.out.println("请输入一个[0, 9) 之间的 并且还未查验过的人作为要查验的对象.");
                int yu2 = scan3.nextInt();
                if(chaYanList.get(yu2) == 0){
                    // 说明这个选择的人还未被查验
                    yuFinal = yu2;
                    break;
                }
            }
        }else{
            // 交由系统随机查验人
            while(true){
                int yu3 = rand.nextInt(9);
                if(chaYanList.get(yu3) == 0){
                    // 说明这个选择的人还未被查验
                    yuFinal = yu3;
                    break;
                }
            }
        }
        chaYanList.add(yuFinal, 1); // 修改身份标记[变为已查验]

        // 成功找到一个没有查验过的, 就查验这个人
        ArrayList<Integer> temp = new ArrayList<>(2); // 形式是 [位置 身份]
        temp.add(0, yuFinal);
        temp.add(1, IDList.get(yuFinal));
        knownList.add(0, temp);

        ArrayList<Integer> temp2 = new ArrayList<>(2); // // 形式是 [位置 身份]
        if(IDList.get(yuFinal) == 1){
            // 如果查验到的是狼人, 则会显示查到的是狼
            temp2.add(0, yuFinal);
            temp2.add(1, 1);
            knownInfo.add(0, temp2);
        } else{
            // 如果查验到的是除狼人以外的身份，则会显示查到的是好人[身份标记为5]
            temp2.add(0, yuFinal);
            temp2.add(1, 5);
            knownInfo.add(0, temp2);
        }

    }

    public void actionDay(){


    }

    public void sayDay(ArrayList<ArrayList<Integer>> allKnown, int day){
        Scanner scan = new Scanner(System.in);

        System.out.println("你想要手动操控预言家发言吗？");
        System.out.println("请输入 [yes] or [no] 来完成以上决定.");
        String cmsd1 = scan.nextLine();
        if(cmsd1.equals("yes")){
            // 手动操控预言家发言, 既 暂时只有一下两种逻辑: ~~~~~
            // 预言家可以不报出任何查验的人的信息，并且说自己是村民或者神
            // 如果预言家说自己的身份是预言家，那么一定要报出前一晚(或者前几晚)的查验的人信息
            System.out.println("你想要把自己的身份说成 [0]村民 或者 [6]神 或者 [4]预言家？");
            int cmsd2 = scan.nextInt();
            if(cmsd2 == 0){
                System.out.println("我[" + this.place + "]号的身份是村民.");
                // 添加自己的身份
                ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
                temp.add(0, this.place);
                temp.add(1, 0);
                allKnown.add(temp);
            }else if(cmsd2 == 6){
                System.out.println("我[" + this.place + "]号的身份是神.");
                // 添加自己的身份
                ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
                temp.add(0, this.place);
                temp.add(1, 6);
                allKnown.add(temp);
            }else{
                System.out.println("我[" + this.place + "]号的身份是预言家.");
                System.out.println("下面我[预言家]开始报验人信息: ");
                for(int i = haiWeiBaoStart; i < day; i ++){
                    System.out.println("第[" + i + "]晚上查验的[" + knownInfo.get(i).get(0) + "]号身份为" +
                            knownInfo.get(i).get(1));
                    // 添加预言家晚上的查验信息
                    ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
                    temp.add(0, knownInfo.get(i).get(0));
                    temp.add(1, knownInfo.get(i).get(1));
                    allKnown.add(temp);
                }
                System.out.println("查验信息播报完毕");
                // 添加本身预言家的信息
                ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
                temp.add(0, this.place);
                temp.add(1, 4);
                allKnown.add(temp);
                // 改变 haiWeiBaoStart
                haiWeiBaoStart = day;
            }
        }else{
            // 系统自动爆出自己的正确真实身份为预言家 [系统不会撒谎]
            System.out.println("我[" + this.place + "]号的身份是预言家.");
            System.out.println("下面我[预言家]开始报验人信息: ");
            System.out.println("第[" + (day-1) + "]晚上查验的[" + knownInfo.get((day-1)).get(0) + "]号身份为" +
                    knownInfo.get((day-1)).get(1));
            System.out.println("查验信息播报完毕");
            // 添加昨晚上查验到人的信息
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(0, knownInfo.get((day-1)).get(0));
            temp.add(1, knownInfo.get((day-1)).get(1));
            allKnown.add(temp);
            // 添加自己的信息[暴露的预言家]
            ArrayList<Integer> temp1 = new ArrayList<>(2); // 格式为 [位置 身份]
            temp1.add(0, this.place);
            temp1.add(1, 4);
            allKnown.add(temp1);
        }
    }





}
