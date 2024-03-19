package web.mates.arriendatufinca.model;

import java.util.UUID;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "property")
@SQLDelete(sql = "UPDATE property SET deleted = true WHERE id=?")
@Filter(name = "active", condition = "deleted=false")
public class Property {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private String description;

  private int rooms;

  private int bathrooms;

  private boolean petFriendly;

  private boolean pool;

  private boolean bbq;

  private int pricePerNight;

  private boolean deleted = Boolean.FALSE;

  @ManyToOne
  @JoinColumn(name = "owner", nullable = false)
  private User owner;
}
