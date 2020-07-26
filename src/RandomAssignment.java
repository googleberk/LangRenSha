public class RandomAssignment {



    public void assign(int i, Player p, int d){
        if (d == 0){
            p = new CunMin(i);
        }else if (d == 1){
            p = new LangRen(i);
        }else if (d == 2){
            p = new NvWu(i);
        } else if (d == 3){
            p = new LieRen(i);
        } else if (d == 4){
            p = new YuYanJia(i);
        } else{
            System.out.println("Something wrong!");
        }

    }


}
