package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enigma.livecodeloan.constant.EInstalmentType;
import org.enigma.livecodeloan.model.entity.InstalmentType;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class InstalmentTypeRequest {
    private String id;
    private EInstalmentType instalmentType;
}
