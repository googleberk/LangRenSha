public class Player{

    public int ID; // 村名为0，狼人为1，女巫为2，猎人为3，预言家为4

    public int ID_status; // 0为可见（村民视角），1为不可见（村民视角）， 2为可见（狼人内部视角）

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

    public void play(){


    }




}
