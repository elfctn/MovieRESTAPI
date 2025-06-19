package com.workintech.s19d1.dto;

import com.workintech.s19d1.entity.Movie; // Film entity'sini import ediyoruz
import java.time.LocalDate; // Tarih tipi için
import java.util.List; // Filmler listesi için

// Java Record: DTO'lar için kısa ve öz bir sentaks sağlar.
// Otomatik olarak final field'lar, bir canonical constructor, getter metotları,
// equals(), hashCode() ve toString() metotlarını oluşturur.
public record ActorResponse(
        long id,              // Aktörün ID'si
        String firstName,     // Aktörün adı
        String lastName,      // Aktörün soyadı
        LocalDate birthDate,  // Aktörün doğum tarihi
        List<Movie> movies    // Aktörün rol aldığı filmlerin listesi (Bu, ilişkisel veriyi de response'a dahil eder)

        // Not: Record'lar varsayılan olarak tüm parametreleri alan bir constructor'a sahiptir.
        // Ayrıca her parametre için otomatik olarak getter metotları oluşturulur (örn: id(), firstName(), vb.).
        // Setter metotları ise otomatik olarak oluşturulmaz çünkü Record'lar immutable (değişmez) olmaya yönelik tasarlanmıştır.
) {
    // Record'lar içinde custom metotlar da tanımlanabilir, ancak genellikle DTO olarak sadece veri tutma amacı güderler.
}