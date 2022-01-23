public enum ElevatorState
{
    STOP("STOP"),
    UP_FLOOR("UP_FLOOR"),
    DOWN_FLOOR("DOWN_FLOOR");


    private String state;

    ElevatorState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
