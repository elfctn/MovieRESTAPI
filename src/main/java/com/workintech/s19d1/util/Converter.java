package com.workintech.s19d1.util;

import com.workintech.s19d1.dto.ActorResponse; // ActorResponse DTO'sunu import ediyoruz
import com.workintech.s19d1.entity.Actor; // Actor entity'sini import ediyoruz

import java.util.ArrayList; // Liste oluşturmak için
import java.util.List; // Liste interface'i için

// Bu sınıf, Entity'ler ve DTO'lar arasında dönüşüm yapmak için yardımcı (utility) metotları içerir.
// Statik metotlar olarak tanımlanması, bir nesne oluşturmadan doğrudan çağrılabilmesini sağlar.
public class Converter {

    // Actor entity listesini ActorResponse DTO listesine dönüştürür.
    public static List<ActorResponse> actorResponseConvert(List<Actor> allActors) {
        List<ActorResponse> actorResponseList = new ArrayList<>(); // Yeni bir ActorResponse listesi oluşturulur.
        // Gelen her bir Actor nesnesi için döngü yapılır.
        for (Actor actor : allActors) {
            // Her Actor nesnesi, ActorResponse record'una dönüştürülür ve listeye eklenir.
            // ActorResponse record'u (id, firstName, lastName, birthDate, movies) parametrelerini alır.
            actorResponseList.add(new ActorResponse(actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getBirthDate(), actor.getMovies()));
        }
        return actorResponseList; // Dönüştürülmüş ActorResponse listesi döndürülür.
    }

    // Tek bir Actor entity'sini ActorResponse DTO'suna dönüştürür.
    // Bu metot, özellikle Actor'ın filmlerinin (movies) yanıt içinde gösterilmek istenmediği durumlar için (örn: listeleme, silme sonrası) kullanılabilir.
    public static ActorResponse actorResponseConvert(Actor actor) {
        // ActorResponse record'u oluşturulurken 'movies' alanı için 'null' değeri geçilmiştir.
        // Bu, aktörün filmlerinin bu yanıtta döndürülmeyeceği anlamına gelir.
        return new ActorResponse(actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getBirthDate(), null);
    }

    // Tek bir Actor entity'sini ActorResponse DTO'suna dönüştürür.
    // Bu metot, genellikle Actor oluşturulduktan sonra veya detay bilgilerinin döndürüleceği durumlarda,
    // aktörün filmlerinin (movies) de yanıt içinde gösterilmesi istendiğinde kullanılır.
    // Not: actorResponseConvert(List<Actor> allActors) metodunda da filmler dönüldüğü için,
    // bu iki tekil dönüşüm metodunun farkı 'movies' listesinin dahil edilip edilmemesiyle ilgilidir.
    // isimlandırma biraz kafa karıştırıcı olabilir, ActorDetailResponse gibi bir isim daha iyi olabilirdi.
    public static ActorResponse actorCreateResponseConvert(Actor actor) {
        // ActorResponse record'u oluşturulurken 'movies' alanı için aktörün kendi filmler listesi geçilmiştir.
        // Bu, aktörün filmlerinin bu yanıtta döndürüleceği anlamına gelir.
        return new ActorResponse(actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getBirthDate(), actor.getMovies());
    }
}