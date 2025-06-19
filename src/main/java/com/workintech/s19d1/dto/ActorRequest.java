package com.workintech.s19d1.dto;

import com.workintech.s19d1.entity.Actor; // Aktör entity'sini import ediyoruz
import com.workintech.s19d1.entity.Movie; // Film entity'sini import ediyoruz
import lombok.Data; // Lombok: Getter, Setter, equals, hashCode ve toString metotlarını otomatik oluşturur
import lombok.NoArgsConstructor; // Lombok: Parametresiz constructor oluşturur
import lombok.AllArgsConstructor; // Lombok: Tüm argümanları alan constructor oluşturur (isteğe bağlı, ama pratik)

import java.util.List; // Filmler listesi için

@NoArgsConstructor // Lombok anotasyonu: Parametresiz bir constructor oluşturur.
@AllArgsConstructor // Lombok anotasyonu: Tüm field'ları (actor, movies) parametre alan bir constructor oluşturur.
@Data // Lombok anotasyonu: Tüm getter, setter, equals, hashCode ve toString metotlarını otomatik oluşturur.
public class ActorRequest {
    private Actor actor; // İstek gövdesinde beklenen Actor nesnesi
    private List<Movie> movies; // İstek gövdesinde beklenen, aktörün rol aldığı filmlerin listesi

    // Not: Bu DTO, client'tan hem aktör bilgilerini hem de ilişkili olduğu filmlerin bilgilerini tek bir JSON objesi içinde almayı sağlar.
    // Örneğin JSON yapısı şöyle olabilir:
    // {
    //   "actor": { "firstName": "Aktör Adı", "lastName": "Aktör Soyadı", ... },
    //   "movies": [ { "name": "Film 1", ... }, { "name": "Film 2", ... } ]
    // }
}