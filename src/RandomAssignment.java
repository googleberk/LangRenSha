public class RandomAssignment {



    public void assign(Player p, int d){
        if (d == 0){
            p = new CunMin();
        }else if (d == 1){
            p = new LangRen();
        }else if (d == 2){
            p = new NvWu();
        } else if (d == 3){
            p = new LieRen();
        } else if (d == 4){
            p = new YuYanJia();
        } else{
            System.out.println("Something wrong!");
        }

    }


}
