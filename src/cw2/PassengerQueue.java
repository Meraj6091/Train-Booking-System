package cw2;


import javafx.scene.control.Alert;

public class PassengerQueue{

  private  Passenger[] trainqueue= new Passenger [21];

        private int first;
        private int  last;
        private int maxStayInQueue;
        private int maxLength;
        private String detail;
        private String detail2;
        private String detail3;
        private String detail4;
        private String detail5;


    public void add(Passenger passenger) {
        if(!isFull()) {
           trainqueue[last] = passenger;
            last =(last + 1)%21;
            maxLength=maxLength+1;
        }
        else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Queue is Full !");
            alert.show();
        }
    }

    public void display() {
        for(int i=0;i<maxLength;i++){
            //System.out.print(trainqueue[(first+i)]+" ");
            if(trainqueue[i] !=null){
                System.out.println(trainqueue[i].getname());
               // return trainqueue[i].getname();
            }
        }
        //return null;
    }

     public Passenger remove(){
         Passenger passenger = trainqueue[first];
         if(!isEmpty()){
             first=(first+1)%21;
             maxLength--;

        }
         else{
             System.out.println("Train Queue is Empty");
         }
         return passenger;
     }

    public int getMaxLength(){
        return maxLength;
    }

    public boolean isEmpty(){
        return getMaxLength()==0;
    }
    public boolean isFull(){
        return getMaxLength()==21;
    }


    public void seatno(){
        cw2.Passenger passenger=new Passenger();
        passenger.getSeatNo();
    }


   public void setpassengers(Integer loop){
        for(int i=0;i<maxLength;i++){
            if(trainqueue[i]!=null){
                if(i==loop){
                    String name=trainqueue[i].getFirstname();
                    String fullname=trainqueue[i].getname();
                    String seat=trainqueue[i].getSeatNo();
                    String sirname=trainqueue[i].getSirname();
                    detail=seat;
                    detail2=name;
                    detail3=fullname;
                    detail4=sirname;
                }
            }
        }
   }
   public String getsetpassengers(){
        return detail;
   }
   public String getseatno(){
        return detail2;
   }

    public String getDetail3() {
        return detail3;
    }

    public String getDetail4() {
        return detail4;
    }

    public Passenger[] getTrainqueue() {
        return trainqueue;
    }
    public void delete(){
        for(int i=0;i<maxLength;i++){
            trainqueue[i]=null;
            maxLength=first=last=0;
        }
    }
    public void setDetails(Integer i){
        this.detail2 = trainqueue[i].getFirstname();
        this.detail4 = trainqueue[i].getSirname();
        this.detail = trainqueue[i].getSeatNo();
        this.detail5= String.valueOf(trainqueue[i].getSecondsinQueue());

    }
    public String getDetails(){
        return detail2;
    }
    public String getseat(){
        return detail;
    }
    private String sirname(){
        return detail4;
    }

    public String getDetail5() {
        return detail5;
    }

    public String getDetail2() {
        return detail2;
    }

    public void setDetails2(Integer i){
        this.detail2 = trainqueue[i].getFirstname();
        this.detail4 = trainqueue[i].getSirname();
        this.detail = trainqueue[i].getSeatNo();
        this.detail5= String.valueOf(trainqueue[i].getSecondsinQueue());
    }


    public int getMaxStayInQueue() {
        return maxStayInQueue;
    }

    public void setMaxStayInQueue(int maxStayInQueue) {
        this.maxStayInQueue = maxStayInQueue;
    }
}