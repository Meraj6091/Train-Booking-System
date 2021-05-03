package cw2;

public class Passenger extends Object{

    private String Firstname;
    private String Sirname;
    private Integer secondsinQueue;
    private Integer SeatNo;


    public void setFirstname(String firstname) {
        Firstname = firstname;

    }

    public void setSirname(String sirname) {
        Sirname = sirname;
    }


    public void setSecondsinQueue(Integer secondsinQueue) {
        this.secondsinQueue = secondsinQueue;
    }

    public void setSeatNo(Integer seatNo) {
        SeatNo = seatNo;
    }

    public String getSeatNo() {
        return String.valueOf(SeatNo);
    }

    public String getFirstname() {
        return Firstname;
    }

    public String getSirname() {
        return Sirname;
    }

    public void setName(String first, String second) {
        this.Firstname=first;
        this.Sirname=second;
    }
    public String getname(){
        return (Firstname+" "+Sirname+"="+SeatNo);
    }
    public String getname2(){
        return (Firstname+" "+Sirname);
    }

    public Integer getSecondsinQueue() {
        return secondsinQueue;
    }
}




