import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
interface Item {

    public int getPrice();
    public String getType();


}

class ExtraItem implements Item{

    private String type;
    ExtraItem(String type){
        if(type.equals("Ketchup") || type.equals("Coke"))
            this.type=type;
        else
            throw new InvalidExtraTypeException();
    }

    @Override
    public int getPrice() {

        if(type.equals("Ketchup"))
            return 3;
        else if(type.equals("Coke"))
            return 5;
        return 0;
    }


    @Override
    public String getType() {
        return type;
    }
}
class PizzaItem implements  Item{

    private String type;
    PizzaItem(String type){



        if(type.equals("Standard") || type.equals("Pepperoni")|| type.equals("Vegetarian"))
            this.type=type;
        else
            throw new InvalidPizzaTypeException();

    }

    @Override
    public int getPrice() {
        if(type.equals("Standard"))
            return 10;
        else if(type.equals("Pepperoni"))
            return 12;
        else if(type.equals("Vegetarian"))
            return 8;
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }
}

class InvalidPizzaTypeException extends RuntimeException {



}
class InvalidExtraTypeException extends RuntimeException{

}

class Order {
    private boolean isUnlocked=true;
    private ArrayList<OrderItem> orderItems;
    Order() {
        orderItems = new ArrayList<>();
    }



    public void displayOrder(){
        int i=1;
        for(OrderItem oi:orderItems){
            System.out.println(String.format("  %d.%-14s x%2d%5d$",i,oi.item.getType(),oi.count,oi.getPrice()));
            i++;

        }
        System.out.println(String.format("%-22s%5d$","Total:",getPrice()));


//"  %d.%-15s x %2d%5d",i,oi.item.getType(),oi.count,oi.item.getPrice());
    }
    public void removeItem(int idx){
        if(isUnlocked) {
            if (orderItems.size() < idx)
                throw new ArrayIndexOutOfBоundsException(idx);
            else {
                orderItems.remove(idx);
            }
        }
        else
            throw new OrderLockedException();
    }
    public void lock(){

        if(orderItems.size()==0){
            throw new EmptyOrder();
        }
        else
        {
            isUnlocked=false;
        }

    }

    public void addItem(Item item, int count){
        if(!isUnlocked) throw new OrderLockedException();

        if (count > 10) throw new ItemOutOfStockException(item);

        for(OrderItem oi : orderItems)
            if(oi.item.getType().equals(item.getType())) {
                oi.count = count;
                return;
            }


        OrderItem orderItem = new OrderItem(count,item);

        orderItems.add(orderItem);

    }
    public int getPrice(){

        return orderItems.stream().mapToInt(x->x.item.getPrice()*x.count).sum();
    }
}

class OrderItem {

    public int count;
    public Item item;

    OrderItem(int count,Item item){
        this.item=item;
        this.count=count;


    }
    public int getPrice(){
        return item.getPrice()*count;

    }
}

class ItemOutOfStockException extends RuntimeException{
    private Item item;
    ItemOutOfStockException(Item item){
        this.item=item;


    }

}
class ArrayIndexOutOfBоundsException extends RuntimeException{


    ArrayIndexOutOfBоundsException(int idx){


    }
}
class EmptyOrder extends RuntimeException{

    EmptyOrder(){

    }
}
class OrderLockedException extends RuntimeException{

}
