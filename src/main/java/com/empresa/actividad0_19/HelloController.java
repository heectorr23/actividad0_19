package com.empresa.actividad0_19;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.bson.Document;


public class HelloController {
    @FXML
    private TextField NombreGuardar;
    @FXML
    private TextField AficionGuardar;
    @FXML
    private TextArea AreaTiempoGuardar;
    @FXML
    private ListView<String> ListaConsultar;
    @FXML
    private TextArea AreaTiempoConsultar;
    @FXML
    private ListView<String> ListaEliminar;
    @FXML
    private Button MostrarDatosEliminar;
    @FXML
    private TextArea AreaTiempoEliminar;
    private DatabaseManager dbManager;

    public HelloController() {
        //this.dbManager = DatabaseManager.getInstance("mongodb+srv://admin:admin@ejemplo.oqmkxob.mongodb.net/", "productos");
        this.dbManager = DatabaseManager.getInstance("mongodb+srv://admin:admin@ejemplo.oqmkxob.mongodb.net/\", \"productos");
    }

    @FXML
    private void handleGuardarInfo() {
        String nombre = NombreGuardar.getText();
        String aficion = AficionGuardar.getText();

        Document document = new Document();
        document.append("nombre", nombre);
        document.append("aficion", aficion);

        // Captura el tiempo antes de la operación
        long startTime = System.nanoTime();

        dbManager.insertDocument("productost", document);

        // Captura el tiempo después de la operación y calcula la diferencia
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  // Duración en milisegundos

        // Muestra el tiempo de ejecución en AreaTiempoGuardar
        AreaTiempoGuardar.setText("Tiempo de ejecución: " + duration + " ms");

        NombreGuardar.clear();
        AficionGuardar.clear();
    }

    @FXML
    private void handleConsultarInfo() {
        // Personalizar el diseño de la lista
        ListaConsultar.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Parse the JSON string back into a Document
                    Document doc = Document.parse(item);

                    // Create a label for the name and the hobby
                    Label nameLabel = new Label("Nombre: " + doc.getString("nombre"));
                    Label hobbyLabel = new Label("Afición: " + doc.getString("aficion"));

                    // Create a VBox to hold the labels
                    VBox vbox = new VBox(nameLabel, hobbyLabel);

                    // Set the VBox as the graphic of the list cell
                    setGraphic(vbox);
                }
            }
        });

        // Captura el tiempo antes de la operación
        long startTime = System.nanoTime();

        // Buscar los documentos y mostrarlos en ListaConsultar
        MongoCollection<Document> collection = dbManager.getCollection("productost");
        FindIterable<Document> documents = collection.find();

        // Captura el tiempo después de la operación y calcula la diferencia
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  // Duración en milisegundos

        // Muestra el tiempo de ejecución en AreaTiempoConsultar
        AreaTiempoConsultar.setText("Tiempo de ejecución: " + duration + " ms");

        // Limpiar la lista antes de agregar los nuevos documentos
        ListaConsultar.getItems().clear();

        for (Document doc : documents) {
            // Aquí puedes personalizar cómo quieres mostrar los documentos en la lista
            ListaConsultar.getItems().add(doc.toJson());
        }
    }

    @FXML
    private void handleMostrarDatosEliminar() {
        // Buscar los documentos y mostrarlos en ListaEliminar
        MongoCollection<Document> collection = dbManager.getCollection("productost");
        FindIterable<Document> documents = collection.find();

        // Limpiar la lista antes de agregar los nuevos documentos
        ListaEliminar.getItems().clear();

        for (Document doc : documents) {
            // Aquí puedes personalizar cómo quieres mostrar los documentos en la lista
            ListaEliminar.getItems().add(doc.toJson());
        }

        // Configurar un ContextMenu para cada elemento de la lista
        ListaEliminar.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Eliminar \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> {
                // Parse the JSON string back into a Document
                Document doc = Document.parse(cell.getItem());

                // Captura el tiempo antes de la operación
                long startTime = System.nanoTime();

                // Delete the document from the collection
                dbManager.getCollection("productost").deleteOne(doc);

                // Captura el tiempo después de la operación y calcula la diferencia
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;  // Duración en milisegundos

                // Muestra el tiempo de ejecución en AreaTiempoEliminar
                AreaTiempoEliminar.setText("Tiempo de ejecución: " + duration + " ms");

                // Remove the item from the list
                ListaEliminar.getItems().remove(cell.getItem());
            });
            contextMenu.getItems().add(deleteItem);

            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }
}