

public enum DoorStatus {
    OPEN(Boolean.TRUE),
    CLOSE(Boolean.FALSE);

    private Boolean state;

    DoorStatus(Boolean state) {
        this.state = state;
    }
}
