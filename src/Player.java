import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player{

    public int ID; // 村名为0，狼人为1，女巫为2，猎人为3，预言家为4, [5为好人牌(仅为预言家能查验出)], [6为神牌]
                   // [7为未知牌(仅为从第二天开始被女巫夜里毒死的人的身份]
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
    public int ID_status; // 0为可见（村民视角），1为不可见（村民视角）， 2为可见（狼人内部视角）
    public int dead = 0; // 死亡为1， 活着为0

    public ArrayList<Integer> langShiJiaoShenSi = new ArrayList<>(); // 存狼人视角里的神牌 // TODO: 这个只会记录死的神牌，
    // TODO: 这样狼人就可以判断剩下的牌中是民少还是神少，然后先选择刀少的一方
    public ArrayList<Integer> langShiJiaoMinSi = new ArrayList<>(); // 存狼人视角里的村民牌  // TODO: 道理同上方的langShiJiaoShenS
//    public ArrayList<Integer> langRenHaiHuo = new ArrayList<>();     // 存还活着的狼人你的编号 // TODO: 这个可能用不到了
    public ArrayList<Integer> langShiJiaoShenHuo = new ArrayList<>(); // 狼人视角中还活着的神牌，供狼人AI刀人
    public ArrayList<Integer> langShiJiaoMinHuo = new ArrayList<>(); // 狼人视角中还活着的民牌，供狼人AI刀人
    public ArrayList<Integer> haoRenShiJiaoLang = new ArrayList<>();  // 存着确定的狼人身份, 用于供女巫女巫毒和白天投票 [女巫和白天投票优先选择这个List里面的狼人]
    public ArrayList<Integer> haoRenShiJiaoShen = new ArrayList<>(); // 存着确定的神牌身份，用于供女巫晚上救人
    // TODO: 注意，[haoRenShiJiaoLang, haoRenShiJiaoShen] 这里面仅存一定确定是活着的牌，死去的牌要被移除

    static class langRenSpecial{
        // this is a Return Type
        int currentNumLang; // 现在还剩几只狼
        ArrayList<Integer> langList = new ArrayList<>();
        langRenSpecial(int a, ArrayList<Integer> b){
            currentNumLang = a;
            langList = b;
        }
    }

    // TODO: 记得要在前面的某个地方Call这个函数
    public static langRenSpecial langRenHaiHuoCheck(ArrayList<Player> playerList, ArrayList<Integer> langRenPlace){
        // TODO: 不如在需要检查还有哪些狼人或者的时候，这个函数都会对照这最新的 [.dead] 来判断还有几只狼或者
        int langNum = 0;
        ArrayList<Integer> langList = new ArrayList<>();
        for(int i = 0; i < 3; i ++){
            if((playerList.get(langRenPlace.get(i))).dead == 0){
                langNum += 1;
                langList.add(langRenPlace.get(i));
            }
        }
        return new langRenSpecial(langNum, langList);
    }



    public String toStringA(int d){
        if(d == 0){
            return "我是村名";
        } else if (d == 1){
            return "我是狼人";
        } else if (d == 2){
            return "我是女巫";
        } else if (d == 3){
            return "我是猎人";
        } else if (d == 4){
            return "我是预言家";
        } else {
            return "未找到相应的身份";
        }
    }

    public void actionNight(){


    }

    public void actionDay(){

    }

    public boolean checkDeadList(ArrayList<ArrayList<Integer>> deadList, int targetShoot){
        int deadLen = deadList.size();
        for(int i = 1; i < deadLen; i++){ // deadList start at 1
            if (deadList.get(i).contains(targetShoot)){
                return true;
            }
        }
        return false;
    }


}
