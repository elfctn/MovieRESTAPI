package com.workintech.s19d1.entity;

import jakarta.persistence.*; // JPA (Java Persistence API) anotasyonları için gerekli importlar
import jakarta.validation.constraints.NotBlank; // Boşluk olmayan, null olmayan String kontrolü
import jakarta.validation.constraints.NotNull;  // Null olmayan kontrolü
import jakarta.validation.constraints.Size;    // String uzunluk kontrolü
import lombok.AllArgsConstructor; // Lombok: Tüm argümanları içeren constructor oluşturur
import lombok.Data;              // Lombok: Getter, Setter, equals, hashCode ve toString metotlarını otomatik oluşturur
import lombok.NoArgsConstructor;   // Lombok: Argümansız constructor oluşturur

import java.time.LocalDate; // Tarih tipi için
import java.util.ArrayList; // Dinamik boyutlu liste için
import java.util.List;      // Liste interface'i için

@NoArgsConstructor // Lombok anotasyonu: Parametresiz constructor oluşturur
@AllArgsConstructor // Lombok anotasyonu: Tüm field'ları parametre alan constructor oluşturur
@Data // Lombok anotasyonu: Getter, Setter, toString, equals, hashCode metotlarını otomatik oluşturur
@Entity // JPA anotasyonu: Bu sınıfın bir veritabanı tablosuna karşılık geldiğini belirtir
@Table(name = "actor", schema = "movie") // JPA anotasyonu: Veritabanındaki tablo adını ve şemasını belirtir
public class Actor {

    @Id // JPA anotasyonu: Bu field'ın primary key olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA anotasyonu: Primary key'in veritabanı tarafından otomatik artırılacağını belirtir
    @Column(name = "actor_id") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    private Long id; // Aktörün benzersiz kimliği

    @Column(name = "first_name") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @NotNull // Jakarta Validation: firstName alanının null olamayacağını belirtir
    @NotBlank // Jakarta Validation: firstName alanının boş veya null olamayacağını belirtir
    @Size(min = 10, max = 150) // Jakarta Validation: firstName alanının uzunluğunun 10 ile 150 karakter arasında olmasını sağlar
    private String firstName; // Aktörün adı

    @Column(name = "last_name") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @NotNull // Jakarta Validation: lastName alanının null olamayacağını belirtir
    @NotBlank // Jakarta Validation: lastName alanının boş veya null olamayacağını belirtir
    @Size(min = 10, max = 50) // Jakarta Validation: lastName alanının uzunluğunun 10 ile 50 karakter arasında olmasını sağlar
    private String lastName; // Aktörün soyadı

    @Column(name = "gender") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @Enumerated(EnumType.STRING) // JPA anotasyonu: Enum değerlerinin veritabanında String olarak saklanmasını sağlar (varsayılan ordinal int olarak saklar)
    private Gender gender; // Aktörün cinsiyeti (Gender enum tipinde)

    @Column(name = "birth_date") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @NotNull // Jakarta Validation: birthDate alanının null olamayacağını belirtir
    private LocalDate birthDate; // Aktörün doğum tarihi

    // Many-to-Many ilişki: Bir aktör birden çok filmde, bir filmde birden çok aktör oynayabilir
    // CascadeType'lar: DETACH, REFRESH, PERSIST, MERGE işlemleri için cascade ayarları
    // @JoinTable: İlişkinin sahipliğini Actor sınıfının üstlendiğini ve aradaki ilişki tablosunun detaylarını belirtir
    // name: İlişki tablosunun adı
    // schema: İlişki tablosunun şeması
    // joinColumns: Aktör tablosundan ilişki tablosuna giden sütun (Actor'ın primary key'i)
    // inverseJoinColumns: Film tablosundan ilişki tablosuna giden sütun (Movie'nin primary key'i)
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_actor", schema = "movie",
            joinColumns = @JoinColumn(name = "actor_id"), // Actor entity'sinin ID'sini bu tabloda "actor_id" olarak tut
            inverseJoinColumns = @JoinColumn(name = "movie_id") // Movie entity'sinin ID'sini bu tabloda "movie_id" olarak tut
    )
    private List<Movie> movies = new ArrayList<>(); // Aktörün rol aldığı filmlerin listesi

    // Yardımcı metod: Aktöre bir film eklemek için kullanılır
    public void addMovie(Movie movie) {
        movies.add(movie); // Filmi film listesine ekler
    }
}