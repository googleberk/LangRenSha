import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class YuYanJia extends Player {

    public int IDclass = 4;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
    public ArrayList<ArrayList<Integer>> knownList = new ArrayList<>(); // 已经查验过的人的信息(上帝视角[好人也能查出具体身份])
    public ArrayList<ArrayList<Integer>> knownInfo = new ArrayList<>(); // 已经查验过的人的信息(正常视角[好人只能查出好人])

    public YuYanJia(int i){
        place = i;
    }

    public String toString() {
        return "我是预言家";
    }

    public void actionNight(ArrayList<Integer> chaYanList, ArrayList<Integer> IDList){
        // decide to 查验一个人的身份
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





}
