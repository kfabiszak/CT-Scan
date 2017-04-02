package machine;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {

    private Process p;

    @FXML
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private ImageView iv3;
    @FXML
    private ImageView iv4;
    @FXML
    private Stage imageStage;

    @FXML
    private TextField loc;
    @FXML
    private Label errorChoose;
    @FXML
    private Button choose;
    @FXML
    private Button load;
    @FXML
    private FileChooser fileChooser;
    private File file;
    private Stage fileStage;

    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private ChoiceBox<String> sex;
    @FXML
    private DatePicker birthDate;
    @FXML
    private Label errorInfo;
    @FXML
    private Button save;
    private boolean filled = false;

    @FXML
    private TextField date;
    @FXML
    private TextArea comment;
    @FXML
    private Label errorExport;
    @FXML
    private Button export;

    @FXML
    private TextField nEmi;
    @FXML
    private Slider slEmi;
    @FXML
    private TextField nDet;
    @FXML
    private Slider slDet;
    @FXML
    private TextField range;
    @FXML
    private Slider slRan;

    public void destroy() {
        if (p != null) {
            p.destroy();
            System.out.println("Controller destroyed.");
        }
    }

    @FXML
    public void showInput(final Image im) {
        Platform.runLater(new Runnable() {
            public void run() {
                iv1.setImage(im);
            }
        });
    }

    @FXML
    public void generateSinogram() {
        try {
            p = new ProcessBuilder("ping", "stackoverflow.com", "-n", "100").start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Scan.scan();
                }
            }).start();
        } catch (IOException e) {
            System.out.println("thread in generateSinogram() error: " + e);
        }
    }

    @FXML
    public void showSinogram(final Image im) {
        Platform.runLater(new Runnable() {
            public void run() {
                iv2.setImage(im);
            }
        });
    }

    @FXML
    public void showResult(final Image im) {
        Platform.runLater(new Runnable() {
            public void run() {
                iv3.setImage(im);
            }
        });
    }

    @FXML
    public void showNormalizedResult(final Image im) {
        Platform.runLater(new Runnable() {
            public void run() {
                iv4.setImage(im);
            }
        });
    }

    @FXML
    public void chooseFile() {
        try {
            errorChoose.setText("");
            FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png");
            fileChooser = new FileChooser();
            fileChooser.setTitle("Choose image");
            fileChooser.getExtensionFilters().add(fileExtensions);
            String userDirectoryString = (System.getProperty("user.dir")) + "\\resource\\machine";
            File userDirectory = new File(userDirectoryString);
            fileChooser.setInitialDirectory(userDirectory);
            file = fileChooser.showOpenDialog(fileStage);
            if (file != null)
                loc.setText(file.toURI().toString());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void notSquare() {
        errorChoose.setText("Obraz nie jest kwadratem.");
    }

    @FXML
    public void chooseAgain() {
        errorChoose.setText("Wybierz obraz ponownie.");
    }

    @FXML
    public void fillInfo() {
        errorInfo.setText("Wypełnij wszystkie pola.");
    }

    @FXML
    public void saved() {
        errorInfo.setText("Zapisano.");
    }

    @FXML
    public void fillComment() {
        errorExport.setText("Wypełnij wszystkie pola.");
    }

    @FXML
    public void fillPatient() {
        errorExport.setText("Wypełnij informacje o pacjencie.");
    }

    @FXML
    public void failedExport() {
        errorExport.setText("Coś poszło nie tak.");
    }

    @FXML
    public void exported() {
        errorExport.setText("Wyeksportowano.");
    }

    @FXML
    public void loadFile() {
        Info.nextPatient();
        Graphics.initImage(file);
    }

    @FXML
    public void showInputWindow() {
        if(Graphics.isLoaded()) {
            imageStage = new Stage();
            imageStage.setTitle("Obraz wejściowy");
            ImageView iv = new ImageView(Graphics.getImage());
            Pane pane = new Pane();
            pane.getChildren().add(iv);
            Scene scene = new Scene(pane);
            imageStage.setScene(scene);
            imageStage.show();
        }
    }

    @FXML
    public void showSinogramWindow() {
        if(Graphics.isLoaded()) {
            imageStage = new Stage();
            imageStage.setTitle("Sinogram");
            ImageView iv = new ImageView(Sinogram.getiSinogram());
            iv.setFitHeight(720.0);
            iv.setFitWidth(720.0);
            iv.setPreserveRatio(true);
            Pane pane = new Pane();
            pane.getChildren().add(iv);
            Scene scene = new Scene(pane);
            imageStage.setScene(scene);
            imageStage.show();
        }
    }

    @FXML
    public void showOutputWindow() {
        if(Scan.isGenerated()) {
            imageStage = new Stage();
            imageStage.setTitle("Obraz wyjściowy");
            final ImageView iv = new ImageView(Scan.getiResult());
            iv.setFitHeight(720.0);
            iv.setFitWidth(720.0);
            iv.setPreserveRatio(true);
            Pane pane = new Pane();
            final Slider iteration = new Slider();
            iteration.setMin(0.0);
            iteration.setMax(10.0);
            iteration.setValue(10);
            iteration.setPrefWidth(iv.getBoundsInParent().getWidth());
            iteration.setShowTickMarks(true);
            iteration.setBlockIncrement(1);
            iteration.setSnapToTicks(true);
            iteration.setMajorTickUnit(1);
            iteration.setMinorTickCount(0);

            iteration.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    try {
                        iv.setImage(Scan.getStages().get((int) Math.floor(iteration.getValue())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            pane.getChildren().add(iv);
            pane.getChildren().add(iteration);
            Scene scene = new Scene(pane);
            imageStage.setScene(scene);
            imageStage.show();
        }
    }

    @FXML
    public void showNormalizedWindow() {
        if(Scan.isGenerated()) {
            imageStage = new Stage();
            imageStage.setTitle("Obraz wyjściowy znormalizowany");
            final ImageView iv = new ImageView(Scan.getiNormalizedResult());
            iv.setFitHeight(720.0);
            iv.setFitWidth(720.0);
            iv.setPreserveRatio(true);
            Pane pane = new Pane();
            pane.getChildren().add(iv);
            Scene scene = new Scene(pane);
            imageStage.setScene(scene);
            imageStage.show();
        }
    }

    @FXML
    public void saveInfo() {
        if(name.getText() != null && surname.getText() != null && birthDate != null) {
            Info.setName(name.getText());
            Info.setSurname(surname.getText());
            Info.setSex(sex.getSelectionModel().getSelectedItem());
            Info.setBirthDate(birthDate.getValue());
            Info.setDate(" ");
            Info.setComment(comment.getText());
            filled = true;
            saved();
        } else {
            fillInfo();
        }
    }

    @FXML
    public void exportDCM() {
        errorExport.setText("");
        if(filled) {
            if (comment != null) {
                try {
                    DICOM.generateDICOM();
                    exported();
                } catch (Exception e) {
                    failedExport();
                }
            } else {
                fillComment();
            }
        } else {
            fillPatient();
        }
    }

    @FXML
    private void applySettings() {
        Settings.setnDet(Integer.parseInt(nDet.getText()));
        Settings.setnEmi(Integer.parseInt(nEmi.getText()));
        Settings.setRange(Integer.parseInt(range.getText()));
    }

    @FXML
    private void initSettings() {
        nEmi.textProperty().bind(Bindings.format("%.0f", slEmi.valueProperty()));
        nDet.textProperty().bind(Bindings.format("%.0f", slDet.valueProperty()));
        range.textProperty().bind(Bindings.format("%.0f", slRan.valueProperty()));
        slEmi.setValue(Settings.getnEmi());
        slDet.setValue(Settings.getnDet());
        slRan.setValue(Settings.getRange());
        sex.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        sex.getSelectionModel().selectLast();
    }

    @FXML
    public void initialize() {
        initSettings();
    }

}
