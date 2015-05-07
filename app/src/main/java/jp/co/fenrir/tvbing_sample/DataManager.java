package jp.co.fenrir.tvbing_sample;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by benson on 15/04/24.
 */
public class DataManager {

    public static final int ROWS = 300;
    public static final int COLUMES = 15;
    public static final int ITEM_HEIGHT_DP = 60;
    public static int ITEM_HEIGHT = 0;
    public static int ITEM_COUNT = 0;

    private static DataManager singleton;

    private List<TVProgram> lists;
    private List<TVProgram> matrixLists;


    private DataManager() {

        generateDummyData(ROWS * COLUMES);
        ITEM_COUNT = lists.size();
    }

    public List<TVProgram> getLists() {
        return lists;
    }
    public List<TVProgram> getMatrixLists() {
        return lists;
    }

    public static DataManager getSharedInstance() {

        if (singleton == null) {

            singleton = new DataManager();
        }

        return singleton;
    }

    public void generateDummyData(int count) {
        ArrayList<TVProgram> items = new ArrayList<>();

        for (int i=0; i < ROWS; i++) {

            for (int j = 0; j < count / ROWS; j++) {

                items.add(new TVProgram(String.valueOf(j + i * COLUMES),String.valueOf(j),"TVBing"+String.valueOf(i), String.valueOf(i) + "Binの説明",i,i));
            }

        }

        List<TVProgram> list = new ArrayList<>();

        for (int i = 0; i < COLUMES; i++) {
            int j = 0;
            int start = 0;
            int end = 0;
            while ( end < ROWS ) {
                int height = (int)(Math.random() * 5);
                if (height <= 1) height = 1;
                end += height;

                if (end > ROWS) {
                    end = ROWS;
                    Log.e("MSG", "ROWS:" + ROWS + "Start:" + start + "i:" + i + "j:" + j);
                }
                TVProgram p = new TVProgram(String.valueOf(i), String.valueOf(i), "TVBing"+ i +"-"+j , "Binの説明"+ i +"-"+j+"-"+start+"-"+end, start, end);
                list.add(p);
                start = end;
                j++;
            }
        }

//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing02"+String.valueOf(0), String.valueOf(0) + "Binの説明0-2",3,5);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing03"+String.valueOf(0), String.valueOf(0) + "Binの説明0-3",5,7);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing04"+String.valueOf(0), String.valueOf(0) + "Binの説明0-4",7,9);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing05"+String.valueOf(0), String.valueOf(0) + "Binの説明0-5",9,12);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing06"+String.valueOf(0), String.valueOf(0) + "Binの説明0-6",12,20);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing07"+String.valueOf(0), String.valueOf(0) + "Binの説明0-7",20,29);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing08"+String.valueOf(0), String.valueOf(0) + "Binの説明0-8",29,35);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing09"+String.valueOf(0), String.valueOf(0) + "Binの説明0-9",35,45);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing010"+String.valueOf(0), String.valueOf(0) + "Binの説明0-10",45,55);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(0),"TVBing011"+String.valueOf(0), String.valueOf(0) + "Binの説明0-11",55,60);
//        list.add(p);
//
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing11"+String.valueOf(0), String.valueOf(0) + "Binの説明1-1",0,2);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing12"+String.valueOf(0), String.valueOf(0) + "Binの説明1-2",2,4);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing13"+String.valueOf(0), String.valueOf(0) + "Binの説明1-3",4,6);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing14"+String.valueOf(0), String.valueOf(0) + "Binの説明1-4",6,10);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing15"+String.valueOf(0), String.valueOf(0) + "Binの説明1-4",10,25);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing16"+String.valueOf(0), String.valueOf(0) + "Binの説明1-4",25,45);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing17"+String.valueOf(0), String.valueOf(0) + "Binの説明1-4",46,49);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing18"+String.valueOf(0), String.valueOf(0) + "Binの説明1-4",49,53);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(1),"TVBing19"+String.valueOf(0), String.valueOf(0) + "Binの説明1-4",53,60);
//        list.add(p);
//
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(2),"TVBing21"+String.valueOf(0), String.valueOf(0) + "Binの説明2-1",0,5);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(2),"TVBing22"+String.valueOf(0), String.valueOf(0) + "Binの説明2-2",5,10);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(2),"TVBing23"+String.valueOf(0), String.valueOf(0) + "Binの説明2-3",10,60);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(3),"TVBing31"+String.valueOf(0), String.valueOf(0) + "Binの説明3-1",0,7);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(3),"TVBing32"+String.valueOf(0), String.valueOf(0) + "Binの説明3-2",7,10);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(3),"TVBing33"+String.valueOf(0), String.valueOf(0) + "Binの説明3-3",10,60);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(4),"TVBing41"+String.valueOf(0), String.valueOf(0) + "Binの説明4-1",0,7);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(4),"TVBing42"+String.valueOf(0), String.valueOf(0) + "Binの説明4-2",7,10);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(4),"TVBing43"+String.valueOf(0), String.valueOf(0) + "Binの説明4-3",10,60);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(5),"TVBing31"+String.valueOf(0), String.valueOf(0) + "Binの説明5-1",0,7);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(5),"TVBing32"+String.valueOf(0), String.valueOf(0) + "Binの説明5-2",7,10);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(5),"TVBing33"+String.valueOf(0), String.valueOf(0) + "Binの説明5-3",10,60);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(6),"TVBing31"+String.valueOf(0), String.valueOf(0) + "Binの説明6-1",0,7);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(6),"TVBing32"+String.valueOf(0), String.valueOf(0) + "Binの説明6-2",7,10);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(6),"TVBing33"+String.valueOf(0), String.valueOf(0) + "Binの説明6-3",10,60);
//        list.add(p);
//
//        p = new TVProgram(String.valueOf(0),String.valueOf(7),"TVBing31"+String.valueOf(0), String.valueOf(0) + "Binの説明7-1",0,15);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(7),"TVBing32"+String.valueOf(0), String.valueOf(0) + "Binの説明7-2",15,20);
//        list.add(p);
//        p = new TVProgram(String.valueOf(0),String.valueOf(7),"TVBing33"+String.valueOf(0), String.valueOf(0) + "Binの説明7-3",20,60);
//        list.add(p);
//
//        for(int i = 8; i < 15 ; i++) {
//
//            p = new TVProgram(String.valueOf(0),String.valueOf(i),"TVBing31"+String.valueOf(0), String.valueOf(0) + "Binの説明"+ i + "-1",0,15);
//            list.add(p);
//            p = new TVProgram(String.valueOf(0),String.valueOf(i),"TVBing32"+String.valueOf(0), String.valueOf(0) + "Binの説明" + i + "-2",15,20);
//            list.add(p);
//            p = new TVProgram(String.valueOf(0),String.valueOf(i),"TVBing33"+String.valueOf(0), String.valueOf(0) + "Binの説明" + i + "-3",20,60);
//            list.add(p);
//        }



        for (int i = 0; i < list.size(); i++) {

            if (i == 1) {
                Log.e("MSG","MSG1");
            }
            handle(items,list.get(i), i);
        }
        lists = list;
        matrixLists = items;

        //return filterItems(items);
    }

    public List<TVProgram> filterItems(List<TVProgram> foo) {
        ArrayList<TVProgram> itemArray = new ArrayList<TVProgram>();
        for (int i = 0;i < foo.size();i++) {
            TVProgram p = foo.get(i);
            if (p.getTimeOffset() != 0) {
                itemArray.add(p);
            }
        }

        return itemArray;
    }

    public void handle(List<TVProgram> items,TVProgram program,int position) {

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).getStationId().equals(program.getStationId()) && (items.get(i).getStartTime() >= program.getStartTime() && items.get(i).getEndTime() <= program.getEndTime())) {
                //items.get(i).setEndTime(program.getEndTime());
                //items.get(i).setTitle(program.getTitle());
                items.get(i).setTrueDataPosition(position);
                program.setTrueDataPosition(position);
                int curRow = (int)Math.ceil(i * 1.0 / COLUMES);
                if (i % COLUMES != 0) {
                    curRow = curRow - 1;
                }
                int curColumn = i % COLUMES;
                if (program.getMatrixStartRow() == -1 || program.getMatrixStartRow() > curRow) {
                    program.setMatrixStartRow(curRow);
                }
                if (program.getMatrixEndRow() < curRow) {
                    program.setMatrixEndRow(curRow);
                }
                program.setMatrixColumn(curColumn);
            }
        }
    }

    public List<Integer> getVisibleData(int firstVisiblePosition, int visibleColumnCount, int VisibleRowCount ){
        Log.d("MSG", "firstVisiblePosition: " + firstVisiblePosition);

        ArrayList<Integer> result = new ArrayList<Integer>();
        int lastVisiblePostion = firstVisiblePosition + ( COLUMES * (VisibleRowCount-1)) + (visibleColumnCount - 1);
        int firstRow = (int)Math.floor((firstVisiblePosition * 1.0) / COLUMES);
        int ColInOneRow = firstVisiblePosition % COLUMES;
        for (int i = firstVisiblePosition; i < lastVisiblePostion; i++) {

           int curRow = (int)Math.floor((i * 1.0) / COLUMES);
           if( i >= ColInOneRow + curRow * COLUMES &&  i < ColInOneRow + curRow * COLUMES + visibleColumnCount  ) {
               //就是范围内的东东
               int position = matrixLists.get(i).getTrueDataPosition();

               if (position >= 0) {
                   //Log.d("MSG", "i: " + i + "position:" + position);
                   if (!result.contains(new Integer(position))) {
                       //Log.d("MSG", "position: " + position);
                       result.add(position);
                       //TODO 假设都有值在中间的时候。
                       lists.get(position).setMatrxVisibleRow(curRow);
                   }
               }

           }

        }

        Collections.sort(result);
        return result;
    }

    public TVProgram getTVProgramItem(int row, int column) {
        //startRow,column都是已0开始
        int matrixPosition = (row * COLUMES) + column;
        return this.getTVProgramItem(matrixPosition);

    }

    public TVProgram getTVProgramItem(int matrixPosition) {
        if (matrixPosition < matrixLists.size()){
            matrixLists.get(matrixPosition);
            int position = matrixLists.get(matrixPosition).getTrueDataPosition();
            if (position >= 0 && position < lists.size()) {
                return lists.get(position);
            }
        }
        return null;
    }

    public int getTVProgramItemPosition(int row, int column) {
        int matrixPosition = (row * COLUMES) + column;
        if (matrixPosition < matrixLists.size()){
            matrixLists.get(matrixPosition);
            int position = matrixLists.get(matrixPosition).getTrueDataPosition();
            return position;
        }
        return -1;
    }
}
