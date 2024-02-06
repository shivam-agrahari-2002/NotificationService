package com.shivam.notificationservice.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchTextSearchRequestBody {
    @NotNull(message = "Message should not be null")
    @NotEmpty(message = "Message should not be empty")
    String text;
    PageDetails pageDetails;
}
