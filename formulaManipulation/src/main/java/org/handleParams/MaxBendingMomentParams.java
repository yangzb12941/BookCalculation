package org.handleParams;

import lombok.Data;

@Data
public class MaxBendingMomentParams {
    private int lastDepthLand;
    private int tcAtLand;

    public MaxBendingMomentParams(
            int tcAtLand,
            int lastDepthLand) {
        this.lastDepthLand = lastDepthLand;
        this.tcAtLand = tcAtLand;
    }
}
