import java.util.ArrayList;
import java.util.Collections;

public class Game {

    int size = 9;

    public void initGame(){
        ArrayList<Integer> IDlist = new ArrayList<>(size);
        IDlist.add(0, 0);
        IDlist.add(1, 0);
        IDlist.add(2, 0);
        IDlist.add(3, 1);
        IDlist.add(4, 1);
        IDlist.add(5, 1);
        IDlist.add(6, 2);
        IDlist.add(7, 3);
        IDlist.add(8, 4);

        // shuffle the above list
        Collections.shuffle(IDlist);
        System.out.println(IDlist.toString());

        // store player in a list
        ArrayList<Player> playerList = new ArrayList<>(size);
        for(int i = 0; i < 9; i++){
            playerList.add(new Player());
        }

        //instantiate each player
        RandomAssignment ra = new RandomAssignment();
        for(int i = 0; i < 9; i ++){
            ra.assign(playerList.get(i), IDlist.get(i));
        }

        // test
        for(int i = 0; i < 9; i++){
            System.out.println(playerList.get(i).toStringA(IDlist.get(i)));
        }

    }




    public void startGame(){


        while(true){



            break;


        }


    }




}
