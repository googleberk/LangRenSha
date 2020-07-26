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


}
