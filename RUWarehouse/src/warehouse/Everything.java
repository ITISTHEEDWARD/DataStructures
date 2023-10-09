package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile("everything.in");
    StdOut.setFile("everything.out");
    
    Warehouse warehouse = new Warehouse();
    int numLines = StdIn.readInt();
    int day,id,stock,demand,currDay,amount,restock;
    String mode,name; 
    for(int i=0;i<numLines;i++){
        mode = StdIn.readString();
        if(mode.equals("add")){
            day = StdIn.readInt();
            id = StdIn.readInt();
            name = StdIn.readString();
            stock = StdIn.readInt();
            demand = StdIn.readInt();

            warehouse.addProduct(id, name, stock, day, demand);
        }
        else if(mode.equals("delete")){
            id = StdIn.readInt();
            warehouse.deleteProduct(id);
        }
        else if(mode.equals("purchase")){
            currDay = StdIn.readInt();
            id = StdIn.readInt();
            amount = StdIn.readInt();
            warehouse.purchaseProduct(id,currDay,amount);
        }
        else if(mode.equals("restock")){
            id = StdIn.readInt();
            restock = StdIn.readInt();
            warehouse.restockProduct(id, restock);
        }

    }
    StdOut.println(warehouse);
    }
}