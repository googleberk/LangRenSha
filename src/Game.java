import java.util.*;

public class Game {

    int size = 9;

    public int yuYanJiaPlace;
    public int lieRenPlace;
    public int nvWuPlace;
    public ArrayList<Integer> cunMinPlace;
    public ArrayList<Integer> langRenPlace;

    public ArrayList<Player> playerList;
    public ArrayList<Integer> chaYanList;
    public ArrayList<Integer> IDlist;

    public int night = 1; // 记录夜晚
    public int day = 1; // 记录白天

    public ArrayList<ArrayList<Integer>> deadList;
    public ArrayList<ArrayList<Integer>> allKnown;  // 存着白天根据发言/(发动的技能)得出的公共信息
    public ArrayList<String> IDtoString = new ArrayList<>(); // 存着从IDClass(int) 到 中文(String)的 Map

    public void initIDtoString(){
        IDtoString.add(0, "村民");
        IDtoString.add(1, "狼人");
        IDtoString.add(2, "女巫");
        IDtoString.add(3, "猎人");
        IDtoString.add(4, "预言家");
    }

    public void initGame(){

        IDlist = new ArrayList<>(size);
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

        // 找出本轮中刚开始时候所有人的位置
        for(int i = 0; i<9; i++){
            if(IDlist.get(i) == 0){
                cunMinPlace.add(i);
            } else if (IDlist.get(i) == 1){
                langRenPlace.add(i);
            } else if (IDlist.get(i) == 2){
                nvWuPlace = i;
            } else if(IDlist.get(i) == 3){
                lieRenPlace = i;
            } else if(IDlist.get(i) == 4){
                yuYanJiaPlace = i;
            } else{
                System.out.println("Something Wrong!");
            }
        }


        // store player in a list
        playerList = new ArrayList<>(size);
        for(int i = 0; i < 9; i++){
            playerList.add(new Player());
        }

        //instantiate each player
        RandomAssignment ra = new RandomAssignment();
        for(int i = 0; i < 9; i ++){
            ra.assign(i, playerList.get(i), IDlist.get(i));
        }

        // test
        for(int i = 0; i < 9; i++){
            System.out.println(playerList.get(i).toStringA(IDlist.get(i)));
        }


        // create 预言家查验 list
        chaYanList = new ArrayList<>(size); // 0 为未查验， 1为已经查验
        for(int i = 0; i<9; i++) {
            chaYanList.add(i, 0);
        }
        // 身份定下来之后，自己就不用查验自己
        chaYanList.add(yuYanJiaPlace, 1);

    }



    // TODO: 想一下怎么处理这两个参数 langShiJiaoShenSet，langShiJiaoMinSet
    public void startGame(ArrayList<Integer> langShiJiaoShenSet, ArrayList<Integer> langShiJiaoMinSet){


        while(true){

            // 第一天夜里:
            int duSi;
            int langDaoDeRen;

            System.out.println("现在是第[ " + night + " ]晚的活动");
            System.out.println("==================================================================");
            System.out.println();
            System.out.println();
            // 先是预言家开始查验
            System.out.println("预言家第[" + night + "]晚开始验人了.");
            ((YuYanJia) playerList.get(yuYanJiaPlace)).actionNight(chaYanList, IDlist);

            // 然后是狼人刀人
            System.out.println("狼人第[" + night + "]晚开始刀人了.");
            // TODO: 为了使得startGame看起来更简洁，原本是在这里先找出要用哪一个狼人，然后call 那个狼人的instance method,
            // TODO: 现在把这些全部移动到actionNight中去了，所以这里把狼人的actionNight变成了一个static method.
            langDaoDeRen = LangRen.actionNight(langRenPlace, playerList, night, langShiJiaoShenSet, langShiJiaoMinSet, deadList);

            // 第一天的时候[最后]是女巫看到刀人的结果，决定救不救这个人, 第一天可以毒人(包括毒自己)
            System.out.println("女巫第" + night + "晚开始行动了.");
            duSi = ((NvWu) playerList.get(nvWuPlace)).actionNight(langDaoDeRen, playerList); // TODO: 女巫的晚上动作需要包含修改deadList
            // 另外【猎人和村民晚上是没有动作的】
            night += 1; // 到第二天白天 这时候[day = 2, night = 2]
            day += 1;

            System.out.println("现在是第[ " + day + " ]天白天的活动");
            System.out.println("==================================================================");
            System.out.println();
            System.out.println();

            // 昨晚死的人先发言 (如果有猎人，行动)
            deadSayAndActionDay(duSi, deadList, playerList, day);

            // 按照 IDlist 中的顺序从 0 号开始发言



            // 真预言家第二天必定会跳，狼人永远不会悍跳预言家
            if(day == 2){
                // check who died yesterday
                checkDied();  // TODO: 把死亡的人添加到deadList 上 [暂时的功能是第一天夜里死亡的对象]
                System.out.println("昨晚一共死了" + deadList.size() + "人.");
                for(int i = 1; i < deadList.size(); i++){
                    System.out.println(deadList.get(1).get(i) + "号在第" + (night-1) + "晚死亡.");
                }
                // 死掉的人(技能)和发言
                System.out.println("下面死掉的人开始(放技能)/发声");
                deadActionAndSayDay(duSi, allKnown, deadList, playerList, day);
            }


        }
    }

    public void baiTianFaYan(){

    }

    public void checkDied(){
        // TODO: 检查这个函数是否只用于搞出deadList 第一天的内容，不过似乎可以在女巫的夜里活动那里把 deadList 填好.
        deadList = new ArrayList<>(10); // 从1开始存，9天default
        // 顺序是: 第一天夜里，第二天白天(白天按照，先被猎人带走，再投票的顺序)，
        // 第二天夜里，第三天白天，第三天夜里... 来存死亡的人的名单(存的是他们的位置).
        int j = 0;
        ArrayList<Integer> temp = new ArrayList<>(2); // 每天(夜里)最多两个人死亡 [存这两个人(或一个人)的编号]

        for(int i = 0; i < 9; i++){
            if(playerList.get(i).dead == 1){
                temp.add(j, i);
                j += 1;
            }
        }
        deadList.add(1, temp);

    }

    public void deadSayAndActionDay(int duSi, ArrayList<ArrayList<Integer>> deadList,
                                    ArrayList<Player> playerList, int day){
        // 这个函数只用来处理白天死掉的人，白天被投票出具的暂不处理（可能将白天投票出局的人之后发生的事情写进另一个函数
        // day:      2 3 4 5
        // deadList: 1 3 5 7
        // day 与 deadList系数之间的对应关系
        int deadLen = deadList.get(2*day-3).size(); // 检查昨晚有几个人死亡
        Player dead1;
        Player dead2;
        int lieRenDaiZou;
        boolean beiLieRenDai = false;

        if(deadLen == 1){
            // 先找出死掉的[ dead1 ]玩家
            dead1 = playerList.get(deadList.get(2*day-3).get(0));
            // 死掉的人先发言
            // 第三天白天以后死去就不能发言了
            if(day >= 3){
                // 不能发言
            }else{
                findWhoAndSay(dead1.ID, dead1.place, beiLieRenDai);
            }

            // 死掉的人如果是猎人 根据情况发动技能,
            // TODO: ??只有猎人是特殊情况，能发动技能的
            if(dead1.ID == 3){
                // 猎人在这里发动技能带走人
                lieRenDaiZou = LieRenDieAndAction(duSi, dead1, day);  // 这个函数里有把这个新被带走的人存在deadList的操作
                // TODO: 这时无论猎人在前面发言时候说自己的身份是什么, 所有人都知道他是一个猎人了.
                // 新死掉的这个人发言
                // 无论是第几天白天死掉的， 只要是被猎人带走的就能发言
                beiLieRenDai = true; // 这里就能确定这个死掉的人确实是被猎人带走的
                findWhoAndSay(IDlist.get(lieRenDaiZou), lieRenDaiZou, beiLieRenDai);
            }else{
                System.out.println("死去的人不能发动技能.");
            }

        }else{
            dead1 = playerList.get(deadList.get(1).get(0));
            dead2 = playerList.get(deadList.get(1).get(1));
            // 小位置编号先发言, (技能不能打在已经死掉的另一个人身上)
            // 大编号后发言
            if(day >= 3){
                // 第三天白天之后就不能发言了
            }else{
                if(dead1.place < dead2.place){
                    // dead1 这个人先发言
                    findWhoAndSay(dead1.ID, dead1.place, beiLieRenDai);
                    findWhoAndSay(dead2.ID, dead2.place, beiLieRenDai);
                }else{
                    // dead2 这个人先发言
                    findWhoAndSay(dead2.ID, dead2.place, beiLieRenDai);
                    findWhoAndSay(dead1.ID, dead1.place, beiLieRenDai);
                }
            }
            // 如果这两人中有猎人则根据有没有被女巫毒发动技能
            // 判断他们中谁是猎人
            boolean dead1IsLieRen = dead1.ID == 3;
            boolean dead2IsLieRen = dead2.ID == 3;
            if(dead1IsLieRen || dead2IsLieRen ){
                // 如果两人中有一个猎人
                // 猎人发动技能
                if(dead1IsLieRen){
                    lieRenDaiZou = LieRenDieAndAction(duSi, dead1, day);  // 这个函数里有把这个新被带走的人存在deadList的操作
                    // TODO: 这时无论猎人在前面发言时候说自己的身份是什么, 所有人都知道他是一个猎人了.
                }else{
                    lieRenDaiZou = LieRenDieAndAction(duSi, dead2, day);  // 这个函数里有把这个新被带走的人存在deadList的操作
                    // TODO: 这时无论猎人在前面发言时候说自己的身份是什么, 所有人都知道他是一个猎人了.
                }
                // 新死掉的这个人发言
                // 无论是第几天白天死掉的， 只要是被猎人带走的就能发言
                beiLieRenDai = true; // 这里就能确定这个死掉的人确实是被猎人带走的
                findWhoAndSay(IDlist.get(lieRenDaiZou), lieRenDaiZou, beiLieRenDai);
            }
        }
    }


    public void findWhoAndSay(int targetIDClass, int targetplace, boolean beiLieRenDai){
        // 只有一个人发言的情况
        if(targetIDClass == 0){
            // 村民发言
            if(beiLieRenDai){
                System.out.println("我[" + targetplace + "]号在第" + day + "天被猎人发动技能带走了.");
            }
            ((CunMin) playerList.get(targetplace)).sayDay(allKnown);
        }else if (targetIDClass == 1){
            // 狼人发言
            if(beiLieRenDai){
                System.out.println("我[" + targetplace + "]号在第" + day + "天被猎人发动技能带走了.");
            }
            ((LangRen) playerList.get(targetplace)).sayDay(allKnown);
        } else if(targetIDClass == 2){
            // 女巫发言
            if(beiLieRenDai){
                System.out.println("我[" + targetplace + "]号在第" + day + "天被猎人发动技能带走了.");
            }
            ((NvWu) playerList.get(targetplace)).sayDay(allKnown);
        } else if (targetIDClass == 3){
            // 猎人发言
            if(beiLieRenDai){
                System.out.println("我[" + targetplace + "]号在第" + day + "天被猎人发动技能带走了.");
            }
            ((LieRen) playerList.get(targetplace)).sayDay(allKnown);
        } else if (targetIDClass == 4){
            // 预言家发言
            if(beiLieRenDai){
                System.out.println("我[" + targetplace + "]号在第" + day + "天被猎人发动技能带走了.");
            }
            ((YuYanJia) playerList.get(targetplace)).sayDay(allKnown, day);
        }
    }

    public int LieRenDieAndAction(int duSi, Player dead1, int day){
        int lieRenDaiZou = 9; // 存被猎人带走的人的位置编号, default 是 9
        if(duSi == dead1.place){
            // 如果死掉的是猎人 并且晚上被女巫毒死, 则第二天不能发动技能

        }else{
            // 如果晚上死掉的是猎人，并且不是被女巫毒死的，则第二天可以发动技能
            // 或者猎人是白天被投票出去的
            lieRenDaiZou = ((LieRen) playerList.get(dead1.place)).actionDay(deadList, playerList, day);
            // TODO: deadList 新增一个被猎人带走的人 [这个操作应该放在猎人的actionDay 里面解决]
            deadList.get(day).add(lieRenDaiZou);
            // 这时候大家都知道这个死去的并且发动技能的人是猎人

        }
        return lieRenDaiZou;
    }




}
