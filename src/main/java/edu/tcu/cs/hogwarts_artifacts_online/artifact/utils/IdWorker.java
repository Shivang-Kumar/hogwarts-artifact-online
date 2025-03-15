package edu.tcu.cs.hogwarts_artifacts_online.artifact.utils;

public class IdWorker {
	 // Epoch timestamp for when the Snowflake algorithm starts (Twitter uses 2010-11-04 11:42:54.657 UTC)
    private final long epoch = 1288834974657L;

    // Number of bits allocated for each field
    private final long machineIdBits = 10L;
    private final long dataCenterIdBits = 10L;
    private final long sequenceBits = 12L;

    // Maximum values for each field
    private final long maxMachineId = ~(-1L << machineIdBits);
    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);
    private final long maxSequence = ~(-1L << sequenceBits);

    // Shifts for each field
    private final long machineIdShift = sequenceBits;
    private final long dataCenterIdShift = sequenceBits + machineIdBits;
    private final long timestampShift = sequenceBits + machineIdBits + dataCenterIdBits;

    // Sequence and last timestamp variables
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    // Machine and Data Center IDs
    private final long machineId;
    private final long dataCenterId;

    // Constructor to initialize machineId and dataCenterId
     public IdWorker(long machineId, long dataCenterId)		// TODO Auto-generated constructor stub
    	 {
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException("Machine ID out of range");
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException("Data Center ID out of range");
        }
        this.machineId = machineId;
        this.dataCenterId = dataCenterId;
    }

    // Generate the next unique ID
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // If the timestamp is the same as the last one, increment sequence number
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // Sequence overflow: wait for next millisecond
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        // Set the last timestamp to the current one
        lastTimestamp = timestamp;

        // Shift the bits and build the ID
        return ((timestamp - epoch) << timestampShift)
                | (dataCenterId << dataCenterIdShift)
                | (machineId << machineIdShift)
                | sequence;
    }

    // Wait until the next millisecond
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
}
    }
    