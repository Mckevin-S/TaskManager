package bc03_ec01.TaskManager.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
  @NotBlank
  @Size(max = 100)
  private String title;

  private String description;

  @NotBlank private String status;
}
