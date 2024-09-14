# Veritabanı Şeması Dokümantasyonu

Bu doküman, **sipariş**, **ürün** ve **teslimat** servisleri için mikroservis mimarisinde kullanılan veritabanı şema tasarımını açıklamaktadır. Şema, servislerin tabloları arasındaki ilişkileri göstermek için tek bir diagramda birleşik bir şekilde gösterilmiştir, ancak her bir tablo ayrı veritabanlarında bulunmaktadır. Veritabanı yönetim sistemi olarak **PostgreSQL** kullanılmıştır.

## 1. **Ürün Servisi**

Bu servis, ürünlerle ilgili bilgileri, stok durumunu, fiyatları ve ürün detaylarını yönetir.

### `product` Tablosu

- **id**: Her bir ürünü benzersiz şekilde tanımlayan bir `UUID`.
- **stock**: Ürünün mevcut stok miktarını gösteren bir `integer`.
- **price**: Ürünün fiyatını temsil eden bir `double`.
- **name**: Ürünün adını içeren bir `text` alanı.
- **created_at**: Ürünün ne zaman oluşturulduğunu gösteren bir `timestamp`.

Bu tablo, **Sipariş Servisi**'ndeki `order_items` tablosuna bağlıdır; her sipariş, birden fazla ürünü içerebilir.

## 2. **Sipariş Servisi**

Bu servis, siparişler ve siparişlerle ilişkili ürünleri yönetir. `order` ve `order_items` tabloları, her bir sipariş ve o siparişin içerdiği ürünler hakkında bilgi tutar.

### `order` Tablosu

- **id**: Her bir siparişi benzersiz şekilde tanımlayan bir `UUID`.
- **total_price**: Siparişin toplam fiyatını temsil eden bir `double`.
- **is_refunded**: Siparişin iade edilip edilmediğini gösteren bir `bool`.
- **created_at**: Siparişin ne zaman oluşturulduğunu belirten bir `timestamp`.
- **status**: Siparişin mevcut durumunu temsil eden bir enum `order_status` (örneğin: `created`, `completed`, `canceled`).

### `order_items` Tablosu

- **id**: Her bir sipariş öğesini benzersiz şekilde tanımlayan bir `UUID`.
- **product_id**: Ürünü `product` tablosuna bağlayan bir `UUID` (foreign key).
- **product_name**: Ürünün adını içeren bir `text` alanı.
- **price**: Siparişteki ürünün fiyatını temsil eden bir `double`.
- **order_id**: Sipariş öğesinin hangi siparişe ait olduğunu belirten bir `UUID` (foreign key).
- **created_at**: Sipariş öğesinin ne zaman eklendiğini gösteren bir `timestamp`.

Her bir `order_items` kaydı, hem bir siparişin parçası olarak `order` tablosuna, hem de satın alınan ürünü temsil eden `product` tablosuna bağlıdır.

## 3. **Teslimat Servisi**

Bu servis, teslimatları ve teslimat için sorumlu sürücülerin durumunu yönetir.

### `delivery` Tablosu

- **id**: Her bir teslimatı benzersiz şekilde tanımlayan bir `UUID`.
- **order_id**: Teslim edilen siparişi temsil eden bir `UUID` (foreign key, `order` tablosundan).
- **driver_id**: Teslimattan sorumlu sürücüyü temsil eden bir `UUID` (foreign key, `driver` tablosundan).
- **status**: Teslimatın durumunu temsil eden bir enum `delivery_status` (örneğin: `processing`, `on_delivery`, `completed`).
- **expiration_date**: Teslimatın tamamlanması gereken maksimum süreyi belirten bir `timestamp`.
- **on_time**: Teslimatın belirtilen süre içinde tamamlanıp tamamlanmadığını gösteren bir `bool`.

### `driver` Tablosu

- **id**: Her bir sürücüyü benzersiz şekilde tanımlayan bir `UUID`.
- **name**: Sürücünün adını içeren bir `text` alanı.
- **status**: Sürücünün mevcut durumunu temsil eden bir enum `driver_status` (örneğin: `available`, `busy`).

`delivery` tablosu, her teslimatın bir siparişe ait olduğunu belirtmek için `order` tablosuna bağlıdır. Ayrıca, teslimatlar sürücülere atanır ve bu atamayı yapmak için `driver` tablosuna bağlıdır.

## Tablolar Arasındaki İlişkiler

- **Sipariş ve Sipariş Öğeleri**: `order_items` tablosu, `order_id` kullanarak `order` tablosuna bağlanır ve bu, belirli bir siparişte satın alınan ürünleri temsil eder.
- **Sipariş Öğeleri ve Ürün**: `order_items` tablosu, `product_id` kullanarak her sipariş öğesini `product` tablosundaki ürünlere bağlar.
- **Sipariş ve Teslimat**: `delivery` tablosu, `order_id` foreign key'ini kullanarak bir teslimatı bir siparişe bağlar.
- **Teslimat ve Sürücü**: `delivery` tablosu, `driver_id` foreign key'ini kullanarak teslimatları sürücülere bağlar.

## Servis Sınırları

- **Ürün Servisi**, `product` tablosunu yönetir ve ürün bilgileri ile stok takibinden sorumludur.
- **Sipariş Servisi**, `order` ve `order_items` tablolarını yönetir ve müşteri siparişlerinin detaylarını takip eder.
- **Teslimat Servisi**, `delivery` ve `driver` tablolarını yönetir ve siparişlerin teslimat süreçlerini ve sürücü atamalarını düzenler.

## Diagram Genel Bakış

Diagram, tablolar arasındaki ilişkileri görsel olarak temsil eder. Tablolar farklı servislerde yer alsa da, iş mantığını tutarlı bir şekilde uygulamak için foreign key ilişkileri üzerinden birbirleriyle etkileşim kurarlar:

- **Sipariş Servisi**'nde yer alan `order` ve `order_items`.
- **Teslimat Servisi**'nde yer alan `delivery` ve `driver`.
- **Ürün Servisi**'nde yer alan `product`.
