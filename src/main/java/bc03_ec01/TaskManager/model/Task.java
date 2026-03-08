package bc03_ec01.TaskManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 100)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @NotBlank private String status; // TODO, IN_PROGRESS, DONE

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Task(String title, String description, String status, User user) {
    this.title = title;
    this.description = description;
    this.status = status;
    this.user = user;
  }
}
