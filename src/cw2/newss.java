package cw2;

public class newss {
    public  static void main(String args[]){
//    String abc[]=new String[5];
//    abc[0]="ant";
//    abc[1]="bat";
//    abc[2]="cat";
//    abc[3]="fish";
//
//    System.out.println("0"+abc[0]);
//    System.out.println();
//    int i=0;
//
//    for(int j=0;j<4;j++){
//        System.out.println(i+" "+abc[j]);
//        System.out.println((i+1)+" "+abc[j+1]);
//        abc[j]=abc[j+1];
//        System.out.println();
//        i++;
//
//    }
//        System.out.println();
//        System.out.println(abc[0]);
//        System.out.println(3+abc[3]);
        Passenger[] waitingRoom = new Passenger[42];
        int waitingroomlenght=5;
        for (int j = 0; j < waitingroomlenght; j++) {
            waitingRoom[j] = waitingRoom[j + 1];
            System.out.println(waitingRoom[j]);
        }
}
    }
