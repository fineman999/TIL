public class Main {
    public static void main(String[] args){
        Mouse mouse = new Mouse();
        Cat cat = new Cat();
        DoorMan doorMan = new DoorMan();
        doorMan.kickOut(mouse);
        doorMan.kickOutCat(cat);
    }
}
// 결과값
//    Get rid of the mouse
//    Get rid of the cat
