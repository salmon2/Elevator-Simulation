import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.currentThread;

public class Elevator {

    //탑승하고 있는 유저
    private List<User> takeInUserList = new ArrayList<>();
    //현재 엘리베이터 무게 default = 0
    private Long currentWeight = 0L;
    // 현재 엘리베이터 위치 default = 1(층)
    private Long currentFloor = 1L;

    // 엘리베이터의 최종 목적지
    private Long destination;

    // 엘리베이터 상태, STOP, UP_FLOOR, DOWN_FLOOR;
    private ElevatorState elevatorState = ElevatorState.STOP;

    // 엘리베이터의 문 상태
    private DoorStatus doorStatus = DoorStatus.CLOSE;

    // 엘리베이터의 속도
    private final Long speed = 1L;

    // 최대 무게
    private final Long maxWeight = 1000L;

    // 엘리베이터가 목적지까지 가는데 걸리는 시간
    private Long operationTime = 0L;



    public List<User> getTakeInUserList() {
        return takeInUserList;
    }

    public Long getCurrentWeight() {
        return currentWeight;
    }

    public Long getCurrentFloor() {
        return currentFloor;
    }

    public Long getDestination() {
        return destination;
    }

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public DoorStatus getDoorStatus() {
        return doorStatus;
    }

    public Long getSpeed() {
        return speed;
    }

    public Long getMaxWeight() {
        return maxWeight;
    }

    public Long getOperationTime() {
        return operationTime;
    }

    //엘리베이터 작동 시작
    public void startElevator() throws InterruptedException {
        int userWait = Building.waitUserList.size();

        //탑승할 유저가 없을때까지 작동
        while(userWait != Building.result.size()){
            //탑승하고 있는 유저, 목적지에 맞다면 내리기
            takeOffElevator();
            //해당 층에서 기다리는 유저가 있다면 탑승하기
            takeInElevator();

            //탑승 시 목적지 선택
            if(takeInUserList.size() != 0){
                destinationSet();
            }
            //탑승 인원이 없다면 다른 층의 탑승 고객 찾기
            else {
                destiantionSetIfNotInUser();
            }
            goDestination();
        }
    }



    private void goDestination() throws InterruptedException {
        while(currentFloor != destination){

            if(this.elevatorState.equals(ElevatorState.UP_FLOOR)){
                Thread.sleep(1000);
                operationTime += 1L;
                currentFloor += speed;
            }
            else if(this.elevatorState.equals(ElevatorState.DOWN_FLOOR)){
                operationTime += 1L;
                currentFloor -= speed;
            }

            //매 층마다 탈사람 내릴사람 보내기
            takeOffElevator();
            takeInElevator();
        }
        //도착했으면 stop 모드로 바꾸기
        elevatorState = ElevatorState.STOP;
    }

    private void destiantionSetIfNotInUser() {
        if(Building.waitUserList.size()!= 0){
            destination = Building.waitUserList.get(0).getStartFloor();
            elevatorState = destination - currentFloor > 0 ? ElevatorState.UP_FLOOR : ElevatorState.DOWN_FLOOR ;
        }
    }

    private void destinationSet() {
        Long[] tmpArr = new Long[takeInUserList.size()];

        for(int q = 0 ; q < tmpArr.length ; q++)
            tmpArr[q] = takeInUserList.get(q).getDestination();

        Arrays.sort(tmpArr);

        //목적지 정하기
        if(this.elevatorState == ElevatorState.UP_FLOOR)
            this.destination = tmpArr[0];
        else
            this.destination = tmpArr[tmpArr.length-1];

        //목적지에 따라 위로 갈지 아래로 갈지 정하기
        if(destination > currentFloor)
            elevatorState = ElevatorState.UP_FLOOR;
        else
            elevatorState = ElevatorState.DOWN_FLOOR;
    }

    public synchronized void takeInElevator() throws InterruptedException {

        for (int i = 0; i< Building.waitUserList.size(); i++) {
            if(maxWeight < currentWeight)
                break;
            User mainUser = Building.waitUserList.get(i);

            ElevatorState userState
            = mainUser.getDestination() - currentFloor > 0 ? ElevatorState.UP_FLOOR : ElevatorState.DOWN_FLOOR;

            if(mainUser.getStartFloor().equals(currentFloor) && (userState.equals(elevatorState) || elevatorState.equals(ElevatorState.STOP))){
                if(User.takeInElevator(mainUser, operationTime)){
                    doorStatus = DoorStatus.OPEN;
                    takeInUserList.add(mainUser);
                    currentWeight += mainUser.getWeight();
                    Building.waitUserList.remove(mainUser);
                }
                else
                    break;

                printStatus(mainUser, "======사용자 탑승", "탑승 목적지 = ", mainUser.getStartFloor(), "하차 목적지 = ", mainUser.getDestination(), "현재 목적지 = ", currentFloor);

                i = i -1;
            }

        }
        doorStatus = DoorStatus.CLOSE;
    }

    private void printStatus(User mainUser, String s, String s2, Long startFloor, String s3, Long destination, String s4, Long currentFloor) {
        System.out.println(s);
        System.out.println("currentThread().getName() = " + currentThread().getName());
        System.out.println("user = " + mainUser.getName());
        System.out.println(s2 + startFloor);
        System.out.println(s3 + destination);
        System.out.println(s4 + currentFloor);
    }

    public synchronized void takeOffElevator() throws InterruptedException {
        for (int i = 0; i< takeInUserList.size(); i++) {
            User user = takeInUserList.get(i);
            if(user.getDestination().equals(currentFloor) ){

                doorStatus = DoorStatus.OPEN;
                user.setTakeOffTime(operationTime);

                Building.result.add(user);

                currentWeight -= user.getWeight();
                takeInUserList.remove(user);

                printStatus(user, "======사용자 목적지 도착", "소요시간 = ", getTime(user), "시작 목적지 = ", user.getStartFloor(), "내리는 목적지 = ", user.getDestination());
                System.out.println("현재 목적지 = " + currentFloor);

                i--;
            }

        }
        doorStatus = DoorStatus.CLOSE;
    }

    private long getTime(User user) {
        return user.getTakeOffTime() - user.getTakeInTime();
    }



}
