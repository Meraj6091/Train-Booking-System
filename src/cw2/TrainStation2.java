package cw2;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrainStation2 extends Application {

    private static PassengerQueue trainqueue=new PassengerQueue();
    private static Passenger[] waitingRoom = new Passenger[42];
    Stage stage2;

    public static void main(String args[]){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        HashMap<String,String>check=new HashMap<String, String>();
        HashMap<String,String>data4=new HashMap<String, String>();
        ArrayList<String>check2=new ArrayList<>();


        //load the database from cw1
        MongoClient mongoClient=new MongoClient("localhost",27017);
        MongoDatabase mongoDatabase=mongoClient.getDatabase("Trainseats");

        MongoCollection collection=mongoDatabase.getCollection("Colombo to Badulla");
        System.out.println("Connected Successfully");

        FindIterable<Document> findIterable=collection.find(); //find data in the collection
        for(Document data: findIterable){
            String name= data.getString("Name");
            String id= data.getString("ID");
            check.put(name,id); // name_sirname,seatno

        }
        System.out.println("------Colombo-Badulla TrainStation------\n");
        Stage stage=new Stage();
        HBox hBox=new HBox();
        Label label2=new Label("COLOMBO - BADULLA");
        label2.setStyle("-fx-background-color:#FFA07A");
        hBox.setPadding(new Insets(10,10,50,150)); //insert a new layout
        hBox.getChildren().addAll(label2);
        FlowPane flowPane=new FlowPane(hBox);
        flowPane.setVgap(20);
        flowPane.setHgap(20);
        for(HashMap.Entry<String,String>entry:check.entrySet()) {     // get the ketset of the hashmap
            Button button=new Button(entry.getKey());

            button.setMinHeight(40);
            button.setMaxHeight(40);
            button.setMinWidth(150);
            button.setMaxWidth(150);

            flowPane.getChildren().add(button);
           button.setMnemonicParsing(false);
            button.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                    check2.add(entry.getKey());
                    check2.add(entry.getValue());
                    button.setDisable(true);
                    button.setTextFill(Color.RED);
                   System.out.println(button.getText()+" "+"Successfully Added to the Waiting-Room");
                }
            });

        }
        Scene scene=new Scene(flowPane,400,500);
        stage.setScene(scene);
        stage.setTitle("Check In Colombo-Badulla");
        stage.showAndWait();

        waitingroom(check2,waitingRoom);

        while (true) {
            //menu:
            Scanner sc = new Scanner(System.in);
            System.out.println("\nPress A to add a passenger to the trainQueue: ");
            System.out.println("Press V to view the trainQueue:");
            System.out.println("Press D to Delete passenger from the trainQueue:");
            System.out.println("Press S to Store trainQueue data into a plain text file:");
            System.out.println("Press L to Load data back from the file into the trainQueue:");
            System.out.println("Press R to Run the simulation and produce report:");
            System.out.println("Press Q to Exit the Programme:");
            System.out.println("-> Enter Your Choice:");
            String input = sc.next();
            switch (input) {
                case ("A"):
                case ("a"):
                    add(waitingRoom,trainqueue);
                    break;
                case ("V"):
                case ("v"):
                    view(data4,trainqueue);
                    break;
                case ("D"):
                case ("d"):
                    delete();
                    break;
                case ("S"):
                case ("s"):
                    store();
                    break;
                case ("L"):
                case ("l"):
                    load();
                    break;
                case ("R"):
                case ("r"):
                    run();
                    break;
                case ("Q"):
                case ("q"):
                    quit();
                    break;
                default:
                    System.out.println("Invalid Input\n");

            }
        }


    }

    private void quit() {
        System.exit(0);   //terminate the programme

    }

    private void run() {

        Stage stage=new Stage();
        TableView tableView = new TableView();  //create a TableView

        TableColumn<String, Passengers> column1 = new TableColumn<>("First Name"); // create columns
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        TableColumn<String,Passengers> column2 = new TableColumn<>("SirName");
        column2.setCellValueFactory(new PropertyValueFactory<>("SirName"));

        TableColumn<String,Passengers> column3 = new TableColumn<>("WaitedTime");
        column3.setCellValueFactory(new PropertyValueFactory<>("WaitedTime"));

        TableColumn<String,Passengers> column4 = new TableColumn<>("SeatNo");
        column4.setCellValueFactory(new PropertyValueFactory<>("SeatNo"));

        TableColumn<String,Passengers> column5 = new TableColumn<>("AvgWaitedTime");
        column5.setCellValueFactory(new PropertyValueFactory<>("AvgWaitedTime"));

        tableView.getColumns().add(column1); //add columns
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);

        HBox hBox=new HBox();

        javafx.scene.control.Label label=new javafx.scene.control.Label("Maximum length of the queue :");
        label.setTextFill(Color.BLACK);
        label.setStyle("-fx-background-color:#FFA07A");


        Text text=new Text(String.valueOf(trainqueue.getTrainqueue().length));
        javafx.scene.control.Label label2=new Label("Maximum length of the queue attained :");
        label2.setTextFill(Color.BLACK);
        label2.setStyle("-fx-background-color:#FFA07A");
        Text text1=new Text(String.valueOf(trainqueue.getMaxLength()));

        hBox.setPadding(new Insets(10,10,10,10)); //insert a new layout
        hBox.setSpacing(10);
        hBox.getChildren().addAll(label,text,label2,text1);


        for(int i=0;i<trainqueue.getMaxLength();i++) {
            trainqueue.setDetails2(i);

            Float time= Float.valueOf(trainqueue.getDetail5());
            String avg= String.valueOf((time/trainqueue.getMaxLength()));
            tableView.getItems().add(new Passengers(trainqueue.getDetails(),trainqueue.getDetail4(), trainqueue.getDetail5() + " " + "Minutes", trainqueue.getseat(), avg + " " + "Minutes"));
        }
        VBox vbox = new VBox(tableView,hBox);
        Scene scene = new Scene(vbox,560,500);
        stage.setScene(scene);
        stage.setTitle("Simulation Report");
        stage.showAndWait();
        System.out.println("DONE");


        Formatter x = new Formatter();  //creating a new formatter

        try {
            x = new Formatter("Simulation Report.txt");
            String lenght= String.valueOf(trainqueue.getTrainqueue().length);
            x.format("Maxlength"+"="+lenght+"   "+"Maximum length of the queue attained"+"="+trainqueue.getMaxLength());


            for(int i=0;i<trainqueue.getMaxLength();i++) {   //insert data into the formatter
                trainqueue.setDetails2(i);
                Float time= Float.valueOf(trainqueue.getDetail5());
                String avg= String.valueOf((time/trainqueue.getMaxLength()));
                x.format("\nFirstname= " + trainqueue.getDetails() + ", " + "Sirname= " + trainqueue.getDetail4() + ", " + "WaitedTime= " + trainqueue.getDetail5() + " " + "Minutes" + ", " + "SeatNo= " + trainqueue.getseat() + ", " + "AvgWaitedTime= " + avg + "\n");
            }

            x.close();   //close the formatter

        }
        catch (Exception e) {
            System.out.println("error");
        }

        trainqueue.remove();

    }
    public class Passengers { //for TableView

        private String firstName;
        private String SirName;
        private String WaitedTime;
        private String SeatNo;
        private String AvgWaitedTime;

        public Passengers(String firstName, String SirName, String first2, String second2, String avg) {
            this.firstName = firstName;
            this.SirName = SirName;
            this.WaitedTime=first2;
            this.SeatNo=second2;
            this.AvgWaitedTime=avg;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getSirName() {
            return SirName;
        }

        public void setSirNameName(String sirName) {
            this.SirName = SirName;
        }

        public String getSeatNo() {
            return SeatNo;
        }

        public void setSeatNo(String seatNo) {
            SeatNo = seatNo;
        }

        public String getAvgWaitedTime() {
            return AvgWaitedTime;
        }

        public String getWaitedTime() {
            return WaitedTime;
        }

        public void setAvgWaitedTime(String avgWaitedTime) {
            AvgWaitedTime = avgWaitedTime;
        }

        public void setWaitedTime(String waitedTime) {
            WaitedTime = waitedTime;
        }
    }

    private void load() {
        MongoClient mongoClient=new MongoClient("localhost",27017);
        MongoDatabase mongoDatabase=mongoClient.getDatabase("Trainseats2");

        MongoCollection collection=mongoDatabase.getCollection("Colombo to Badulla");
        System.out.println("Connected Successfully");

        FindIterable<Document>findIterable=collection.find(); //find data in the collection
        for(Document data: findIterable){
            String name= data.getString("Name");
            String sirname= data.getString("Sirname");
            Integer id= Integer.valueOf(data.getString("SeatNo"));
            String time= String.valueOf(data.getString("Waited-Time"));
            Passenger passenger=new Passenger();
            passenger.setName(name,sirname);    //insert the data into specific objects
            passenger.setSeatNo(id);
            String[] arrOfStr = time.split(" ");
            String first = arrOfStr[0];
            passenger.setSecondsinQueue(Integer.valueOf(first));
            trainqueue.add(passenger);
        }

    }

    private void store() {
        MongoClient mongoClient=new MongoClient("localhost",27017);

        MongoDatabase mongoDatabase=mongoClient.getDatabase("Trainseats2"); //connect to the database

        MongoCollection collection=mongoDatabase.getCollection("Colombo to Badulla"); //get a collection
        System.out.println("Connected Successfully");

        BasicDBObject basicDBObject=new BasicDBObject();
        collection.deleteMany(basicDBObject);

        Document document=new Document();

        for(int i=0;i<trainqueue.getMaxLength();i++){
            trainqueue.setDetails(i);
            document.append("Name",trainqueue.getDetail2());
            document.append("Sirname",trainqueue.getDetail4());
            document.append("SeatNo",trainqueue.getseat());
            document.append("Waited-Time",trainqueue.getDetail5()+" "+"Minutes");
            Float time= Float.valueOf(trainqueue.getDetail5());
            String avg= String.valueOf((time/trainqueue.getMaxLength()));
            document.append("AvgWaited-Time",avg+" "+"Minutes");
            collection.insertOne(document);
            document.clear();
        }


    }

    private void delete() {
        if (trainqueue.getMaxLength() == 0) {
            System.out.println("Trainqueue Is Empty");
        }
        else {
            trainqueue.display();
            Scanner sc = new Scanner(System.in);
            System.out.println("\nEnter The First Name:");
            String name = sc.next();
            System.out.println("Enter The Sirname:");
            String name2 = sc.next();
            String fname = (name + "_" + name2);

            HashMap<String, String> delete = new HashMap<String, String>();
            for (int i = 0; i < trainqueue.getMaxLength(); i++) {
                trainqueue.setDetails(i);
                String name1 = trainqueue.getDetails();
                String seat = trainqueue.getseat();
                String sirname = trainqueue.getDetail4();
                String time = trainqueue.getDetail5();
                delete.put(name1 + "_" + sirname, seat + "_" + time);

            }


            if (delete.containsKey(fname.toLowerCase())) {
                trainqueue.delete();
                delete.remove(fname.toLowerCase());
                for (HashMap.Entry<String, String> entry : delete.entrySet()) {
                    String str = entry.getKey();
                    String[] arrOfStr = str.split("_");     //split the value and put it into a string list
                    String first = arrOfStr[0];                    //get the value from the index
                    String sirname = arrOfStr[1];                  //get the value from the index

                    String str2 = entry.getValue();
                    String[] arrOfStr2 = str2.split("_");
                    String seat = arrOfStr2[0];
                    String time = arrOfStr2[1];
                    Passenger passenger1 = new Passenger();
                    passenger1.setName(first, sirname);
                    passenger1.setSeatNo(Integer.valueOf(seat));
                    passenger1.setSecondsinQueue(Integer.valueOf(time));
                    trainqueue.add(passenger1);

                }
                System.out.println("DONE");
                trainqueue.display();

            } else {
                System.out.println("Invalid input");
            }


        }
    }
    private void view(HashMap<String, String> data4, PassengerQueue trainqueue) {
            data4.clear();
        for(int i=0;i<trainqueue.getMaxLength();i++){
            trainqueue.setDetails(i);
            data4.put(trainqueue.getseat(),trainqueue.getDetail2());

        }
        System.out.println(data4);
        Stage stage3=new Stage();
        FlowPane layout2=new FlowPane();
        layout2.setHgap(20);
        layout2.setVgap(20);
        for(int i=0;i<42;i++){
            Button button3=new Button(String.valueOf(i + 1));
            button3.setMinWidth(100);
            button3.setMaxWidth(100);
            layout2.getChildren().add(button3);
            String asas=button3.getText();
            if(data4.containsKey(asas)){
                String str = data4.get(asas);
                String[] arrOfStr = str.split("_");
                String first=arrOfStr[0];
                button3.setText(first);
                button3.setTextFill(Color.RED);
            }

        }
        Scene scene2=new Scene(layout2,500,550);
        stage3.setTitle("TrainQueue");
        stage3.setScene(scene2);
        stage3.showAndWait();

    }

    public int waitingroomlenght=0;

    public void waitingroom(ArrayList<String> check2, Passenger[] waitingRoom) {

        ArrayList<String>custname=new ArrayList<String>();
        ArrayList<Integer>custno=new ArrayList<Integer>();

        for(int i=0;i<check2.size();i++){
            if(i%2==0){
                custname.add(check2.get(i));
            }
            else {
                custno.add(Integer.valueOf(check2.get(i)));
            }

        }


        for(int i=0;i<custname.size();i++){
            Integer seatno=custno.get(i);
            String fullname=custname.get(i);
                Random dice = new Random();
                int number;
                number = 1 + dice.nextInt(18);
                int[]maxandmin={number};
            Arrays. sort(maxandmin);
            Integer max=maxandmin[0];
            Integer min=maxandmin[maxandmin.length-1];
            String[] arrOfStr = fullname.split("_");
            String first = arrOfStr[0]; //get the value from the index
            String second = arrOfStr[1];
            Passenger passenger=new Passenger();
            passenger.setSeatNo(seatno);
            passenger.setName(first,second);
            passenger.setSecondsinQueue(number);
            waitingRoom[i]=passenger;
            trainqueue.setMaxStayInQueue(max);
            waitingroomlenght++;

        }



    }
    private void add(Passenger[] waitingRoom, PassengerQueue trainqueue) {

        Stage stage=new Stage();
        HBox hBox=new HBox();
        Label label2=new Label("WAITING-ROOM");
        label2.setStyle("-fx-background-color:#FFA07A");
        Button button=new Button("ADD");
        button.setMinHeight(30);
        button.setMaxHeight(30);
        button.setMinWidth(90);
        button.setMaxWidth(90);
        hBox.setPadding(new Insets(10,10,50,10)); //insert a new layout
        hBox.setSpacing(180);
        hBox.getChildren().addAll(label2,button);
        FlowPane flowPane=new FlowPane(hBox);
        flowPane.setVgap(20);
        flowPane.setHgap(20);

        for(int i=0;i<waitingroomlenght;i++){

           Button btn=new Button(waitingRoom[i].getname2());
            btn.setMinHeight(40);
            btn.setMaxHeight(40);
            btn.setMinWidth(150);
            btn.setMaxWidth(150);
            flowPane.getChildren().add(btn);
            btn.setMnemonicParsing(false);

        }

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                button.setTextFill(Color.RED);

                Random dice = new Random();
                int number;
                number = 1 + dice.nextInt(6);
                System.out.println("Random Number ="+number);
                if (number <= waitingroomlenght) {
                    for (int i = 0; i < number; i++) {
                        trainqueue.add(waitingRoom[0]);
                        for (int j = 0; j < waitingroomlenght; j++) {
                            waitingRoom[j] = waitingRoom[j + 1];
                        }
                        waitingroomlenght--;
                    }
                    System.out.println("Successfully added to the TrainQueue ");
                    trainqueuegui(trainqueue);
                }
                else {
                    if(waitingroomlenght == 0){
                        System.out.println("Waiting-Room is Empty");
                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Please Add Passengers First !");
                        alert.showAndWait();
                        stage.close();
                        stage2.close();

                    }
                    else {
                        System.out.println("Successfully added to the TrainQueue ");
                        int x = waitingroomlenght;
                        for (int i = 0; i < x; i++) {
                            trainqueue.add(waitingRoom[0]);
                            for (int j = 0; j < waitingroomlenght; j++) {
                                waitingRoom[j] = waitingRoom[j + 1];
                            }
                            waitingroomlenght--;
                        }
                        trainqueuegui(trainqueue);

                    }
                }


            }
        });
        Scene scene=new Scene(flowPane,400,500);
        stage.setScene(scene);
        stage.setTitle("Waiting Room");
        stage.showAndWait();


    }

    private void trainqueuegui(PassengerQueue trainqueue) {

        stage2=new Stage();
        HBox hBox=new HBox();
        Label label2=new Label("TRAIN-QUEUE");
        label2.setStyle("-fx-background-color:#FFA07A");
        hBox.setPadding(new Insets(10,10,50,170)); //insert a new layout
        hBox.getChildren().addAll(label2);
        VBox vBox=new VBox(hBox);
        vBox.setSpacing(20);
       // flow.setHgap(20);
        for(int i=0;i<trainqueue.getMaxLength();i++){

            Button btn=new Button(trainqueue.getTrainqueue()[i].getname());
            btn.setMinHeight(30);
            btn.setMaxHeight(30);
            btn.setMinWidth(150);
            btn.setMaxWidth(150);
            vBox.getChildren().add(btn);
            btn.setMnemonicParsing(false);

        }

        Scene scene2=new Scene(vBox,400,500);
        stage2.setScene(scene2);
        stage2.setTitle("TrainQueue");
        stage2.showAndWait();

    }
}