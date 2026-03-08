package com.revworkforce.hrm.dto;

public class LeaveSummaryResponse {
    private int totalLeaves;
    private int remainingLeaves;
    private int pendingApprovals;
    private int usedLeaves;
    private int pendingLeaveDays;

    public LeaveSummaryResponse() {
    }

    public LeaveSummaryResponse(int totalLeaves, int remainingLeaves, int pendingApprovals, int usedLeaves, int pendingLeaveDays) {
        this.totalLeaves = totalLeaves;
        this.remainingLeaves = remainingLeaves;
        this.pendingApprovals = pendingApprovals;
        this.usedLeaves = usedLeaves;
        this.pendingLeaveDays = pendingLeaveDays;
    }

    public int getTotalLeaves() {
        return totalLeaves;
    }

    public void setTotalLeaves(int totalLeaves) {
        this.totalLeaves = totalLeaves;
    }

    public int getRemainingLeaves() {
        return remainingLeaves;
    }

    public void setRemainingLeaves(int remainingLeaves) {
        this.remainingLeaves = remainingLeaves;
    }

    public int getPendingApprovals() {
        return pendingApprovals;
    }

    public void setPendingApprovals(int pendingApprovals) {
        this.pendingApprovals = pendingApprovals;
    }

    public int getUsedLeaves() {
        return usedLeaves;
    }

    public void setUsedLeaves(int usedLeaves) {
        this.usedLeaves = usedLeaves;
    }

    public int getPendingLeaveDays() {
        return pendingLeaveDays;
    }

    public void setPendingLeaveDays(int pendingLeaveDays) {
        this.pendingLeaveDays = pendingLeaveDays;
    }
}
