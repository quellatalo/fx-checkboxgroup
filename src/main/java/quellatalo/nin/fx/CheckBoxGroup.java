package quellatalo.nin.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBoxGroup<T> extends AnchorPane {
    private static final Font bold = Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize());
    @FXML
    private CheckBox cMain;
    @FXML
    private FlowPane fpContent;
    private Map<String, T> checkItems;
    private Map<String, CheckBoxGroup<?>> childrenCheckBoxGroup;
    private Runnable childSelectEvent;
    private Runnable childAction;

    public CheckBoxGroup() {
        childrenCheckBoxGroup = new HashMap<>();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CheckBoxGroup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        getChildren().remove(fpContent);
        cMain.setOnAction(event -> {
            if (cMain.isAllowIndeterminate()) cMain.setAllowIndeterminate(false);
            resetChecks(cMain.isSelected());
            if (childAction != null) childAction.run();
        });
        childSelectEvent = () -> {
            cMain.setAllowIndeterminate(true);
            cMain.setIndeterminate(true);
        };
    }

    public CheckBoxGroup(String mainText, Map<String, T> checkItems) {
        this();
        setMainText(mainText);
        setCheckItems(checkItems);
    }

    private void setChildAction(Runnable childAction) {
        this.childAction = childAction;
    }

    public Map<String, T> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(Map<String, T> checkItems) {
        this.checkItems = checkItems;
        fpContent.getChildren().clear();
        if (checkItems.size() > 0) {
            if (!getChildren().contains(fpContent)) {
                getChildren().add(fpContent);
                cMain.toFront();
                cMain.setFont(bold);
            }
            for (String m : checkItems.keySet()) {
                CheckBoxGroup<?> child = new CheckBoxGroup<>();
                child.setMainText(m);
                childrenCheckBoxGroup.put(m, child);
                fpContent.getChildren().add(child);
            }
            for (CheckBoxGroup<?> c : childrenCheckBoxGroup.values()) {
                c.setChildAction(childSelectEvent);
            }
            resetChecks();
        } else {
            getChildren().remove(fpContent);
            cMain.setFont(Font.getDefault());
        }
    }

    public String getMainText() {
        return cMain.getText();
    }

    public void setMainText(String text) {
        cMain.setText(text);
    }

    public void setOnAction(EventHandler<ActionEvent> actionEventHandler) {
        cMain.setOnAction(actionEventHandler);
    }

    public List<String> getCheckedTexts() {
        List<String> checkedTexts = new ArrayList<>();
        for (CheckBoxGroup<?> child : childrenCheckBoxGroup.values()) {
            if (child.isSelected()) checkedTexts.add(child.getText());
        }
        return checkedTexts;
    }

    public String getText() {
        return cMain.getText();
    }

    public CheckBoxGroup<?> getChildGroup(String key) {
        return childrenCheckBoxGroup.get(key);
    }

    public List<T> getCheckedItems() {
        List<T> checkedItems = new ArrayList<>();
        for (CheckBoxGroup<?> child : childrenCheckBoxGroup.values()) {
            if (child.isSelected() || child.isIndeterminate()) checkedItems.add(checkItems.get(child.getText()));
        }
        return checkedItems;
    }

    public void resetChecks() {
        resetChecks(true);
    }

    public void resetChecks(boolean checked) {
        cMain.setSelected(checked);
        for (CheckBoxGroup<?> c : childrenCheckBoxGroup.values()) {
            c.resetChecks(checked);
        }
    }

    public boolean isSelected() {
        return cMain.isSelected();
    }

    public void setSelected(boolean selected) {
        cMain.setSelected(selected);
    }

    public boolean isIndeterminate() {
        return cMain.isIndeterminate();
    }

    public Map<String, CheckBoxGroup<?>> getChildrenCheckBoxGroup() {
        return childrenCheckBoxGroup;
    }
}
