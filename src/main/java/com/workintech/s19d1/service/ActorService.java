package com.workintech.s19d1.service;

import com.workintech.s19d1.entity.Actor; // Actor entity sınıfını import ediyoruz

import java.util.List; // Tüm aktörleri döndürmek için List interface'ini import ediyoruz

// Bu arayüz, Actor entity'si ile ilgili iş mantığı operasyonlarını tanımlar.
// Controller katmanı bu arayüz üzerinden Actor işlemleri için çağrılar yapacaktır.
// Arayüz kullanmak, gerçek implementasyon detaylarını gizler ve daha esnek bir tasarım sağlar.
public interface ActorService {
    // Yeni bir aktörü veritabanına kaydeder veya mevcut bir aktörü günceller.
    // Kaydedilen/güncellenen Actor nesnesini geri döndürür.
    Actor save(Actor actor);

    // Belirli bir Actor nesnesini veritabanından siler.
    // Geriye bir değer döndürmez (void).
    // Not: Proje gereksinimlerinde genellikle ID ile silme (DELETE /actors/{id}) istendiği için,
    // Actor yerine 'Long id' parametresi almak daha yaygın bir yaklaşımdır.
    // Ancak bu şekilde de kullanılabilir, implementasyonda Actor nesnesinin ID'si kullanılarak silme işlemi yapılır.
    void delete(Actor actor);

    //Actor delete(Long id); // veya void delete(Long id);Daha Yaygın RESTful Yaklaşım


    // Belirli bir ID'ye sahip aktörü veritabanından bulur ve geri döndürür.
    // Aktör bulunamazsa null veya bir exception (ileride tanımlayacağımız) fırlatılmalıdır.
    Actor findById(long id);

    // Veritabanındaki tüm aktörleri bir liste olarak geri döndürür.
    List<Actor> findAll();
}