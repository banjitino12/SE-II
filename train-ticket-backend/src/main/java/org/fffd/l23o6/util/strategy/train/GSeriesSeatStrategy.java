package org.fffd.l23o6.util.strategy.train;

import java.util.*;

import jakarta.annotation.Nullable;


public class GSeriesSeatStrategy extends TrainSeatStrategy {
    public static final GSeriesSeatStrategy INSTANCE = new GSeriesSeatStrategy();
     
    private final Map<Integer, String> BUSINESS_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> FIRST_CLASS_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> SECOND_CLASS_SEAT_MAP = new HashMap<>();
    private final Map<Integer,String> NO_SEAT_MAP = new HashMap<>();

    private final Map<GSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(GSeriesSeatType.BUSINESS_SEAT, BUSINESS_SEAT_MAP);
        put(GSeriesSeatType.FIRST_CLASS_SEAT, FIRST_CLASS_SEAT_MAP);
        put(GSeriesSeatType.SECOND_CLASS_SEAT, SECOND_CLASS_SEAT_MAP);
        put(GSeriesSeatType.NO_SEAT, NO_SEAT_MAP);
    }};


    private GSeriesSeatStrategy() {

        int counter = 0;

        for (String s : Arrays.asList("1车1A","1车1C","1车1F")) {
            BUSINESS_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("2车1A","2车1C","2车1D","2车1F","2车2A","2车2C","2车2D","2车2F","3车1A","3车1C","3车1D","3车1F")) {
            FIRST_CLASS_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("4车1A","4车1B","4车1C","4车1D","4车2F","4车2A","4车2B","4车2C","4车2D","4车2F","4车3A","4车3B","4车3C","4车3D","4车3F")) {
            SECOND_CLASS_SEAT_MAP.put(counter++, s);
        }

        for(int i=0;i<10;i++) NO_SEAT_MAP.put(counter++,"无座");

    }

    public Integer findSeatNumberByInfo(String seatInfo) {
        for (Map<Integer, String> seatMap : TYPE_MAP.values()) {
            for (Map.Entry<Integer, String> entry : seatMap.entrySet()) {
                if (entry.getValue().equals(seatInfo)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }



    public enum GSeriesSeatType implements SeatType {
        BUSINESS_SEAT("商务座"), FIRST_CLASS_SEAT("一等座"), SECOND_CLASS_SEAT("二等座"), NO_SEAT("无座");
        private String text;
        GSeriesSeatType(String text){
            this.text=text;
        }
        public String getText() {
            return this.text;
        }
        public static GSeriesSeatType fromString(String text) {
            for (GSeriesSeatType b : GSeriesSeatType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }


    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, GSeriesSeatType type, boolean[][] seatMap) {
        Map<Integer, String> map = TYPE_MAP.get(type);
        int startSeat=-1;
        int endSeat=-1;
        for(Integer key: map.keySet()){
            if(startSeat==-1) startSeat=key;
            else startSeat=Math.min(key,startSeat);
            endSeat=Math.max(key,endSeat);
        }


        for (int i = startSeat; i<=endSeat;i++) {
            boolean vacant=true;
            for (int j = startStationIndex; j < endStationIndex; j++) {
                boolean[] seats = seatMap[j];
                if(seats[i]){
                    vacant=false;
                    break;
                }
            }
            if(vacant){
                for (int j = startStationIndex; j < endStationIndex; j++) {
                    boolean[] seats = seatMap[j];
                    seats[i]=true;
                }
                return map.get(i);
            }
        }
        return null;

    }

    public Map<GSeriesSeatType, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        // TODO（solved in listTrains）
        return null;
    }

    public boolean[][] initSeatMap(int stationCount) {
        return new boolean[stationCount - 1][BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size() + SECOND_CLASS_SEAT_MAP.size()+10];
    }
}
