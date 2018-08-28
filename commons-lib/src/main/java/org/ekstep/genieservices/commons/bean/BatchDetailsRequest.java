package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

public class BatchDetailsRequest {

    private String batchId;
    private boolean refreshBatchDetails;

    private BatchDetailsRequest(String batchId, boolean refreshBatchDetails) {
        this.batchId = batchId;
        this.refreshBatchDetails = refreshBatchDetails;
    }

    public String getBatchId() {
        return batchId;
    }

    public boolean isRefreshBatchDetails() {
        return refreshBatchDetails;
    }

    public static class Builder {
        private String batchId;
        private boolean refreshBatchDetails;

        public Builder forBatch(String batchId) {
            if (StringUtil.isNullOrEmpty(batchId)) {
                throw new IllegalArgumentException("batchId should not be null or empty.");
            }
            this.batchId = batchId;
            return this;
        }

        /**
         * The batch details are refreshed from the server only if this flag is set.
         */
        public Builder refreshBatchDetailsFromServer() {
            this.refreshBatchDetails = true;
            return this;
        }

        public BatchDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(batchId)) {
                throw new IllegalStateException("batchId required.");
            }

            return new BatchDetailsRequest(batchId, refreshBatchDetails);
        }
    }
}
