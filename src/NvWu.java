import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NvWu extends Player{

    public int IDclass = 2;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
//    public int dead = 0; // 死亡为1， 活着为0

    public int duYao = 1; // 最开始有一瓶毒药，毒药用完之后 duYao 就会变成0
    public int jieYao = 1; // 最开始有一瓶解药，解药用完之后  jieYao 就会变成0

    public NvWu(int i){
        place = i;
    }

    public String toString() {
        return "我是女巫";
    }

    /** 返回谁被毒死了 */
    public int actionNight(int langDaoDeRen, ArrayList<Player> playerList, int night,
                           ArrayList<ArrayList<Integer>> deadList, ArrayList<Integer> haoRenShiJiaoLang){
        // 女巫第一天(随机)[或者根据用户的输入]考虑救不救这个被刀的人
        // 如果手动控制，则解药和毒药都由手动控制
        // 如果AI控制，则解药和毒药都由AI控制
        int duSi = 9; // 如果duSi = 9, 说明没有毒人。
        Scanner scan = new Scanner(System.in);  // create a Scanner object

        System.out.println("你想要手动操控女巫吗?");
        System.out.println("请输入 [yes] or [no] 来完成上述决定.");
        String nv1 = scan.nextLine();
        if(nv1.equals("yes")){
            // 决定手动操控女巫
            String jiu;

            if((jieYao == 1) && (duYao == 1)){
                // 理论上可以使用解药和毒药中的其中一种

                System.out.println("你想要救[ " + langDaoDeRen + " ]号吗?");
                System.out.println("请输入[yes] or [no] 来完成上述决定");
                jiu = scan.nextLine();
                if(jiu.equals("yes")){
                    // 使用解药救下这个被狼刀的人
                    playerList.get(langDaoDeRen).dead = 0;
                    jieYao = 0; // 用完仅有一次的解药
                    // 把这个人从deadList 中移除
                    deadList.get(2*night-1).remove(langDaoDeRen);
                    System.out.println("你已经成功救下[ " + langDaoDeRen + " ]号.");
                }else{
                    // 没使用解药，使用毒药
                    System.out.println("你想要使用毒药吗?");
                    System.out.println("请输入[yes] or [no] 来完成上述决定");
                    String du = scan.nextLine();
                    if(du.equals("yes")) {
                        // 选择毒人
                        System.out.println("你想要毒几号?");
                        // 这里虽然是手动的毒人，但是我加了一条保险判断，就是不能毒到已经死掉的人身上
                        while(true){
                            System.out.println("请输入[0, 9) 中的一个整数来完成上述决定");
                            int du3 = scan.nextInt();
                            if(playerList.get(du3).dead == 0){
                                // 即将被毒的这个人还是活着的，说明可以下毒
                                playerList.get(du3).dead = 1;
                                deadList.get(2*night-1).add(du3);
                                duYao = 0; //用完这瓶毒药
                                System.out.println("你[女巫]第[ " + night + " ]晚成功毒了[ " + du3 + " ]号.");
                                duSi = du3;
                                break;
                            }
                        }
                    }else{
                        // 也不使用毒药
                        System.out.println("你在第[ " + night + " ]晚没有使用毒药.");
                    }
                }
            }else if((duYao == 1) && (jieYao == 0)){
                // 理论上不能用解药，只能使用毒药
                System.out.println("你之前已经使用过解药，所以本轮不能使用解药.");
                System.out.println("你是否要使用毒药?");
                System.out.println("请输入 [yes] or [no] 完成以上操作");
                String nv2 = scan.nextLine();
                if(nv2.equals("yes")){
                    // 确认使用毒药
                    System.out.println("你想要毒几号?");
                    // 这里虽然是手动的毒人，但是加了一条保险判断，就是不能毒到已经死掉的人身上
                    while(true){
                        System.out.println("请输入[0, 9) 中的一个整数来完成上述决定");
                        int du3 = scan.nextInt();
                        if(playerList.get(du3).dead == 0){
                            // 即将被毒的这个人还是活着的，说明可以下毒
                            playerList.get(du3).dead = 1;
                            deadList.get(2*night-1).add(du3);
                            duYao = 0; //用完这瓶毒药
                            System.out.println("你[女巫]第[ " + night + " ]晚成功毒了[ " + du3 + " ]号.");
                            duSi = du3;
                            break;
                        }
                    }
                }else{
                    System.out.println("你在第[ " + night + " ]晚没有使用毒药.");
                }
            }else if((duYao == 0) && (jieYao == 1)){
                // 理论上不能用毒药，只能使用解药
                System.out.println("你之前已经使用过毒药，所以本轮不能使用毒药.");
                System.out.println("你是否要使用解药?");
                System.out.println("请输入 [yes] or [no] 完成以上操作");
                String nv3 = scan.nextLine();
                if(nv3.equals("yes")){
                    // 确认使用解药
                    // 使用解药救下这个被狼刀的人
                    playerList.get(langDaoDeRen).dead = 0;
                    jieYao = 0; // 用完仅有一次的解药
                    // 把这个人从deadList 中移除
                    deadList.get(2*night-1).remove(langDaoDeRen);
                    System.out.println("你在第[ " + night + " ]晚已经成功救下[ " + langDaoDeRen + " ]号.");
                }else{
                    System.out.println("你在第[ " + night + " ]晚没有使用解药.");
                }
            }else{
                // 毒药和解药都已经用完
                System.out.println("你在第[ " + night + " ]晚不能使用毒药和解药.");
            }
        }else{
            // 系统AI控制女巫的行为
            if((jieYao == 1) && (duYao == 1)){
                // 理论上可以使用解药和毒药中的其中一种
                // 逻辑: 女巫先看这张被刀的牌是不是神牌，如果是就救，如果不是, 再看现在有没有确定的狼可以毒，如果有狼可以毒，就毒狼，
                // 如果没有，那么有(p=15/20)的概率救这个人, 有(p = 4/20)的概率不救，有(p = 1/20)的概率盲毒（毒在神牌之外的没死的人)，
                if(haoRenShiJiaoShen.contains(langDaoDeRen)){
                    // 被刀的是神牌，救他一命
                    playerList.get(langDaoDeRen).dead = 0;
                    deadList.get(2*night-1).remove(langDaoDeRen);
                    System.out.println("你在第[ " + night + " ]晚救下[ " + langDaoDeRen + " ]号.");
                    jieYao = 0;
                }else{
                    if(haoRenShiJiaoShen.size() != 0){
                        // 有可以毒的狼牌
                        // 随机一个狼毒
                        int langNum = haoRenShiJiaoLang.size();
                        Random rand = new Random();
                        int randl = rand.nextInt(langNum);
                        int targetLang = haoRenShiJiaoShen.get(randl);

                        playerList.get(targetLang).dead = 1;
                        deadList.get(2*night-1).add(targetLang);
                        // TODO: remove 这个狼 from haoRenShiJiaoShen
                        haoRenShiJiaoLang.remove(targetLang);
                        System.out.println("你在第[ " + night + " ]晚毒死[ " + targetLang + " ]号.");
                        duYao = 0;
                    }else{
                        double gaiLv = new Random().nextDouble();
                        if(gaiLv < 0.75){
                            // 15/20的概率救下这个被刀的人
                            playerList.get(langDaoDeRen).dead = 0;
                            deadList.get(2*night-1).remove(langDaoDeRen);
                            System.out.println("你在第[ " + night + " ]晚救下[ " + langDaoDeRen + " ]号.");
                            jieYao = 0;
                        }else if((gaiLv >= 0.75) && (gaiLv < 0.95)){
                            // 4/20的概率不救这个人
                            System.out.println("你在第[ " + night + " ]晚没有使用解药救下[ " + langDaoDeRen + " ]号.");
                        }else{
                            // 1/20的概率不救这个人 并且还随机盲毒一个人
                            while(true){
                                Random rand = new Random();
                                int du = rand.nextInt(9);
                                if((playerList.get(du).dead == 0) && (!haoRenShiJiaoShen.contains(du))){
                                    playerList.get(du).dead = 1;
                                    deadList.get(2*night-1).add(du);
                                    System.out.println("你在第[ " + night + " ]晚毒死[ " + du + " ]号.");
                                    duYao = 0;
                                }
                            }
                        }
                    }
                }

            } else if((duYao == 1) && (jieYao == 0)){
                // 理论上不能用解药，只能使用毒药
                // 检查haoRenShiJiaoLang中有没有狼人可以毒，如果有则随机里面一个狼毒，如果没有就(p=19/20)的概率不毒人，(p=1/20)的概率盲毒
                if(haoRenShiJiaoLang.size() != 0){
                    // 随机下毒里面的一个狼
                    int langNum = haoRenShiJiaoLang.size();
                    Random rand = new Random();
                    int rand1 = rand.nextInt(langNum);
                    int targetLang = haoRenShiJiaoLang.get(rand1);

                    playerList.get(targetLang).dead = 1;
                    deadList.get(2*night-1).add(targetLang);
                    haoRenShiJiaoLang.remove(targetLang);
                    System.out.println("你在第[ " + night + " ]晚毒死[ " + targetLang + " ]号.");
                    duYao = 0;
                }else{
                    double gaiLv = new Random().nextDouble();
                    if(gaiLv < 0.95){
                        // 不会毒人
                        System.out.println("你在第[ " + night + " ]晚没有下毒.");
                    }else{
                        // 盲毒
                        Random rand = new Random();
                        while(true){
                            int rand1 = rand.nextInt(9);
                            if((!haoRenShiJiaoShen.contains(rand1)) && (playerList.get(rand1).dead == 0)){
                                // 毒的人不在神牌中，并且还没有死
                                playerList.get(rand1).dead = 1;
                                deadList.get(2*night-1).add(rand1);
                                System.out.println("你在第[ " + night + " ]晚毒死[ " + rand1 + " ]号.");
                                duYao = 0;
                            }
                        }
                    }
                }
            } else if((duYao == 0) && (jieYao == 1)){
                // 理论上不能用毒药，只能使用解药
                // 看被刀的人是否在haoRenShiJiaoShen中，如果是则救下他，如果不是，如果再haoRenShiJiaoLang 中，则不会救，如果不在，有
                // (p=17/20) 的概率会救，有(p=3/20)的概率不会救
                if (haoRenShiJiaoShen.contains(langDaoDeRen)){
                    // 被刀的人在神牌中，可以救他
                    playerList.get(langDaoDeRen).dead = 0;
                    deadList.get(2*night-1).remove(langDaoDeRen);
                    System.out.println("你在第[ " + night + " ]晚救下[ " + langDaoDeRen + " ]号.");
                    jieYao = 0;
                }else{
                    if(haoRenShiJiaoLang.contains(langDaoDeRen)){
                        // 狼人自刀，不会救
                        System.out.println("你在第[ " + night + " ]晚没有救下[ " + langDaoDeRen + " ]号.");
                    }else{
                        double gaiLv = new Random().nextDouble();
                        if(gaiLv < 0.85){
                            // 救下这个人
                            playerList.get(langDaoDeRen).dead = 0;
                            deadList.get(2*night-1).remove(langDaoDeRen);
                            System.out.println("你在第[ " + night + " ]晚救下[ " + langDaoDeRen + " ]号.");
                            jieYao = 0;
                        }else{
                            // 没救这个人
                            System.out.println("你在第[ " + night + " ]晚没有救下[ " + langDaoDeRen + " ]号.");
                        }
                    }
                }
            }else{
                // 毒药和解药都已经用完
                System.out.println("你在第[ " + night + " ]晚不能使用毒药和解药.");
            }
        }
        return duSi;
    }

    public void actionDay(){



    }

    public void sayDay(ArrayList<ArrayList<Integer>> allKnown){
        Scanner scan = new Scanner(System.in);
        System.out.println("你想要手动操控女巫发言吗？");
        System.out.println("请输入 [yes] or [no] 来完成以上决定.");
        String cmsd1 = scan.nextLine();
        if(cmsd1.equals("yes")){
            // 手动操控女巫发言, 既 女巫可以说自己的身份为神[不能悍跳预言家]，或者是村民，暂时不支持说自己的身份为狼
            System.out.println("你想要把自己的身份说成 [0]村民 或者 [6]神 或者 [2]女巫 吗？");
            int cmsd2 = scan.nextInt();
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(this.place, cmsd2);
            allKnown.add(temp);
        }else{
            // 系统自动爆出自己的正确真实身份 [系统不会撒谎]
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(this.place, 2);
        }

    }


}
