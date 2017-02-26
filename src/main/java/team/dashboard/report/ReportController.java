package team.dashboard.report;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import team.dashboard.AbstractDashboardController;
import team.model.Location;
import team.model.PurityReport;
import team.model.SourceReport;
import team.model.enums.PurityCondition;
import team.model.enums.SourceCondition;
import team.model.enums.SourceType;
import team.model.enums.UserType;

import java.text.DateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Handles the viewing and submitting of water source and purity reports.
 * */
public class ReportController extends AbstractDashboardController implements MapComponentInitializedListener {

    private static final double INIT_LATITUDE = 14.22;
    private static final double INIT_LONGITUDE = 13.07;
    @FXML
    private GoogleMapView mapView;
    private GoogleMap map;

    @FXML //List of current water reports.
    private ListView<SourceReport> reportListView;

    @FXML //Plus button to add new source report.
    private Button plusBtn;
    @FXML //"Double click to select location".
    private Label plusBtnText;


    @FXML //Side pane that displays info about water source.
    private AnchorPane sidePane;
    @FXML //Displays water source info.
    private Label sidePaneText;
    @FXML //Displays water source reported date.
    private Label sidePaneDateText;
    @FXML //Plus button to add new purity report.
    private Button purityReportBtn;
    @FXML //List of purity reports for selected water source.
    private ListView<PurityReport> sidePaneReports;
    //Currently selected water source ID.
    private int selectedWaterSourceID;

    @FXML //Handles new report form submission for both source and purity reports.
    private AnchorPane newReportPane;

    @FXML //Bottom pane to submit new source report.
    private Pane sourceReportPane;
    @FXML //Combo box to select source type in new source report.
    private ComboBox<SourceType> sourceTypeComboBox;
    @FXML //Combo box to select source condition in new source report.
    private ComboBox<SourceCondition> sourceConditionComboBox;
    //Current location to be submitted in report.
    private LatLong newSourceLocation;
    private boolean canSelectLocation = false;
    private Marker newReportMarker;

    @FXML //Bottom pane to submit new purity report.
    private Pane purityReportPane;
    @FXML //User input for virusPPM in purity report.
    private TextField virusPPM;
    @FXML //User input for contaminantPPM in purity report.
    private TextField contaminantPPM;
    @FXML //Combo box to select purity condition in new purity report.
    private ComboBox<PurityCondition> purityConditionComboBox;
    @FXML //Label displaying the purity error text
    private Label purityErrorText;
    @FXML //Grid pane displaying the graph plane
    private GridPane graphPane;
    @FXML //ComboBox of integers representing the years
    private ComboBox<Integer> graphYearComboBox;



    /**
     * Set default conditions of UI elements.
     * Add map listener.
    * */
    @Override
    public void initController() {
        sourceTypeComboBox.setItems(FXCollections.observableArrayList(SourceType.values()));
        sourceConditionComboBox.setItems(FXCollections.observableArrayList(SourceCondition.values()));
        purityConditionComboBox.setItems(FXCollections.observableArrayList(PurityCondition.values()));
        sourceTypeComboBox.setValue(SourceType.STREAM);
        sourceConditionComboBox.setValue(SourceCondition.WASTE);
        purityConditionComboBox.setValue(PurityCondition.UNSAFE);
        mapView.addMapInializedListener(this);
        newReportPane.setPrefHeight(0);
        sidePane.setPrefWidth(0);
        plusBtnText.setScaleX(0);
        virusPPM.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                virusPPM.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        contaminantPPM.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                contaminantPPM.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        UserType userType = dashboardController.getUser().getUserType();
        if ((userType != UserType.WORKER) && (userType != UserType.MANAGER)) {
            purityReportBtn.setVisible(false);
        }
        final int current_year = Calendar.getInstance().getWeekYear();
        if (userType == UserType.MANAGER) {
            for (int i = current_year; i > (current_year - 20); i--) {
                graphYearComboBox.getItems().add(i);
            }
            graphYearComboBox.setValue(current_year);
        } else {
            AnchorPane.setBottomAnchor(sidePaneReports, 0.0);
        }
    }

    /**
     * Setup map from mapView and add options.
     * Called after initController.
    * */
    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        //set up the center location for the map
        LatLong center = new LatLong(INIT_LATITUDE, INIT_LONGITUDE);

        options.center(center)
                .zoom(9)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .mapType(MapTypeIdEnum.TERRAIN);

        map = mapView.createMap(options);

        // Add UI Event handler for selecting the new report location.
        map.addUIEventHandler(UIEventType.dblclick, (JSObject obj) -> {
            if (canSelectLocation) {
                canSelectLocation = false;
                newSourceLocation = new LatLong((JSObject) obj.getMember("latLng"));
                openSourceReportForm();
            }

        });
        loadReportData();

    }

    /**
     * Gets all source reports and updates map elements.
     */
    private void loadReportData() {

        List<SourceReport> waterReportList = ReportBS.getSourceReports();
        reportListView.getItems().clear();
        for (SourceReport report : waterReportList) {
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(new LatLong(report.getLocation().getLatitude(), report.getLocation().getLongitude()))
                    .visible(true)
                    .title("Water Report");
            markerOptions.getJSObject().setMember("icon", "http://i.imgur.com/3qBm5il.png");
            Marker reportMarker = new Marker(markerOptions);

            map.addUIEventHandler(reportMarker,
                    UIEventType.click,
                    (JSObject obj) -> {
                        selectedWaterSourceID = report.getReportId();
                        animateSidePane(true);
                        sidePaneText.setText(report.toString());
                        DateFormat formatter = DateFormat.getDateTimeInstance(
                                DateFormat.SHORT,
                                DateFormat.SHORT);
                        sidePaneDateText.setText(formatter.format(report.getDate()));
                        sidePaneReports.getItems().clear();
                        sidePaneReports.getItems().
                                addAll(FXCollections.observableArrayList(ReportBS.
                                        getPurityReports(report.getReportId())));
                        map.panTo(new LatLong((JSObject)obj.getMember("latLng")));
                    });
            map.addMarker(reportMarker);

            reportListView.getItems().add(report);
        }
    }

    /**
     * Spawn new report pane when Plus Button is clicked.
     * */
    @FXML
    private void newSourceReport() {
        sourceReportPane.setVisible(true);
        purityReportPane.setVisible(false);
        animatePlusButtonText(true);
        animatePlusButton(false);
        canSelectLocation = true;
    }

    /**
     * Called after user double clicks to select location of new report.
     * */
    private void openSourceReportForm() {
        animatePlusButtonText(false);
        animateNewReportPane(true);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(newSourceLocation)
                .visible(true)
                .title("New Report Here");
        markerOptions.getJSObject().setMember("icon", "http://i.imgur.com/3qBm5il.png");
        newReportMarker = new Marker(markerOptions);
        map.addMarker(newReportMarker);
    }

    /**
     * Adds new report to the model and updates the list of current reports.
     * */
    @FXML
    private void submitSourceReport() {
        ReportBS.addSourceReport(new Location(
                newSourceLocation.getLatitude(),
                newSourceLocation.getLongitude()),
                sourceTypeComboBox.getValue(),
                sourceConditionComboBox.getValue(),
                dashboardController.getUser().getUsername());
        animateNewReportPane(false);
        animatePlusButton(true);
        map.removeMarker(newReportMarker);
        loadReportData();
    }

    /**
     * Cancels the current new report from being submitted, deletes temporary marker.
     */
    @FXML
    private void cancelSourceReport() {
        animateNewReportPane(false);
        animatePlusButton(true);
        if (newReportMarker != null) {
            map.removeMarker(newReportMarker);
        }
    }

    /**
     * Opens new purity report pane on bottom of screen.
     */
    @FXML
    private void newPurityReport() {
        cancelSourceReport();
        purityReportPane.setVisible(true);
        sourceReportPane.setVisible(false);
        animateNewReportPane(true);
        purityErrorText.setVisible(false);
    }

    /**
     * Submits worker-created purity report.
     */
    @FXML
    private void submitPurityReport() {
        try {
            ReportBS.addPurityReport(
                    selectedWaterSourceID,
                    purityConditionComboBox.getValue(),
                    Integer.parseInt(virusPPM.getText()),
                    Integer.parseInt(contaminantPPM.getText()),
                    dashboardController.getUser().getUsername()
            );
            sidePaneReports.getItems().clear();
            sidePaneReports.setItems(FXCollections.observableArrayList(ReportBS
                    .getPurityReports(selectedWaterSourceID)));
            animateNewReportPane(false);
        } catch (NumberFormatException ne) {
            purityErrorText.setVisible(true);
        }

    }

    /**
     * Cancels purity report without submitting.
     */
    @FXML
    private void cancelPurityReport() {
        virusPPM.clear();
        contaminantPPM.clear();
        animateNewReportPane(false);
    }

    @FXML
    private void closeSidePane() {
        animateSidePane(false);
    }

    /**
     * Call showGraph, passing in the boolean to specify a virus graph
     */
    @FXML
    private void showVirusGraph() {
        showGraph(true);
    }

    /**
     * Call showGraph, passing in the boolean to specify a contaminant graph
     */
    @FXML
    private void showContaminantGraph() {
        showGraph(false);
    }

    /**
     * Display graph on report screen for the year in graphYearComboBox
     * @param isVirus   true if graphing VirusPPM, false if graphing ContaminantPPM
     */
    private void showGraph(boolean isVirus) {
        if (graphPane.getChildren().size() > 2) {
            graphPane.getChildren().remove(2);
        }
        graphPane.setVisible(true);
        BarChart<String, Number> barChart = new BarChart<>(
                new CategoryAxis(),
                new NumberAxis()
        );

        //Array entry for every month
        Integer[] ppm = new Integer[12];
        for (PurityReport pr : ReportBS.getPurityReports(selectedWaterSourceID)){
            Date date = pr.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (calendar.get(Calendar.YEAR) == graphYearComboBox.getValue()){
                int month = calendar.get(Calendar.MONTH);
                if (ppm[month] != null) {
                    ppm[month] = (ppm[month] + (isVirus ? pr.getVirusPPM() : pr.getContaminantPPM())) / 2;
                } else {
                    ppm[month] = (isVirus ? pr.getVirusPPM() : pr.getContaminantPPM());
                }
            }
        }

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        for(int i = 0; i < ppm.length; i++) {
            series.getData().add(new XYChart.Data(Month.of(i+1).toString(), (ppm[i] != null) ? ppm[i] : 0));
        }
        barChart.getData().addAll(series);

        graphPane.getChildren().add(barChart);
        barChart.setOnMouseClicked(event -> {
            graphPane.getChildren().remove(barChart);
            graphPane.setVisible(false);
        });
    }

    //All animation methods below
    private void animateNewReportPane(boolean in) {
        Timeline tm = new Timeline();
        KeyValue kv1 = new KeyValue(newReportPane.prefHeightProperty(), in?0:175);
        KeyValue kv2 = new KeyValue(newReportPane.prefHeightProperty(), in?175:0);
        KeyFrame kf = new KeyFrame(Duration.millis(150), kv1, kv2);
        tm.getKeyFrames().addAll(kf);
        tm.play();
    }
    private void animatePlusButton(boolean in) {
        Timeline tm = new Timeline();
        KeyValue kv1 = new KeyValue(plusBtn.scaleXProperty(), in?0:1);
        KeyValue kv2 = new KeyValue(plusBtn.scaleXProperty(), in?1:0);
        KeyFrame kf = new KeyFrame(Duration.millis(150), kv1, kv2);
        tm.getKeyFrames().addAll(kf);

        Timeline ytm = new Timeline();
        KeyValue ykv1 = new KeyValue(plusBtn.scaleYProperty(), in?0:1);
        KeyValue ykv2 = new KeyValue(plusBtn.scaleYProperty(), in?1:0);
        KeyFrame ykf = new KeyFrame(Duration.millis(150), ykv1, ykv2);
        ytm.getKeyFrames().addAll(ykf);
        tm.play();
        ytm.play();
    }
    private void animatePlusButtonText(boolean in) {
        Timeline tm = new Timeline();
        KeyValue kv1 = new KeyValue(plusBtnText.scaleXProperty(), in?0:1);
        KeyValue kv2 = new KeyValue(plusBtnText.scaleXProperty(), in?1:0);
        KeyFrame kf = new KeyFrame(Duration.millis(150), kv1, kv2);
        tm.getKeyFrames().addAll(kf);
        tm.play();
    }
    private void animateSidePane(boolean in) {
        Timeline tm = new Timeline();
        KeyValue kv1 = new KeyValue(sidePane.prefWidthProperty(), in?0:200);
        KeyValue kv2 = new KeyValue(sidePane.prefWidthProperty(), in?200:0);
        KeyFrame kf = new KeyFrame(Duration.millis(150), kv1, kv2);
        tm.getKeyFrames().addAll(kf);
        tm.play();
    }




}