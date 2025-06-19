package com.workintech.s19d1.entity;

import jakarta.persistence.*; // JPA (Java Persistence API) anotasyonları için gerekli importlar
import jakarta.validation.constraints.NotBlank; // Boşluk olmayan, null olmayan String kontrolü
import jakarta.validation.constraints.NotNull;  // Null olmayan kontrolü
import jakarta.validation.constraints.Positive; // Pozitif sayı kontrolü
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
@Table(name = "movie", schema = "movie") // JPA anotasyonu: Veritabanındaki tablo adını ve şemasını belirtir
public class Movie {
    @Id // JPA anotasyonu: Bu field'ın primary key olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA anotasyonu: Primary key'in veritabanı tarafından otomatik artırılacağını belirtir (IDENTITY MySQL/PostgreSQL gibi DB'ler için uygundur)
    @Column(name = "movie_id") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    private Long id; // Filmin benzersiz kimliği

    @Column(name = "name") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @NotNull // Jakarta Validation: name alanının null olamayacağını belirtir
    @NotBlank // Jakarta Validation: name alanının boş (sadece boşluk karakteri içeren) veya null olamayacağını belirtir
    @Size(min = 10, max = 150) // Jakarta Validation: name alanının uzunluğunun 10 ile 150 karakter arasında olmasını sağlar
    private String name; // Filmin adı

    @Column(name = "director_name") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @NotNull // Jakarta Validation: directorName alanının null olamayacağını belirtir
    @NotBlank // Jakarta Validation: directorName alanının boş veya null olamayacağını belirtir
    @Size(min = 10, max = 50) // Jakarta Validation: directorName alanının uzunluğunun 10 ile 50 karakter arasında olmasını sağlar
    private String directorName; // Filmin yönetmeninin adı

    @Column(name = "rating") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @Positive // Jakarta Validation: rating alanının pozitif bir sayı olmasını sağlar
    private Integer rating; // Filmin derecelendirmesi

    @Column(name = "release_date") // JPA anotasyonu: Veritabanındaki sütun adını belirtir
    @NotNull // Jakarta Validation: releaseDate alanının null olamayacağını belirtir
    private LocalDate releaseDate; // Filmin yayınlanma tarihi

    // Many-to-Many ilişki: Bir filmde birden çok aktör, bir aktör birden çok filmde oynayabilir
    // mappedBy = "movies": İlişkinin sahipliğinin Actor sınıfındaki "movies" alanı tarafından yönetildiğini belirtir
    // CascadeType.ALL: Movie üzerinde yapılan tüm persist, merge, remove gibi işlemleri ilişkili Actor'lar üzerinde de yapar
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "movies")
    private List<Actor> actors = new ArrayList<>(); // Filmde rol alan aktörlerin listesi

    // Yardımcı metod: Filme bir aktör eklemek için kullanılır
    public void addActor(Actor actor) {
        actors.add(actor); // Aktörü aktör listesine ekler
    }
}