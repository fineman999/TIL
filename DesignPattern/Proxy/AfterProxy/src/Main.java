public class Main {
    public static void main(String[] args){
        Cat cat = new Cat();
        Mouse mouse = new Mouse();
        DoorManProxy doorMan = new DoorManProxy(new DoorMan());
        doorMan.kickOut(mouse);
        doorMan.kickOut(cat);
    }
}
// 결과값
//    Get rid of the mouse
//    Get rid of the cat
