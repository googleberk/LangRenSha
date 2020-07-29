import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LangRen extends Player{

    public int IDclass = 1;
    public int place; // 在本轮游戏中的位置， 0到8中其中一个
//    public int dead = 0; // 死亡为1， 活着为0

    public LangRen(int i){
        place = i;
    }

    public String toString() {
        return "我是狼人";
    }

    public static int actionNight(ArrayList<Integer> langRenPlace, ArrayList<Player> playerList, int night,
                                  ArrayList<Integer> langShiJiaoShenSi, ArrayList<Integer> langShiJiaoMinSi,
                                  ArrayList<ArrayList<Integer>> deadList, ArrayList<Integer> langShiJiaoShenHuo,
                                  ArrayList<Integer> langShiJiaoMinHuo){
        // 狼人的刀人逻辑: 第一天随机刀人(可以自刀), 第二天往后根据场上死掉的村名和神的相对数量(狼人视角,好人可以撒谎)
        //                                     来决定刀谁(这时候不能自刀了)，

        Random rand;
        int die = 9; //  TODO: 暂时不支持狼人空刀，即狼人每晚都必须刀人
        Scanner scan1 = new Scanner(System.in);  // create a Scanner object
        System.out.println("你想要选择某一个狼人来执行刀人的指令吗？");
        System.out.println("请输入[yes] or [no] 来完成上述决定");
        String daoRen1 = scan1.nextLine();
        if(daoRen1.equals("yes")){
            // 选择某一狼人 就代表会继续选择刀谁，暂时不支持选择某一狼人然后再交给系统执行刀人指令
            System.out.println("你想要选择哪一个狼人来执行刀人的指令吗? ");
            System.out.print("请输入以下整数中的一个: ");
            // 这里用了一个比较复杂的AI 来帮助判断还有几只狼是活着的
            langRenSpecial lrs = langRenHaiHuoCheck(playerList, langRenPlace);  // 检查狼人活着的最新情况
            for(int k = 0; k < lrs.currentNumLang; k ++){
                System.out.print(lrs.langList.get(k));
                System.out.print(" ");
            }
            System.out.println();
            int daoRen2 = scan1.nextInt();
            System.out.println("你已成功选择[" + daoRen2 + "]号狼人作为视角执行刀人.");

            // 只有第一晚可以自刀，后面暂时不能自刀
            if(night == 1){
                // 第一晚
                System.out.println("你想要刀几号？");
                System.out.println("Hint: 现在是第一晚，规则允许自刀(也可以刀狼同伴)");
                System.out.println("请输入[0, 9) 之间的一个整数来完成上述决定.");
                int die2 = scan1.nextInt();
                // 刀这个人
                System.out.println("你已经成功刀了[" + die2 + "]号.");
                playerList.get(die2).dead = 1;
                // TODO: 修改deadList
                deadList.get(2*night-1).add(die2);
                die = die2;
            }else{
                // 第二晚及以后
                // 不能自刀，不能刀狼同伴, 也不能刀已经死过的人
                System.out.println("你想要刀几号？");
                System.out.println("Hint: 现在是第二晚及以后，规则不允许自刀(也不可以刀狼同伴， 并且不能刀已经死了的人)");
                // 这里为了简便，使用了while(true)来判断,
                while(true){
                    System.out.println("请输入[0, 9) 之间的一个整数来完成上述决定.");
                    int die3 = scan1.nextInt();
                    if((die3 != langRenPlace.get(0)) && (die3 != langRenPlace.get(1))
                            && (die3 != langRenPlace.get(2)) && (playerList.get(die3).dead != 1)){
                        // 找到一个符合条件的刀人对象
                        // 刀这个人
                        System.out.println("你已经成功刀了[" + die3 + "]号.");
                        playerList.get(die3).dead = 1;
                        // TODO: 修改deadList
                        deadList.get(2*night-1).add(die3);
                        die = die3;
                        break;
                    }
                }
            }
        }else{
            // 系统将自动随机一个狼人来执行刀人指令
            rand = new Random();

            // 如果是第一晚，三狼中任意一狼都可以作为刀人的视角
            // 这里用了一个比较复杂的AI 来帮助判断还有几只狼是活着的
            langRenSpecial lrs = langRenHaiHuoCheck(playerList, langRenPlace);  // 检查狼人活着的最新情况 TODO: 似乎这里看起来是没用的
            System.out.print("狼人还剩以下几只是活着的: ");
            for(int k = 0; k < lrs.currentNumLang; k++){
                System.out.print(lrs.langList.get(k));
                System.out.print(" ");
            }
            System.out.println();
            int randLang = rand.nextInt(lrs.currentNumLang);
            System.out.println("系统已经成功选择[" + lrs.langList.get(randLang) + "]号狼人作为视角.");

            // 只有第一晚可以自刀，后面暂时不能自刀
            if(night == 1){
                // 第一晚
                int die2 = rand.nextInt(9); // 第一晚狼人可以刀[0, 8]人中间任何一个
                // 刀这个人
                System.out.println("你在第[ " + night + " ]晚已经成功刀了[" + die2 + "]号.");
                playerList.get(die2).dead = 1;
                deadList.get(2*night-1).add(die2); // 修改deadList
                die = die2;
            }else{
                // 第二晚及以后
                // 不能自刀，不能刀狼同伴, 也不能刀已经死过的人

                // TODO: 这里系统AI还有一些额外的复杂判断: 狼会自动先在神牌和民牌中选择少的一方随机刀
                int minLeft = (3 - langShiJiaoMinSi.size()); // 狼视角里剩余的民个数
                int shenLeft = (3- langShiJiaoShenSi.size()); // 狼视角里剩余的神个数
                if ((shenLeft < minLeft)){
                    // 这里狼人选择先刀神
                    if(langShiJiaoShenHuo.size() != 0){
                        // 说明有神牌可以刀
                        int lang1 = rand.nextInt(langShiJiaoShenHuo.size());   // 随机其中一张神牌
                        int targetLang = langShiJiaoShenHuo.get(lang1);        // 选定这张牌
                        playerList.get(targetLang).dead = 1;                   // 刀这张牌
                        langShiJiaoShenHuo.remove(targetLang); // 把这张牌从langShiJiaoShenHuo中移除
                        langShiJiaoShenSi.add(targetLang);     // 添加到langShiJiaoShenSi中去
                        System.out.println("你[狼]成功在第[" + night + "]晚刀死[" + targetLang + "]号玩家.");
                        // TODO: 注意这里因为女巫每晚会在狼人之后行动，所以女巫有改变被狼刀的人的状态, 但是还是要在这里先手改变狼刀的人
                        // TODO: 的状态，因为女巫可能双药用完，或者已经死了
                        // TODO: 目前我能想出来的状态有  player.dead 和 deadList.get(2*night - 1).add()/remove()
                        deadList.get(2*night-1).add(targetLang); // 添加这张牌到deadList中去
                        die = targetLang;
                    }else{
                        // 神牌比民牌少，但是没有进一步确定的神牌可以刀，这时候随机刀(不能刀死去的人，不能刀自己和狼同伴，不能刀已知的村民中的牌)
                        int lang1 = rand.nextInt(9);
                        while(true){
                            if(langShiJiaoMinHuo.size() != 0){
                                // 说明此时还有确定的民牌活着, 那在随机刀的时候避开这里面的牌
                                if((playerList.get(lang1).dead == 0) && (!langShiJiaoMinHuo.contains(lang1))
                                        && (!langRenPlace.contains(lang1))){
                                    playerList.get(lang1).dead = 1;       // 刀这张牌
                                    deadList.get(2*night-1).add(lang1);   // 添加进deadList
                                    System.out.println("你成功在第[ " + night + " ]晚刀死[ " + lang1 + " ]号.");
                                    die = lang1;
                                    break;
                                }
                            }else{
                                // 说明此时没有确定的民牌活着, 那在随机刀的时候不用考虑
                                if((playerList.get(lang1).dead == 0) && (!langRenPlace.contains(lang1))){
                                    playerList.get(lang1).dead = 1;       // 刀这张牌
                                    deadList.get(2*night-1).add(lang1);   // 添加进deadList
                                    System.out.println("你成功在第[ " + night + " ]晚刀死[ " + lang1 + " ]号.");
                                    die = lang1;
                                    break;
                                }
                            }
                        }
                    }
                }else if(shenLeft == minLeft){
                    // 神牌和民牌剩余的一样
                    // 如果有神牌，就先刀神牌，如果没有神牌，就先检查有没有民牌，如果有民牌，就先刀民，如果也没有民牌，那么就随机刀人
                    if(langShiJiaoShenHuo.size() != 0){
                        // 可以选择确定的神刀
                        int shenNum = langShiJiaoShenHuo.size();
                        int lang2 = rand.nextInt(shenNum);
                        int targetShen = langShiJiaoShenHuo.get(lang2);  // 确认要刀的人
                        playerList.get(targetShen).dead = 1;    // 刀死这个人
                        deadList.get(2*night-1).add(targetShen);  // 添加到deadList
                        langShiJiaoShenHuo.remove(targetShen);   // 从活着的神牌中移除
                        langShiJiaoShenSi.add(targetShen);     // 添加到死掉的神牌中去
                        die = targetShen;    // 赋值die
                        System.out.println("你成功在第[ " + night + " ]晚刀死[ " + targetShen + " ]号.");
                    }else{
                        // 没有确定的神牌可以刀
                        if(langShiJiaoMinHuo.size() != 0){
                            // 有确定的民牌活着可以刀
                            int minNum = langShiJiaoMinHuo.size();
                            int min2 = rand.nextInt(minNum);
                            int targetMin = langShiJiaoMinHuo.get(min2);  // 确认要刀的人
                            playerList.get(targetMin).dead = 1;          // 刀死这个人
                            deadList.get(2*night-1).add(targetMin);      // 添加到deadList
                            langShiJiaoMinHuo.remove(targetMin);         // 从活着的民牌中移除这张牌
                            langShiJiaoMinSi.add(targetMin);             // 添加到死掉的民牌中去
                            die = targetMin;  // 赋值die
                            System.out.println("你成功在第[ " + night + " ]晚刀死[ " + targetMin + " ]号.");
                        }else{
                            // 这时候既没有确定的神牌又没有确定的民牌可以刀，那么就随机刀人
                            int rand3 = rand.nextInt(9);         // 确认要刀的人
                            while(true){
                                if((playerList.get(rand3).dead == 0) && (!langRenPlace.contains(rand3))){
                                    playerList.get(rand3).dead = 1;     // 刀死这个人
                                    deadList.get(2*night-1).add(rand3); // 添加到deadList中去
                                    die = rand3;     // 赋值die
                                    System.out.println("你成功在第[ " + night + " ]晚刀死[ " + rand3 + " ]号.");
                                }
                                break;
                            }
                        }
                    }
                } else{
                    // 这时候狼人会选择先杀民
                    if(langShiJiaoMinHuo.size() != 0){
                        // 有确定的民牌可以刀
                        int minSize = langShiJiaoMinHuo.size();
                        int randMin = rand.nextInt(minSize);
                        int targetMin = langShiJiaoMinHuo.get(randMin);    // 确认要刀的牌
                        playerList.get(targetMin).dead = 1;                 // 刀死这个民牌
                        deadList.get(2*night-1).add(targetMin);     // 添加到deadList中去
                        langShiJiaoMinHuo.remove(targetMin);        // 从活着的民牌中移除这张牌
                        langShiJiaoMinSi.add(targetMin);            // 添加到死掉的民牌中去
                        die = targetMin;     // 赋值die
                    }else{
                        // 应该刀民的，但是没有确定的民牌可以刀, 就随机刀
                        if (langShiJiaoShenHuo.size() != 0){
                            // 如果有确定的神牌还活着，那么随机刀的时候避开这些神牌
                            int rand4 = rand.nextInt(9);     // 随机一张要刀的牌
                            while(true){
                                if((playerList.get(rand4).dead == 0) && (!langRenPlace.contains(rand4))
                                        &&(!langShiJiaoShenHuo.contains(rand4))){
                                    playerList.get(rand4).dead = 1;       // 刀死这张牌
                                    deadList.get(2*night-1).add(rand4);   // 添加到deadList中去
                                    System.out.println("你成功在第[ " + night + " ]晚刀死[ " + rand4 + " ]号.");
                                    die = rand4;          // 赋值die
                                    break;
                                }
                            }
                        }else{
                            // 没有确定的神牌还活着，所以不用特意避开神牌刀
                            int rand5 = rand.nextInt(9);           // 随机一张要刀的牌
                            while(true){
                                if((playerList.get(rand5).dead == 0) && (!langRenPlace.contains(rand5))){
                                    playerList.get(rand5).dead = 1;      // 刀死这张牌
                                    deadList.get(2*night-1).add(rand5);  //添加到deadList中去
                                    System.out.println("你成功在第[ " + night + " ]晚刀死[ " + rand5 + " ]号.");
                                    die = rand5;                         // 赋值die
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return die;
    }

    public void voteDay(){

    }

    public void sayDay(ArrayList<ArrayList<Integer>> allKnown){
        // TODO: 考虑狼可以把自己的身份报成 村民，和神牌，方便对接女巫的AI判断，暂时不支持报成好人牌
        Scanner scan = new Scanner(System.in);
        System.out.println("你想要手动操控狼人发言吗？");
        System.out.println("请输入 [yes] or [no] 来完成以上决定.");
        String cmsd1 = scan.nextLine();
        if(cmsd1.equals("yes")){
            // 手动操控狼人发言, 既 狼人可以说自己的身份为狼 [暂时不支持说成其他身份]
            System.out.println("你想要把自己的身份说成 [1]狼人 吗?");
            int cmsd2 = scan.nextInt();
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(this.place, cmsd2);
            allKnown.add(temp);
        }else{
            // 系统自动爆出自己的正确真实身份 [系统不会撒谎]
            ArrayList<Integer> temp = new ArrayList<>(2); // 格式为 [位置 身份]
            temp.add(this.place, 1);
        }

    }


}
