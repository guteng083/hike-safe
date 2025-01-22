package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TrackerDevicesResponse {
    private String id;
    private String SerialNumber;
    private String status;
    private List<TransactionsResponse> transactions;

    public static TrackerDevicesResponse trackerDevicesToTrackerDevicesResponse(TrackerDevices trackerDevices) {
        System.out.println(trackerDevices.getTransactions());
        return TrackerDevicesResponse.builder()
                .id(trackerDevices.getId())
                .SerialNumber(trackerDevices.getSerialNumber())
                .status(trackerDevices.getStatus().toString())
                .transactions(trackerDevices.getTransactions() != null ? trackerDevices.getTransactions().stream()
                        .map(TransactionsResponse::toTransactionResponseWithoutTrackerDevice)
                        .toList() : null)
                .build();
    }
}
