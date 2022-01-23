public class User {
    //목적지
    private Long destination;

    //시작 층
    private Long startFloor;

    //탑승 시각
    private Long takeInTime;

    //하선 시간
    private Long takeOffTime;

    // 사람 무게
    private Long weight;
    //이름
    private String name;
    public static synchronized boolean takeInElevator(User user, Long startTime) {
        if (Building.waitUserList.contains(user)) {
            user.setTakeInTime(startTime);
            Building.waitUserList.remove(user);
            return true;
        }
        else
            return false;
    }


    public User(Long destination, Long weight, String name) {
        this.destination = destination;
        this.weight = weight;
        this.name = name;
    }



    public Long getDestination() {
        return destination;
    }


    public Long getStartFloor() {
        return startFloor;
    }


    public Long getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public void setDestination(Long destination) {
        this.destination = destination;
    }


    public void setStartFloor(Long startFloor) {
        this.startFloor = startFloor;
    }

    public void setTakeInTime(Long takeInTime) {
        this.takeInTime = takeInTime;
    }

    public void setTakeOffTime(Long takeOffTime) {
        this.takeOffTime = takeOffTime;
    }

    public Long getTakeInTime() {
        return takeInTime;
    }

    public Long getTakeOffTime() {
        return takeOffTime;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public void setName(String name) {
        this.name = name;
    }
}
