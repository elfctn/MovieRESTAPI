package com.workintech.s19d1.repository;

import com.workintech.s19d1.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

// Movie entity'si için veritabanı CRUD işlemlerini sağlayan Repository arayüzü
// JpaRepository'den extend ederek Spring Data JPA'nın sağladığı temel metodları otomatik olarak alırız.
// <Movie, Long>: İlk parametre, bu repository'nin hangi Entity sınıfı için olduğunu (Movie),
// İkinci parametre ise bu Entity'nin ID (Primary Key) alanının veri tipini (Long) belirtir.
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Özel sorgular burada tanımlanabilir (Örn: Movie findByName(String name);)
    // Şu an için varsayılan CRUD metotları yeterli.
}