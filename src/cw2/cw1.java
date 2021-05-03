package cw2;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.bson.Document;

import java.lang.*;
import java.util.*;

public class cw1<x> extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        HashMap<String, String> seatsdata = new HashMap<String, String>();
        HashMap<String, String> seatsdata2 = new HashMap<String, String>();
        HashMap<String, String> seatsdata3 = new HashMap<String, String>();
        HashMap<String, String> seatsdata4 = new HashMap<String, String>();
        HashMap<String, String> seatsdata5 = new HashMap<String, String>();
        HashMap<String, String> seatsdata6 = new HashMap<String, String>();

        // Menu:
        while (true) {
            menu:
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter A to add customers:");
            System.out.println("Enter V to view all seats:");
            System.out.println("Enter E to Display Empty seats:");
            System.out.println("Enter F to Find the seat for a given customers name:");
            System.out.println("Enter D to Delete Customers:");
            System.out.println("Enter S to Store program data in to file:");
            System.out.println("Enter L to Load program data from file:");
            System.out.println("Enter O to View seats Ordered alphabetically by name:");
            System.out.println("Enter Q to exit:");
            System.out.println("Enter your choice:");
            String x = sc.next();

            if (x.equals("v") || x.equals("V")) {
                view(seatsdata,seatsdata2);

            } else if (x.equals("q") || x.equals("Q")) {
                quit();

            } else if (x.equals("a") || x.equals("A")) {
                add(seatsdata,seatsdata2,seatsdata3,seatsdata4,seatsdata5,seatsdata6);

            } else if (x.equals("d") || x.equals("D")) {
                delete(seatsdata,seatsdata2,seatsdata3,seatsdata4);

            } else if (x.equals("e") || x.equals("E")) {
                Empty(seatsdata,seatsdata2);

            } else if (x.equals("f") || x.equals("F")) {
                find(seatsdata,seatsdata2,seatsdata3,seatsdata4);

            } else if (x.equals("s") || x.equals("S")) {
                Store(seatsdata,seatsdata2,seatsdata5,seatsdata6);


            } else if (x.equals("l") || x.equals("L")) {
                Load(seatsdata,seatsdata2);

            } else if (x.equals("o") || x.equals("O")) {
                sort(seatsdata,seatsdata2);

            } else {
                System.out.println("Invalid Input\n");
            }


        }

    }
    //Load the Data from Database
    private void Load(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2) {

        MongoClient mongoClient=new MongoClient("localhost",27017);
        MongoDatabase mongoDatabase=mongoClient.getDatabase("Trainseats");

        MongoCollection collection=mongoDatabase.getCollection("Colombo to Badulla");
        MongoCollection collection2=mongoDatabase.getCollection("Badulla to Colombo");
        System.out.println("Connected Successfully");

        FindIterable<Document>findIterable=collection.find(); //find data in the collection
        for(Document data: findIterable){
            String name= data.getString("Name");
            String[] arrOfStr = name.split("_");
            String first = arrOfStr[0];
            String second = arrOfStr[1];
            String id= data.getString("ID");
            seatsdata.put(first,id);  //import data into hashmap
            System.out.println(first+"="+id);
        }

        FindIterable<Document>findIterable2=collection2.find();
        for(Document data: findIterable2){
            String name= data.getString("Name");
            String id= data.getString("ID");
            seatsdata2.put(name,id);
            System.out.println(name+"="+id);

        }


    }
    //Store the booking details in to MongoDB Database
    private void Store(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2, HashMap<String, String> seatsdata5, HashMap<String, String> seatsdata6) {

        MongoClient mongoClient=new MongoClient("localhost",27017);

        MongoDatabase mongoDatabase=mongoClient.getDatabase("Trainseats"); //connect to the database

        MongoCollection collection=mongoDatabase.getCollection("Colombo to Badulla"); //get a collection
        MongoCollection collection2=mongoDatabase.getCollection("Badulla to Colombo");

        System.out.println("Connected Successfully");
        BasicDBObject basicDBObject=new BasicDBObject();
        collection.deleteMany(basicDBObject);
        collection2.deleteMany(basicDBObject);


        Document document=new Document();
        Document document2=new Document();


        for(HashMap.Entry<String,String>entry:seatsdata6.entrySet()){
            document.append("Name", entry.getKey()); //inset data into the document
            document.append("ID", entry.getValue());
            collection.insertOne(document); //insert the Document into the collection
            document.clear();
        }


        for(HashMap.Entry<String,String>entry:seatsdata2.entrySet()) {

            document2.append("Name", entry.getKey());
            document2.append("ID", entry.getValue());
            collection2.insertOne(document2);
            document2.clear();

        }

    }

    //find Seats For A Given Customers Name
    private void find(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2, HashMap<String, String> seatsdata3, HashMap<String, String> seatsdata4) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Your Name:");
        String name1 = sc.next();
        if (seatsdata.containsKey(name1)&&seatsdata3.containsKey(name1)) {
            String seat = seatsdata.get(name1);
            System.out.println("Colombo to Badulla");
            System.out.println("Seat no= "+ seat);
            String date=seatsdata3.get(name1);
            System.out.println("date= "+date+"\n");
        }

        if(seatsdata2.containsKey(name1)&&seatsdata4.containsKey(name1)){
            String seat2=seatsdata2.get(name1);
            System.out.println("Badulla to Colombo");
            System.out.println("Seat no= "+ seat2);
            String date1=seatsdata4.get(name1);
            System.out.println("date= "+date1+"\n");
        }

        else {
            System.out.println("Invalid Name");
        }

    }

    //view Empty seats
    private void Empty(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 1 to view Empty seats from Colombo to Badulla");
        System.out.println("Press 2 to view Empty seats from Badulla to Colombo");
        Integer empty = sc.nextInt();
        if (empty.equals(1)){  //if the user enter "1" display the Colombo to Badulla booking system
            Stage stage = new Stage();
            FlowPane layout1 = new FlowPane();
            for (int k = 0; k < 42; k++) {
                Button buttons = new Button(String.valueOf(k + 1));
                layout1.getChildren().add(buttons);
                buttons.setId(Integer.toString(k));
                buttons.setMinHeight(50);
                buttons.setMaxHeight(50);
                buttons.setMinWidth(50);
                buttons.setMaxWidth(50);
                buttons.setTextFill(Color.GREEN);
                if (seatsdata.containsValue(buttons.getText())) {
                    buttons.setVisible(false);
                } else {
                    buttons.setVisible(true);
                }

            }
            Button menu=new Button("Menu");
            menu.setTextFill(Color.RED);
            menu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            layout1.getChildren().add(menu);

            Scene scene1 = new Scene(layout1, 500, 480);
            layout1.setHgap(20);
            layout1.setVgap(20);
            stage.setScene(scene1);
            stage.setTitle("Colombo to Badulla");
            stage.showAndWait();

        }
        if(empty.equals(2)){
            Stage stage = new Stage();
            FlowPane layout1 = new FlowPane();
            for (int k = 0; k < 42; k++) {
                Button  buttons = new Button(String.valueOf(k + 1));
                layout1.getChildren().add(buttons);
                buttons.setId(Integer.toString(k));
                buttons.setMinHeight(50);
                buttons.setMaxHeight(50);
                buttons.setMinWidth(50);
                buttons.setMaxWidth(50);
                buttons.setTextFill(Color.GREEN);
                if (seatsdata2.containsValue(buttons.getText())) {
                    buttons.setVisible(false); //set the buttons visibility off
                } else {
                    buttons.setVisible(true);
                }


            }
            Button menu=new Button("Menu");
            menu.setTextFill(Color.RED);
            menu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            layout1.getChildren().add(menu);

            Scene scene1 = new Scene(layout1, 500, 480);
            layout1.setHgap(20);
            layout1.setVgap(20);
            stage.setScene(scene1);
            stage.setTitle("Badulla to Colombo");
            stage.showAndWait();

        }

    }
    // Delete customers from seats
    private void delete(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2, HashMap<String, String> seatsdata3, HashMap<String, String> seatsdata4) {
        Scanner sc1=new Scanner(System.in);
        System.out.println("Enter 1 to Delete seats form Colombo to Badulla");
        System.out.println("Enter 2 to Delete seats form Badulla to Colombo");
        Integer delete=sc1.nextInt();
        if(delete.equals(1)){
            Scanner sc=new Scanner(System.in);
            System.out.println("Enter Your Name:");
            String name2=sc.next();
            System.out.println("Enter the Date");
            String date=sc.next();
            if(seatsdata.containsKey(name2)&&seatsdata3.containsValue(date)){
                seatsdata.remove(name2);
                seatsdata3.remove(date);
                System.out.println("DONE");
                System.out.println(seatsdata);
            }


            else{
                System.out.println("There are no any customers according to your input ");
                System.out.println("Please Try again....\n");
            }


        }
        if(delete.equals(2)){
            Scanner sc=new Scanner(System.in);
            System.out.println("Enter Your Name:");
            String name2=sc.next();
            System.out.println("Enter the Date");
            String date=sc.next();
            if(seatsdata2.containsKey(name2)&&seatsdata4.containsValue(date)){
                seatsdata2.remove(name2);
                seatsdata4.remove(date);
                System.out.println("DONE");
                System.out.println(seatsdata2);
            }
            else{
                System.out.println("There are no any customers according to your input ");
                System.out.println("Please Try again....\n");
            }
        }

    }
    //Give parameters of four Hashmaps
    private void add(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2, HashMap<String, String> seatsdata3, HashMap<String, String> seatsdata4, HashMap<String, String> seatsdata5, HashMap<String, String> seatsdata6) {

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter 1 to book seats from Colombo to Badulla");
        System.out.println("Enter 2 to book seats from Badulla to Colombo");
        Integer colombo=sc.nextInt();

        if(colombo.equals(1)){
            Stage stage=new Stage();
            FlowPane layout1=new FlowPane();

            //Create 42 Buttons
            for (int k = 0; k < 42; k++) {
                Button buttons = new Button(String.valueOf(k + 1));
                layout1.getChildren().add(buttons);
                buttons.setId(Integer.toString(k));
                buttons.setMinHeight(50);
                buttons.setMaxHeight(50);
                buttons.setMinWidth(50);
                buttons.setMaxWidth(50);
                if (seatsdata.containsValue(buttons.getText())) {
                    buttons.setTextFill(Color.RED);
                }
                else {
                    buttons.setTextFill(Color.GREEN);
                    int finalK = k;
                    int finalK1 = k;
                    buttons.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String add = buttons.getText();
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Enter Your Name:");
                            String name = sc.next();
                            System.out.println("Enter Your Sir Name:");
                            String name2=sc.next();
                            System.out.println("Enter Date");
                            String date=sc.next();
                            seatsdata3.put(name,date);
                            buttons.setTextFill(Color.RED);
                            seatsdata.put(name, add);
                            seatsdata6.put(name+"_"+name2, add);
                            seatsdata5.put(name,name2);
                            buttons.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Already Booked");
                                }
                            });
                        }
                    });
                }
            }
            Button menu=new Button("Menu");
            menu.setTextFill(Color.RED);
            menu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            layout1.getChildren().add(menu);


            Scene scene1=new Scene(layout1,500,480);
            layout1.setHgap(20);
            layout1.setVgap(20);
            stage.setScene(scene1);
            stage.setTitle("Colombo To Badulla");
            stage.showAndWait();

        }

        if(colombo.equals(2)){
            Stage stage=new Stage();
            FlowPane layout1=new FlowPane();


            for (int k = 0; k < 42; k++) {
                Button buttons = new Button(String.valueOf(k + 1));
                layout1.getChildren().add(buttons);
                buttons.setId(Integer.toString(k));
                buttons.setMinHeight(50);
                buttons.setMaxHeight(50);
                buttons.setMinWidth(50);
                buttons.setMaxWidth(50);
                if (seatsdata2.containsValue(buttons.getText())) {
                    buttons.setTextFill(Color.RED);
                }
                else {
                    buttons.setTextFill(Color.GREEN);
                    int finalK = k;
                    int finalK1 = k;
                    buttons.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String add = buttons.getText();
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Enter Your Name:");
                            String name = sc.next();
                            System.out.println("Enter Date");
                            String date=sc.next();
                            seatsdata4.put(name,date);
                            buttons.setTextFill(Color.RED); //set the booked seats colour to red
                            seatsdata2.put(name, add);      // put the booking details in to the hashmap
                            buttons.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Already Booked");
                                }
                            });
                        }
                    });
                }
            }

            Button menu=new Button("Menu");
            menu.setTextFill(Color.RED);
            menu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            layout1.getChildren().add(menu);

            Scene scene1=new Scene(layout1,500,480);
            layout1.setHgap(20);
            layout1.setVgap(20);
            stage.setScene(scene1);
            stage.setTitle("Badulla To Colombo");
            stage.showAndWait();

        }

    }

    //sort the booked customers details in to a alphabetical order
    private void sort(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2) {
        ArrayList<String>seats=new ArrayList<String>();
        for(HashMap.Entry<String,String>entry:seatsdata.entrySet()){
            seats.add((String.valueOf(entry.getKey())));
            /*seats.add(String.valueOf(entry.getValue()));*/}
        for(HashMap.Entry<String,String>entry:seatsdata2.entrySet()){
            seats.add((String.valueOf(entry.getKey())));
            /* seats.add(String.valueOf(entry.getValue()));*/}
        String x;
        for (int j = 0; j < seats.size(); j++) {
            for (int i = j + 1; i < seats.size(); i++) {
                if (seats.get(i).compareTo(seats.get(j)) < 0) {
                    x = seats.get(j);
                    seats.set(j, seats.get(i));
                    seats.set(i, x);
                }
            }
            System.out.println(seats.get(j));
        }
    }
    private void quit() {
        System.exit(0); //exit the program

    }

    //view all Available and Unavailable seats
    private void view(HashMap<String, String> seatsdata, HashMap<String, String> seatsdata2) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to View seats from Colombo to Badulla:");
        System.out.println("Enter 2 to View seats from Badulla to Colombo:");
        Integer view = sc.nextInt();
        if (view.equals(1)) {
            Stage stage = new Stage();
            FlowPane layout1 = new FlowPane();
            for (int k = 0; k < 42; k++) {
                Button buttons = new Button(String.valueOf(k + 1));
                layout1.getChildren().add(buttons);
                buttons.setId(Integer.toString(k));
                buttons.setMinHeight(50);
                buttons.setMaxHeight(50);
                buttons.setMinWidth(50);
                buttons.setMaxWidth(50);

                if(seatsdata.containsValue(buttons.getText())){
                    buttons.setTextFill(Color.RED);
                }

                else {
                    buttons.setTextFill(Color.GREEN);
                }
            }
            Button menu=new Button("Menu");
            menu.setTextFill(Color.RED);
            menu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            layout1.getChildren().add(menu);


            Scene scene1 = new Scene(layout1, 500, 480);
            layout1.setHgap(20);
            layout1.setVgap(20);
            stage.setScene(scene1);
            stage.setTitle("Colombo to Badulla");
            stage.showAndWait();
        }
        if(view.equals(2)){
            Stage stage = new Stage();
            FlowPane layout1 = new FlowPane();
            for (int k = 0; k < 42; k++) {
                Button buttons = new Button(String.valueOf(k + 1));
                layout1.getChildren().add(buttons);
                buttons.setId(Integer.toString(k));
                buttons.setMinHeight(50);
                buttons.setMaxHeight(50);
                buttons.setMinWidth(50);
                buttons.setMaxWidth(50);

                if (seatsdata2.containsValue(buttons.getText())) {
                    buttons.setTextFill(Color.RED);
                } else {
                    buttons.setTextFill(Color.GREEN);

                }

            }
            Button menu=new Button("Menu");
            menu.setTextFill(Color.RED);
            menu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });
            layout1.getChildren().add(menu);

            Scene scene1 = new Scene(layout1, 500, 480);
            layout1.setHgap(20);
            layout1.setVgap(20);
            stage.setScene(scene1);
            stage.setTitle("Badulla to Colombo");
            stage.showAndWait();
        }

    }

    public static void main(String[] args) {

        launch();  //launch the gui

    }



}


