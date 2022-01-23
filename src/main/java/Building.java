import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Building {

    private static final Long BuildingHeight = 5L;

    //도착한 사람
    public static List<User> result = new ArrayList<>();

    //대기하는 사람
    public static List<User> waitUserList = new ArrayList<>();





    //엘리베이터
    private static Elevator elevator;


    public static void main(String[] args) throws InterruptedException {
        setting();

        //2개의 쓰레드가 동시에 하나의 레퍼런스에 접근하고 있음
        for(int i = 0; i < 2; i ++) {
            new Thread(new MyThread()).start();
        }

    }

    private static void setting() {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            User user = new User(
                    Long.valueOf(rand.nextInt(4) + 2),
                    Long.valueOf(rand.nextInt(50) + 40),
                    "User" + i
            );

            Long startFloor;

            while(true){
                startFloor =  Long.valueOf(rand.nextInt(5) + 1);
                //startFloor = 1L;
                if(!user.getDestination().equals(startFloor))
                    break;
            }

            user.setStartFloor(startFloor);

            waitUserList.add(user);
        }

        for (User user : waitUserList) {
            System.out.println("=====대기명단======");
            System.out.println("이름 = " + user.getName());
            System.out.println("무게 = " + user.getWeight());
            System.out.println("시작 층 = " + user.getStartFloor());
            System.out.println("목적지 층 = " + user.getDestination());
        }
    }

}
