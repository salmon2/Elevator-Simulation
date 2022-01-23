
class MyThread implements Runnable{
    Elevator elevator;

    public MyThread() {
        this.elevator = new Elevator();
    }
    @Override
    public void run() {
        try {
            elevator.startElevator();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
