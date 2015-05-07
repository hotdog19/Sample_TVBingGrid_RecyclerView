package jp.co.fenrir.tvbing_sample;

/**
 * Created by Tozawa on 2015/03/23.
 */
public class TVProgram {

    private String id;
    private String stationId;

    private String title;
    private String summary;

    private int startTime;
    private int endTime;

    public int getTrueDataPosition() {
        return trueDataPosition;
    }

    public void setTrueDataPosition(int trueDataPosition) {
        this.trueDataPosition = trueDataPosition;
    }

    private int trueDataPosition = -1;

    public int getMatrixColumn() {
        return MatrixColumn;
    }

    public void setMatrixColumn(int matrixColumn) {
        MatrixColumn = matrixColumn;
    }

    private int MatrixColumn = -1;

    public int getMatrixStartRow() {
        return MatrixStartRow;
    }

    public void setMatrixStartRow(int matrixStartRow) {
        MatrixStartRow = matrixStartRow;
    }

    public int getMatrixEndRow() {
        return MatrixEndRow;
    }

    public void setMatrixEndRow(int matrixEndRow) {
        MatrixEndRow = matrixEndRow;
    }

    private int MatrixStartRow = -1;
    private int MatrixEndRow = -1;

    public int getMatrxVisibleRow() {
        return MatrxVisibleRow;
    }

    public void setMatrxVisibleRow(int matrxVisibleRow) {
        MatrxVisibleRow = matrxVisibleRow;
    }

    private int MatrxVisibleRow = -1;



    public String getId() {
        return id;
    }

    public String getStationId() {
        return stationId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public TVProgram(String id, String stationId, String title, String summary, int startTime, int endTime) {
        this.id = id;
        this.stationId = stationId;
        this.title = title;
        this.summary = summary;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public int getTimeOffset() {

        return getEndTime() - getStartTime();
    }

    public int leftOffset() {

        return Integer.parseInt(stationId);
    }

    public int topOffset() {

        return startTime;
    }
}
