package ui.utils;

import eyesimo.processor.judgers.AnalyzeResult;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Created by Anton_Erde on 22.03.2017.
 */
public class UpdateThread implements Runnable{

    int i;
    AnalyzeResult result;

    TableView imageTable;
    ObservableList<ImageTableValue> tableData;

    public UpdateThread(int _i, AnalyzeResult _result, TableView _imageTable, ObservableList<ImageTableValue> _tableData) {
        i = _i;
        result = _result;
        imageTable = _imageTable;
        tableData = _tableData;
    }

    @Override
    public void run() {

        ImageTableValue tmp = tableData.get(i);
        tmp.setImageClass( result.getClassName() );

        tableData.set(i, tmp);
        imageTable.setItems( tableData );
    }
}
