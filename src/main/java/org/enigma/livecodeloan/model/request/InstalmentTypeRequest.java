package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class InstalmentTypeRequest {
    private String id;
    private String instalmentType;
}
