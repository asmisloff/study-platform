package ru.amisfloff.studyplatform.solution.aggregation;

import java.util.List;
import java.util.Map;

abstract class RtnSolutionAggregator {

    public abstract void append(BlockSsDc ss, BlockPayloadDc pl);

    void solve() {
        updateRequest();
        ssBuffer.clear();
        plBuffer.clear();
        solver.solve(request);
        solver.reset();
        for (var e : rtnDevicesAggregators.entrySet()) {
            e.getValue().append(e.getKey());
        }
    }

    protected abstract void updateRequest();

    protected RtnRequest request;
    protected List<BlockSsDc> ssBuffer;
    protected List<BlockPayloadDc> plBuffer;
    protected RtnSolver solver;
    protected Map<RtnDevice, RtnDeviceSolutionAggregator> rtnDevicesAggregators;
}

abstract class RtnDeviceSolutionAggregator {

    abstract void append(RtnDevice device);

    protected double maxAmperage = 0.0;
    protected double avgAmperage = 0.0;
    protected double maxAvgAmperage10 = 0.0;
}

interface BlockSsDc { }

interface BlockPayloadDc { }

interface RtnDevice { }

interface RtnSolver {

    void solve(RtnRequest request);

    void reset();
}

interface RtnRequest { }

interface SchemaObjectId {
    int x();

    int lineIndex();

    Class<?> type();
}
