import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quellatalo.nin.fx.CheckBoxGroup;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Map<String, String> items = new HashMap<>();
        items.put("a", "a");
        items.put("b", "b");
        items.put("c", "c");
        CheckBoxGroup<String> checkBoxGroup = new CheckBoxGroup<>("main", items);

        Map<String, String> bSubItems = new HashMap<>();
        bSubItems.put("ba", "b1");
        bSubItems.put("bb", "b2");
        bSubItems.put("bc", "b3");
        ((CheckBoxGroup<String>) checkBoxGroup.getChildGroup("b")).setCheckItems(bSubItems);

        primaryStage.setScene(new Scene(checkBoxGroup));
        primaryStage.show();
    }
}
