package view;

import javafx.scene.layout.StackPane;

public class Cells extends StackPane {
	String Cells[][];
	int column;
    int row;
    
    public Cells(String[][] Cells,int column, int row) {
    	this.Cells = Cells;
        this.column = column;
        this.row = row;
         
    }
}
