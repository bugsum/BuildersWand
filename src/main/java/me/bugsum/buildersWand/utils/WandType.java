package me.bugsum.buildersWand.utils;

public enum WandType {
    NORMAL(Integer.MAX_VALUE), // No block limit
    BRIDGE(64),               // Limit to 64 blocks
    TOWER(128),               // Limit to 128 blocks
    WALL(256);                // Limit to 256 blocks

    private final int maxBlocks;

    WandType(int maxBlocks) {
        this.maxBlocks = maxBlocks;
    }

    public int getMaxBlocks() {
        return maxBlocks;
    }
}
