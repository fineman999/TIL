public class DoorManProxy {
    private DoorMan doorMan;

    public DoorManProxy(DoorMan doorMan) {
        this.doorMan = doorMan;
    }
    public void kickOut(Animal animal){
        System.out.println("Hi!!, " + animal.getName());
        doorMan.kickOut(animal);
    }
}
