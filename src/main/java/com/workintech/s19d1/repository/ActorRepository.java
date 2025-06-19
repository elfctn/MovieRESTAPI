package com.workintech.s19d1.repository;

import com.workintech.s19d1.entity.Actor; // Actor entity sınıfını import ediyoruz
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA'nın temel repository interface'ini import ediyoruz

// Actor entity'si için veritabanı CRUD işlemlerini sağlayan Repository arayüzü
// JpaRepository'den extend ederek Spring Data JPA'nın sağladığı tüm temel metodları (findAll, findById, save, delete vb.)
// otomatik olarak alırız. Bu sayede manuel olarak DAO implementasyonları yazmamıza gerek kalmaz.
// <Actor, Long>: İlk parametre, bu repository'nin hangi Entity sınıfı için olduğunu (Actor),
// İkinci parametre ise bu Entity'nin ID (Primary Key) alanının veri tipini (Long) belirtir.
public interface ActorRepository extends JpaRepository<Actor, Long> {
    // Burada, Spring Data JPA'nın otomatik olarak oluşturduğu CRUD metotlarına ek olarak
    // özel sorgular tanımlayabiliriz. Örneğin:
    // Actor findByFirstName(String firstName); // Aktörün ismine göre bulma
    // List<Actor> findByGender(Gender gender); // Cinsiyete göre aktörleri listeleme
    // Şu an için proje gereksinimlerinde özel bir sorgu bulunmadığı için boş bırakıyoruz.
}