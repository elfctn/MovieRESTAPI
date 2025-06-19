package com.workintech.s19d1.service;

import com.workintech.s19d1.entity.Actor; // Actor entity sınıfını import ediyoruz
import com.workintech.s19d1.exceptions.ApiException; // Özel hata sınıfımızı import ediyoruz (henüz oluşturulmadı, oluşturacağız)
import com.workintech.s19d1.repository.ActorRepository; // ActorRepository'yi import ediyoruz
import org.springframework.beans.factory.annotation.Autowired; // Bağımlılık enjeksiyonu için
import org.springframework.http.HttpStatus; // HTTP durum kodları için
import org.springframework.stereotype.Service; // Bu sınıfın bir servis bileşeni olduğunu belirtir

import java.util.List; // Liste tipi için import
import java.util.Optional; // findById metodunun Optional döndürmesi için, dolaylı olarak kullanılıyor

@Service // Bu anotasyon, Spring'e bu sınıfın bir servis bileşeni olduğunu ve Spring IoC container'ı tarafından yönetilmesi gerektiğini söyler.
public class ActorServiceImpl implements ActorService { // ActorService arayüzünü implement ediyoruz

    private final ActorRepository actorRepository; // ActorRepository bağımlılığı tanımlanıyor. 'final' olması, atamanın sadece constructor'da yapılacağını garanti eder.

    // Constructor Injection: Spring, ActorServiceImpl nesnesini oluştururken ActorRepository'nin bir örneğini otomatik olarak buraya enjekte edecektir.
    // @Autowired anotasyonu burada zorunlu değildir çünkü tek bir constructor olduğu sürece Spring bunu otomatik olarak algılar.
    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository; // Enjekte edilen ActorRepository nesnesini sınıf değişkenine atıyoruz.
    }

    @Override // ActorService arayüzündeki metodun override edildiğini belirtir
    public Actor save(Actor actor) {
        // actorRepository'nin save metodu çağrılarak yeni bir aktör kaydedilir veya mevcut bir aktör güncellenir.
        // Spring Data JPA, id varsa update, yoksa insert işlemi yapar.
        return actorRepository.save(actor);
    }

    @Override // ActorService arayüzündeki metodun override edildiğini belirtir
    public void delete(Actor actor) {
        // actorRepository'nin delete metodu çağrılarak belirli bir Actor nesnesi veritabanından silinir.
        // Gelen Actor nesnesinin ID'si üzerinden silme işlemi yapılır.
        actorRepository.delete(actor);
    }

    @Override // ActorService arayüzündeki metodun override edildiğini belirtir
    public Actor findById(long id) {
        // actorRepository'nin findById metodu çağrılarak belirli bir ID'ye sahip aktör aranır.
        // findById metodu bir Optional<Actor> döndürür.
        // .orElseThrow(): Eğer aktör bulunamazsa (Optional boşsa), parantez içindeki lambda ifadesi çalışır ve bir ApiException fırlatılır.
        // new ApiException(...): Özel ApiException sınıfımız ile 'actor is not found' mesajı ve HTTP 404 (NOT_FOUND) durum kodu döner.
        return actorRepository.findById(id).orElseThrow(() ->
                new ApiException("actor is not found with id: " + id, HttpStatus.NOT_FOUND)
        );
    }

    @Override // ActorService arayüzündeki metodun override edildiğini belirtir
    public List<Actor> findAll() {
        // actorRepository'nin findAll metodu çağrılarak veritabanındaki tüm aktörler liste olarak getirilir.
        return actorRepository.findAll();
    }
}