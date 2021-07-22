public abstract class PhoneScreen {
    // width of screen is 48
    // height of screen is 30

    public static final int PHONE_WIDTH = 48;
    public static final int PHONE_HEIGHT = 30;

    public void printHomeScreenTop(){
        System.out.print("┌");
        for(int i=0; i<PHONE_WIDTH; i++){
            System.out.print("─");
        }
        System.out.println("┐");
    }

    public void printHomeScreenBottom(){
        System.out.print("└");
        for(int i=0; i<PHONE_WIDTH; i++){
            System.out.print("─");
        }
        System.out.println("┘");
    }

    public void printHomeScreenVertical(){
        for(int i=0; i<PHONE_HEIGHT; i++){
            System.out.print("│");
            for(int j=0; j<PHONE_WIDTH; j++){
                System.out.print(" ");
            }
            System.out.println("│");
        }
    }
}
