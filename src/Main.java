import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author lin
 * @version 1.0
 */

//test
public class Main {
    public static final int DISPLAYS_FREE_PARTITIONS = 1;
    public static final int ASSIGN_JOBS = 2;
    public static final int RECYCLE_JOBS = 3;
    public static final int EXIT_THE_PROGRAM = 0;

    static LinkedList<FreePartitions> freePartitions = new LinkedList<>();//空闲表集合
    static LinkedList<JCB> workList = new LinkedList<>();//作业集合
    static LinkedList<JCB> finishWorkList = new LinkedList<>(); //完成作业集合

    static int is = 0;
    static int flag;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        FreePartitions fP = new FreePartitions("0", 128, 896, 'n');
        freePartitions.add(fP);
        menu();
    }

    //操作选择页面
    private static void menu() {

        while (true) {
            System.out.println();
            System.out.println(("*********************************************\t"));
            System.out.println(("\t\t\t\t存储管理实验演示"));
            System.out.println(("*********************************************\t\t"));
            System.out.println(("\t\t\t\t1.显示空闲分区."));
            System.out.println(("\t\t\t\t2.分配作业."));
            System.out.println(("\t\t\t\t3.回收作业."));
            System.out.println(("\t\t\t\t0.退出程序."));
            System.out.println(("\t\t\t\t选择所要操作:"));
            int choose = sc.nextInt();
            switch (choose) {
                case EXIT_THE_PROGRAM -> {
                    System.out.println("退出程序");
                    System.exit(0);
                }
                case DISPLAYS_FREE_PARTITIONS -> DisFp();   //显示空闲分区表
                case ASSIGN_JOBS -> {
                    System.out.println("首次适应算法:");
                    assignJobs();   //分配作业
                }
                case RECYCLE_JOBS -> recycleJobs();
                default -> System.out.println("选择错误,重新选择.");
            }
        }
    }

    //回收作业
    private static void recycleJobs() {
        if (finishWorkList != null) {
            checkWorkAndRecycle();
        } else {
            System.out.println("没有可回收的作业!");
        }
    }

    //在完成作业集合中查找要回收的作业并完成回收释放空闲区
    private static void checkWorkAndRecycle() {
        int has = 0;  //存入作业名的标志位
        System.out.println("输入作业名:");
        String checkName = sc.next();
        Iterator<JCB> fwl = finishWorkList.iterator();
        while (fwl.hasNext()) {
            JCB work = fwl.next();
            if (work.getName().equals(checkName)) { //在完成作业集合中查找要回收的作业
                has = 1;//作业存在
                reclaim(work);
                fwl.remove();
                break;
            }
        }
        if (has == 0) {
            System.out.println("作业名输入错误,请重新输入!");
            checkWorkAndRecycle();
        }
    }

    //回收空闲区
    private static void reclaim(JCB recWork) {
        int success = 0;
        int hasUp = 0;//上邻标识
        int hasDown = 0;//下邻标识
        Iterator<FreePartitions> iterator = freePartitions.iterator();
        while (iterator.hasNext()) {
            FreePartitions freePartition = iterator.next();
            if ((freePartition.getAddr() == (recWork.getAddr() + recWork.getSize()))) {//回收区有下邻
                recWork.setSize(recWork.getSize() + freePartition.getSize());
                hasDown = 1;
                for (int i = 0; i < freePartitions.size(); i++) { //查找上邻区
                    FreePartitions freeArea = freePartitions.get(i);
                    if (recWork.getAddr() == (freeArea.getAddr() + freeArea.getSize())) { //有下邻又有上邻
                        freeArea.setSize(freeArea.getSize() + recWork.getSize());//有下邻又有上邻回收方法,回收到上邻区域
                        freePartitions.remove(i + 1);//删除下邻区域
                        hasUp = 1;
                        success = 1;
                        break;
                    }
                }
                if (hasUp == 0) { //有下邻无上邻
                    freePartition.setAddr(recWork.getAddr()); //有下邻无上邻回收方案
                    freePartition.setSize(recWork.getSize());
                    success = 1;
                }
                break;
            }
        }
        if (hasDown == 0) { //区域无下邻判断
            FreePartitions freePartitions1 = new FreePartitions();
            for (int i = 0; i < freePartitions.size(); i++) {
                freePartitions1 = freePartitions.get(i);
                if ((freePartitions1.getAddr() + freePartitions1.getSize()) == recWork.getAddr()) { //无下邻有上邻
                    freePartitions1.setSize(freePartitions1.getSize() + recWork.getSize());
                    hasUp = 1;
                    success = 1;
                    break;
                }
                //无下邻且无上邻
                if (freePartitions == null) {
                    FreePartitions newFree = new FreePartitions("0", recWork.getAddr(), recWork.getSize(), 'n');
                    freePartitions.add(newFree);
                    success = 1;
                    break;
                }//空闲表不空
                int index = checkIndex(recWork);//获取下一个空闲表的索引
                if (index >= 0) { //后面存在空闲表
                    FreePartitions newFree = new FreePartitions((freePartitions.get(index).getName()), recWork.getAddr(), recWork.getSize(), 'n');
                    for (int i1 = index; i1 < freePartitions.size(); i1++) {
                        freePartitions.get(i1).setName(freePartitions.get(i1).getName() + 1);
                    }
                    freePartitions.add(index, newFree);
                    success = 1;
                    break;
                }
                //后面没有空闲表
                FreePartitions newFree = new FreePartitions((freePartitions.getLast().getName()) + 1, recWork.getAddr(), recWork.getSize(), 'n');
                freePartitions.add(newFree);
                success = 1;
                break;
            }
  /*          if (hasUp == 0) { //无下邻且无上邻
                FreePartitions newFree = new FreePartitions(recWork.getName(), recWork.getAddr(), recWork.getSize(), 'n');
                if (freePartitions == null) {  //空闲表为空
                    freePartitions.add(newFree);
                } else {  //空闲表不空
                    for (int i = 0; i < freePartitions.size(); i++) {
                        FreePartitions getFree = freePartitions.get(i);
                        if (getFree.getAddr() > recWork.getAddr()) {
                            freePartitions.add(i, newFree);
                            break;
                        } else {
                            freePartitions.add(newFree);
                        }
                    }
                }
            }*/
        }
        System.out.println("作业" + recWork.getName() + "回收完成");
    }

    private static int checkIndex(JCB recWork) {
        for (int i1 = 0; i1 < freePartitions.size(); i1++) {
            FreePartitions getFree = freePartitions.get(i1);
            if (getFree.getAddr() > recWork.getAddr())  //后面有空闲区域
                return i1;
            break;
        }
        return -1;
    }


    //分配作业
    private static void assignJobs() {
        imputWork();
        Iterator<JCB> workIt = workList.iterator();
        while (workIt.hasNext()) {
            JCB jcbFirst = workIt.next();
            assignWorkspaces(jcbFirst);
            workIt.remove();
            System.out.println("完成分配后空闲分区表");
            DisFp();
            System.out.println("已分配作业表");
            DisFinWorkList();
        }
        if (is == 0) {
            System.out.println("全部作业已经被分配内存!");
        } else {
            System.out.println("作业没有全部被内存分配");
        }
    }


    //将作业分区.
    private static void assignWorkspaces(JCB jcbNeedAssign) {
        Iterator<FreePartitions> it = freePartitions.iterator();
        while (it.hasNext()) {
            FreePartitions freePartition = it.next();
            if ((freePartition.getSize() > jcbNeedAssign.getSize()) && (freePartition.getState() == 'n')) {  //空闲分区大于作业空间
                jcbNeedAssign.setAddr(freePartition.getAddr());//记录作业的起始地址
                freePartition.setSize(freePartition.getSize() - jcbNeedAssign.getSize());//记录空闲分区的大小
                freePartition.setAddr(freePartition.getAddr() + jcbNeedAssign.getSize());//记录空闲分区的起始位置;
                flag = 1;//分配成功
                jcbNeedAssign.setState('r');//状态改变
                finishWorkList.add(jcbNeedAssign);//将分配好的作业加到完成作业集合
                System.out.println(jcbNeedAssign.getName() + "作业的分区为:");
                System.out.println("分区为:" + freePartition.getName() + "首地址:" + jcbNeedAssign.getAddr());
                break;
            } else if ((freePartition.getSize() == jcbNeedAssign.getSize()) && (freePartition.getState() == 'n')) { //空闲分区等于作业空间
                jcbNeedAssign.setAddr(freePartition.getAddr());
                flag = 1;
                finishWorkList.add(jcbNeedAssign);
                it.remove();//删除空闲分区
            } else {
                System.out.printf("作业%s长度过大,内存不足,分区分配出错\n", jcbNeedAssign.getName());
                is = 1;
            }
        }
    }

    //输入作业
    private static void imputWork() {
        System.out.println("输入作业数:");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            JCB jcb = new JCB();
            System.out.println("输入作业名");
            String workName = sc.next();
            jcb.setName(workName);
            System.out.println("输入作业大小");
            int workSize = sc.nextInt();
            jcb.setSize(workSize);
            jcb.setState('w');
            workList.add(jcb);
        }
/*        while (true) {
            JCB jcb = new JCB();
            System.out.println("输入作业名");
            String workName = sc.next();
            jcb.setName(workName);
            System.out.println("输入作业大小");
            int workSize = sc.nextInt();
            jcb.setSize(workSize);
            jcb.setState('w');
            workList.add(jcb);
            System.out.println("是否还要输入作业:y/n");
            String judgment = sc.next();
            if (judgment.equalsIgnoreCase("n")) {
                break;
            }
        }*/
    }

    //展示已完成分配作业表
    private static void DisFinWorkList() {
        System.out.println("作业名\t  首地址\t\t  长度\t\t  状态");
        for (JCB jcb : finishWorkList) {
            System.out.println(jcb.toString());
        }
    }

    //展示空闲分区表
    private static void DisFp() {
        System.out.println("分区\t  首地址\t\t  长度\t\t  状态");
        for (FreePartitions freePartitions : freePartitions) {
            System.out.println(freePartitions.toString());
        }
    }
}
