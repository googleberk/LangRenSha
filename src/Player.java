import java.util.ArrayList;

public class Player{

    public int ID; // 村名为0，狼人为1，女巫为2，猎人为3，预言家为4, [5为好人牌]
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
    public int ID_status; // 0为可见（村民视角），1为不可见（村民视角）， 2为可见（狼人内部视角）
    public int dead = 0; // 死亡为1， 活着为0

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
        for(int i = 0; i < deadLen; i++){
            if (deadList.get(i).contains(targetShoot)){
                return true;
            }
        }
        return false;
    }


}
