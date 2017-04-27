package treno;

import gestioneLim.Percorso;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author Marco Tramontini
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private GridPane griglia;
    
    private String celle[][];
    
    @FXML
    private HBox tavolozza;

    private Image selezione = null;

    protected static final int NUMERO_RIGHE_GRIGLIA = 5;
    protected static final int NUMERO_COLONNE_GRIGLIA = 6;

    protected static final int CELL_WIDTH = 228;
    protected static final int CELL_HEIGHT = 100;
    
    private Percorso percorso;
    

    private void inizializzaGriglia() {

        Node tabella[][] = new Node[NUMERO_RIGHE_GRIGLIA][NUMERO_COLONNE_GRIGLIA];

        for (int r = 0; r < NUMERO_RIGHE_GRIGLIA; r++) {
            for (int c = 0; c < NUMERO_COLONNE_GRIGLIA; c++) {
                Pane contenitore = new Pane();
                ImageView app = new ImageView();
                contenitore.setOnMouseClicked(e -> {
                    if (selezione != null) {
                        app.setFitHeight(CELL_HEIGHT);
                        app.setFitWidth(CELL_WIDTH);
                        app.setImage(selezione);

                        setDragSource(app, false);

                    }
                });
                contenitore.getChildren().add(app);

                final int fr = r;
                final int fc = c;

                setDragTarget(contenitore, app, false);
                tabella[r][c] = contenitore;
            }
        }

        for (int r = 0; r < NUMERO_RIGHE_GRIGLIA; r++) {
            griglia.addRow(r, tabella[r]);
        }

    }

    private void setDragSource(ImageView imgv, boolean permanente) {
        imgv.setOnDragDetected((MouseEvent event) -> {


            Dragboard db = imgv.startDragAndDrop(TransferMode.ANY);

            // salvo l'immagine della dragboard
            ClipboardContent content = new ClipboardContent();
            content.putImage(imgv.getImage());
            db.setContent(content);

            event.consume();
        });

        inizializzaGriglia();

        imgv.setOnDragDone((DragEvent event) -> {
            /* Se l'immagine è stata spostata, cancello l'originale */
            if (event.getTransferMode() == TransferMode.MOVE) {
                if (!permanente) {
                    imgv.setImage(null);
                }
            }
            event.consume();
        });
    }

    private void setDragTarget(Node contenitore, ImageView app, boolean tavolozza) {
        contenitore.setOnDragOver((DragEvent event) -> {
            /* Se il nodo di destinazione e di partenza non coincidono e c'è un'immagine da spostare */
            if (event.getGestureSource() != contenitore && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        contenitore.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                app.setFitHeight(CELL_HEIGHT);
                app.setFitWidth(CELL_WIDTH);
                app.setImage(db.getImage());

                if (!tavolozza) {
                    setDragSource(app, false);
                }
                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });
    }
    
    @FXML
    private void play(){
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        percorso = new Percorso(NUMERO_RIGHE_GRIGLIA,NUMERO_COLONNE_GRIGLIA);
        celle = new String[NUMERO_RIGHE_GRIGLIA][NUMERO_COLONNE_GRIGLIA];
        tavolozza.getChildren().stream().forEach((x) -> {
            final ImageView img = (ImageView) x;
            setDragSource(img, true);
            setDragTarget(img, img, true);
            x.setOnMouseClicked(e -> {
                if (selezione == img.getImage()) {
                    selezione = null;
                    img.setScaleX(1);
                    img.setScaleY(1);
                } else {
                    selezione = img.getImage();
                    img.setScaleX(1.5);
                    img.setScaleY(1.5);
                }
            });
        });
    }

}
