package ru.amisfloff.studyplatform.solution.aggregation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class RtnSolutionAggregator {

    RtnSolutionAggregator(RtnRequest rtnRequest, RawDataRequest rawDataRequest, double avgPeriod, int capacity) {
        request = rtnRequest;
        if (rtnRequest != null) {
            rtnDevicesAggregators = new HashMap<>();
            for (var device : rtnRequest.rtnDevices()) {
                Cleat cleat = rawDataRequest == null
                    ? null
                    : rawDataRequest.findCleat(device.schemaObjectId());
                RtnDeviceSolutionAggregator ag = new RtnDeviceSolutionAggregator(
                    capacity, avgPeriod, cleat != null && cleat.series() != null
                );
                rtnDevicesAggregators.put(device, ag);
            }
        }
    }

    public void append(BlockPayloadDc pl) {
        plBuffer[pl.branchIndex()].add(pl);
    }

    void solve() {
        if (request != null) {
            updateRequest();
            solver.solve(request);
            solver.reset();
            for (var e : rtnDevicesAggregators.entrySet()) {
                e.getValue().append(e.getKey());
            }
        }
    }

    private void updateRequest() {
        for (int branchIndex = 0; branchIndex < ssBuffer.length; ++branchIndex) {
            for (var ss : ssBuffer[branchIndex]) {
                /* update reverse feeder data */
            }
        }
        for (int branchIndex = 0; branchIndex < plBuffer.length; ++branchIndex) {
            for (var pl : plBuffer[branchIndex]) {
                /* update payload data */
            }
            plBuffer[branchIndex].clear();
        }
    }

    protected RtnRequest request;
    protected BlockSsDc[][] ssBuffer;
    protected List<BlockPayloadDc>[] plBuffer;
    protected RtnSolver solver;
    protected Map<RtnDevice, RtnDeviceSolutionAggregator> rtnDevicesAggregators;
}

interface RawDataRequest {
    Iterable<RtnDevice> rtnDevices();

    Cleat findCleat(Object schemaObjectId);
}

class RtnDeviceSolutionAggregator {

    public RtnDeviceSolutionAggregator(int capacity, double avgPeriod, boolean full) {
        this.avgPeriod = (float) avgPeriod;
        this.full = full;
        this.data = new FloatCyclicQueue(full ? capacity : 60);
    }

    public void plusAssign(RtnDeviceSolutionAggregator other) { }

    public void append(RtnDevice device) { }

    protected FloatCyclicQueue data;
    protected boolean full;
    protected float avgPeriod;
    protected double maxAmperage = 0.0;
    protected double avgAmperage = 0.0;
    protected double maxAvgAmperage10 = 0.0;
}

class FloatCyclicQueue {

    public FloatCyclicQueue(int capacity) {
        array = new float[capacity];
    }

    public void append(float value) {
        inc();
        array[end - 1] = value;
    }

    public float get(int i) {
        Objects.checkIndex(i, array.length);
        return array[(i + begin) % array.length];
    }

    private void inc() {
        ++end;
        if (begin > 0) {
            begin = (begin + 1) % array.length;
        }
        if (end > array.length) {
            end = 1;
            begin = 1;
        }
    }

    private int begin = 0;
    private int end = 0;
    private final float[] array;
}

interface BlockSsDc { }

interface BlockPayloadDc {
    int branchIndex();
}

interface RtnDevice {
    Object schemaObjectId();
}

interface RtnSolver {

    void solve(RtnRequest request);

    void reset();
}

interface RtnRequest {
    Iterable<RtnDevice> rtnDevices();
}

interface SchemaObjectId {
    int x();

    int lineIndex();

    Class<?> type();
}

interface Cleat {
    Bus bus();

    Side shoulder();

    Integer number();

    Series series();
}

enum Bus {Cn, Sp, Rail}

enum Side {Left, Right}

enum PhysicalQuantity {Amperage, Voltage, Power}

enum Axis {Active, Reactive, Full}

interface Series {
    PhysicalQuantity pq();

    Axis axis();

    float[] data();
}