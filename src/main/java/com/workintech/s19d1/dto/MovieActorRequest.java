package com.workintech.s19d1.dto;

import com.workintech.s19d1.entity.Actor; // Aktör entity'sini import ediyoruz
import com.workintech.s19d1.entity.Movie; // Film entity'sini import ediyoruz
import lombok.Getter; // Lombok: Getter metotlarını otomatik oluşturur (sadece getter'lar yeterli, çünkü sadece veri alınacak)
import lombok.NoArgsConstructor; // Lombok: Parametresiz constructor oluşturur (JSON deserileştirme için gerekli olabilir)
import lombok.AllArgsConstructor; // Lombok: Tüm argümanları alan constructor oluşturur (isteğe bağlı, ama pratik)
import lombok.Setter; // Lombok: Setter metotlarını otomatik oluşturur (eğer dışarıdan set edilecekse)

@NoArgsConstructor // Lombok anotasyonu: Parametresiz bir constructor oluşturur. JSON'dan veri okunurken Spring/Jackson tarafından kullanılır.
@AllArgsConstructor // Lombok anotasyonu: Tüm field'ları (movie, actor) parametre alan bir constructor oluşturur.
@Getter // Lombok anotasyonu: 'movie' ve 'actor' field'ları için getter metotlarını otomatik oluşturur.
@Setter // Lombok anotasyonu: 'movie' ve 'actor' field'ları için setter metotlarını otomatik oluşturur.
// @Data // Eğer hem Getter hem Setter hem de toString, equals/hashCode isterseniz @Data kullanabilirsiniz.
public class MovieActorRequest {
    private Movie movie; // İstek gövdesinde beklenen Movie nesnesi
    private Actor actor; // İstek gövdesinde beklenen Actor nesnesi

    // Not: Bu DTO, client'tan hem film hem de aktör bilgilerini tek bir JSON objesi içinde almayı sağlar.
    // Örneğin JSON yapısı şöyle olabilir:
    // {
    //   "movie": { "name": "Film Adı", "directorName": "Yönetmen", ... },
    //   "actor": { "firstName": "Aktör Adı", "lastName": "Aktör Soyadı", ... }
    // }
}