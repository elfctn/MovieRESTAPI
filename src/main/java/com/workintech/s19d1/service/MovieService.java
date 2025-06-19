package com.workintech.s19d1.service;

import com.workintech.s19d1.entity.Actor; // Bu import aslında MovieService için doğrudan gerekli değil, çünkü MovieService sadece Movie ile ilgileniyor. İlerde Actor ve Movie'yi birlikte işleyecek bir servis olabilir ama şuan için çıkarılabilir.
import com.workintech.s19d1.entity.Movie; // Movie entity sınıfını import ediyoruz

import java.util.List; // Tüm filmleri döndürmek için List interface'ini import ediyoruz

// Bu arayüz, Movie entity'si ile ilgili iş mantığı operasyonlarını tanımlar.
// Controller katmanı bu arayüz üzerinden Movie işlemleri için çağrılar yapacaktır.
// Arayüz kullanmak, gerçek implementasyon detaylarını gizler ve daha esnek bir tasarım sağlar.
public interface MovieService {
    // Yeni bir filmi veritabanına kaydeder veya mevcut bir filmi günceller.
    // Kaydedilen/güncellenen Movie nesnesini geri döndürür.
    Movie save(Movie movie);

    // Belirli bir Movie nesnesini veritabanından siler.
    // Geriye bir değer döndürmez (void).
    // Not: Proje gereksinimlerinde genellikle ID ile silme (DELETE /movies/{id}) istendiği için,
    // Movie yerine 'Long id' parametresi almak daha yaygın bir yaklaşımdır.
    // Ancak bu şekilde de kullanılabilir, implementasyonda Movie nesnesinin ID'si kullanılarak silme işlemi yapılır.
    void delete(Movie movie);

    //Movie delete(Long id); // veya void delete(Long id);Daha Yaygın RESTful Yaklaşım


    // Belirli bir ID'ye sahip filmi veritabanından bulur ve geri döndürür.
    // Film bulunamazsa null veya bir exception (ileride tanımlayacağımız) fırlatılmalıdır.
    Movie findById(long id);

    // Veritabanındaki tüm filmleri bir liste olarak geri döndürür.
    List<Movie> findAll();
}