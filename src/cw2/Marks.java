package cw2;

public class Marks {
  private String MarksAss01;
  private int MarksAss02;
  private int Design;

    public int getMarksAss01() {
        return Integer.parseInt(MarksAss01);
    }

    public void setMarksAss01(String marksAss01) {
        MarksAss01 = marksAss01;
    }

    public int getMarksAss02() {
        return MarksAss02;
    }

    public void setMarksAss02(int marksAss02) {
        MarksAss02 = marksAss02;
    }

    public int getDesign() {
        return Design;
    }

    public void setDesign(int design) {
        Design = design;
    }

    public int getImplement() {
        return Implement;
    }

    public void setImplement(int implement) {
        Implement = implement;
    }

    public int getFinal_exam() {
        return Final_exam;
    }

    public void setFinal_exam(int final_exam) {
        Final_exam = final_exam;
    }

    private int Implement;
  private int Final_exam;


}
